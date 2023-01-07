package cn.yiidii.lab.apiplatform.config;

import cn.yiidii.base.R;
import cn.yiidii.base.util.JsonUtils;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

/**
 * @author ed w
 * @since 1.0
 */
@Slf4j
@Configuration
public class GlobalExceptionHandler extends cn.yiidii.web.config.GlobalExceptionHandler {

    @PostConstruct
    public void init(){
        System.out.println(" api GlobalExceptionHandler");
    }

    public GlobalExceptionHandler(HttpServletRequest request) {
        super(request);
    }

    @ExceptionHandler(FeignException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public R<?> feignException(FeignException ex) {
        return JsonUtils.parseObject(ex.contentUTF8(), R.class);
    }

}
