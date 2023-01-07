package cn.yiidii.lab.apiplatform.model.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 时光相册签到响应
 *
 * @author ed w
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EverPhotoSignInResponseDTO {

    private Integer code;
    private String message;
    private Data data;

    @lombok.Data
    public static class Data {
        @JsonAlias("checkin_result")
        private boolean checkinResult;

        private Integer continuity;

        @JsonAlias("total_reward")
        private Long totalReward;

        @JsonAlias("tomorrow_reward")
        private Long tomorrowReward;

        @JsonAlias("cache_time")
        private Integer cacheTime;

        @JsonAlias("checkin_push")
        private Integer checkinPush;
    }
}
