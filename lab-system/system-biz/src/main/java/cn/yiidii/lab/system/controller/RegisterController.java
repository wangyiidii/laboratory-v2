package cn.yiidii.lab.system.controller;

import cn.yiidii.lab.system.model.body.RegisterBody;
import cn.yiidii.lab.system.service.IRegisterService;
import cn.yiidii.base.R;
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
