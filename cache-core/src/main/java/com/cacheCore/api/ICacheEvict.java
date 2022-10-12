package com.cacheCore.api;

/**
 * 驱逐策略
 *
 * @author sgh
 * @date 2022/10/11 10:49
 */
public interface ICacheEvict<K, V> {

    /**
     * 驱除策略
     *
     * @param context 上下文
     * @return 被移除的明细，无则返回null
     */
    ICacheEntry<K, V> evict(final ICacheEvictContext<K, V> context);

    /**
     * 更新key信息
     *
     * @param key key
     */
    void updateKey(final K key);

    /**
     * 删除key信息
     *
     * @param key key
     */
    void removeKey(final K key);
}
