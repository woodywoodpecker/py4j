package org.py4j.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by warrenpu on 5/12/2018.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Py4jPointCut {

    Class<?> targetClass();

    String targetMethod();

    PointCutType pointCutType();

    enum PointCutType {
        BEFORE , AFTER , AROUND
    }
}
