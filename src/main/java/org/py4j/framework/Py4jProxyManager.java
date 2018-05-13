package org.py4j.framework;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.py4j.framework.aop.EnhanceHandler;
import org.py4j.framework.bean.ProxyHandler;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @Author Warren
 * @CreateTime 12.May.2018
 * @Version V1.0
 */
public class Py4jProxyManager {

    public static <T> T createProxy(final Class<?> targetClass, final List<EnhanceHandler> enhanceHandlers) {
        return (T) Enhancer.create(targetClass, new MethodInterceptor() {
            public Object intercept(Object targetObject, Method targetMethod,
                                    Object[] methodParams, MethodProxy methodProxy) throws Throwable {
                return new ProxyHandler(targetClass,targetObject,targetMethod,methodProxy,methodParams,enhanceHandlers).doHandler();
            }
        });
    }

}
