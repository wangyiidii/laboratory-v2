package cn.yiidii.lab.system.model.vo;

import cn.yiidii.base.domain.entity.TreeEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * @author ed w
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SysMenuVO extends TreeEntity<SysMenuVO, Long> {

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
