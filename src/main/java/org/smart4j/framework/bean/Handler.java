package org.smart4j.framework.bean;

import java.lang.reflect.Method;

/**
 * @Author Warren
 * @CreateTime 01.May.2018
 * @Version V1.0
 */
public class Handler {

    /**
     * controller class
     */
    private Class<?> controllerClass;

    /**
     * 需要调用的方法
     */
    private Method actionMethod;

    public Handler(Class<?> pControllerClass, Method pActionMethod) {
        controllerClass = pControllerClass;
        actionMethod = pActionMethod;
    }

    public Class<?> getControllerClass() {
        return controllerClass;
    }

    public void setControllerClass(Class<?> pControllerClass) {
        controllerClass = pControllerClass;
    }

    public Method getActionMethod() {
        return actionMethod;
    }

    public void setActionMethod(Method pActionMethod) {
        actionMethod = pActionMethod;
    }
}
