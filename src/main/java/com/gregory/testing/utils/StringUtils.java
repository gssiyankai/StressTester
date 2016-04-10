package com.gregory.testing.utils;

import java.util.Arrays;
import java.util.List;

public final class StringUtils {

    private StringUtils() {
    }

    public static String join(String separator, Object... objects) {
        return join(separator, Arrays.asList(objects));
    }

    public static String join(String separator, List<Object> objects) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < objects.size() - 1; i++) {
            builder.append(objects.get(i));
            builder.append(separator);
        }
        builder.append(objects.get(objects.size() - 1));
        return builder.toString();
    }

}
