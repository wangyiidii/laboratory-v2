package cn.yiidii.lab.system.mapper;

import cn.yiidii.lab.system.model.dto.SysUserInfoDTO;
import cn.yiidii.lab.system.model.entity.SysUser;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * SysUserMapper
 *
 * @author ed w
 * @since 1.0
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    SysUserInfoDTO selectUserInfoByUsername(@Param("username") String username);

    SysUserInfoDTO selectUserInfoBySourceAndUUID(@Param("source") String source, @Param("uuid") String uuid);

    SysUserInfoDTO selectUserInfoByUid(@Param("uid") Long uid);

    Page<SysUser> selectPageUserList(@Param("page") Page<SysUser> page, @Param(Constants.WRAPPER) Wrapper<SysUser> queryWrapper);

    void resetPassword(@Param("id") Long id, @Param("password") String password);

    void changeStatus(@Param("id") Long id, @Param("status") Integer status);

}
