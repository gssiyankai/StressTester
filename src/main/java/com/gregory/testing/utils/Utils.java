package com.gregory.testing.utils;

public final class Utils {

    private Utils() {
    }

    public static String join(String separator, Object... objects) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < objects.length - 1; i++) {
            builder.append(objects[i]);
            builder.append(separator);
        }
        builder.append(objects[objects.length - 1]);
        return builder.toString();
    }

}
