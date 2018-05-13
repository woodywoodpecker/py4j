package org.py4j.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by warrenpu on 5/10/2018.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Py4jRequestMapping {

    String value() default "";

    HttpMethod method() default HttpMethod.GET;

    enum HttpMethod {
        GET , POST , PUT , DELETE
    }

}
