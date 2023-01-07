package cn.yiidii.lab.system.model.entity;

import cn.yiidii.base.domain.entity.Entity;
import cn.yiidii.lab.system.model.enums.ResourceType;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * SysRoleResource
 *
 * @author ed w
 * @since 1.0
 */
@Data
@NoArgsConstructor
public class SysRoleResource extends Entity<Long> {
    private Long roleId;
    @TableField("res_id")
    private Long resId;
    private ResourceType type;

    public SysRoleResource(Long roleId, Long resId, ResourceType type) {
        this.roleId = roleId;
        this.resId = resId;
        this.type = type;
    }
}
