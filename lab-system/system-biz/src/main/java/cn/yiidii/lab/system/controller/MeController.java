package cn.yiidii.lab.system.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import cn.yiidii.lab.system.model.dto.SysPermissionInfoDTO;
import cn.yiidii.lab.system.model.dto.SysRoleInfoDTO;
import cn.yiidii.lab.system.model.dto.SysUserInfoDTO;
import cn.yiidii.lab.system.model.vo.RouterVo;
import cn.yiidii.lab.system.service.ISysUserService;
import cn.yiidii.base.R;
import cn.yiidii.auth.satoken.LoginHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * MeController
 *
 * @author ed w
 * @since 1.0
 */
@Slf4j
@SaCheckLogin
@RestController
@RequestMapping("/sys/user/current")
@RequiredArgsConstructor
public class MeController {

    private final ISysUserService sysUserService;

    /**
     * 获取当前用户的角色
     *
     * @return
     */
    @GetMapping("/info")
    public R<?> currentUserInfo() {
        SysUserInfoDTO sysUserInfoDTO = sysUserService.selectUserInfoByUsername(LoginHelper.getLoginUser().getUsername());
        return R.ok(sysUserInfoDTO);
    }

    /**
     * 获取当前用户的角色
     *
     * @return
     */
    @GetMapping("/role")
    public R<?> currentUserRole() {
        long id = StpUtil.getLoginIdAsLong();
        List<SysRoleInfoDTO> roles = sysUserService.getRoleByUserId(id);
        return R.ok(roles);
    }

    @GetMapping("/permission")
    public R<?> currentUserPermission() {
        long id = StpUtil.getLoginIdAsLong();
        List<SysPermissionInfoDTO> permissionInfos = sysUserService.getPermissionByUserId(id);
        return R.ok(permissionInfos);
    }

    @GetMapping("/router")
    public R<?> currentUserRouter() {
        List<RouterVo> router = sysUserService.getRouter();
        return R.ok(router);
    }


}
