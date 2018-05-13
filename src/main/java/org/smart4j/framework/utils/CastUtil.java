package org.smart4j.framework.utils;


/**
 * @Author Warren
 * @CreateTime 01.May.2018
 * @Version V1.0
 */
public class CastUtil {

    public static int castInt(Object pProperty) {
        return castInt(pProperty,0);
    }

    public static int castInt(Object pProperty,int defaultValue) {
        if (pProperty == null) return defaultValue;
        return Integer.valueOf((String) pProperty);
    }

    public static long castLong(Object pProperty) {
        return castLong(pProperty,0);
    }

    public static long castLong(Object pProperty,long defaultValue) {
        if (pProperty == null) return defaultValue;
        return Long.valueOf((String) pProperty);
    }
}
