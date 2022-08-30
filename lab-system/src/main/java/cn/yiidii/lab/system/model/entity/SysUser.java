package cn.yiidii.lab.system.model.entity;

import cn.yiidii.base.domain.entity.Entity;
import cn.yiidii.lab.system.model.enums.UserSource;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * SysUser
 *
 * @author ed w
 * @since 1.0
 */
@Data
@SuperBuilder
@NoArgsConstructor
public class SysUser extends Entity<Long> {

    private String uuid;
    private UserSource source;
    private String username;
    private String nickname;
    private String password;
    private String avatar;
    private String email;

}
