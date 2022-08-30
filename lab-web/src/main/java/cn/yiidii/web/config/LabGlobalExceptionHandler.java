package cn.yiidii.web.config;

import cn.dev33.satoken.exception.DisableLoginException;
import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import cn.yiidii.lab.system.model.enums.SysExceptionCode;
import cn.yiidii.web.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ed w
 * @since 1.0
 */
@Slf4j
@Configuration
public class LabGlobalExceptionHandler extends GlobalExceptionHandler {

    public LabGlobalExceptionHandler(HttpServletRequest request) {
        super(request);
    }

    @ExceptionHandler({NotLoginException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public R<?> notLoginException(NotLoginException ex) {
        log.error("请求地址: {}, 未登录: {}", request.getRequestURI(), ex.getMessage());
        log.debug("请求地址: {}, 未登录: {}", request.getRequestURI(), ex);
        return R.failed(SysExceptionCode.UNAUTHORIZED.getCode(), SysExceptionCode.UNAUTHORIZED.getMsg());
    }

    @ExceptionHandler({NotPermissionException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public R<?> noPermissionException(NotPermissionException ex) {
        log.error("请求地址: {}, 无权限: {}", request.getRequestURI(), ex.getMessage());
        log.debug("请求地址: {}, 无权限: {}", request.getRequestURI(), ex);
        return R.failed(SysExceptionCode.PERMISSION_FORBIDDEN.getCode(), SysExceptionCode.PERMISSION_FORBIDDEN.getMsg());
    }

    @ExceptionHandler({NotRoleException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public R<?> notRoleException(NotRoleException ex) {
        log.error("请求地址: {}, 无角色: {}", request.getRequestURI(), ex.getMessage());
        log.debug("请求地址: {}, 无角色: {}", request.getRequestURI(), ex);
        return R.failed(SysExceptionCode.ROLE_PERMISSION_FORBIDDEN.getCode(), SysExceptionCode.ROLE_PERMISSION_FORBIDDEN.getMsg());
    }

    @ExceptionHandler({DisableLoginException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public R<?> disableLoginException(DisableLoginException ex) {
        log.error("请求地址: {}, 此账号已被封禁, 限制登录: {}", request.getRequestURI(), ex.getMessage());
        log.debug("请求地址: {}, 此账号已被封禁, 限制登录: {}", request.getRequestURI(), ex);
        return R.failed(SysExceptionCode.USER_DISABLED.getCode(), SysExceptionCode.USER_DISABLED.getMsg());
    }
}
