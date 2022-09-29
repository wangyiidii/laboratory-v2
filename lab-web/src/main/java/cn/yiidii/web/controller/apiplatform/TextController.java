package cn.yiidii.web.controller.apiplatform;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.text.UnicodeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.yiidii.apiplatform.model.body.FileBody;
import cn.yiidii.base.annotation.RateLimiter;
import cn.yiidii.base.domain.dto.ProcessResultDTO;
import cn.yiidii.base.exception.BizException;
import cn.yiidii.base.util.JsonUtils;
import cn.yiidii.base.util.LabFileUtil;
import cn.yiidii.base.util.ProcessUtil;
import cn.yiidii.web.R;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * TextController
 *
 * @author ed w
 * @since 1.0
 */
@Slf4j
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

    @RateLimiter(count = 1, time = 1)
    @PostMapping("/captcha/recognize")
    public R<Map<String, String>> captchaRecognize(@RequestBody FileBody body) {
        String base64 = body.getBase64();
        if (StrUtil.isBlank(base64)) {
            throw new IllegalArgumentException("缺少base64参数");
        }

        // 临时图片文件，用完删除
        File tempFile = null;
        try {
            tempFile = FileUtil.createTempFile(".png", true);

            // base64转文件
            if (!Base64.isBase64(base64)) {
                throw new IllegalArgumentException("base64格式不正确");
            }
            Base64.decodeToFile(base64, tempFile);

            // 调用python ddddorc识别
            File ddddocrPy = LabFileUtil.getVarFileFromClassPath("/script/captcha_recognize.py", false);
            String cmd = StrUtil.format("python {} -p {}", ddddocrPy.getAbsolutePath(), tempFile.getAbsolutePath());
            ProcessResultDTO<String> ret = ProcessUtil.execForStr(cmd);

            if (ret.getCode() != 0) {
                throw new BizException(ret.getMessage());
            }

            // 返回
            HashMap<String, String> retData = Maps.newHashMap();
            retData.put("text", ret.getResult());

            return R.ok(retData);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (Objects.nonNull(tempFile)) {
                FileUtil.del(tempFile);
            }
        }
    }


    /**
     * 文字转emoji
     *
     * @param text text
     * @param mode mode
     * @return emoji
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
