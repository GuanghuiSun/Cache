package com.cacheCore.api;

import com.cacheCore.annotation.CacheInterceptor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author sgh
 * @date 2022/10/13 8:41
 */
public interface ICacheProxyBsContext {

    /**
     * 拦截器信息
     *
     * @return 拦截器
     */
    CacheInterceptor interceptor();

    /**
     * 获取代理对象信息
     * @return 结果
     */
    ICache target();

    /**
     * 目标对象
     * @param target 对象
     * @return 结果
     */
    ICacheProxyBsContext target(final ICache target);

    /**
     * 参数信息
     * @return 参数信息
     */
    Object[] params();

    /**
     * 方法信息
     * @return 方法信息
     */
    Method method();

    /**
     * 方法执行
     * @return 执行
     */
    Object process() throws InvocationTargetException, IllegalAccessException;
}
