package com.github.commonlib.util;

import android.text.TextUtils;

public class AmountUtil {
    public static String formatPoint(StringBuilder builder, String s) {
        if (TextUtils.isEmpty(s)) return s;
        if (builder == null)
            builder = new StringBuilder();
        builder.append(s);
        int pointIndex = builder.indexOf(".");
        if (pointIndex > -1) {
            int length = builder.length();
            builder.delete(pointIndex, length);
        }
        builder.reverse();
        int length1 = builder.length() / 3;
        for (int i = 1; i <= length1; i++) {
            builder.insert(i * 3 + (i - 1), '.');
        }
        builder.reverse();
        if (builder.charAt(0) == '.')
            builder.delete(0, 1);
        s = builder.toString();
        builder.delete(0, builder.length());
        return s;
    }

    public static String formatPoint(String s) {
        if (TextUtils.isEmpty(s)) return s;
        StringBuilder builder = new StringBuilder();
        return formatPoint(builder, s);
    }
}
