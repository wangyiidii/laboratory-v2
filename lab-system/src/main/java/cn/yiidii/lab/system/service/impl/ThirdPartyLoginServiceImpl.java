package cn.yiidii.lab.system.service.impl;

import cn.yiidii.lab.system.model.dto.SysUserInfoDTO;
import cn.yiidii.lab.system.model.dto.ThirdPartyLoginBody;
import cn.yiidii.lab.system.model.enums.SysExceptionCode;
import cn.yiidii.lab.system.model.enums.UserSource;
import cn.yiidii.lab.system.service.ISysUserService;
import cn.yiidii.lab.system.service.IThirdPartyLoginService;
import cn.yiidii.base.exception.BizException;
import com.xkcoding.justauth.AuthRequestFactory;
import lombok.RequiredArgsConstructor;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthRequest;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 三方登录
 *
 * @author ed w
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class ThirdPartyLoginServiceImpl implements IThirdPartyLoginService {

    private final AuthRequestFactory factory;
    private final ISysUserService sysUserService;

    @Override
    public SysUserInfoDTO login(String type, ThirdPartyLoginBody loginBody) {
        UserSource userSource = UserSource.get(type);
        if (Objects.isNull(userSource)) {
            throw new BizException(SysExceptionCode.UNSUPPORTED_PLATFORM);
        }

        AuthRequest authRequest = factory.get(type);
        AuthCallback authCallback = new AuthCallback();
        authCallback.setCode(loginBody.getCode());
        authCallback.setState(loginBody.getState());

        AuthResponse<AuthUser> response = authRequest.login(authCallback);
        if (response.getCode() != 2000) {
            throw new BizException(response.getMsg());
        }

        // 添加用户并返回
        AuthUser authUser = response.getData();
        SysUserInfoDTO sysUserInfoDTO = sysUserService.addUser(authUser);
        return sysUserInfoDTO;
    }
}
