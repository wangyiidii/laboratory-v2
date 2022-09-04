package cn.yiidii.apiplatform.model.dto;

import cn.yiidii.base.annotation.Sensitive;
import cn.yiidii.base.domain.enums.SensitiveStrategy;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 联通用量差异
 *
 * @author ed w
 * @since 1.0
 */
@Data
@Accessors(chain = true)
public class LtUsageDiffDTO {

    @Sensitive(strategy = SensitiveStrategy.PHONE)
    private String phoneNumber;
    private String pkgName;

    /**
     * 包内总共用量（包含了定向和通用，如果不知道定向的话，其实是没什么用）
     */
    private BigDecimal sum;
    /**
     * 包内流量
     */
    private Pkg pkg;
    /**
     * 免费流量
     */
    private BigDecimal free;

    /**
     * 跳点
     */
    private BigDecimal diff;

    /**
     * 上次查询时间
     */
    private LocalDateTime lastTime;

    /**
     * 包内流量
     */
    @Data
    @Accessors(chain = true)
    public static class Pkg {

        /**
         * 通用流量
         */
        private BigDecimal generic;

        /**
         * 定向流量
         */
        private BigDecimal direction;
    }
}
