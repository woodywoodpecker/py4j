package org.py4j.framework.aop;

import org.py4j.framework.annotation.Py4jPointCut;
import org.py4j.framework.bean.ProxyHandler;
import org.smart4j.framework.utils.ReflectionUtil;

import java.lang.reflect.Method;

/**
 * @Author Warren
 * @CreateTime 12.May.2018
 * @Version V1.0
 */
public final class EnhanceHandler {

    private final Object aspectInstance;

    private final Method aspectMethod;

    private final Py4jPointCut.PointCutType pointCutType;

    private final Class<?> targetClass;

    private final String targetMethod;

    public EnhanceHandler(Object pAspectInstance, Method pAspectMethod,
                          Py4jPointCut.PointCutType pPointCutType,
                          Class<?> pTargetClass,String pTargetMethod) {
        aspectInstance = pAspectInstance;
        aspectMethod = pAspectMethod;
        pointCutType = pPointCutType;
        targetClass = pTargetClass;
        targetMethod = pTargetMethod;
    }

    public boolean validate (Class<?> pTargetClass,String pTargetMethod) {
        if (!this.targetClass.equals(pTargetClass)) {
            return false;
        }
        if (!this.targetMethod.equals(pTargetMethod)) {
            return false;
        }
        return true;
    }

    public Object doHandler (ProxyHandler pProxyHandler) {
        return ReflectionUtil.invokeMethod(this.aspectInstance,this.aspectMethod,pProxyHandler);
    }

    public Object getAspectInstance() {
        return aspectInstance;
    }

    public Method getAspectMethod() {
        return aspectMethod;
    }

    public Py4jPointCut.PointCutType getPointCutType() {
        return pointCutType;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public String getTargetMethod() {
        return targetMethod;
    }
}
