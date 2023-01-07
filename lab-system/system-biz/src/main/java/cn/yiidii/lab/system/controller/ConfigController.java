package cn.yiidii.lab.system.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.extra.spring.SpringUtil;
import cn.yiidii.auth.config.AuthExceptionHandler;
import cn.yiidii.auth.consts.AuthExceptionCode;
import cn.yiidii.base.core.service.ConfigService;
import cn.yiidii.lab.system.model.entity.DictDistrict;
import cn.yiidii.lab.system.service.IDictDistrictService;
import cn.yiidii.redis.RedisUtils;
import cn.yiidii.base.R;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * ConfigController
 *
 * @author ed w
 * @since 1.0
 */
@RestController
@SaCheckLogin
@RequestMapping("/config")
@RequiredArgsConstructor
public class ConfigController {

    private final ConfigService configService;

    @GetMapping("")
    @SaCheckPermission("xx")
    public String getConfig(@RequestParam String key, @RequestParam String dft) {
        Map<String, AuthExceptionHandler> beansOfType = SpringUtil.getBeansOfType(AuthExceptionHandler.class);
        return configService.get(key, dft);
    }

    @GetMapping("redis")
    public R<?> redis() {
        RedisUtils.setCacheObject("test", "test" + System.currentTimeMillis());

        Object ss = RedisUtils.getCacheObject("test");
        return R.ok(ss);
    }


}
