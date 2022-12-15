package cn.yiidii.apiplatform.support.videoparse;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.yiidii.apiplatform.model.dto.VideoParseResponseDTO;
import cn.yiidii.apiplatform.model.enums.ApiExceptionCode;
import cn.yiidii.apiplatform.support.VideoParser;
import cn.yiidii.apiplatform.util.Util;
import cn.yiidii.base.exception.BizException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

/**
 * 哔哩哔哩视频解析
 *
 * @author ed w
 * @since 1.0
 */
@Component("bilibiliVideoParser")
public class BilibliVideoParser implements VideoParser {

    @Override
    public VideoParseResponseDTO parse(String s) {
        String url = Util.getUrl(s);
        Assert.isTrue(StrUtil.isNotBlank(s), "请检查链接是否正确~");

        return this.doParse(url);
    }


    /**
     * 检查bv是否正确
     *
     * @param url url
     * @return bvId
     */
    private String checkBv(String url) {
        int index = url.indexOf("?");
        if (index > 0) {
            url = url.substring(0, index);
        }

        if (StrUtil.endWith(url, "/")) {
            url = url.substring(0, url.length()- 1);
        }

        index = url.lastIndexOf("/");
        String bvId = url.substring(index + 1);

        if (StrUtil.startWithIgnoreCase(bvId, "bv")) {
            if (bvId.length() == 12) {
                HttpResponse res = HttpRequest.get(StrUtil.format("https://api.bilibili.com/x/web-interface/view?bvid={}", bvId))
                        .execute();
                JSONObject resJo = JSONObject.parseObject(res.body());
                if (resJo.getInteger("code") != 0) {
                    throw new BizException(ApiExceptionCode.VIDEO_PARSE_EXCEPTION);
                }
            } else {
                throw new BizException(ApiExceptionCode.VIDEO_PARSE_EXCEPTION);
            }
        } else {
            throw new BizException(ApiExceptionCode.VIDEO_PARSE_EXCEPTION);
        }
        return bvId;
    }

    private VideoParseResponseDTO doParse(String url) {
        String bvId = checkBv(url);

        HttpResponse res = HttpRequest.get(StrUtil.format("https://api.bilibili.com/x/web-interface/view?bvid={}", bvId))
                .execute();
        JSONObject resJo = JSON.parseObject(res.body());

        JSONObject data = resJo.getJSONObject("data");
        String userName = data.getJSONObject("owner").getString("name");
        String title = data.getString("title");
        String cover = data.getString("pic");

        return VideoParseResponseDTO.builder()
                .type(VideoParseResponseDTO.VIDEO)
                .nickname(userName)
                .title(title)
                .cover(cover)
                .urls(Lists.newArrayList(StrUtil.format("https://v.aapi.eu.org/bili/{}", bvId)))
                .raw(resJo)
                .build();
    }
}
