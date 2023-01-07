package cn.yiidii.lab.apiplatform.model.body;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 小米运动刷新步数
 *
 * @author ed w
 * @since 1.0
 */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MiBrushStepBody {

    @NotNull(message = "手机号不能为空")
    @Pattern(regexp = "^(?:(?:\\+|00)86)?1\\d{10}$", message = "手机号格式不正确")
    private String phone;

    @NotNull(message = "密码不能为空")
    private String password;

    @NotNull(message = "请填要刷的步数")
    @Min(value = 0, message = "请填写合理的步数")
    private Long step;

}
