package org.smart4j.framework.Proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @Author Warren
 * @CreateTime 06.May.2018
 * @Version V1.0
 */
public class ProxyManager {

    /**
     * 下面这个api怎么理解呢，在我们要使用cglib这个工具生成proxy的代理对象的时候，可以参考这段代码。
         * public class CglibProxyFactory implements MethodInterceptor {

             private Object target;

             public CglibProxyFactory(Object target) {
                this.target = target;
             }

             @Override
             public Object intercept(Object obj, HttpMethod method, Object[] args, MethodProxy proxy) throws Throwable {
                System.out.println("cglib transaction start.");
                Object returnValue = method.invoke(target, args);
                System.out.println("cglib transaction end.");
                return returnValue;
             }


             public Object getProxyInstance () {
                 //1.工具类
                 Enhancer en = new Enhancer();
                 //2.设置父类
                 en.setSuperclass(target.getClass());
                 //3.设置回调函数
                 en.setCallback(this);
                 //4.创建子类(代理对象)
                 return en.create();
             }
         }

         我们其实是实现了MethodInterceptor这个回调接口，然后使用Enhancer来生成的。上面这个比较简单，直接在复写
         public Object intercept(Object obj, HttpMethod method, Object[] args, MethodProxy proxy)的时候，
         把需要增加的切面业务逻辑代码放到了这里，而下面的createProxy则是又封装了一层。通过调用一个ProxyChain来实现。
     *
     *
     * @param targetClass
     * @param proxyList
     * @param <T>
     * @return
     */
    public static <T> T createProxy(final Class<?> targetClass, final List<Proxy> proxyList) {
        return (T) Enhancer.create(targetClass, new MethodInterceptor() {
            public Object intercept(Object targetObject, Method targetMethod,
                                    Object[] methodParams, MethodProxy methodProxy) throws Throwable {
                return new ProxyChain(targetClass,targetObject,targetMethod,methodProxy,methodParams,proxyList).doProxyChain();
            }
        });
    }

}
