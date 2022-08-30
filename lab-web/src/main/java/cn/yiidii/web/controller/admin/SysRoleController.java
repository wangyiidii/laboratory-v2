package cn.yiidii.web.controller.admin;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.yiidii.base.domain.enums.Status;
import cn.yiidii.lab.system.model.dto.*;
import cn.yiidii.lab.system.service.ISysRoleService;
import cn.yiidii.web.PageQuery;
import cn.yiidii.web.R;
import cn.yiidii.web.annotation.Update;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * SysRoleController
 *
 * @author ed w
 * @since 1.0
 */
@Validated
@SaCheckLogin
@RestController
@RequestMapping("/sys/role")
@RequiredArgsConstructor
public class SysRoleController {

    public final ISysRoleService sysRoleService;

    @GetMapping("/list")
    public R<?> listRole(SysRoleQueryParam sysUserQueryParam, PageQuery pageQuery) {
        return R.ok(sysRoleService.listRole(sysUserQueryParam, pageQuery));
    }

    @GetMapping("/{id}")
    public R<?> getRole(@PathVariable Long id) {
        return R.ok(sysRoleService.getRoleById(id));
    }

    @SaCheckPermission("sys:role:add")
    @PostMapping
    public R<?> add(@RequestBody @Validated SysRoleSaveBody sysRoleSaveBody) {
        sysRoleService.addRole(sysRoleSaveBody);
        return R.ok(null, "新建角色成功");
    }

    @SaCheckPermission("sys:role:edit")
    @PutMapping("")
    public R<?> updateRole(@RequestBody @Validated SysRoleSaveBody sysRoleSaveBody) {
        sysRoleService.updateRole(sysRoleSaveBody);
        return R.ok(null, "保存成功");
    }

    @SaCheckPermission("sys:role:edit")
    @PutMapping("/{id}/status/{status}")
    public R<?> changeStatus(@PathVariable @NotNull(message = "用户id不能为空") Long id,
                             @PathVariable @NotNull(message = "状态不能为空") Integer status) {
        sysRoleService.changeStatus(id, Status.get(status, Status.ENABLED));
        return R.ok();
    }

    @SaCheckPermission("sys:role:delete")
    @DeleteMapping
    public R<?> deleteRole(@RequestBody @Validated @NotNull(message = "角色id不能为空") @NotEmpty(message = "角色id不能为空") List<Long> ids) {
        sysRoleService.deleteRole(ids);
        return R.ok();
    }

    @SaCheckPermission("sys:role:edit")
    @PutMapping("/bindUser")
    public R<?> bindUser(@RequestBody @Validated(Update.class) SysRoleSaveBody sysRoleSaveBody) {
        sysRoleService.bindUser(sysRoleSaveBody.getId(), sysRoleSaveBody.getUserIds());
        return R.ok();
    }

    @SaCheckPermission("sys:role:edit")
    @PutMapping("/bindMenu")
    public R<?> bindMenu(@RequestBody @Validated(Update.class) SysRoleSaveBody sysRoleSaveBody) {
        sysRoleService.bindMenu(sysRoleSaveBody.getId(), sysRoleSaveBody.getMenuIds());
        return R.ok();
    }

    @SaCheckPermission("sys:role:edit")
    @PutMapping("/bindPermission")
    public R<?> bindPermission(@RequestBody @Validated(Update.class) SysRoleSaveBody sysRoleSaveBody) {
        sysRoleService.bindPermission(sysRoleSaveBody.getId(), sysRoleSaveBody.getPermissionIds());
        return R.ok();
    }

    @GetMapping("/{roleId}/user")
    public R<?> getRoleByRoleId(@PathVariable Long roleId) {
        List<SysUserVO> users = sysRoleService.getUserByRoleId(roleId);
        return R.ok(users);
    }

    @GetMapping("/{roleId}/menu")
    public R<?> getMenuByRoleId(@PathVariable Long roleId) {
        List<SysMenuVO> menus = sysRoleService.getMenuByRoleId(roleId);
        return R.ok(menus);
    }

    @GetMapping("/{roleId}/permission")
    public R<?> getPermissionByRoleId(@PathVariable Long roleId) {
        List<SysPermissionVO> menus = sysRoleService.getPermissionByRoleId(roleId);
        return R.ok(menus);
    }

}
