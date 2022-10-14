package com.cacheCore.support.proxy.dynamic;

import com.cacheCore.api.ICache;
import com.cacheCore.support.proxy.ICacheProxy;
import com.cacheCore.support.proxy.bs.CacheProxyBs;
import com.cacheCore.support.proxy.bs.CacheProxyBsContext;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 动态代理
 *
 * @author sgh
 * @date 2022/10/13 11:04
 */
public class DynamicProxy implements InvocationHandler, ICacheProxy {

    private final ICache target;

    public DynamicProxy(ICache target) {
        this.target = target;
    }

    @Override
    public Object proxy() {
        InvocationHandler handler = new DynamicProxy(target);
        return Proxy.newProxyInstance(handler.getClass().getClassLoader(),
                target.getClass().getInterfaces(), handler);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        CacheProxyBsContext context = CacheProxyBsContext.newInstance().
                method(method).params(args).target(this.target);
        return CacheProxyBs.newInstance().context(context).e;
    }
}
