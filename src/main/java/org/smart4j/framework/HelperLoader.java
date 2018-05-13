package org.smart4j.framework;

import org.smart4j.framework.helper.*;
import org.smart4j.framework.utils.ClassUtil;

/**
 * @Author Warren
 * @CreateTime 01.May.2018
 * @Version V1.0
 */
public final class HelperLoader {

    public static void init() {
        Class<?>[] classList = {
            ClassHelper.class,
            BeanHelper.class,
            AopHelper.class,
            IocHelper.class ,
            ControllerHelper.class
        };
        for (Class<?> clazz : classList) {
            ClassUtil.loadClass(clazz.getName(),true);
        }
    }

}
