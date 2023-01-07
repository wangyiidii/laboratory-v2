package cn.yiidii.lab.system.api.feign;

import cn.yiidii.base.core.service.ConfigService;
import cn.yiidii.lab.system.api.feign.fallback.ConfigFeignFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 配置
 *
 * @author ed w
 * @since 1.0
 */
@FeignClient(name = "lab-system", fallback = ConfigFeignFallback.class)
public interface ConfigFeign extends ConfigService {

    @Override
    @GetMapping("/config")
    String get(@RequestParam("key") String key,
               @RequestParam("dft") String dft);
}
