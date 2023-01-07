package cn.yiidii.lab.system.model.entity;

import cn.yiidii.base.domain.entity.Entity;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * SysConfig
 *
 * @author ed w
 * @since 1.0
 */
@Data
public class SysConfig extends Entity<Long> {

    private String name;
    @TableField("`key`")
    private String key;
    private String value;
    private String remark;
    @TableField("`readonly_`")
    private Integer readOnly;
}
