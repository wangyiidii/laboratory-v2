package cn.yiidii.web.controller.aplplatform;

import cn.hutool.core.util.StrUtil;
import cn.yiidii.apiplatform.service.SignInService;
import cn.yiidii.apiplatform.model.dto.MiBrushStepBody;
import cn.yiidii.base.core.service.ConfigService;
import cn.yiidii.web.R;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * SignInController
 *
 * @author ed w
 * @since 1.0
 */
@RestController
@RequestMapping("/sign-in")
@RequiredArgsConstructor
public class SignInController {

    private final SignInService singInService;
    private final ConfigService configService;

    /**
     * 小米运动刷新步数
     *
     * @param body body
     * @return R
     */
    @PostMapping("/mi-step")
    public R<?> testSignIn(@RequestBody @Validated MiBrushStepBody body) {
        singInService.miStep(body);
        return R.ok(null, StrUtil.format("成功刷新{}步", body.getStep()));
    }

    @GetMapping("/test")
    public R<?> testConfig(@RequestParam String key) {
        return R.ok(configService.get(key, "xxx"));
    }

}
