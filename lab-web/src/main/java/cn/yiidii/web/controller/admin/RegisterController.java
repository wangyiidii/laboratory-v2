package cn.yiidii.web.controller.admin;

import cn.dev33.satoken.stp.StpUtil;
import cn.yiidii.lab.system.model.dto.LoginBody;
import cn.yiidii.lab.system.model.dto.LoginSuccessVO;
import cn.yiidii.lab.system.model.dto.RegisterBody;
import cn.yiidii.lab.system.model.enums.LoginChannel;
import cn.yiidii.lab.system.service.ILoginService;
import cn.yiidii.lab.system.service.IRegisterService;
import cn.yiidii.web.R;
import cn.yiidii.base.exception.BizException;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * RegisterController
 *
 * @author ed w
 * @since 1.0
 */
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class RegisterController {
    private final IRegisterService registerService;

    @PostMapping("/register")
    public R<?> register(@RequestBody @Validated RegisterBody registerBody) {
        registerService.regUser(registerBody);
        return R.ok(null, "注册成功");
    }

}
