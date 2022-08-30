package cn.yiidii.lab.system.model.dto;

import cn.yiidii.lab.system.model.constant.SysConst;
import cn.yiidii.web.annotation.Update;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * RoleAddDTO
 *
 * @author ed w
 * @since 1.0
 */
@Data
public class SysRoleSaveBody {

    @NotNull(message = "角色id不能为空", groups = Update.class)
    private Long id;

    @NotNull(message = "角色编码不能为空")
    @Pattern(regexp = SysConst.REGX_CODE, message = "角色编码格式不正确，必须包含数字、字母或下划线，且长度最小4位")
    private String code;

    @NotNull(message = "角色名称不能为空")
    @Length(min = 1, message = "角色名称不能为空")
    private String name;

    private List<Long> userIds;
    
    private List<Long> menuIds;

    private List<Long> permissionIds;
}
