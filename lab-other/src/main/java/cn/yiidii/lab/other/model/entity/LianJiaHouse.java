package cn.yiidii.lab.other.model.entity;

import cn.yiidii.base.domain.entity.SuperEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 链家房间
 *
 * @author ed w
 * @since 1.0
 */
@Data
@TableName("lianjia_house")
@Accessors(chain = true)
public class LianJiaHouse extends SuperEntity<Long> {

    private String houseCode;
    private String picture;
    private String title;
    private BigDecimal price;
    private Integer provinceId;
    private Integer cityId;
    private Integer districtId;
    private String address;
    private BigDecimal area;
    private String orientation;
    private String type;
    private String tags;
    private LocalDate lastMaintainTime;
    private LocalDate date;

}
