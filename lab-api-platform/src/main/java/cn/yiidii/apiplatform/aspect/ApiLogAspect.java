package cn.yiidii.apiplatform.aspect;

import cn.yiidii.apiplatform.model.entity.ApiLog;
import cn.yiidii.apiplatform.service.IApiLogService;
import cn.yiidii.base.exception.BaseException;
import cn.yiidii.boot.base.BaseAspect;
import cn.yiidii.boot.util.ThreadUtil;
import cn.yiidii.web.R;
import cn.yiidii.web.support.ServletUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * Api日志切面
 *
 * @author ed w
 * @since 1.0
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class ApiLogAspect extends BaseAspect {

    private final IApiLogService apiLogService;

    @AfterReturning(value = "within(cn.yiidii.web.controller.apiplatform.*)", returning = "ret")
    public void afterReturning(Object ret) {
        if (ret instanceof R) {
            R<?> r = (R<?>) ret;
            this.saveApiLogAsync(String.valueOf(r.getCode()), r.getMsg());
        }
    }

    @AfterThrowing(value = "within(cn.yiidii.web.controller.apiplatform.*)", throwing = "ex")
    public void afterReturning(Exception ex) {
        if (ex instanceof BaseException) {
            BaseException exception = (BaseException) ex;
            this.saveApiLogAsync(String.valueOf(exception.getCode()), exception.getMessage());
        }
    }

    private void saveApiLogAsync(String code, String msg) {
        ApiLog apiLog = ApiLog.builder()
                .url(ServletUtil.getRequest().getRequestURI())
                .ip(ServletUtil.getClientIP())
                .location(ServletUtil.getLocation())
                .code(code)
                .msg(msg)
                .build();
        ThreadUtil.execute(() -> apiLogService.save(apiLog));
    }
}
