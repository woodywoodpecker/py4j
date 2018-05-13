package org.py4j.framework.utils;

import org.py4j.framework.annotation.Py4jAspect;
import org.py4j.framework.annotation.Py4jController;
import org.py4j.framework.annotation.Py4jService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Author Warren
 * @CreateTime 11.May.2018
 * @Version V1.0
 */
public final class ClassTemplateUtil {

    private ClassTemplateUtil() {}

    public static List<Class<?>> getControllerClassList (List<Class<?>> clsList) {
        Objects.requireNonNull(clsList);
        List<Class<?>> clazzes = new ArrayList<Class<?>>();
        for (Class<?> cls : clsList) {
            if (cls.isAnnotationPresent(Py4jController.class)) {
                clazzes.add(cls);
            }
        }
        return clazzes;
    }

    public static List<Class<?>> getServiceClassList (List<Class<?>> clsList) {
        Objects.requireNonNull(clsList);
        List<Class<?>> clazzes = new ArrayList<Class<?>>();
        for (Class<?> cls : clsList) {
            if (cls.isAnnotationPresent(Py4jService.class)) {
                clazzes.add(cls);
            }
        }
        return clazzes;
    }

    public static List<Class<?>> getAspectClassList (List<Class<?>> clsList) {
        Objects.requireNonNull(clsList);
        List<Class<?>> clazzes = new ArrayList<Class<?>>();
        for (Class<?> cls : clsList) {
            if (cls.isAnnotationPresent(Py4jAspect.class)) {
                clazzes.add(cls);
            }
        }
        return clazzes;
    }

    public static List<Class<?>> getAllClassList (List<Class<?>> clsList) {
        Objects.requireNonNull(clsList);
        List<Class<?>> clazzes = new ArrayList<Class<?>>();
        clazzes.addAll(getControllerClassList(clsList));
        clazzes.addAll(getServiceClassList(clsList));
        clazzes.addAll(getAspectClassList(clsList));
        return clazzes;
    }
}
