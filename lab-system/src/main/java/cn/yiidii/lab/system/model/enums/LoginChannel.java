package cn.yiidii.lab.system.model.enums;

import cn.yiidii.base.domain.enums.IEnum;
import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.Objects;

/**
 * LoginType
 *
 * @author ed w
 * @since 1.0
 */
@AllArgsConstructor
public enum LoginChannel implements IEnum {

    USERNAME_PASSWORD(10, "用户名密码"),
    EMAIL_PASSWORD(20, "邮箱密码"),
    ;

    @EnumValue
    private Integer channel;
    private String desc;

    @Override
    public Integer code() {
        return this.channel;
    }

    @Override
    public String desc() {
        return this.desc;
    }

    public static LoginChannel get(Integer channel) {
        return getOrDefault(channel, null);
    }

    public static LoginChannel getOrDefault(Integer channel, LoginChannel dft) {
        if (Objects.isNull(channel)) {
            return dft;
        }
        return Arrays.stream(values()).parallel().filter(e -> e.channel.equals(channel)).findFirst().orElse(dft);
    }
}
