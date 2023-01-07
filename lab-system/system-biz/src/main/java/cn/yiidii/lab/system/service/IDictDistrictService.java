package cn.yiidii.lab.system.service;

import cn.yiidii.lab.system.model.entity.DictDistrict;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * IDictDistrictService
 *
 * @author ed w
 * @since 1.0
 */
public interface IDictDistrictService extends IService<DictDistrict> {

    DictDistrict getById(Integer id);
    List<DictDistrict> getByPid(Integer pid);

    DictDistrict get(Integer pid, String name);
}
