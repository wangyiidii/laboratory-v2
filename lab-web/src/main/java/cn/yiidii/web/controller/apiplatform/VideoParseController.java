package cn.yiidii.web.controller.apiplatform;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.yiidii.apiplatform.model.dto.VideoParseResponseDTO;
import cn.yiidii.apiplatform.service.VideoParseService;
import cn.yiidii.web.R;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * DouyinController
 *
 * @author ed w
 * @since 1.0
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class VideoParseController {

    private final VideoParseService videoParseService;

    /**
     * [GET] 短视频解析
     * 支持 短视频/图文/直播
     *
     * @param s   分享链接或者短链
     * @param raw 是否返回原始响应
     * @return {@link VideoParseResponseDTO}
     */
    @GetMapping("/video-parse")
    public R<VideoParseResponseDTO> parse(@RequestParam String s, @RequestParam(defaultValue = "0") Integer raw) {
        VideoParseResponseDTO dto = videoParseService.parse(s, raw);
        return R.ok(dto, "解析成功");
    }

    /**
     * [POST] 短视频解析
     * 支持 短视频/图文/直播
     *
     * @return {@link VideoParseResponseDTO}
     */
    @PostMapping("/video-parse")
    public R<VideoParseResponseDTO> parse(@RequestBody Map<String, String> param) {
        String s = param.getOrDefault("s", "");
        Integer raw = Convert.convert(Integer.class, param.getOrDefault("raw", ""), 0);

        Assert.isTrue(StrUtil.isNotBlank(s), "缺少必须的[String]类型的参数[s]");

        VideoParseResponseDTO dto = videoParseService.parse(s, raw);
        return R.ok(dto, "解析成功");
    }

}
