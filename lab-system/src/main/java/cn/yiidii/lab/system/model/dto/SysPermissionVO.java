package cn.yiidii.lab.system.model.dto;

import lombok.Data;

/**
 * SysPermissionVO
 *
 * @author ed w
 * @since 1.0
 */
@Data
public class SysPermissionVO {

    private Long id;
    private Long menuId;
    private String code;
    private String name;
    private String desc;
    private Integer  readOnly;
}
