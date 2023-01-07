package cn.yiidii.lab.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.yiidii.lab.system.mapper.SysPermissionMapper;
import cn.yiidii.lab.system.model.body.SysPermissionSaveBody;
import cn.yiidii.lab.system.model.entity.SysPermission;
import cn.yiidii.lab.system.model.entity.SysRoleResource;
import cn.yiidii.lab.system.model.enums.ResourceType;
import cn.yiidii.lab.system.model.enums.SysExceptionCode;
import cn.yiidii.lab.system.service.ISysPermissionService;
import cn.yiidii.lab.system.service.ISysRoleResourceService;
import cn.yiidii.base.exception.BizException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * SysPermissionServiceImpl
 *
 * @author ed w
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements ISysPermissionService {

    private final ISysRoleResourceService sysRoleResourceService;

    @Override
    public void addPermission(SysPermissionSaveBody saveBody) {
        String code = saveBody.getCode();
        Long count = this.lambdaQuery().eq(SysPermission::getCode, code).count();
        if (count > 0) {
            throw new BizException(SysExceptionCode.PERMISSION_CODE_EXIST);
        }

        this.save(BeanUtil.toBean(saveBody, SysPermission.class));
    }

    @Override
    public void deletePermissionBatch(List<Long> ids) {
        ids.removeIf(Objects::isNull);
        List<Map> errorRets = new LinkedList<>();
        for (Long id : ids) {
            try {
                this.deletePermission(id);
            } catch (BizException e) {
                errorRets.add(Map.of(id, e.getMessage()));
            } catch (Exception e) {
                errorRets.add(Map.of(id, "未知错误"));
            }
        }

        if (errorRets.size() > 0) {
            throw new BizException(SysExceptionCode.PERMISSION_DELETE_EX, errorRets);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePermission(Long id) {
        SysPermission sysPermission = this.getById(id);
        if (Objects.isNull(sysPermission)) {
            throw new BizException(SysExceptionCode.PERMISSION_NOT_EXIST_OR_DELETED);
        }

        Long count = sysRoleResourceService.lambdaQuery()
                .eq(SysRoleResource::getResId, id)
                .eq(SysRoleResource::getType, ResourceType.PERMISSION)
                .count();
        if (count > 0) {
            throw new BizException(SysExceptionCode.PERMISSION_NOT_EXIST_OR_DELETED);
        }

        this.baseMapper.deleteById(id);
    }
}
