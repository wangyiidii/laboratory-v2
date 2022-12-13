package cn.yiidii.web.controller.apiplatform;

import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.yiidii.apiplatform.model.dto.VideoParseResponseDTO;
import cn.yiidii.base.core.service.ConfigService;
import cn.yiidii.web.R;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * KuaiShouController
 *
 * @author ed w
 * @since 1.0
 */
@Slf4j
@RestController
@RequestMapping("/ks")
@RequiredArgsConstructor
public class KuaiShouController {

    private final ConfigService configService;

    /**
     * 快手解析
     * 支持 短视频/图文/直播
     *
     * @param s   分享链接或者短链
     * @param raw 是否返回原始响应
     * @return {@link VideoParseResponseDTO}
     */
    @GetMapping("/parse")
    public R<?> parse(@RequestParam String s, @RequestParam(defaultValue = "0") Integer raw) {
        if (StrUtil.isBlank(s)) {
            throw new IllegalArgumentException("参数[s]必传且不为空");
        }
        VideoParseResponseDTO dto = doParse(s);
        if (raw == 0) {
            dto.setRaw(null);
        }

        return R.ok(dto, "解析成功");
    }

    private VideoParseResponseDTO doParse(String s) {
        // 提取短连
        s = ReUtil.getGroup0("(https?|ftp|file)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]", s);
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
