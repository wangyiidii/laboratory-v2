package cn.yiidii.lab.system.service.impl;

import cn.yiidii.lab.system.model.entity.SysRoleResource;
import cn.yiidii.lab.system.mapper.SysRoleResourceMapper;
import cn.yiidii.lab.system.model.enums.ResourceType;
import cn.yiidii.lab.system.model.enums.SysExceptionCode;
import cn.yiidii.lab.system.service.ISysRoleResourceService;
import cn.yiidii.base.exception.BizException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * SysRoleResourceServiceImpl
 *
 * @author ed w
 * @since 1.0
 */
@Service
public class SysRoleResourceServiceImpl extends ServiceImpl<SysRoleResourceMapper, SysRoleResource> implements ISysRoleResourceService {

    @Override
    public List<SysRoleResource> getByType(Long roleId, ResourceType type) {
        if (Objects.isNull(type)) {
            throw new BizException(SysExceptionCode.UN_SUPPORTED_RESOURCE_TYPE);
        }

        return this.lambdaQuery()
                .eq(SysRoleResource::getRoleId, roleId)
                .eq(SysRoleResource::getType, type)
                .list();
    }
}
