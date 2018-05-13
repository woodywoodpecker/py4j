package org.py4j.framework.bean;

import org.py4j.framework.annotation.Py4jRequestMapping;

import java.lang.reflect.Method;

/**
 * @Author Warren
 * @CreateTime 11.May.2018
 * @Version V1.0
 */
public class Handler {

    private String requestPath;

    private Py4jRequestMapping.HttpMethod requestMethod;

    private Object targetInstance;

    private Method targetMethod;

    public Handler(String pRequestPath, Py4jRequestMapping.HttpMethod pRequestMethod, Object pTargetInstance, Method pTargetMethod) {
        requestPath = pRequestPath;
        requestMethod = pRequestMethod;
        targetInstance = pTargetInstance;
        targetMethod = pTargetMethod;
    }

    public String getRequestPath() {
        return requestPath;
    }

    public Py4jRequestMapping.HttpMethod getRequestMethod() {
        return requestMethod;
    }

    public Object getTargetInstance() {
        return targetInstance;
    }

    public Method getTargetMethod() {
        return targetMethod;
    }

    @Override
    public String toString() {
        return "Handler{" +
                "requestPath='" + requestPath + '\'' +
                ", requestMethod=" + requestMethod +
                ", targetInstance=" + targetInstance +
                ", targetMethod=" + targetMethod +
                '}';
    }
}
