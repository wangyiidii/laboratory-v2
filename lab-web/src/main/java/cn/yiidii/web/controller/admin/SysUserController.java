package cn.yiidii.web.controller.admin;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.yiidii.lab.system.model.body.ChangeStatusBody;
import cn.yiidii.lab.system.model.body.ResetUserPasswordBody;
import cn.yiidii.lab.system.model.vo.SysUserQueryParam;
import cn.yiidii.lab.system.model.body.SysUserSaveBody;
import cn.yiidii.lab.system.service.ISysUserService;
import cn.yiidii.web.PageQuery;
import cn.yiidii.web.R;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

/**
 * SysUserController
 *
 * @author ed w
 * @since 1.0
 */
@Slf4j
@SaCheckLogin
@RestController
@RequestMapping("/sys/user")
@RequiredArgsConstructor
public class SysUserController {

    private final ISysUserService sysUserService;

    @GetMapping("/list")
    public R<?> listUser(SysUserQueryParam sysUserQueryParam, PageQuery pageQuery) {
        return R.ok(sysUserService.listUser(sysUserQueryParam, pageQuery));
    }

    @GetMapping("/{id}")
    public R<?> getUserById(@PathVariable Long id) {
        return R.ok(sysUserService.selectUserInfoById(id));
    }

    @SaCheckPermission("sys:user:edit")
    @PutMapping("/{id}")
    public R<?> updateUser(@PathVariable Long id,
                           @RequestBody @Validated SysUserSaveBody body) {
        body.setId(id);
        sysUserService.updateUser(body);
        return R.ok();
    }

    @SaCheckPermission("sys:user:delete")
    @DeleteMapping("/{id}")
    public R<?> updateUser(@PathVariable Long id) {
        sysUserService.deleteUser(id);
        return R.ok();
    }

    @SaCheckPermission("sys:user:edit")
    @PutMapping("/{id}/reset-password")
    public R<?> resetPassword(@PathVariable @NotNull(message = "用户id不能为空") Long id,
                              @RequestBody ResetUserPasswordBody body) {
        sysUserService.resetPassword(id, body.getPassword());
        return R.ok(null, "密码重置成功");
    }

    @SaCheckPermission("sys:user:edit")
    @PutMapping("/{id}/change-status")
    public R<?> changeStatus(@PathVariable @NotNull(message = "用户id不能为空") Long id,
                             @RequestBody ChangeStatusBody body) {
        sysUserService.changeStatus(id, body.getStatus());
        return R.ok();
    }

}
