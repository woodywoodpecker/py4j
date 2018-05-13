package org.smart4j.framework.Proxy;

/**
 * Created by warrenpu on 5/6/2018.
 */
public interface Proxy {

    Object doProxy(ProxyChain proxyChain) throws Throwable;

}
