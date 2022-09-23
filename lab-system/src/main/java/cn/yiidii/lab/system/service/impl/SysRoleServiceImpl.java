package cn.yiidii.lab.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.yiidii.base.domain.enums.Status;
import cn.yiidii.lab.system.model.body.SysRoleSaveBody;
import cn.yiidii.lab.system.model.dto.*;
import cn.yiidii.lab.system.model.entity.*;
import cn.yiidii.lab.system.model.enums.ResourceType;
import cn.yiidii.lab.system.model.enums.SysExceptionCode;
import cn.yiidii.lab.system.model.vo.SysMenuVO;
import cn.yiidii.lab.system.model.vo.SysPermissionVO;
import cn.yiidii.lab.system.model.vo.SysRoleQueryParam;
import cn.yiidii.lab.system.model.vo.SysUserVO;
import cn.yiidii.lab.system.service.*;
import cn.yiidii.lab.system.mapper.SysRoleMapper;
import cn.yiidii.web.PageQuery;
import cn.yiidii.base.exception.BizException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * SysRoleServiceImpl
 *
 * @author ed w
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    private final ISysUserService userService;
    private final ISysRoleUserService roleUserService;
    private final ISysMenuService menuService;
    private final ISysPermissionService permissionService;
    private final ISysRoleResourceService roleResourceService;

    @Override
    public void addRole(SysRoleSaveBody sysRoleSaveBody) {
        String roleCode = sysRoleSaveBody.getCode();

        SysRole sysRoleDB = lambdaQuery().eq(SysRole::getCode, roleCode).one();
        if (Objects.nonNull(sysRoleDB)) {
            Status status = sysRoleDB.getStatus();
            if (status.in(Status.ENABLED, Status.DISABLED)) {
                throw new BizException(SysExceptionCode.ROLE_CODE_EXIST);
            }

            // 恢复删除的
            SysRole sysRole = BeanUtil.toBean(sysRoleSaveBody, SysRole.class);
            sysRole.setId(sysRoleDB.getId());
            sysRole.setStatus(Status.ENABLED);

            this.updateById(sysRole);
        } else {
            // 插入
            SysRole sysRole = BeanUtil.toBean(sysRoleSaveBody, SysRole.class);
            this.save(sysRole);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRole(SysRoleSaveBody sysRoleSaveBody) {
        SysRole sysRole = BeanUtil.toBean(sysRoleSaveBody, SysRole.class);
        this.updateById(sysRole);
    }

    @Override
    public void deleteRole(List<Long> ids) {
        List<Object> exRoleIds = new LinkedList<>();
        for (Long id : ids) {
            try {
                this.deleteRole(id);
            } catch (Exception e) {
                exRoleIds.add(id);
            }
        }

        if (!exRoleIds.isEmpty()) {
            List<String> roleNames = this.lambdaQuery().in(SysRole::getId, exRoleIds).list().stream().map(SysRole::getName).collect(Collectors.toList());
            throw new BizException(SysExceptionCode.ROLE_USER_EXIST, roleNames);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRole(Long id) {
        Long count = roleUserService.getBaseMapper()
                .selectCount(new QueryWrapper<SysRoleUser>().in("role_id", id));
        if (count > 0) {
            throw new BizException(SysExceptionCode.ROLE_USER_EXIST);
        }

        SysRole sysRole = SysRole.builder().id(id).status(Status.DELETED).build();
        this.updateById(sysRole);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeStatus(Long id, Status status) {
        SysRole sysRole = SysRole.builder().id(id).status(status).build();
        this.updateById(sysRole);
    }

    @Override
    public void bindUser(Long roleId, List<Long> userIds) {

        SysRole sysRole = this.getById(roleId);
        if (Objects.isNull(sysRole)) {
            throw new BizException(SysExceptionCode.ROLE_NOT_EXIST);
        }

        // 先过滤下userIds的有效性
        if (CollUtil.isNotEmpty(userIds)) {
            userIds = userService.lambdaQuery()
                    .select(SysUser::getId)
                    .in(SysUser::getId, userIds).list()
                    .stream().map(SysUser::getId)
                    .collect(Collectors.toList());
        }

        // 原来角色绑定的用户 key: userId, value: roleUser
        List<SysRoleUser> sysUsersDB = roleUserService.lambdaQuery().eq(SysRoleUser::getRoleId, roleId).list();
        Map<Long, SysRoleUser> sysUsersMapDB = sysUsersDB.stream().collect(Collectors.toMap(e -> e.getUserId(), e -> e));

        List<Long> delRoleUserIds = new LinkedList<>(sysUsersMapDB.keySet());
        delRoleUserIds.removeAll(userIds);

        List<Long> insertUserIds = new LinkedList<>(userIds);
        insertUserIds.removeAll(sysUsersMapDB.keySet());
        List<SysRoleUser> insertRoleUsers = insertUserIds.stream()
                .map(userId -> new SysRoleUser(roleId, userId)).collect(Collectors.toList());

        if (delRoleUserIds.size() > 0) {
            roleUserService.getBaseMapper().delete(new QueryWrapper<SysRoleUser>().eq("role_id", roleId).in("user_id", delRoleUserIds));
        }

        if (insertRoleUsers.size() > 0) {
            roleUserService.saveBatch(insertRoleUsers);
        }

    }

    @Override
    public void bindMenu(Long roleId, List<Long> menuIds) {
        Assert.isTrue(CollUtil.isNotEmpty(menuIds), "请选择菜单");

        SysRole sysRole = this.getById(roleId);
        if (Objects.isNull(sysRole)) {
            throw new BizException(SysExceptionCode.ROLE_NOT_EXIST);
        }

        menuIds = menuService.lambdaQuery()
                .select(SysMenu::getId)
                .in(SysMenu::getId, menuIds).list()
                .stream().map(SysMenu::getId)
                .collect(Collectors.toList());
        Assert.isTrue(CollUtil.isNotEmpty(menuIds), "所选菜单无效");

        List<SysRoleResource> resourcesDB = roleResourceService.getByType(roleId, ResourceType.MENU);
        Map<Long, SysRoleResource> resourceMapDB = resourcesDB.stream().collect(Collectors.toMap(e -> e.getResId(), e -> e));

        List<Long> delRoleResourceIds = new LinkedList<>(resourceMapDB.keySet());
        delRoleResourceIds.removeAll(menuIds);

        List<Long> insertRoleResourceIds = new LinkedList<>(menuIds);
        insertRoleResourceIds.removeAll(resourceMapDB.keySet());
        List<SysRoleResource> insertRoleResources = insertRoleResourceIds.stream().map(menuId -> new SysRoleResource(roleId, menuId, ResourceType.MENU)).collect(Collectors.toList());

        if (delRoleResourceIds.size() > 0) {
            roleResourceService.getBaseMapper().delete(new QueryWrapper<SysRoleResource>().eq("role_id", roleId).in("res_id", delRoleResourceIds));
        }

        if (insertRoleResources.size() > 0) {
            roleResourceService.saveBatch(insertRoleResources);
        }

    }

    @Override
    public void bindPermission(Long roleId, List<Long> permissionIds) {
        SysRole sysRole = this.getById(roleId);
        if (Objects.isNull(sysRole)) {
            throw new BizException(SysExceptionCode.ROLE_NOT_EXIST);
        }

        if (CollUtil.isNotEmpty(permissionIds)) {
            permissionIds = permissionService.lambdaQuery()
                    .select(SysPermission::getId)
                    .in(SysPermission::getId, permissionIds)
                    .list()
                    .stream().map(SysPermission::getId)
                    .collect(Collectors.toList());
        }

        List<SysRoleResource> rolePermissionsDB = roleResourceService.lambdaQuery()
                .eq(SysRoleResource::getRoleId, roleId)
                .eq(SysRoleResource::getType, ResourceType.PERMISSION)
                .list();
        Map<Long, SysRoleResource> rolePermissionsMapDB = rolePermissionsDB.stream().collect(Collectors.toMap(SysRoleResource::getResId, e -> e));


        List<Long> delPermissionIds = new LinkedList<>(rolePermissionsMapDB.keySet());
        delPermissionIds.removeAll(permissionIds);

        List<Long> insertRoleResources = new LinkedList<>(permissionIds);
        insertRoleResources.removeAll(rolePermissionsMapDB.keySet());
        List<SysRoleResource> insertRoleUsers = insertRoleResources.stream()
                .map(permissionId -> new SysRoleResource(roleId, permissionId, ResourceType.PERMISSION)).collect(Collectors.toList());

        if (delPermissionIds.size() > 0) {
            roleResourceService.getBaseMapper().delete(new QueryWrapper<SysRoleResource>().eq("role_id", roleId).in("res_id", delPermissionIds));
        }

        if (insertRoleUsers.size() > 0) {
            roleResourceService.saveBatch(insertRoleUsers);
        }
    }

    @Override
    public List<SysUserVO> getUserByRoleId(Long roleId) {
        List<SysUser> sysUsers = this.baseMapper.selectUserByRoleId(roleId);
        return BeanUtil.copyToList(sysUsers, SysUserVO.class);
    }

    @Override
    public List<SysMenuVO> getMenuByRoleId(Long roleId) {
        List<SysMenu> sysMenus = this.baseMapper.selectMenuByRoleId(roleId);
        return BeanUtil.copyToList(sysMenus, SysMenuVO.class);
    }

    @Override
    public List<SysPermissionVO> getPermissionByRoleId(Long roleId) {
        List<SysPermission> sysMenus = this.baseMapper.selectPermissionByRoleId(roleId);
        return BeanUtil.copyToList(sysMenus, SysPermissionVO.class);
    }

    @Override
    public Page<SysRole> listRole(SysRoleQueryParam sysRoleQueryParam, PageQuery pageQuery) {
        return this.getBaseMapper().selectPage(pageQuery.build(), sysRoleQueryParam.buildQueryWrapper());
    }

    @Override
    public SysRoleInfoDTO getRoleById(Long id) {
        return BeanUtil.toBean(this.getById(id), SysRoleInfoDTO.class);
    }

}
