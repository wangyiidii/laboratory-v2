package cn.yiidii.lab.system.service.impl;

import cn.yiidii.base.domain.enums.Status;
import cn.yiidii.lab.system.mapper.SysUserConfigMapper;
import cn.yiidii.lab.system.model.entity.SysUserConfig;
import cn.yiidii.lab.system.service.ISysUserConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * SysUserConfigServiceImpl
 *
 * @author ed w
 * @since 1.0
 */
@Service
public class SysUserConfigServiceImpl extends ServiceImpl<SysUserConfigMapper, SysUserConfig> implements ISysUserConfigService {

    @Override
    public SysUserConfig getConfigByUserId(Long userId, String key) {
        return this.lambdaQuery().eq(SysUserConfig::getUserId, userId)
                .eq(SysUserConfig::getKey, key)
                .eq(SysUserConfig::getStatus, Status.ENABLED)
                .one();
    }
}
