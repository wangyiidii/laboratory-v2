package cn.yiidii.lab.system.model.entity;

import cn.yiidii.base.domain.entity.Entity;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * SysUserConfig
 *
 * @author ed w
 * @since 1.0
 */
@Data
public class SysUserConfig extends Entity<Long> {

    private String name;
    private Long userId;
    @TableField("`key`")
    private String key;
    private String value;
}
