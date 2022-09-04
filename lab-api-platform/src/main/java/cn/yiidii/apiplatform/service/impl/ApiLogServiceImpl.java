package cn.yiidii.apiplatform.service.impl;

import cn.yiidii.apiplatform.mapper.ApiLogMapper;
import cn.yiidii.apiplatform.model.entity.ApiLog;
import cn.yiidii.apiplatform.service.IApiLogService;
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
