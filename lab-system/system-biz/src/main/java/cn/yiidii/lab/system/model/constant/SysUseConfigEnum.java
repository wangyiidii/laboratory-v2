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

    SIGNIN_CK_TENCENT_VIDEO("signin_ck_tencent_video", "腾讯视频Cookie"),
    SIGNIN_CK_DINGDONG("signin_ck_ding_dong", "叮咚买菜Cookie"),
    SIGNIN_CK_EVER_PHOTO("signin_ck_ever_photo", "时光相册Cookie"),
    SIGNIN_CFG_IIOS("signin_cfg_iios", "IIOS配置"),
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
