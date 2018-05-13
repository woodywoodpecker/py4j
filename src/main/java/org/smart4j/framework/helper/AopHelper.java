package org.smart4j.framework.helper;

import org.apache.log4j.Logger;
import org.smart4j.framework.Proxy.AspectProxy;
import org.smart4j.framework.Proxy.Proxy;
import org.smart4j.framework.Proxy.ProxyManager;
import org.smart4j.framework.Proxy.TransactionProxy;
import org.smart4j.framework.annotation.Aspect;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * @Author Warren
 * @CreateTime 07.May.2018
 * @Version V1.0
 */
public final class AopHelper {

    private static final Logger LOGGER = Logger.getLogger(AopHelper.class);

    static {
        try {
            // 这里是创建一个以切面class为key，被代理的class（例如RestController）集合为value的一个map
            Map<Class<?>, Set<Class<?>>> proxyMap = createProxyMap();
            // 这里是创建一个以被代理的class（例如RestController）为key，以切面class instance的集合为value的map
            Map<Class<?>, List<Proxy>> targetMap = createTargetMap(proxyMap);
            for (Map.Entry<Class<?>, List<Proxy>> targetEntry : targetMap.entrySet()) {
                Class<?> targetClass = targetEntry.getKey();    //  被代理的class（例如RestController）为key
                List<Proxy> proxyList = targetEntry.getValue(); //  切面class instance的集合
                Object proxy = ProxyManager.createProxy(targetClass, proxyList);
                // 这一步targetClass原来的key value所指的instance会被这个proxy所覆盖
                BeanHelper.setBean(targetClass,proxy);
            }
        } catch (Exception pE) {
            LOGGER.error("aop failure",pE);
        }
    }

    private static Map<Class<?>,Set<Class<?>>> createProxyMap () {
        Map<Class<?>,Set<Class<?>>> proxyMap = new HashMap<Class<?>, Set<Class<?>>>();
        addAspectProxy(proxyMap);
        addTransactionProxy(proxyMap);
        return proxyMap;
    }

    private static void addTransactionProxy(final Map<Class<?>, Set<Class<?>>> pProxyMap) {
        Set<Class<?>> serviceClassSet = ClassHelper.getServiceClassSet();
        pProxyMap.put(TransactionProxy.class,serviceClassSet);
    }

    private static void addAspectProxy(final Map<Class<?>, Set<Class<?>>> proxyMap) {
        // 通过AspectProxy找到它的子类，这个地方相当于我们默认了如果在目前的框架里面，要实现一个切面，
        // 那么我们需要继承AspectProxy这个抽象类，然后复写里面的增强方法，例如before，after。
        Set<Class<?>> proxyClassSet = ClassHelper.getClassSetBySuper(AspectProxy.class);
        for (Class<?> proxyClass : proxyClassSet) {
            if (proxyClass.isAnnotationPresent(Aspect.class)) {
                // 获取我们子类的注解上下文
                Aspect aspect = proxyClass.getAnnotation(Aspect.class);
                // 从我们已经加载的IOC容器里面，根据注解上下文里面配置的目标注解，找到对应的targetClass
                // @Aspect(Py4jController.class) ->
                //      找到Controller.class这个注解 ->
                //          然后找到RestController这个class，因为是加了@Controller这个注解的
                Set<Class<?>> targetClassSet = createTargetClassSet(aspect);
                proxyMap.put(proxyClass,targetClassSet);
            }
        }
    }

    private static Set<Class<?>> createTargetClassSet(Aspect aspect) {
        Set<Class<?>> targetClassSet = new HashSet<Class<?>>();
        Class<? extends Annotation> annotation = aspect.value();
        if (null != annotation && !annotation.equals(Aspect.class)) {
            targetClassSet.addAll(ClassHelper.getClassSetByAnnotation(annotation));
        }
        return targetClassSet;
    }

    private static Map<Class<?>,List<Proxy>> createTargetMap(Map<Class<?>,Set<Class<?>>> proxyMap) throws Exception {
        Map<Class<?>,List<Proxy>> targetMap = new HashMap<Class<?>, List<Proxy>>();
        for (Map.Entry<Class<?>, Set<Class<?>>> proxyEntry : proxyMap.entrySet()) {
            Class<?> proxyClass = proxyEntry.getKey();      //  切面class
            Set<Class<?>> targetClassSet = proxyEntry.getValue();   //  被代理的class集合，例如RestController
            for (Class<?> targetClass : targetClassSet) {
                Proxy proxy = (Proxy) proxyClass.newInstance();
                if (targetMap.get(targetClass) != null) {
                    targetMap.get(targetClass).add(proxy);
                } else {
                    List<Proxy> proxyList = new ArrayList<Proxy>();
                    proxyList.add(proxy);
                    targetMap.put(targetClass,proxyList);
                }
            }
        }
        return targetMap;
    }
}
