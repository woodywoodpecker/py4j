package org.smart4j.framework.annotation;

import java.lang.annotation.*;

/**
 * Created by warrenpu on 5/6/2018.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Aspect {

    Class<? extends Annotation> value();

}
