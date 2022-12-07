package cn.yiidii.lab.other.model.dto;

import lombok.Data;

import java.util.List;

/**
 * LianJiaCollectConfigDTO
 *
 * @author ed w
 * @since 1.0
 */
@Data
public class LianJiaCollectConfigDTO {
    private String city;
    private Integer cityId;
    private String domain;
    private List<String> keywords;
}
