package cn.yiidii.apiplatform.util;

import cn.hutool.core.util.ReUtil;

import java.util.Optional;

/**
 * 工具类
 *
 * @author ed w
 * @since 1.0
 */
public class Util {

    public static String getDomain(String s) {
        return Optional.ofNullable(ReUtil.getGroup0("[a-zA-Z]{0,62}(\\.[a-zA-Z][a-zA-Z]{0,62})+\\.?", s)).orElse("");
    }

    public static String getUrl(String s) {
        return ReUtil.getGroup0("(https?|http|ftp|file):\\/\\/[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]", s);
    }
}
