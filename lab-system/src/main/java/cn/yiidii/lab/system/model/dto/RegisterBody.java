package cn.yiidii.lab.system.model.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * RegisterRequestDTO
 *
 * @author ed w
 * @since 1.0
 */
@Data
public class RegisterBody {

    @NotNull(message = "用户名不能为空")
    private String username;
    @NotNull(message = "昵称不能为空")
    private String nickname;
    @NotNull(message = "密码不能为空")
    private String textPassword;
    private String avatar;
    @NotNull(message = "邮箱不能为空")
    private String email;
}
