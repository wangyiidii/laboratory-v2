package cn.yiidii.apiplatform.support.videoparse;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.yiidii.apiplatform.model.dto.VideoParseResponseDTO;
import cn.yiidii.apiplatform.support.VideoParser;
import cn.yiidii.base.core.service.ConfigService;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 快手视频解析
 *
 * @author ed w
 * @since 1.0
 */
@Service("ksVideoParser")
@RequiredArgsConstructor
public class KuaiShouVideoParser implements VideoParser {

    private final ConfigService configService;

    @Override
    public VideoParseResponseDTO parse(String s) {
        // 提取短连
        s = ReUtil.getGroup0("(https?|ftp|file)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]", s);

        Assert.isTrue(StrUtil.isNotBlank(s), "请检查链接是否正确~");

        // 解析
        return parseVideo(s);
    }

    private VideoParseResponseDTO parseVideo(String url) {
        HttpResponse resp = HttpRequest.get(url)
                .execute();
        String loc = resp.header(Header.LOCATION.getValue());

        // 获取id
        String tmp = loc.substring(0, loc.indexOf("?"));
        String id = tmp.substring(tmp.lastIndexOf("/") + 1);

        // 快手cookie
        String kuaishouCookie = configService.get("kuaishou_cookie", "");

        JSONObject jo = new JSONObject();
        jo.put("photoId", id);
        jo.put("isLongVideo", false);
        HttpResponse res = HttpRequest.post("https://v.m.chenzhongtech.com/rest/wd/photo/info")
                .contentType(ContentType.JSON.getValue())
                .cookie(kuaishouCookie)
                .body(jo.toJSONString())
                .header(Header.REFERER, loc)
                .execute();
        JSONObject resJo = JSONObject.parseObject(res.body());

        boolean isImg = resJo.containsKey("atlas");

        String title = resJo.getJSONObject("shareInfo").getString("shareTitle");
        List<String> urlList = Lists.newLinkedList();
        if (isImg) {
            JSONObject atlasJo = resJo.getJSONObject("atlas");
            String cdn = atlasJo.getJSONArray("cdn").getString(0);
            atlasJo.getJSONArray("list")
                    .stream().map(l -> StrUtil.format("https://{}{}", cdn, l))
                    .forEach(urlList::add);
        } else {
            urlList.add(resJo.getString("mp4Url"));
        }

        JSONObject photoJo = resJo.getJSONObject("photo");
        String userName = photoJo.getString("userName");
        String cover = photoJo.getJSONArray("webpCoverUrls").getJSONObject(0).getString("url");

        return VideoParseResponseDTO.builder()
                .type(!isImg ? VideoParseResponseDTO.VIDEO : VideoParseResponseDTO.IMAGE)
                .nickname(userName)
                .title(title)
                .cover(cover)
                .urls(urlList)
                .raw(resJo)
                .build();
    }
}
