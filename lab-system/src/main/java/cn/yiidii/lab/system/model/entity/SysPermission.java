package cn.yiidii.lab.system.model.entity;

import cn.yiidii.base.domain.entity.Entity;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * SysPermission
 *
 * @author ed w
 * @since 1.0
 */
@Data
public class SysPermission extends Entity<Long> {

    private Long menuId;
    private String code;
    private String name;
    @TableField("`desc`")
    private String desc;
    @TableField("`readonly_`")
    private Integer  readOnly;
}
