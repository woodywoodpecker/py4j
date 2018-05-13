package org.py4j.framework.bean;

import net.sf.cglib.proxy.MethodProxy;
import org.py4j.framework.annotation.Py4jPointCut;
import org.py4j.framework.aop.EnhanceHandler;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @Author Warren
 * @CreateTime 12.May.2018
 * @Version V1.0
 */
public class ProxyHandler {

    private final Class<?> targetClass;
    private final Object targetObject;
    private final Method targetMethod;
    private final MethodProxy methodProxy;
    private final Object[] methodParams;

    private final List<EnhanceHandler> enhanceHandlers;


    public ProxyHandler(Class<?> pTargetClass, Object pTargetObject, Method pTargetMethod, MethodProxy pMethodProxy,
                        Object[] pMethodParams,List<EnhanceHandler> pEnhanceHandler) {
        targetClass = pTargetClass;
        targetObject = pTargetObject;
        targetMethod = pTargetMethod;
        methodProxy = pMethodProxy;
        methodParams = pMethodParams;
        enhanceHandlers = pEnhanceHandler;
    }

    public final Object doHandler () {
        try {
            Object result;
            for (EnhanceHandler handler : enhanceHandlers) {
                if (Py4jPointCut.PointCutType.BEFORE.equals(handler.getPointCutType())
                        || Py4jPointCut.PointCutType.AROUND.equals(handler.getPointCutType())) {
                    if (handler.validate(targetClass,targetMethod.getName())) {
                        handler.doHandler(this);
                    }
                }
            }
            result = this.methodProxy.invokeSuper(this.targetObject,this.methodParams);
            for (EnhanceHandler handler : enhanceHandlers) {
                if (Py4jPointCut.PointCutType.AFTER.equals(handler.getPointCutType())
                        || Py4jPointCut.PointCutType.AROUND.equals(handler.getPointCutType())) {
                    if (handler.validate(targetClass,targetMethod.getName())) {
                        handler.doHandler(this);
                    }
                }
            }
            return result;
        } catch (Throwable pThrowable) {
            throw new RuntimeException("ProxyHandler exec failure",pThrowable);
        }
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public Object getTargetObject() {
        return targetObject;
    }

    public Method getTargetMethod() {
        return targetMethod;
    }

    public MethodProxy getMethodProxy() {
        return methodProxy;
    }

    public Object[] getMethodParams() {
        return methodParams;
    }

}
