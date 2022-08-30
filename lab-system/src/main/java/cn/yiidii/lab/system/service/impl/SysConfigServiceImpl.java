package cn.yiidii.lab.system.service.impl;

import cn.yiidii.base.core.service.ConfigService;
import cn.yiidii.lab.system.mapper.SysConfigMapper;
import cn.yiidii.lab.system.mapper.SysPermissionMapper;
import cn.yiidii.lab.system.model.entity.SysConfig;
import cn.yiidii.lab.system.model.entity.SysPermission;
import cn.yiidii.lab.system.service.ISysConfigService;
import cn.yiidii.lab.system.service.ISysPermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * SysConfigServiceImpl
 *
 * @author ed w
 * @since 1.0
 */
@Service
public class SysConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfig> implements ISysConfigService, ConfigService {

    @Override
    public String get(String key, String dft) {
        SysConfig config = this.lambdaQuery().eq(SysConfig::getKey, key).one();
        if (Objects.isNull(config)) {
            return dft;
        }
        return config.getValue();
    }
}
