package org.smart4j.framework.helper;


import org.smart4j.framework.annotation.Inject;
import org.smart4j.framework.utils.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;

/**
 * @Author Warren
 * @CreateTime 01.May.2018
 * @Version V1.0
 */
public final class IocHelper {

    static {
        doLoad();
    }

    public static void doLoad() {
        Map<Class<?>,Object> beanMap = BeanHelper.getBeanMap();
        if (!beanMap.isEmpty()) {
            Iterator<Map.Entry<Class<?>, Object>> iterator = beanMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Class<?>, Object> entry = iterator.next();
                Class<?> beanClass = entry.getKey();
                Object beanInstance = entry.getValue();
                Field[] fields = beanClass.getDeclaredFields();
                if (fields != null && fields.length > 0) {
                    for (Field beanFiled: fields) {
                        if (beanFiled.isAnnotationPresent(Inject.class)) {
                            Class<?> beanFieldType = beanFiled.getType();
                            Object beanFieldInstance = beanMap.get(beanFieldType);
                            if (beanFieldInstance != null) {
                                ReflectionUtil.setField(beanInstance,beanFiled,beanFieldInstance);
                            }
                        }
                    }
                }
            }
        }
    }

}
