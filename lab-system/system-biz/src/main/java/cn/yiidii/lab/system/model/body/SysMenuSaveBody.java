package cn.yiidii.lab.system.model.body;

import cn.yiidii.web.annotation.Add;
import cn.yiidii.web.annotation.Update;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author ed w
 * @since 1.0
 */
@Data
public class SysMenuSaveBody {

    @NotNull(message = "id不能为空", groups = Update.class)
    private Long id;

    @NotNull(message = "父级菜单不能为空", groups = Add.class)
    private Long parentId;

    @NotNull(message = "菜单名称不能为空", groups = Add.class)
    private String name;

    @NotNull(message = "菜单路径不能为空", groups = Add.class)
    private String path;

    @NotNull(message = "菜单组件不能为空", groups = Add.class)
    private String component;

    @NotNull(message = "菜单图标不能为空", groups = Add.class)
    private String icon;

}
