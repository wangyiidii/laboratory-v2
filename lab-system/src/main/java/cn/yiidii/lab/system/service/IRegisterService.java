package cn.yiidii.lab.system.service;

import cn.yiidii.lab.system.model.dto.RegisterBody;

/**
 * 注册
 *
 * @author ed w
 * @since 1.0
 */
public interface IRegisterService {


    /**
     * 注册用户
     *
     * @param registerBody 注册表单
     */
    void regUser(RegisterBody registerBody);


}
