package cn.yiidii.web.controller.admin;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import cn.yiidii.base.domain.enums.Status;
import cn.yiidii.base.exception.BizException;
import cn.yiidii.lab.system.model.entity.SysUser;
import cn.yiidii.lab.system.model.enums.SysExceptionCode;
import cn.yiidii.lab.system.service.ISysUserService;
import cn.yiidii.web.R;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;

/**
 * SessionManageController
 *
 * @author ed w
 * @since 1.0
 */
@RestController
@RequestMapping("/session")
@RequiredArgsConstructor
public class SessionManageController {

    public final ISysUserService sysUserService;

    /**
     * 注销一个用户
     *
     * @param id
     * @return
     */
    @SaCheckPermission("session:edit")
    @SaCheckLogin
    @PostMapping("/logout/{id}")
    public R<?> logoutSomeone(@PathVariable String id) {
        SysUser sysUser = sysUserService.getById(id);
        if (Objects.isNull(sysUser)) {
            throw new BizException(SysExceptionCode.USER_UNREGISTER);
        }
        StpUtil.logout(id);
        return R.ok(null, StrUtil.format("注销{}({})成功", sysUser.getNickname(), sysUser.getUsername()));
    }

    @SaCheckPermission("session:edit")
    @SaCheckLogin
    @PostMapping("/kickout/{id}")
    public R<?> kickoutSomeone(@PathVariable String id) {
        SysUser sysUser = sysUserService.getById(id);
        if (Objects.isNull(sysUser)) {
            throw new BizException(SysExceptionCode.USER_UNREGISTER);
        }
        StpUtil.kickout(id);
        return R.ok(null, StrUtil.format("踢人{}({})下线成功", sysUser.getNickname(), sysUser.getUsername()));
    }

    @SaCheckPermission("session:edit")
    @SaCheckLogin
    @PostMapping("/disable/{id}")
    public R<?> disableSomeone(@PathVariable String id, @RequestBody Map<String, Object> paramJo) {
        SysUser sysUser = sysUserService.getById(id);
        if (Objects.isNull(sysUser)) {
            throw new BizException(SysExceptionCode.USER_UNREGISTER);
        }

        // 判断是否禁用了
        boolean isDisable = StpUtil.isDisable(id) || sysUser.getStatus().equals(Status.DISABLED);
        if (isDisable) {
            throw new BizException(SysExceptionCode.USER_DISABLED);
        }

        long time = Long.parseLong(paramJo.getOrDefault("time", -1).toString());

        // 先注销
        StpUtil.kickout(id);

        // 再禁用
        StpUtil.disable(id, time);

        return R.ok(null, StrUtil.format("踢人{}({})下线成功", sysUser.getNickname(), sysUser.getUsername()));
    }

    @SaCheckPermission("session:edit")
    @SaCheckLogin
    @PostMapping("/untieDisable/{id}")
    public R<?> untieDisableSomeone(@PathVariable String id) {
        SysUser sysUser = sysUserService.getById(id);
        if (Objects.isNull(sysUser)) {
            throw new BizException(SysExceptionCode.USER_UNREGISTER);
        }

        // 判断是否禁用了
        boolean normal = !(StpUtil.isDisable(id) || sysUser.getStatus().equals(Status.DISABLED));
        if (normal) {
            throw new BizException(SysExceptionCode.USER_UN_DISABLED);
        }

        // 解禁
        StpUtil.untieDisable(id);

        return R.ok(null, StrUtil.format("解禁{}({})成功", sysUser.getNickname(), sysUser.getUsername()));
    }

}
