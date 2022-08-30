package cn.yiidii.lab.system.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author ed w
 * @since 1.0
 */
@Data
@Accessors(chain = true)
public class LoginSuccessVO {

    private String token;
    private SysUserInfoDTO userInfo;
}
