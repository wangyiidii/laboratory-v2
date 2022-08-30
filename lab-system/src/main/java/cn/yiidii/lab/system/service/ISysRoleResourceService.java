package cn.yiidii.lab.system.service;

import cn.yiidii.lab.system.model.entity.SysRoleResource;
import cn.yiidii.lab.system.model.enums.ResourceType;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * ISysRoleResourceService
 *
 * @author ed w
 * @since 1.0
 */
public interface ISysRoleResourceService extends IService<SysRoleResource> {

    /**
     * 通过类型获取资源的id
     *
     * @param type  类型
     * @return 资源id
     */
    List<SysRoleResource> getByType(Long roleId, ResourceType type);
}
