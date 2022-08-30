package cn.yiidii.lab.system.service;

import cn.yiidii.base.domain.enums.Status;
import cn.yiidii.lab.system.model.dto.*;
import cn.yiidii.lab.system.model.entity.SysUser;
import cn.yiidii.lab.system.model.enums.UserSource;
import cn.yiidii.web.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import me.zhyd.oauth.model.AuthUser;

import java.util.List;

/**
 * ISysUserService
 *
 * @author ed w
 * @since 1.0
 */
public interface ISysUserService extends IService<SysUser> {

    SysUserInfoDTO addUser(AuthUser authUser);

    void updateUser(SysUserSaveBody saveBody);

    void deleteUser(Long id);

    void resetPassword(Long id, String password);

    void changeStatus(Long id, Status status);

    /**
     * 根据用户名获取用户信息
     *
     * @param username 用户名
     * @return 用户信息（包含角色、权限信息）
     */
    SysUserInfoDTO selectUserInfoByUsername(String username);

    SysUserInfoDTO selectUserInfoById(Long id);

    /**
     * 根据平台和uuid获取用户信息（一般三方登录会用到）
     *
     * @param userSource {@link cn.yiidii.lab.system.model.enums.UserSource}
     * @param uuid   uuid
     * @return 用户信息（包含角色、权限信息）
     */
    SysUserInfoDTO selectUserInfoBySourceAndUUId(UserSource userSource, String uuid);

    /**
     * 根据用户ID获取角色
     *
     * @param userId 用户ID
     * @return 用户角色信息
     */
    List<SysRoleInfoDTO> getRoleByUserId(Long userId);

    /**
     * 根据用户ID获取权限信息
     *
     * @param userId 用户ID
     * @return 权限信息
     */
    List<SysPermissionInfoDTO> getPermissionByUserId(Long userId);

    List<RouterVo> getRouter();

    Page<SysUser> listUser(SysUserQueryParam sysUserQueryParam, PageQuery pageQuery);

}
