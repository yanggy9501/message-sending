package com.freeing.message.sending.core.util;

import java.util.Collection;

public class CollectionUtil {
    private CollectionUtil() {
    }

    public static <T> boolean isEmpty(Collection<T> collection) {
        return collection == null || collection.isEmpty();
    }

    public static <T> boolean isNotEmpty(Collection<T> collection) {
        return !isEmpty(collection);
    }
}
