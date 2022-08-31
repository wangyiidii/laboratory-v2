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

    COOKIE_EXPIRED(200001, "Cookie已失效"),
    ALREADY_SIGN_IN(200002, "今日已签到"),
    UN_TENCENT_VIP(200003, "不是vip会员"),

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
