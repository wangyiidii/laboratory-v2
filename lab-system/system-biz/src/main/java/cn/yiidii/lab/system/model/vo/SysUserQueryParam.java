package cn.yiidii.lab.system.model.vo;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.yiidii.base.domain.enums.Status;
import cn.yiidii.lab.system.model.entity.SysUser;
import cn.yiidii.lab.system.model.enums.UserSource;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.Data;

import java.time.LocalDate;

/**
 * UserQueryParam
 *
 * @author ed w
 * @since 1.0
 */
@Data
public class SysUserQueryParam {

    private Long id;
    private UserSource source;
    private String username;
    private String nickname;
    private String email;
    private Long deptId;
    private LocalDate startTime;
    private LocalDate endTime;

    public Wrapper<SysUser> buildQueryWrapper() {
        QueryWrapper<SysUser> wrapper = Wrappers.query();
        wrapper.eq(ObjectUtil.isNotNull(id), "u.id", id)
                .like(StrUtil.isNotBlank(username), "u.username", username)
                .like(StrUtil.isNotBlank(email), "u.email", email)
                .between(startTime != null && endTime != null,
                        "u.create_time", startTime, endTime)
//                .and(ObjectUtil.isNotNull(deptId), w -> {
//                    List<SysDept> deptList = deptMapper.selectList(new LambdaQueryWrapper<SysDept>()
//                            .select(SysDept::getDeptId)
//                            .apply(DataBaseHelper.findInSet(user.getDeptId(), "ancestors")));
//                    List<Long> ids = StreamUtils.toList(deptList, SysDept::getDeptId);
//                    ids.add(user.getDeptId());
//                    w.in("u.dept_id", ids);
//                })
                .ne("u.status", Status.DELETED.getCode())
        ;
        return wrapper;
    }
}
