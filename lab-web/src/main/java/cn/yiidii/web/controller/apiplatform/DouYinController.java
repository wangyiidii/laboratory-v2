package cn.yiidii.web.controller.apiplatform;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.yiidii.apiplatform.model.dto.VideoParseResponseDTO;
import cn.yiidii.base.exception.BizException;
import cn.yiidii.web.R;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * DouyinController
 *
 * @author ed w
 * @since 1.0
 */
@Slf4j
@RestController
@RequestMapping("/dy")
@RequiredArgsConstructor
public class DouYinController {

    private static final int MAX_TIME = 20;
    private static final String MOBILE_UA = "Mozilla/5.0 (iPhone; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1";
    private static final String DY_VIDEO_PATH = "https://www.iesdouyin.com/web/api/v2/aweme/iteminfo/?item_ids=";

    /**
     * 抖音解析
     * 支持 短视频/图文/直播
     *
     * @param s   分享链接或者短链
     * @param raw 是否返回原始响应
     * @return {@link VideoParseResponseDTO}
     */
    @GetMapping("/parse")
    public R<?> parse(@RequestParam String s,
                      @RequestParam(defaultValue = "0") Integer raw) {

        if (StrUtil.isBlank(s)) {
            throw new IllegalArgumentException("参数[s]必传且不为空");
        }

        VideoParseResponseDTO dto = doParse(s);
        if (raw == 0) {
            dto.setRaw(null);
        }

        return R.ok(dto, "解析成功");
    }

    private VideoParseResponseDTO doParse(String url) {

        // 提取短链
        url = ReUtil.getGroup0("(((ht|f)tps?):\\/\\/)?[\\w-]+(\\.[\\w-]+)+([\\w.,@?^=%&:/~+#-]*[\\w@?^=%&/~+#-])?", url);

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
                throw new BizException("解析异常，换个链接试试吧~");
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
        // 先获取itemId
        HttpResponse res = HttpRequest.get(url).header(Header.USER_AGENT, MOBILE_UA).execute();
        url = Jsoup.parse(res.body()).getElementsByTag("a").attr("href");
        String itemId = url.substring(url.indexOf("video/"), url.lastIndexOf("/")).replace("video/", "");

        // 再去解析
        res = HttpRequest.get(DY_VIDEO_PATH + itemId).header(Header.USER_AGENT, MOBILE_UA).execute();
        JSONObject resJo = JSONObject.parseObject(res.body());
        JSONArray itemList = resJo.getJSONArray("item_list");
        if (CollUtil.isEmpty(itemList)) {
            throw new BizException("itemlist");
        }

        // item信息
        JSONObject info = itemList.getJSONObject(0);
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

        return VideoParseResponseDTO.builder().type(isVideo ? VideoParseResponseDTO.VIDEO : VideoParseResponseDTO.IMAGE).nickname(nickname).title(desc).cover(cover).urls(urlList).raw(info).build();
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
