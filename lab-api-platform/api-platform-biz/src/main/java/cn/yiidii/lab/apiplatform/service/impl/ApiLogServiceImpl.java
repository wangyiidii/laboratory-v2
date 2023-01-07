package cn.yiidii.lab.apiplatform.service.impl;

import cn.yiidii.lab.apiplatform.mapper.ApiLogMapper;
import cn.yiidii.lab.apiplatform.model.entity.ApiLog;
import cn.yiidii.lab.apiplatform.service.IApiLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * Api调用日志
 *
 * @author ed w
 * @since 1.0
 */
@Service
public class ApiLogServiceImpl extends ServiceImpl<ApiLogMapper, ApiLog> implements IApiLogService {

}
