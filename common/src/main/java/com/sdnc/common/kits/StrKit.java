package com.sdnc.common.kits;

import org.springframework.util.StringUtils;

/**
 *
 * 字符串工具类
 *
 */
public class StrKit extends StringUtils {

    /**
     * 是否为空
     */
    public static boolean isBlank(CharSequence str) {
        return containsWhitespace(str);
    }

    /**
     * 是否不为空
     */
    public static boolean notBlank(CharSequence str) {
        return hasText(str);
    }

}
