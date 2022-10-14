package com.cacheCore.api;

import java.lang.reflect.Method;

/**
 * 拦截器上下文
 *
 * @author sgh
 * @date 2022/10/13 14:18
 */
public interface ICacheInterceptorContext<K, V> {

    /**
     * 缓存信息
     *
     * @return cache
     */
    ICache<K, V> cache();

    /**
     * 执行方法信息
     *
     * @return 方法
     */
    Method method();

    /**
     * 参数列表
     *
     * @return 参数
     */
    Object[] params();

    /**
     * 方法执行结果
     *
     * @return 结果
     */
    Object result();

    /**
     * 开始时间
     *
     * @return 时间戳
     */
    long startMills();

    /**
     * 结束时间
     *
     * @return 时间戳
     */
    long endMills();
}
