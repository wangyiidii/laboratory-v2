package cn.yiidii.lab.system.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.yiidii.base.domain.enums.Status;
import cn.yiidii.lab.system.model.dto.SysUserInfoDTO;
import cn.yiidii.lab.system.model.entity.SysUser;
import cn.yiidii.lab.system.model.enums.SysExceptionCode;
import cn.yiidii.lab.system.service.ILoginService;
import cn.yiidii.lab.system.service.ISysUserService;
import cn.yiidii.base.exception.BizException;
import cn.yiidii.web.satoken.LoginHelper;
import cn.yiidii.web.support.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Objects;

/**
 * @author ed w
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements ILoginService {

    private final ISysUserService sysUserService;

    @Override
    public LoginUser usernamePasswordLogin(String username, String textPassword) {
        Assert.isTrue(StrUtil.isNotBlank(username), "用户名不能为空");
        Assert.isTrue(StrUtil.isNotBlank(textPassword), "密码不能为空");

        // 用户名校验
        SysUser sysUserDB = sysUserService.lambdaQuery()
                .eq(SysUser::getUsername, username)
                .ne(SysUser::getStatus, Status.DELETED)
                .one();
        if (Objects.isNull(sysUserDB)) {
            throw new BizException(SysExceptionCode.USER_UNREGISTER);
        }
        // 状态
        if (sysUserDB.getStatus().equals(Status.DISABLED)) {
            throw new BizException(SysExceptionCode.USER_DISABLED);
        }
        // 密码校验
        if (!StrUtil.equals(DigestUtil.sha256Hex(textPassword), sysUserDB.getPassword())) {
            throw new BizException(SysExceptionCode.PASSWORD_INCORRECT);
        }

        // 登录成功
        SysUserInfoDTO sysUserInfoDTO = sysUserService.selectUserInfoByUsername(username);
        LoginUser loginUser = sysUserInfoDTO.toLoginUser();
        LoginHelper.login(loginUser);

        return loginUser;
    }
}
