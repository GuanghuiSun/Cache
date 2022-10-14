package com.cacheCore.api;

/**
 * 慢日志监听接口上下文
 *
 * @author sgh
 * @date 2022/10/13 14:26
 */
public interface ICacheSlowListenerContext {

    /**
     * 方法名称
     *
     * @return name
     */
    String methodName();

    /**
     * 方法参数列表
     *
     * @return params
     */
    Object[] params();

    /**
     * 方法执行结果
     *
     * @return result
     */
    Object result();

    /**
     * 开始时间
     *
     * @return 时间戳
     */
    long startTimeMills();

    /**
     * 结束时间
     *
     * @return 时间戳
     */
    long endTimeMills();

    /**
     * 消耗时间
     *
     * @return 时间戳
     */
    long costTimeMills();
}
