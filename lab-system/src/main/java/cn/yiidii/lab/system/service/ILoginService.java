package cn.yiidii.lab.system.service;

import cn.yiidii.lab.system.model.dto.LoginSuccessVO;
import cn.yiidii.web.support.LoginUser;

/**
 * ILoginService
 *
 * @author ed w
 * @since 1.0
 */
public interface ILoginService {

    /**
     * 用户名密码登录
     *
     * @param username     用户名
     * @param textPassword 明文密码
     * @return
     */
    LoginUser usernamePasswordLogin(String username, String textPassword);

}
