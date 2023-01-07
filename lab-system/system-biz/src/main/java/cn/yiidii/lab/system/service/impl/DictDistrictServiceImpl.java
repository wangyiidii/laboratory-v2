package cn.yiidii.lab.system.service.impl;

import cn.yiidii.lab.system.mapper.DictDistrictMapper;
import cn.yiidii.lab.system.model.entity.DictDistrict;
import cn.yiidii.lab.system.service.IDictDistrictService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * DictDistrictServiceImpl
 *
 * @author ed w
 * @since 1.0
 */
@Service
public class DictDistrictServiceImpl extends ServiceImpl<DictDistrictMapper, DictDistrict> implements IDictDistrictService {

    @Override
    public DictDistrict getById(Integer id) {
        return this.lambdaQuery().eq(DictDistrict::getId, id).one();
    }

    @Override
    public List<DictDistrict> getByPid(Integer pid) {
        return this.lambdaQuery().eq(DictDistrict::getPid, pid).list();
    }

    @Override
    public DictDistrict get(Integer pid, String name) {
        return this.lambdaQuery().eq(DictDistrict::getPid, pid)
                .and(d -> d.eq(DictDistrict::getName, name).or().eq(DictDistrict::getExtName, name))
                .one();
    }
}
