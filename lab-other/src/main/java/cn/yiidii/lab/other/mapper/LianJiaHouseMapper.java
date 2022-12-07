package cn.yiidii.lab.other.mapper;

import cn.yiidii.lab.other.model.entity.LianJiaHouse;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * LianJiaMapper
 *
 * @author ed w
 * @since 1.0
 */
@Mapper
public interface LianJiaHouseMapper extends BaseMapper<LianJiaHouse> {

    int batchSaveOrUpdateOnDuplicateUpdate(@Param("list") List<LianJiaHouse> list);

    Page<LianJiaHouse> selectListPage(@Param("page") Page<LianJiaHouse> page, @Param(Constants.WRAPPER) Wrapper<LianJiaHouse> queryWrapper);

}
