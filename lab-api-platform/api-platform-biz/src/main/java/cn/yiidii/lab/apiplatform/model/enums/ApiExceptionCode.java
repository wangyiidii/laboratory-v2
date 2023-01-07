package cn.yiidii.lab.apiplatform.model.enums;

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
    VIDEO_PARSE_PLATFORM_NOT_SUPPORT(200005, "暂未支持该平台"),
    VIDEO_PARSE_EXCEPTION(200006, "解析异常，请检查链接是否正确"),

    ;

    private final int code;
    private final String message;

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String getMsg() {
        return this.message;
    }
}
