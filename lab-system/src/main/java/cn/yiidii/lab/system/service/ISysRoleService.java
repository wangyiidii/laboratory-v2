package cn.yiidii.lab.system.service;

import cn.yiidii.base.domain.enums.Status;
import cn.yiidii.lab.system.model.body.SysRoleSaveBody;
import cn.yiidii.lab.system.model.dto.*;
import cn.yiidii.lab.system.model.entity.SysRole;
import cn.yiidii.lab.system.model.vo.SysMenuVO;
import cn.yiidii.lab.system.model.vo.SysPermissionVO;
import cn.yiidii.lab.system.model.vo.SysRoleQueryParam;
import cn.yiidii.lab.system.model.vo.SysUserVO;
import cn.yiidii.web.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * ISysRoleService
 *
 * @author ed w
 * @since 1.0
 */
public interface ISysRoleService extends IService<SysRole> {

    Page<SysRole> listRole(SysRoleQueryParam sysRoleQueryParam, PageQuery pageQuery);

    SysRoleInfoDTO getRoleById(Long id);

    /**
     * 添加角色
     *
     * @param sysRoleSaveBody 角色表单
     */
    void addRole(SysRoleSaveBody sysRoleSaveBody);

    void updateRole(SysRoleSaveBody sysRoleSaveBody);

    void deleteRole(List<Long> ids);

    void deleteRole(Long roleId);

    void changeStatus(Long roleId, Status status);

    void bindUser(Long roleId, List<Long> userIds);
    void bindMenu(Long roleId, List<Long> menuIds);

    void bindPermission(Long roleId, List<Long> permissionIds);

    List<SysUserVO> getUserByRoleId(Long roleId);
    List<SysMenuVO> getMenuByRoleId(Long roleId);
    List<SysPermissionVO> getPermissionByRoleId(Long roleId);
}
