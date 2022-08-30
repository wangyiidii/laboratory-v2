package cn.yiidii.lab.system.model.entity;

import cn.yiidii.base.domain.entity.Entity;
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
public class SysRole extends Entity<Long> {
    private String code;
    private String name;
    private Integer readonly;
}
