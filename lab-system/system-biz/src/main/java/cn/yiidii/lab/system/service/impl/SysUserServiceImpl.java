package cn.yiidii.lab.system.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.yiidii.base.domain.enums.Status;
import cn.yiidii.base.util.TreeUtil;
import cn.yiidii.lab.system.mapper.SysMenuMapper;
import cn.yiidii.lab.system.mapper.SysRoleMapper;
import cn.yiidii.lab.system.mapper.SysUserMapper;
import cn.yiidii.lab.system.model.body.SysUserSaveBody;
import cn.yiidii.lab.system.model.dto.*;
import cn.yiidii.lab.system.model.entity.SysMenu;
import cn.yiidii.lab.system.model.entity.SysUser;
import cn.yiidii.lab.system.model.enums.UserSource;
import cn.yiidii.lab.system.model.vo.MetaVO;
import cn.yiidii.lab.system.model.vo.RouterVo;
import cn.yiidii.lab.system.model.vo.SysUserQueryParam;
import cn.yiidii.lab.system.service.ISysUserService;
import cn.yiidii.web.PageQuery;
import cn.yiidii.auth.satoken.LoginHelper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.model.AuthUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * SysUserServiceImpl
 *
 * @author ed w
 * @since 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    private final SysRoleMapper sysRoleMapper;
    private final SysMenuMapper sysMenuMapper;

    @Override
    public SysUserInfoDTO addUser(AuthUser authUser) {
        if (Objects.isNull(authUser)) {
            throw new IllegalArgumentException("authUser cannot be null");
        }

        String username = authUser.getUsername();
        UserSource userSource = UserSource.get(authUser.getSource());
        String uuid = authUser.getUuid();

        // 先根据 source + uuid + username
        SysUser userDB = this.lambdaQuery()
                .eq(SysUser::getSource, userSource)
                .eq(SysUser::getUuid, uuid)
                .eq(SysUser::getUsername, username)
                .one();
        if (Objects.isNull(userDB)) {
            // 再判断用户名唯一
            do {
                userDB = this.lambdaQuery().eq(SysUser::getUsername, username).one();
            } while (Objects.nonNull(userDB));

            if (Objects.isNull(userDB)) {
                // 用户名重复了就在username后面拼接上随机6位数字，再添加
                username = StrUtil.format("{}-{}", username, RandomUtil.randomNumbers(6));
                authUser.setUsername(username);
            } else {
                return this.selectUserInfoByUsername(username);
            }
        } else {

        }
        SysUser user = BeanUtil.toBean(authUser, SysUser.class);
        user.setEmail("");
        user.setPassword(DigestUtil.sha256Hex("123465"));
        this.save(user);
        return this.selectUserInfoBySourceAndUUId(UserSource.get(authUser.getSource()), authUser.getUuid());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(SysUserSaveBody saveBody) {
        Assert.isTrue(Objects.nonNull(saveBody.getId()), "用户id不能为空");
        SysUser sysUser = BeanUtil.toBean(saveBody, SysUser.class);
        this.updateById(sysUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(Long id) {
        SysUser sysUser = SysUser.builder().id(id).status(Status.DELETED).build();
        this.updateById(sysUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetPassword(Long id, String password) {
        this.getBaseMapper().resetPassword(id, DigestUtil.sha256Hex(password));
        StpUtil.logout(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeStatus(Long id, Status status) {
        this.getBaseMapper().changeStatus(id, status.code());
    }

    @Override
    public SysUserInfoDTO selectUserInfoByUsername(String username) {
        SysUserInfoDTO sysUserInfoDTO = this.getBaseMapper().selectUserInfoByUsername(username);
        if (Objects.nonNull(sysUserInfoDTO)) {
            List<SysPermissionInfoDTO> permissions = this.getPermissionByUserId(sysUserInfoDTO.getId());
            sysUserInfoDTO.setPermissions(permissions);
        }
        return sysUserInfoDTO;
    }

    @Override
    public SysUserInfoDTO selectUserInfoById(Long id) {
        SysUserInfoDTO sysUserInfoDTO = this.getBaseMapper().selectUserInfoByUid(id);
        if (Objects.nonNull(sysUserInfoDTO)) {
            List<SysPermissionInfoDTO> permissions = sysRoleMapper.selectRolePermissionByUserId(sysUserInfoDTO.getId());
            sysUserInfoDTO.setPermissions(permissions);
        }
        return sysUserInfoDTO;
    }

    @Override
    public SysUserInfoDTO selectUserInfoBySourceAndUUId(UserSource userSource, String uuid) {
        SysUserInfoDTO sysUserInfoDTO = this.getBaseMapper().selectUserInfoBySourceAndUUID(userSource.code().toString(), uuid);
        if (Objects.nonNull(sysUserInfoDTO)) {
            List<SysPermissionInfoDTO> permissions = sysRoleMapper.selectRolePermissionByUserId(sysUserInfoDTO.getId());
            sysUserInfoDTO.setPermissions(permissions);
        }
        return sysUserInfoDTO;
    }

    @Override
    public List<SysRoleInfoDTO> getRoleByUserId(Long userId) {
        return sysRoleMapper.selectRoleByUserId(userId);
    }

    @Override
    public List<SysPermissionInfoDTO> getPermissionByUserId(Long userId) {
        if (LoginHelper.isAdmin(userId)) {
            SysPermissionInfoDTO adminPerm = new SysPermissionInfoDTO(0L, 0L, "*", "所有权限", "管理员权限", 0);
            return List.of(adminPerm);
        }
        return sysRoleMapper.selectRolePermissionByUserId(userId);
    }

    @Override
    public List<RouterVo> getRouter() {
        List<SysMenu> sysMenus;
        if (LoginHelper.isAdmin()) {
            sysMenus = sysMenuMapper.selectList(new QueryWrapper<>());
        } else {
            sysMenus = sysMenuMapper.selectByUid(StpUtil.getLoginIdAsLong());
        }
        sysMenus = sysMenus.stream().filter(Objects::nonNull).collect(Collectors.toList());
        if (Objects.isNull(sysMenus)) {
            return new ArrayList<>();
        }
        List<RouterVo> routerVos = sysMenus.stream().map(m -> {
            RouterVo routerVo = BeanUtil.toBean(m, RouterVo.class);
            routerVo.setMeta(new MetaVO(m.getName(), m.getIcon()));
            routerVo.setStatus(null);
            routerVo.setCreateTime(null);
            routerVo.setCreatedBy(null);
            routerVo.setUpdateTime(null);
            routerVo.setUpdatedBy(null);
            return routerVo;
        }).collect(Collectors.toList());
        return TreeUtil.buildTree(routerVos);
    }

    @Override
    public Page<SysUser> listUser(SysUserQueryParam sysUserQueryParam, PageQuery pageQuery) {
        Page<SysUser> sysUserPage = this.getBaseMapper().selectPageUserList(pageQuery.build(),
                sysUserQueryParam.buildQueryWrapper());
        return sysUserPage;
    }

}
