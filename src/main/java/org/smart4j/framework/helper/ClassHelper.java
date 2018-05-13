package org.smart4j.framework.helper;

import org.smart4j.framework.annotation.Controller;
import org.smart4j.framework.annotation.Service;
import org.smart4j.framework.utils.ClassUtil;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author Warren
 * @CreateTime 01.May.2018
 * @Version V1.0
 */
public final class ClassHelper {

    private static final Set<Class<?>> CLASS_SET;

    static {
        String basePackage = ConfigHelper.getAppBasePackage();
        CLASS_SET = ClassUtil.getClassSet(basePackage);
    }

    public static Set<Class<?>> getClassSet() {
        return CLASS_SET;
    }

    public static Set<Class<?>> getControllerClassSet() {
        Set<Class<?>> serviceSet = new HashSet<Class<?>>();
        for (Class<?> cls: CLASS_SET) {
            if (cls.isAnnotationPresent(Controller.class)) {
                serviceSet.add(cls);
            }
        }
        return serviceSet;
    }

    public static Set<Class<?>> getServiceClassSet() {
        Set<Class<?>> serviceSet = new HashSet<Class<?>>();
        for (Class<?> cls: CLASS_SET) {
            if (cls.isAnnotationPresent(Service.class)) {
                serviceSet.add(cls);
            }
        }
        return serviceSet;
    }

    public static Set<Class<?>> getBeanClassSet() {
        Set<Class<?>> beanSet = new HashSet<Class<?>>();
        beanSet.addAll(getControllerClassSet());
        beanSet.addAll(getServiceClassSet());
        return beanSet;
    }

    /**
     * 寻找子类或者接口实现类
     * @param superClass
     * @return
     */
    public static Set<Class<?>> getClassSetBySuper(Class<?> superClass) {
        Set<Class<?>> classSet = new HashSet<Class<?>>();
        for (Class<?> cls: CLASS_SET) {
            if (superClass.isAssignableFrom(cls) && !superClass.equals(cls)) {
                classSet.add(cls);
            }
        }
        return classSet;
    }

    public static Set<Class<?>> getClassSetByAnnotation(Class<? extends Annotation> annotationClass){
        Set<Class<?>> classSet = new HashSet<Class<?>>();
        for (Class<?> cls: CLASS_SET) {
            if (cls.isAnnotationPresent(annotationClass)) {
                classSet.add(cls);
            }
        }
        return classSet;
    }
}
