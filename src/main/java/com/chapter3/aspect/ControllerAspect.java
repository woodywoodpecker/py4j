package com.chapter3.aspect;

import org.apache.log4j.Logger;
import org.smart4j.framework.Proxy.AspectProxy;
import org.smart4j.framework.annotation.Aspect;
import org.smart4j.framework.annotation.Controller;

import java.lang.reflect.Method;

/**
 * @Author Warren
 * @CreateTime 06.May.2018
 * @Version V1.0
 */
@Aspect(Controller.class)
public class ControllerAspect extends AspectProxy {

    private static final Logger LOGGER = Logger.getLogger(ControllerAspect.class);

    private long begin;

    @Override
    public void before(Class<?> cls, Method method, Object[] params) throws Throwable {
        LOGGER.info("-----------begin-----------");
        LOGGER.info(String.format("class: %s",cls.getName()));
        LOGGER.info(String.format("method: %s",method.getName()));
        begin = System.currentTimeMillis();
    }

    @Override
    public void after(Class<?> cls, Method method, Object[] params) throws Throwable {
        LOGGER.info(String.format("time: %dms",System.currentTimeMillis()-begin));
        LOGGER.info("-------------end-----------");
    }
}
