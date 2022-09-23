package cn.yiidii.lab.system.model.constant;

import cn.yiidii.base.domain.enums.IEnum;
import lombok.AllArgsConstructor;

/**
 * SysUseConfigEnum
 *
 * @author ed w
 * @since 1.0
 */
@AllArgsConstructor
public enum SysUseConfigEnum implements IEnum {

    CK_TENCENT_VIDEO("ck_tencent_video", "腾讯视频Cookie"),
    CK_DINGDONG("ck_ding_dong", "叮咚买菜Cookie"),
    ;

    private String key;
    private String name;

    @Override
    public Integer code() {
        return 0;
    }

    @Override
    public String desc() {
        return name;
    }

    public String getKey() {
        return this.key;
    }
}
