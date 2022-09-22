package cn.yiidii.apiplatform.model.enums;

import cn.yiidii.base.exception.code.BaseExceptionCode;
import lombok.AllArgsConstructor;

/**
 * ApiExceptionCode
 *
 * @author ed w
 * @since 1.0
 */
@AllArgsConstructor
public enum ApiExceptionCode implements BaseExceptionCode {

    COOKIE_EXPIRED(200001, "登录状态已失效"),
    ALREADY_SIGN_IN(200002, "今日已签到"),
    UN_TENCENT_VIP(200003, "不是vip会员"),
    COOKIE_EXPIRED_LT(200004, "用户异地登录，请重新尝试登录手厅"),

    ;

    private int code;
    private String message;

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String getMsg() {
        return this.message;
    }
}
