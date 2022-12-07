package cn.yiidii.lab.other.service;

import cn.yiidii.lab.other.model.entity.LianJiaHouse;
import cn.yiidii.lab.other.model.vo.LianJiaHouseQueryParam;
import cn.yiidii.web.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * ILianjiaService
 *
 * @author ed w
 * @since 1.0
 */
public interface ILianjiaHouseService extends IService<LianJiaHouse> {

    void collect();

    Page<LianJiaHouse> listHousePage(LianJiaHouseQueryParam queryParam, PageQuery pageQuery);

    List<LianJiaHouse> getListByHouseCode(String houseCode);
}
