package cn.yiidii.lab.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.yiidii.base.domain.enums.Status;
import cn.yiidii.lab.system.model.constant.SysConst;
import cn.yiidii.lab.system.model.dto.RegisterBody;
import cn.yiidii.lab.system.model.entity.SysUser;
import cn.yiidii.lab.system.model.enums.SysExceptionCode;
import cn.yiidii.lab.system.model.enums.UserSource;
import cn.yiidii.lab.system.service.IRegisterService;
import cn.yiidii.lab.system.service.ISysUserService;
import cn.yiidii.base.exception.BizException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Objects;

/**
 * IRegisterServiceImpl
 *
 * @author ed w
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class RegisterServiceImpl implements IRegisterService {

    private final ISysUserService sysUserService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void regUser(RegisterBody registerBody) {
        // 邮箱格式校验
        String email = registerBody.getEmail();
        Assert.isTrue(ReUtil.isMatch(SysConst.REGX_MAIL, email), "邮箱格式不正确");

        // 用户名校验
        SysUser sysUserDB = sysUserService.lambdaQuery()
                .eq(SysUser::getUsername, registerBody.getUsername())
                .one();
        if (Objects.nonNull(sysUserDB) && !sysUserDB.getStatus().equals(Status.DELETED)) {
            throw new BizException(SysExceptionCode.USERNAME_EXIST);
        }

        // 邮箱校验
        sysUserDB = sysUserService.lambdaQuery()
                .eq(SysUser::getEmail, registerBody.getEmail())
                .notIn(SysUser::getStatus, Status.DELETED).one();
        if (Objects.nonNull(sysUserDB)) {
            throw new BizException(SysExceptionCode.EMAIL_EXIST);
        }

        // 插入数据库
        SysUser sysUser = BeanUtil.toBean(registerBody, SysUser.class);
        sysUser.setPassword(DigestUtil.sha256Hex(registerBody.getTextPassword()));
        sysUser.setSource(UserSource.SYSTEM);
        sysUserService.save(sysUser);
    }

}
