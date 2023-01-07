package cn.yiidii.lab.apiplatform.support;

import cn.yiidii.lab.apiplatform.model.dto.VideoParseResponseDTO;

/**
 * 视频解析接口
 *
 * @author ed w
 * @since 1.0
 */
public interface VideoParser {

    String MOBILE_UA = "Mozilla/5.0 (iPhone; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1";

    /**
     * 视频解析
     *
     * @param s 分享链接或短链接
     * @return {@link VideoParseResponseDTO}
     */
    VideoParseResponseDTO parse(String s);

}
