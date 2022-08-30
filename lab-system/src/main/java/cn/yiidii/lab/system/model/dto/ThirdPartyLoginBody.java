package cn.yiidii.lab.system.model.dto;

import lombok.Data;

/**
 * 三方登录body
 *
 * @author ed w
 * @since 1.0
 */
@Data
public class ThirdPartyLoginBody {
    private String state;
    private String code;
}
