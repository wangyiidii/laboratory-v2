package cn.yiidii.web.controller.apiplatform;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.yiidii.apiplatform.model.body.CookieBody;
import cn.yiidii.apiplatform.model.dto.TencentVideoSignInResponseDTO;
import cn.yiidii.apiplatform.service.SignInService;
import cn.yiidii.apiplatform.model.body.MiBrushStepBody;
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

    /**
     * 小米运动刷新步数
     *
     * @param body body
     * @return R
     */
    @PostMapping("/mi-step")
    public R<?> miStep(@RequestBody @Validated MiBrushStepBody body) {
        singInService.miStep(body);
        return R.ok(null, StrUtil.format("成功刷新{}步", body.getStep()));
    }

    @PostMapping("/tencent-video")
    public R<?> testConfig(@RequestBody CookieBody cookieBody) {
        TencentVideoSignInResponseDTO ret = singInService.tencentVideo(cookieBody);
        return R.ok(null, StrUtil.format("签到成功, 获取{}积分", ret.getCheckinScore()));
    }

}
