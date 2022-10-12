package com.cacheCore.api;

/**
 * 删除监听器上下文
 *
 * @author sgh
 * @date 2022/10/12 21:49
 */
public interface ICacheRemoveListenerContext<K, V> {

    /**
     * 键
     *
     * @return key
     */
    K key();

    /**
     * 值
     *
     * @return value
     */
    V value();

    /**
     * 删除类型
     *
     * @return type
     */
    String type();
}
