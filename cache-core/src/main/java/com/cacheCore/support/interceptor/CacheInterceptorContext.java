package com.cacheCore.support.interceptor;

import com.cacheCore.api.ICache;
import com.cacheCore.api.ICacheInterceptorContext;

import java.lang.reflect.Method;

/**
 * 拦截器上下文实现
 *
 * @author sgh
 * @date 2022/10/13 14:18
 */
public class CacheInterceptorContext<K, V> implements ICacheInterceptorContext<K, V> {

    /**
     * 缓存信息
     */
    private ICache<K, V> cache;

    /**
     * 执行方法信息
     */
    private Method method;

    /**
     * 执行方法参数
     */
    private Object[] params;

    /**
     * 执行方法结果
     */
    private Object result;

    /**
     * 开始时间
     */
    private long startMills;

    /**
     * 结束时间
     */
    private long endMills;

    public static <K, V> CacheInterceptorContext<K, V> newInstance() {
        return new CacheInterceptorContext<>();
    }

    @Override
    public ICache<K, V> cache() {
        return cache;
    }

    public CacheInterceptorContext<K, V> cache(ICache<K, V> cache) {
        this.cache = cache;
        return this;
    }

    @Override
    public Method method() {
        return method;
    }

    public CacheInterceptorContext<K, V> method(Method method) {
        this.method = method;
        return this;
    }

    @Override
    public Object[] params() {
        return params;
    }

    public CacheInterceptorContext<K, V> params(Object[] params) {
        this.params = params;
        return this;
    }

    @Override
    public Object result() {
        return result;
    }

    public CacheInterceptorContext<K, V> result(Object result) {
        this.result = result;
        return this;
    }

    @Override
    public long startMills() {
        return startMills;
    }

    public CacheInterceptorContext<K, V> startMills(long startMills) {
        this.startMills = startMills;
        return this;
    }

    @Override
    public long endMills() {
        return endMills;
    }

    public CacheInterceptorContext<K, V> endMills(long endMills) {
        this.endMills = endMills;
        return this;
    }
}
