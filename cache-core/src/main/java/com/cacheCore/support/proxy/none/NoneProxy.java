package com.cacheCore.support.proxy.none;

import com.cacheCore.support.proxy.ICacheProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 没有代理
 *
 * @author sgh
 * @date 2022/10/13 10:51
 */
public class NoneProxy implements ICacheProxy, InvocationHandler {

    /**
     * 代理对象
     */
    private final Object target;

    public NoneProxy(Object target) {
        this.target = target;
    }

    /**
     * 返回原始对象 没有代理
     * @return 原始对象
     */
    @Override
    public Object proxy() {
        return this.target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return method.invoke(proxy, args);
    }
}
