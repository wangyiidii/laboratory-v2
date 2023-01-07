package cn.yiidii.lab.system.service.impl;

import cn.yiidii.lab.system.model.entity.SysRoleUser;
import cn.yiidii.lab.system.service.ISysRoleUserService;
import cn.yiidii.lab.system.mapper.SysRoleUserMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * SysRoleUserServiceImpl
 * 
 * @author ed w
 * @since 1.0
 */
@Service
public class SysRoleUserServiceImpl extends ServiceImpl<SysRoleUserMapper, SysRoleUser> implements ISysRoleUserService {
}
