package cn.yiidii.lab.system.model.enums;

import cn.hutool.core.util.StrUtil;
import cn.yiidii.base.domain.enums.IEnum;
import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;

import java.util.Arrays;

/**
 * UserSource
 *
 * @author ed w
 * @since 1.0
 */
@AllArgsConstructor
public enum UserSource implements IEnum {
    SYSTEM(0, "系统"),
    GITEE(10, "Gitee");

    @EnumValue
    private Integer code;
    private String desc;


    @Override
    public Integer code() {
        return this.code;
    }

    @Override
    public String desc() {
        return this.desc;
    }

    public static UserSource get(String name) {
        if(StrUtil.isBlank(name)) {
            return null;
        }
        return Arrays.stream(UserSource.values()).filter(s -> s.name().equalsIgnoreCase(name)).findAny().orElseGet(null);
    }
}
