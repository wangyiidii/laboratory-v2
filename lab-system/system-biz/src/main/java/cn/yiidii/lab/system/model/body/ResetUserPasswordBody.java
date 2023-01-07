package cn.yiidii.lab.system.model.body;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 重置密码表单
 *
 * @author ed w
 * @since 1.0
 */
@Data
public class ResetUserPasswordBody {

    @NotNull(message = "密码不能为空")
    private String password;
}
