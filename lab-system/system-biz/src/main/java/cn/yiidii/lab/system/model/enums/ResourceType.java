package cn.yiidii.lab.system.model.enums;

import cn.yiidii.base.domain.enums.IEnum;
import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;

import java.util.stream.Stream;

/**
 * ResourceType
 *
 * @author ed w
 * @since 1.0
 */
@AllArgsConstructor
public enum ResourceType implements IEnum {

    PERMISSION(10, "权限"),
    MENU(20, "菜单"),
    ;

    @EnumValue
    private Integer type;
    private String desc;

    @Override
    public Integer code() {
        return this.type;
    }

    @Override
    public String desc() {
        return this.desc;
    }

    public static ResourceType get(Integer val) {
        return get(val, null);
    }

    public static ResourceType get(int val, ResourceType def) {
        return Stream.of(values()).parallel().filter((item) -> item.type == val).findAny().orElse(def);
    }
}
