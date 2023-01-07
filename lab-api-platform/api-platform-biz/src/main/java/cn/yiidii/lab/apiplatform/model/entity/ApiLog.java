package cn.yiidii.lab.apiplatform.model.entity;

import cn.yiidii.base.domain.entity.SuperEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * 接口调用日志
 *
 * @author ed w
 * @since 1.0
 */
@Data
@SuperBuilder
@AllArgsConstructor
@TableName("api_api_log")
public class ApiLog extends SuperEntity<Long> {

    private String url;
    private String ip;
    private String location;
    private String code;
    private String msg;

}
