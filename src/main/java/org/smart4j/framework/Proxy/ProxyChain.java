package org.smart4j.framework.Proxy;

import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Warren
 * @CreateTime 06.May.2018
 * @Version V1.0
 */
public class ProxyChain {

    private final Class<?> targetClass;
    private final Object targetObject;
    private final Method targetMethod;
    private final MethodProxy methodProxy;
    private final Object[] methodParams;
    private List<Proxy> proxyList = new ArrayList<Proxy>();
    private int proxyIndex = 0;

    public ProxyChain(Class<?> pTargetClass, Object pTargetObject, Method pTargetMethod,
                      MethodProxy pMethodProxy, Object[] pMethodParams, List<Proxy> pProxyList) {
        targetClass = pTargetClass;
        targetObject = pTargetObject;
        targetMethod = pTargetMethod;
        methodProxy = pMethodProxy;
        methodParams = pMethodParams;
        proxyList = pProxyList;
    }

    public Object doProxyChain () throws Throwable {
        Object methodResult;
        if (this.proxyIndex < this.proxyList.size()) {
            methodResult = this.proxyList.get(this.proxyIndex++).doProxy(this);
        } else {
            // 调用这个invokeSuper的时候，代表要退出当前的MethodInterceptor了
            methodResult = this.methodProxy.invokeSuper(this.targetObject,this.methodParams);
        }
        return methodResult;
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

    public List<Proxy> getProxyList() {
        return proxyList;
    }

    public int getProxyIndex() {
        return proxyIndex;
    }

}
