package cn.yiidii.lab.system.model.dto;

import cn.yiidii.lab.system.model.enums.UserSource;
import lombok.Data;

/**
 * SysUserVO
 *
 * @author ed w
 * @since 1.0
 */
@Data
public class SysUserVO {

    private Long id;
    private String uuid;
    private UserSource source;
    private String username;
    private String nickname;
    private String avatar;
    private String email;
}
