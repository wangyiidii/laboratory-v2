package cn.yiidii.lab.apiplatform.model.enums;

import cn.yiidii.base.domain.enums.IEnum;
import cn.yiidii.base.exception.BizException;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

/**
 * 视频解析平台
 *
 * @author ed w
 * @since 1.0
 */
@Getter
@AllArgsConstructor
public enum VideoParsePlatformEnum implements IEnum<String> {

    DOUYIN("dy", "抖音", Lists.newArrayList("v.douyin.com")),
    KUAISHOU("ks", "快手", Lists.newArrayList("v.kuaishou.com")),
    BILIBLI("bilibili", "哔哩哔哩", Lists.newArrayList("bilibili.com", "www.bilibili.com")),
    ;

    private final String code;
    private final String desc;
    private final List<String> domains;

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String desc() {
        return this.desc;
    }

    public static VideoParsePlatformEnum check(String domain) {
        return Arrays.stream(values())
                .filter(t -> t.getDomains().contains(domain))
                .findAny()
                .orElseThrow(() -> new BizException(ApiExceptionCode.VIDEO_PARSE_PLATFORM_NOT_SUPPORT));
    }
}
