package cn.yiidii.lab.system.service;

import cn.yiidii.lab.system.model.dto.SysUserInfoDTO;
import cn.yiidii.lab.system.model.body.ThirdPartyLoginBody;

/**
 * OAuthService
 *
 * @author ed w
 * @date 2022/7/29 16:50
 */
public interface IThirdPartyLoginService {

    /**
     * 三方登录
     *
     * @param type      类型
     * @param loginBody 登录参数
     * @return SysUserInfoDTO
     */
    SysUserInfoDTO login(String type, ThirdPartyLoginBody loginBody);

}
