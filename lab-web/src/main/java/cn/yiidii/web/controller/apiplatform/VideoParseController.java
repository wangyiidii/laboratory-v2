package cn.yiidii.web.controller.apiplatform;

import cn.yiidii.apiplatform.model.dto.VideoParseResponseDTO;
import cn.yiidii.apiplatform.service.VideoParseService;
import cn.yiidii.web.R;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
     * 短视频解析
     * 支持 短视频/图文/直播
     *
     * @param s   分享链接或者短链
     * @param raw 是否返回原始响应
     * @return {@link VideoParseResponseDTO}
     */
    @GetMapping("/video-parse")
    public R<VideoParseResponseDTO> parse(@RequestParam String s,@RequestParam(defaultValue = "0") Integer raw) {
        VideoParseResponseDTO dto = videoParseService.parse(s, raw);
        return R.ok(dto, "解析成功");
    }


}
