package com.cacheCore.support.evict;

import com.cacheCore.api.ICache;
import com.cacheCore.api.ICacheEntry;
import com.cacheCore.api.ICacheEvictContext;
import com.cacheCore.model.CacheEntry;
import com.cacheCore.support.struct.DoublyLinkedList;

import java.util.LinkedList;

/**
 * 驱除策略 —— FIFO 先进先出策略
 *
 * 双向链表实现队列
 *
 * @author sgh
 * @date 2022/10/11 16:56
 */
public class CacheEvictFifo<K, V> extends AbstractCacheEvict<K, V>{

    /**
     * 队列 —— 双向链表实现
     */
    private final DoublyLinkedList<K> queue = new DoublyLinkedList<>();

    @Override
    protected ICacheEntry<K, V> doEvict(ICacheEvictContext<K, V> context) {
        ICacheEntry<K, V> res = null;

        final ICache<K, V> cache = context.cache();
        //超出限制开始移除
        if (cache.size() >= context.size()) {
            K evictKey = queue.remove();
            V evictValue = cache.remove(evictKey);
            res = new CacheEntry<>(evictKey, evictValue);
        }

        //将新加的元素添加到队尾
        final K key = context.key();
        queue.add(key);

        return res;
    }
}
