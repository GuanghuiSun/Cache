package com.cacheCore.api;

/**
 * 拦截器
 *
 * @author sgh
 * @date 2022/10/13 14:18
 */
public interface ICacheInterceptor<K, V> {

    /**
     * 方法执行前
     *
     * @param context 上下文
     */
    void before(ICacheInterceptorContext<K, V> context);

    /**
     * 方法执行后
     *
     * @param context 上下文
     */
    void after(ICacheInterceptorContext<K, V> context);
}
