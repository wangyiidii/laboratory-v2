package cn.yiidii.lab.system.model.entity;

import cn.yiidii.base.domain.entity.TreeEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * @author ed w
 * @since 1.0
 */
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_menu")
@ApiModel(value = "Menu", description = "菜单")
@AllArgsConstructor
public class SysMenu extends TreeEntity<SysMenu, Long> {

    @ApiModelProperty(value = "资源code")
    @TableField(value = "code")
    private String code;

    @ApiModelProperty(value = "路径")
    private String path;

    @ApiModelProperty(value = "组件")
    private String component;

    @ApiModelProperty(value = "菜单图标")
    private String icon;

    @ApiModelProperty(value = "是否缓存该页面: 1:是  0:不是")
    private String keepAlive;

    @ApiModelProperty(value = "重定向")
    private String redirect;

    @ApiModelProperty(value = "是否隐藏")
    private String hidden;

    @ApiModelProperty(value = "是否外链")
    private String target;

    private Boolean isDefault;
}
