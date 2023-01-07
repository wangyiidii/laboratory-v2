package cn.yiidii.lab.apiplatform.support.videoparse;

import cn.hutool.core.net.URLDecoder;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.yiidii.lab.apiplatform.model.dto.VideoParseResponseDTO;
import cn.yiidii.lab.apiplatform.model.enums.ApiExceptionCode;
import cn.yiidii.lab.apiplatform.support.VideoParser;
import cn.yiidii.lab.apiplatform.util.Util;
import cn.yiidii.base.core.service.ConfigService;
import cn.yiidii.base.exception.BizException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 抖音视频解析
 *
 * @author ed w
 * @since 1.0
 */
@Service("dyVideoParser")
@RequiredArgsConstructor
public class DouYinVideoParser implements VideoParser {

    private static final int MAX_TIME = 20;
    private final ConfigService configService;

    @Override
    public VideoParseResponseDTO parse(String s) {
        String url = Util.getUrl(s);
        // 如果有19位房间id就先处理直播
        String roomId = ReUtil.getGroup0("\\d{19}", url);
        if (StrUtil.isNotBlank(roomId)) {
            try {
                return parseLiveByRoomId(roomId);
            } catch (Exception ex) {
                // ignored
            }
        }

        VideoParseResponseDTO dto;
        try {
            // 先解析视频
            dto = parseVideo(url);
        } catch (Exception e) {
            try {
                // 再解析直播
                dto = parseLive(url);
            } catch (Exception ex) {
                // 异常
                throw new BizException(ApiExceptionCode.VIDEO_PARSE_EXCEPTION);
            }
        }
        return dto;
    }


    /**
     * 解析 短视频/图文
     *
     * @param url url
     * @return {@link VideoParseResponseDTO}
     */
    private VideoParseResponseDTO parseVideo(String url) {
        // 抖音cookie
        String cookie = configService.get("douyin_cookie", "");

        // 请求短链，获取重定向地址
        HttpResponse res = HttpRequest.get(url)
                .execute();
        String loc = res.header(Header.LOCATION);

        // 请求重定向地址，获取html并解析
        res = HttpRequest.get(loc)
                .cookie(cookie)
                .header(Header.USER_AGENT, "Mozilla/5.0 (iPhone; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1")
                .execute();
        Document document = Jsoup.parse(res.body());
        String renderData = document.getElementsByAttributeValue("id", "RENDER_DATA").get(0).html();
        renderData = URLDecoder.decode(renderData, StandardCharsets.UTF_8);
        JSONObject renderDataJo = JSONObject.parseObject(renderData);

        JSONObject videoInfoRefs = renderDataJo.getJSONObject("app").getJSONObject("videoInfoRes");
        JSONObject info = videoInfoRefs.getJSONArray("item_list").getJSONObject(0);

        // 作者基本信息
        JSONObject author = info.getJSONObject("author");
        String nickname = author.getString("nickname");
        // 描述
        String desc = info.getString("desc");

        // 根据image大小判断是图片还是视频
        JSONArray imageJa = info.getJSONArray("images");
        boolean isVideo = Objects.isNull(imageJa) || imageJa.isEmpty();

        List<String> urlList;
        String cover = "";
        if (isVideo) {
            // 视频，提取播放地址，和第一个封面
            JSONArray urlJa = info.getJSONObject("video").getJSONObject("play_addr").getJSONArray("url_list");
            cover = info.getJSONObject("video").getJSONObject("dynamic_cover").getJSONArray("url_list").getString(0);
            urlList = JSONArray.parseArray(JSON.toJSONString(urlJa), String.class);
            // 移除水印处理
            urlList = urlList.stream().map(u -> u.replace("playwm", "play")).collect(Collectors.toList());
        } else {
            // 图片则取出每一个图片list的第一个图片
            urlList = info.getJSONArray("images").stream().map(imgObj -> {
                JSONObject imgJo = (JSONObject) imgObj;
                return imgJo.getJSONArray("url_list").getString(0);
            }).collect(Collectors.toList());
        }

        // 音频
        List<String> audioUrls = info.getJSONObject("music").getJSONObject("play_url").getJSONArray("url_list").toJavaList(String.class);

        return VideoParseResponseDTO.builder()
                .type(isVideo ? VideoParseResponseDTO.VIDEO : VideoParseResponseDTO.IMAGE)
                .nickname(nickname)
                .title(desc)
                .cover(cover)
                .urls(urlList)
                .audioUrls(audioUrls)
                .raw(info)
                .build();
    }

    /**
     * 解析直播
     *
     * @param url url
     * @return {@link VideoParseResponseDTO}
     */
    private VideoParseResponseDTO parseLive(String url) {
        String loc = HttpRequest.get(url).execute().header(Header.LOCATION);
        String roomId = ReUtil.getGroup0("\\d{19}", loc);
        return parseLiveByRoomId(roomId);
    }


    /**
     * 解析直播
     *
     * @param roomId roomId
     * @return {@link VideoParseResponseDTO}
     */
    private VideoParseResponseDTO parseLiveByRoomId(String roomId) {

        JSONObject rawData = null;
        for (int i = 0; i < MAX_TIME; i++) {
            HttpResponse res = HttpRequest.get(StrUtil.format("https://webcast.amemv.com/webcast/room/reflow/info/?verifyFp=&type_id=0&live_id=1&room_id={}&sec_user_id=&app_id=1128&msToken=&X-Bogus=", roomId)).execute();
            try {
                rawData = JSON.parseObject(res.body()).getJSONObject("data");
            } catch (Exception e) {
                // ignored
            }
        }
        if (Objects.isNull(rawData)) {
            // MAX_TIME都没有请求出来
            throw new BizException("服务器抽风~");
        }

        JSONObject room = rawData.getJSONObject("room");
        String nickname = room.getJSONObject("owner").getString("nickname");
        String title = room.getString("title");
        JSONObject streamUrl = room.getJSONObject("stream_url");
        String hlsPullUrl = streamUrl.getString("hls_pull_url");
        String cover = room.getJSONObject("cover").getJSONArray("url_list").getString(0);

        return VideoParseResponseDTO.builder()
                .type(VideoParseResponseDTO.LIVE)
                .nickname(nickname)
                .title(title)
                .cover(cover)
                .urls(Lists.newArrayList(hlsPullUrl)).raw(rawData).build();
    }
}
