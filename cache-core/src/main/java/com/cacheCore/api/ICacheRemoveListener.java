package com.cacheCore.api;

/**
 * 删除监听器
 *
 * @author sgh
 * @date 2022/10/12 21:48
 */
public interface ICacheRemoveListener<K, V> {

    /**
     * 监听接口
     * @param context 删除上下文
     */
    void listen(final ICacheRemoveListenerContext<K, V> context);
}
