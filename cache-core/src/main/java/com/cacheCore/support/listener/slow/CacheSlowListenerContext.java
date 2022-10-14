package com.cacheCore.support.listener.slow;

import com.cacheCore.api.ICacheSlowListenerContext;

/**
 * 慢日志上下文实现
 *
 * @author sgh
 * @date 2022/10/13 19:52
 */
public class CacheSlowListenerContext implements ICacheSlowListenerContext {
    /**
     * 方法名称
     */
    private String methodName;

    /**
     * 参数信息
     */
    private Object[] params;

    /**
     * 执行结果
     */
    private Object result;

    /**
     * 开始时间
     */
    private long startTimeMills;

    /**
     * 结束时间
     */
    private long endTimeMills;

    /**
     * 实例化
     * @return instance
     */
    public static CacheSlowListenerContext newInstance() {
        return new CacheSlowListenerContext();
    }

    @Override
    public String methodName() {
        return methodName;
    }

    public CacheSlowListenerContext methodName(String methodName) {
        this.methodName = methodName;
        return this;
    }

    @Override
    public Object[] params() {
        return params;
    }

    public CacheSlowListenerContext params(Object[] params) {
        this.params = params;
        return this;
    }

    @Override
    public Object result() {
        return result;
    }

    public CacheSlowListenerContext result(Object result) {
        this.result = result;
        return this;
    }

    @Override
    public long startTimeMills() {
        return startTimeMills;
    }

    public CacheSlowListenerContext startTimeMills(long startTimeMills) {
        this.startTimeMills = startTimeMills;
        return this;
    }

    @Override
    public long endTimeMills() {
        return endTimeMills;
    }

    public CacheSlowListenerContext endTimeMills(long endTimeMills) {
        this.endTimeMills = endTimeMills;
        return this;
    }

    @Override
    public long costTimeMills() {
        return endTimeMills - startTimeMills;
    }
}
