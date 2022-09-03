package cn.yiidii.web.controller.apiplatform;

import cn.hutool.core.util.StrUtil;
import cn.yiidii.apiplatform.model.body.CookieBody;
import cn.yiidii.apiplatform.model.body.MiBrushStepBody;
import cn.yiidii.apiplatform.model.dto.DingDongSignInResponseDTO;
import cn.yiidii.apiplatform.model.dto.EverPhotoSignInResponseDTO;
import cn.yiidii.apiplatform.model.dto.TencentVideoSignInResponseDTO;
import cn.yiidii.apiplatform.service.SignInService;
import cn.yiidii.base.annotation.ApiPostNotify;
import cn.yiidii.web.R;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     * @param body {@link CookieBody}
     * @return {@link R}
     */
    @ApiPostNotify
    @ApiOperation("小米运动刷新步数")
    @PostMapping("/mi-step")
    public R<?> miStep(@RequestBody @Validated MiBrushStepBody body) {
        singInService.miStep(body);
        return R.ok(null, StrUtil.format("成功刷新{}步", body.getStep()));
    }

    /**
     * 腾讯视频签到
     *
     * @param body {@link CookieBody}
     * @return {@link R}
     */
    @ApiPostNotify
    @ApiOperation("腾讯视频签到")
    @PostMapping("/tencent-video")
    public R<?> tencentVideo(@RequestBody CookieBody body) {
        String cookie = body.getCookie();
        if (StrUtil.isBlank(cookie)) {
            throw new IllegalArgumentException("cookie不能为空");
        }

        TencentVideoSignInResponseDTO ret = singInService.tencentVideo(body.getCookie());
        return R.ok(null, StrUtil.format("签到成功, 获取{}V力值", ret.getCheckinScore()));
    }

    /**
     * 时光相册签到
     *
     * @param body {@link CookieBody}
     * @return {@link R}
     */
    @ApiPostNotify
    @ApiOperation("时光相册签到")
    @PostMapping("/ever-photo")
    public R<?> everPhoto(@RequestBody CookieBody body) {
        String xTtToken = body.getToken();
        if (StrUtil.isBlank(xTtToken)) {
            throw new IllegalArgumentException("token不能为空");
        }

        EverPhotoSignInResponseDTO resp = singInService.everPhoto(body.getToken());
        Integer continuity = resp.getData().getContinuity();
        Long totalReward = resp.getData().getTotalReward();
        return R.ok(null, StrUtil.format("签到成功，当前连续签到{}天，共获取{}MB空间", continuity, totalReward / 1024 / 1024));
    }

    /**
     * 叮咚买菜签到
     *
     * @param body {@link CookieBody}
     * @return {@link R}
     */
    @ApiPostNotify
    @ApiOperation("叮咚买菜签到")
    @PostMapping("/dingdong")
    public R<?> dingDong(@RequestBody CookieBody body) {
        String cookie = body.getCookie();
        if (StrUtil.isBlank(cookie)) {
            throw new IllegalArgumentException("cookie不能为空");
        }

        DingDongSignInResponseDTO resp = singInService.dingDong(cookie);
        return R.ok(null, StrUtil.format("签到成功, 获取{}积分", resp.getData().getPointNum()));
    }

}
