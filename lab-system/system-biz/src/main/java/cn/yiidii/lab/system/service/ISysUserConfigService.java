package cn.yiidii.lab.system.service;

import cn.yiidii.lab.system.model.entity.SysUserConfig;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * ISysUserConfigService
 *
 * @author ed w
 * @since 1.0
 */
public interface ISysUserConfigService extends IService<SysUserConfig> {

    SysUserConfig getConfigByUserId(Long userId, String key);

}
