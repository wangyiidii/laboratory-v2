package cn.yiidii.lab.system.api.feign.fallback;

import cn.yiidii.base.exception.BizException;
import cn.yiidii.lab.system.api.feign.ConfigFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author ed w
 * @since 1.0
 */
@Slf4j
@Component
public class ConfigFeignFallback implements ConfigFeign {

    @Override
    public String get(String key, String dft) {
        throw new NullPointerException("连接配置服务器异常");
    }
}
