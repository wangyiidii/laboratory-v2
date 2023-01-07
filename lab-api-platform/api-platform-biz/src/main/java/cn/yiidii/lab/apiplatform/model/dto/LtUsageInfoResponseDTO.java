package cn.yiidii.lab.apiplatform.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

/**
 * LtAccountInfo
 *
 * @author ed w
 * @since 1.0
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class LtUsageInfoResponseDTO {

    private String packageName;
    private List<Resource> resources;
    private String time;
    private Summary summary;

    @Data
    @Accessors(chain = true)
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Summary {

        private BigDecimal freeFlow;
        private BigDecimal sum;
    }

    @Data
    @Accessors(chain = true)
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Resource {

        private String type;
        private BigDecimal userResource;
        private BigDecimal remainResource;
        private List<Detail> details;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain = true)
    public static class Detail {

        private String addUpItemName;
        private String endDate;
        private String feePolicyName;

        private BigDecimal use;
        private BigDecimal remain;
        private BigDecimal usedPercent;
        private String resourceType;

    }

}