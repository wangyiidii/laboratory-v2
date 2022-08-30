package cn.yiidii.lab.system.model.dto;

import cn.yiidii.web.annotation.Add;
import cn.yiidii.web.annotation.Update;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * SysPermissionSaveBody
 *
 * @author ed w
 * @since 1.0
 */
@Data
public class SysPermissionSaveBody {

    @NotNull(message = "id不能为空", groups = Update.class)
    private Long id;

    @NotNull(message = "菜单id不能为空", groups = Add.class)
    private Long menuId;

    @NotNull(message = "菜单名称不能为空", groups = Add.class)
    private String name;

    @NotNull(message = "权限编码不能为空", groups = Add.class)
    private String code;

    private String desc;


}
