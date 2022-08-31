package cn.yiidii.web.controller.apiplatform;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.text.UnicodeUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.yiidii.base.annotation.RateLimiter;
import cn.yiidii.base.exception.BizException;
import cn.yiidii.base.util.JsonUtils;
import cn.yiidii.web.R;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.Objects;

/**
 * TextController
 *
 * @author ed w
 * @since 1.0
 */
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class TextController {

    @RateLimiter(count = 1, time = 5)
    @GetMapping("/emoji-convert")
    public R<?> emojiConvertGet(@RequestParam @NotNull(message = "文本不能为空") String text,
                                @RequestParam(defaultValue = "2") Integer mode) {
        return R.ok(emojiConvert(text, mode));
    }

    @PostMapping("/emoji-convert")
    public R<?> emojiConvertGet(@RequestBody Map<String, String> bodyJo) {
        return R.ok(emojiConvert(bodyJo.get("text"), Integer.parseInt(bodyJo.getOrDefault("status", "1"))));
    }

    /**
     * 文字转emoji
     *
     * @param text
     * @param mode
     * @return
     */
    private String emojiConvert(String text, Integer mode) {
        if (Objects.isNull(mode)) {
            mode = 2;
        }
        HttpResponse response = HttpRequest.post("https://www.emojidaquan.com/Transfer/index")
                .form("text", text)
                .form("mode", mode)
                .execute();
        Map map = JsonUtils.parseObject(response.body(), Map.class);
        String msg = UnicodeUtil.toString(Convert.toStr(map.get("msg")));
        if (!Convert.toBool(map.get("status"))) {
            throw new BizException(msg);
        }
        return msg;
    }

}
