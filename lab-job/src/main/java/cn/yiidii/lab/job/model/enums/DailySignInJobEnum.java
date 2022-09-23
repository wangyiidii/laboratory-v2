package cn.yiidii.lab.job.model.enums;

import cn.hutool.core.util.StrUtil;
import cn.yiidii.base.domain.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * DailySignInJobEnum
 *
 * @author ed w
 * @date 2022/9/23 11:28
 */
@Getter
@AllArgsConstructor
public enum DailySignInJobEnum implements IEnum {

    TENCENT_VIDEO("腾讯视频"),
    DINGDONG("叮咚买菜");

    private String desc;

    @Override
    public Integer code() {
        return 0;
    }

    @Override
    public String desc() {
        return desc;
    }

    public static DailySignInJobEnum get(String name) {
        return getOrDefaultByName(name, null);
    }

    public static DailySignInJobEnum getOrDefaultByName(String name, DailySignInJobEnum dft) {
        if (StrUtil.isBlank(name)) {
            return dft;
        }
        return Arrays.stream(values()).parallel().filter(e -> e.name().equals(name)).findFirst().orElse(dft);
    }

}
