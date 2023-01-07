package cn.yiidii.lab.apiplatform.model.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

/**
 * TencentVideoSignInResponseDTO
 *
 * @author ed w
 * @since 1.0
 */
@Data
public class TencentVideoSignInResponseDTO {
    private Integer ret;
    private String msg;
    @JsonAlias(value = "checkin_score")
    private String checkinScore;
}
