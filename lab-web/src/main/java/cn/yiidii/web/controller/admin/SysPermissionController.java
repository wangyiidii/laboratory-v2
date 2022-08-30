package cn.yiidii.web.controller.admin;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.bean.BeanUtil;
import cn.yiidii.lab.system.model.dto.SysPermissionInfoDTO;
import cn.yiidii.lab.system.model.dto.SysPermissionSaveBody;
import cn.yiidii.lab.system.model.entity.SysMenu;
import cn.yiidii.lab.system.model.entity.SysPermission;
import cn.yiidii.lab.system.service.ISysPermissionService;
import cn.yiidii.web.R;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * SysPermissionController
 *
 * @author ed w
 * @since 1.0
 */
@SaCheckLogin
@RestController
@RequestMapping("/sys/permission")
@RequiredArgsConstructor
public class SysPermissionController {

    private final ISysPermissionService sysPermissionService;

    @GetMapping("/{menuId}/list")
    public R<?> listPerms(@PathVariable Long menuId) {
        List<SysPermission> menuList = sysPermissionService.lambdaQuery().eq(SysPermission::getMenuId, menuId).list();
        return R.ok(BeanUtil.copyToList(menuList, SysPermissionInfoDTO.class));
    }

    @GetMapping("/{id}")
    public R<?> getPermissionById(@PathVariable Long id) {
        SysPermission sysPermission = sysPermissionService.getById(id);
        return R.ok(BeanUtil.toBean(sysPermission, SysPermissionInfoDTO.class));
    }

    @SaCheckPermission("sys:permission:add")
    @PostMapping
    public R<?> addPermission(@RequestBody SysPermissionSaveBody saveBody) {
        sysPermissionService.addPermission(saveBody);
        return R.ok();
    }

    @SaCheckPermission("sys:permission:edit")
    @PutMapping
    public R<?> updatePermission(@RequestBody SysPermissionSaveBody saveBody) {
        sysPermissionService.updateById(BeanUtil.toBean(saveBody, SysPermission.class));
        return R.ok();
    }

    @SaCheckPermission("sys:permission:delete")
    @DeleteMapping
    public R<?> deleteRole(@RequestBody @Validated @NotEmpty(message = "权限id不能为空") List<Long> ids) {
        sysPermissionService.deletePermissionBatch(ids);
        return R.ok();
    }

}
