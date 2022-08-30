package cn.yiidii.lab.system.model.dto;

import lombok.Data;

/**
 * LoginRequestDTO
 *
 * @author ed w
 * @since 1.0
 */
@Data
public class LoginBody {
    private String username;
    private String password;
}
