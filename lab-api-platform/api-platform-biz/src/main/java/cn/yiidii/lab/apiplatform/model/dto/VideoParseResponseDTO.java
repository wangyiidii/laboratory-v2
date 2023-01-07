package cn.yiidii.lab.apiplatform.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 短视频解析统一响应
 *
 * @author ed w
 * @since 1.0
 */
@Data
@Builder
public class VideoParseResponseDTO {

    public static final Integer IMAGE = 1;
    public static final Integer VIDEO = 2;
    public static final Integer LIVE = 3;

    private Integer type;
    private String nickname;
    private String title;
    private String cover;
    private List<String> urls;
    private List<String> audioUrls;
    private Object raw;


}
