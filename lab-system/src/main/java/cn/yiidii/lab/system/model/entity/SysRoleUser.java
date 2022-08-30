package cn.yiidii.lab.system.model.entity;

import cn.yiidii.base.domain.entity.SuperEntity;
import lombok.Data;

/**
 * @author ed w
 * @since 1.0
 */
@Data
public class SysRoleUser extends SuperEntity<Long> {
    private Long roleId;
    private Long userId;

    public SysRoleUser( Long roleId, Long userId) {
        this.roleId = roleId;
        this.userId = userId;
    }
}
