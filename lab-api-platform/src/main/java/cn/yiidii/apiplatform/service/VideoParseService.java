package cn.yiidii.apiplatform.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.yiidii.apiplatform.model.dto.VideoParseResponseDTO;
import cn.yiidii.apiplatform.model.enums.VideoParsePlatformEnum;
import cn.yiidii.apiplatform.support.VideoParser;
import cn.yiidii.apiplatform.util.Util;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * 视频解析服务
 *
 * @author ed w
 * @since 1.0
 */
@Component
public class VideoParseService {

    public VideoParseResponseDTO parse(String s, Integer raw) {
        // 链接校验
        String url = Util.getUrl(s);
        Assert.isTrue(StrUtil.isNotBlank(url), "请检查连接是否正确");

        // 平台校验
        String domain = Util.getDomain(s);
        VideoParsePlatformEnum platformEnum = VideoParsePlatformEnum.check(domain);

        // 获取parser并解析
        String beanName = StrUtil.format("{}VideoParser", platformEnum.getCode());
        VideoParser videoParser = SpringUtil.getBean(beanName, VideoParser.class);
        VideoParseResponseDTO dto = videoParser.parse(s);

        // 是否返回raw
        if (raw == 0) {
            dto.setRaw(null);
        }

        return dto;
    }

}
