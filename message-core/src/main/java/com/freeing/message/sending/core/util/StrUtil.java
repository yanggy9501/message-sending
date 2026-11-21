package com.freeing.message.sending.core.util;

public class StrUtil {
    private StrUtil() {

    }

    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }
}
