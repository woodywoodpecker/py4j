package org.smart4j.framework.helper;

import org.smart4j.framework.utils.ReflectionUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Author Warren
 * @CreateTime 01.May.2018
 * @Version V1.0
 */
public final class BeanHelper {

    private static final Map<Class<?>,Object> BEAN_MAP = new HashMap<Class<?>,Object>();

    static {
        Set<Class<?>> beanClassSet = ClassHelper.getBeanClassSet();
        for (Class<?> clazz : beanClassSet) {
            Object obj = ReflectionUtil.newInstance(clazz);
            BEAN_MAP.put(clazz,obj);
        }
    }

    public static Map<Class<?>,Object> getBeanMap () {
        return BEAN_MAP;
    }

    /**
     * 这里实现的bean还全部是单例，因为BEAN_MAP里面存放的是最初new出来的instance
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<?> clazz) {
        if (!BEAN_MAP.containsKey(clazz)) {
            throw new RuntimeException("can not get bean by class :: " + clazz);
        }
        return (T) BEAN_MAP.get(clazz);
    }

    public static void setBean(Class<?> cls,Object bean) {
        BEAN_MAP.put(cls,bean);
    }
}
