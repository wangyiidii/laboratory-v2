package cn.yiidii.lab.system.mapper;

import cn.yiidii.lab.system.model.dto.SysPermissionInfoDTO;
import cn.yiidii.lab.system.model.dto.SysRoleInfoDTO;
import cn.yiidii.lab.system.model.entity.SysMenu;
import cn.yiidii.lab.system.model.entity.SysPermission;
import cn.yiidii.lab.system.model.entity.SysRole;
import cn.yiidii.lab.system.model.entity.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * SysRoleMapper
 *
 * @author ed w
 * @since 1.0
 */
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {

    /**
     * 根据用户id获取角色
     *
     * @param uid 用户id
     * @return 角色信息
     */
    List<SysRoleInfoDTO> selectRoleByUserId(@Param("uid") Long uid);

    /**
     * 根据用户id获取权限
     *
     * @param uid 用户id
     * @return 权限信息
     */
    List<SysPermissionInfoDTO> selectRolePermissionByUserId(@Param("uid") Long uid);

    List<SysUser> selectUserByRoleId(@Param("roleId") Long roleId);
    List<SysMenu> selectMenuByRoleId(@Param("roleId") Long roleId);

    List<SysPermission> selectPermissionByRoleId(@Param("roleId") Long roleId);
}
