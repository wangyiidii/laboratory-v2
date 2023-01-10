package cn.yiidii.lab.apiplatform.support.videoparse;

import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.yiidii.base.exception.BizException;
import cn.yiidii.lab.apiplatform.model.dto.VideoParseResponseDTO;
import cn.yiidii.lab.apiplatform.model.enums.ApiExceptionCode;
import cn.yiidii.lab.apiplatform.support.VideoParser;
import cn.yiidii.lab.apiplatform.util.Util;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    @Override
    public VideoParseResponseDTO parse(String s) {
        String url = Util.getUrl(s);
        try {
            // 先解析视频
            return parseVideo(url);
        } catch (Exception e) {
            throw new BizException(ApiExceptionCode.VIDEO_PARSE_EXCEPTION);
        }
    }


    /**
     * 解析 短视频/图文
     *
     * @param url url
     * @return {@link VideoParseResponseDTO}
     */
    private VideoParseResponseDTO parseVideo(String url) {

        // 请求短链，获取重定向地址
        HttpResponse res = HttpRequest.get(url)
                .execute();
        String loc = res.header(Header.LOCATION);

        String videoId = ReUtil.getGroup0("\\d{19}", loc);

        // 请求重定向地址，获取html并解析
        res = HttpRequest.get(StrUtil.format("https://www.iesdouyin.com/aweme/v1/web/aweme/detail/?aweme_id={}", videoId))
                .header(Header.USER_AGENT, "Mozilla/5.0 (iPhone; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1")
                .execute();
        String body = res.body();
        JSONObject info = JSONObject.parseObject(body).getJSONObject("aweme_detail");

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

}
