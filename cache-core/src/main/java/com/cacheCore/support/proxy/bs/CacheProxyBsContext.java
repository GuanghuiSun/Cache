package com.cacheCore.support.proxy.bs;

import com.cacheCore.annotation.CacheInterceptor;
import com.cacheCore.api.ICache;
import com.cacheCore.api.ICacheProxyBsContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 代理引导类上下文
 *
 * @author sgh
 * @date 2022/10/13 8:46
 */
public class CacheProxyBsContext implements ICacheProxyBsContext {

    /**
     * 目标
     */
    private ICache target;

    /**
     * 参数
     */
    private Object[] params;

    /**
     * 方法
     */
    private Method method;

    /**
     * 拦截器
     */
    private CacheInterceptor interceptor;

    public static CacheProxyBsContext newInstance() {
        return new CacheProxyBsContext();
    }

    @Override
    public CacheInterceptor interceptor() {
        return interceptor;
    }

    @Override
    public ICache target() {
        return target;
    }

    @Override
    public CacheProxyBsContext target(ICache target) {
        this.target = target;
        return this;
    }

    @Override
    public Object[] params() {
        return params;
    }

    public CacheProxyBsContext params(Object[] params) {
        this.params = params;
        return this;
    }

    @Override
    public Method method() {
        return method;
    }

    public CacheProxyBsContext method(Method method) {
        this.method = method;
        this.interceptor = method.getAnnotation(CacheInterceptor.class);
        return this;
    }

    @Override
    public Object process() throws InvocationTargetException, IllegalAccessException {
        return this.method.invoke(target, params);
    }

}
