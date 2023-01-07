package cn.yiidii.lab.system.service;

import cn.yiidii.lab.system.model.body.SysPermissionSaveBody;
import cn.yiidii.lab.system.model.entity.SysPermission;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * ISysPermissionService
 *
 * @author ed w
 * @since 1.0
 */
public interface ISysPermissionService extends IService<SysPermission> {

    void addPermission(SysPermissionSaveBody saveBody);
    void deletePermissionBatch(List<Long> ids);

    void deletePermission(Long id);
}
