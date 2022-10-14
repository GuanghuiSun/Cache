package com.cacheCore.api;

/**
 * 慢日志监听接口
 *
 * @author sgh
 * @date 2022/10/13 14:26
 */
public interface ICacheSlowListener {

    /**
     * 监听
     *
     * @param context 上下文
     */
    void listen(final ICacheSlowListenerContext context);

    /**
     * 慢日志阈值 毫秒
     *
     * @return 阈值
     */
    long slowThreshold();
}
