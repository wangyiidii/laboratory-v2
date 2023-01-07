package cn.yiidii.lab.system.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * SysPermissionInfoDTO
 *
 * @author ed w
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysPermissionInfoDTO {
    private Long id;
    private Long menuId;
    private String code;
    private String name;
    private String desc;
    private Integer readOnly;
}
