package cn.yiidii.web.controller.admin;

import cn.dev33.satoken.stp.StpUtil;
import cn.yiidii.lab.system.model.dto.LoginBody;
import cn.yiidii.lab.system.model.dto.LoginSuccessVO;
import cn.yiidii.lab.system.model.dto.RegisterBody;
import cn.yiidii.lab.system.model.enums.LoginChannel;
import cn.yiidii.lab.system.service.ILoginService;
import cn.yiidii.web.R;
import cn.yiidii.base.exception.BizException;
import cn.yiidii.web.support.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * LoginController
 *
 * @author ed w
 * @since 1.0
 */
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class LoginController {
    private final ILoginService loginService;

    @PostMapping("/login")
    public R<?> login(@RequestBody @Validated LoginBody loginBody) {
        LoginUser loginUser = loginService.usernamePasswordLogin(loginBody.getUsername(), loginBody.getPassword());
        return R.ok(loginUser, "登录成功");
    }

    @PostMapping("/logout")
    public R<?> logout() {
        StpUtil.logout();
        return R.ok(null, "注销成功");
    }

}
