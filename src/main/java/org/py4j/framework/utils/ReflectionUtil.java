package org.py4j.framework.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @Author Warren
 * @CreateTime 01.May.2018
 * @Version V1.0
 */
public final class ReflectionUtil {

    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(ReflectionUtil.class);

    public static Object newInstance (Class<?> clazz) {
        Object instance;
        try {
            instance = clazz.newInstance();
        } catch (Exception pE) {
            LOGGER.error("new instance failure",pE);
            throw new RuntimeException(pE);
        }
        return instance;
    }

    public static Object invokeMethod (Object obj, Method method , Object...args) {
        Object result;
        try {
            method.setAccessible(true);
            result = method.invoke(obj,args);
        } catch (Exception pE) {
            LOGGER.error("invoke method failure",pE);
            throw new RuntimeException(pE);
        }
        return result;
    }

    public static void setField (Object obj, Field field,Object value) {
        try {
            field.setAccessible(true);
            field.set(obj,value);
        } catch (Exception pE) {
            LOGGER.error("set field failure",pE);
            throw new RuntimeException(pE);
        }
    }

}
