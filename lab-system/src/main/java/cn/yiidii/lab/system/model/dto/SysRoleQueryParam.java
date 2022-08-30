package cn.yiidii.lab.system.model.dto;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.yiidii.base.domain.enums.Status;
import cn.yiidii.lab.system.model.entity.SysRole;
import cn.yiidii.lab.system.model.entity.SysUser;
import cn.yiidii.lab.system.model.enums.UserSource;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * SysRoleQueryParam
 *
 * @author ed w
 * @since 1.0
 */
@Data
public class SysRoleQueryParam {


    private String code;
    private String name;
    private LocalDate startTime;
    private LocalDate endTime;

    public Wrapper<SysRole> buildQueryWrapper() {
        QueryWrapper<SysRole> wrapper = Wrappers.query();
        wrapper
                .like(StrUtil.isNotBlank(code), "code", code)
                .like(StrUtil.isNotBlank(name), "name", name)
                .between(startTime != null && endTime != null,
                        "create_time", startTime, endTime)
                .ne("status", Status.DELETED.getCode())
                .orderByDesc("create_time")
        ;
        return wrapper;
    }
}
