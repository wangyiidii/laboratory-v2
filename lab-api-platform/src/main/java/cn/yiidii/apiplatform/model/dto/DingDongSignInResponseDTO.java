package cn.yiidii.apiplatform.model.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 叮咚买菜签到响应
 *
 * @author ed w
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DingDongSignInResponseDTO {

    private Integer code;
    private Boolean success;
    private String msg;
    private Data data;

    @lombok.Data
    public static class Data {
        @JsonAlias("point_num")
        private Integer pointNum;

        @JsonAlias("user_sign")
        private UserSign userSign;
    }

    @lombok.Data
    public static class UserSign {
        @JsonAlias("is_today_sign")
        private Boolean isTodaySign;
    }
}
