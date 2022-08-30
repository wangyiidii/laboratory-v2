package cn.yiidii.lab.system.mapper;

import cn.yiidii.lab.system.model.entity.SysMenu;
import cn.yiidii.lab.system.model.entity.SysPermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * SysMenuMapper
 *
 * @author ed w
 * @since 1.0
 */
@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    List<SysMenu> selectByUid(@Param("uid") Long uid);
}
