package com.cacheCore.support.evict;

import com.cacheCore.api.ICache;
import com.cacheCore.api.ICacheEntry;
import com.cacheCore.api.ICacheEvictContext;
import com.cacheCore.model.CacheEntry;
import com.cacheCore.support.struct.DoublyLinkedList;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sgh
 * @date 2022/10/14 17:58
 */
public class CacheEvictLru<K, V> extends AbstractCacheEvict<K, V> {

    /**
     * 双向链表
     */
    private final DoublyLinkedList<K> queue = new DoublyLinkedList<>();

    /**
     * 哈希表 存储 键——节点对
     */
    private final Map<K, DoublyLinkedList<K>.Node<K>> map = new HashMap<>();

    @Override
    protected ICacheEntry<K, V> doEvict(ICacheEvictContext<K, V> context) {
        ICacheEntry<K, V> res = null;
        final ICache<K, V> cache = context.cache();
        if (cache.size() >= context.size()) {
            K evictKey = queue.removeTail();
            V evictValue = cache.remove(evictKey);
            res = new CacheEntry<>(evictKey, evictValue);
        }
        return res;
    }

    @Override
    public void updateKey(K key) {
        DoublyLinkedList<K>.Node<K> node = map.get(key);
        if (node == null)
            return;
        //移动至队头
        queue.removeToHead(node);
    }

    @Override
    public void removeKey(K key) {
        DoublyLinkedList<K>.Node<K> node = map.get(key);

        if (node == null) {
            return;
        }
        //移除这个结点
        queue.remove(node);
        this.map.remove(key);
    }
}
