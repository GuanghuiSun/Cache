package com.cacheCore.support.evict;

import com.cacheCore.api.ICache;
import com.cacheCore.api.ICacheEntry;
import com.cacheCore.api.ICacheEvictContext;
import com.cacheCore.model.CacheEntry;
import com.cacheCore.support.struct.DoublyLinkedList;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * 驱除策略 LRU—K 这里k取2
 *
 * @author sgh
 * @date 2022/10/15 15:30
 */
@Slf4j
public class CacheEvictLru2<K, V> extends AbstractCacheEvict<K, V> {

    /**
     * 缓存队列
     */
    private final DoublyLinkedList<K> cacheQueue = new DoublyLinkedList<>();

    /**
     * 哈希表 存储 键——节点对
     */
    private final Map<K, DoublyLinkedList<K>.Node<K>> cacheMap = new HashMap<>();

    /**
     * 历史队列
     */
    private final DoublyLinkedList<K> historyQueue = new DoublyLinkedList<>();

    /**
     * 哈希表 存储 键——节点对
     */
    private final Map<K, DoublyLinkedList<K>.Node<K>> historyMap = new HashMap<>();

    @Override
    protected ICacheEntry<K, V> doEvict(ICacheEvictContext<K, V> context) {
        ICacheEntry<K, V> res = null;
        final ICache<K, V> cache = context.cache();
        //超出限制
        if (cache.size() >= context.size()) {
            K evictKey = null;
            //先移除历史队列中的元素 FIFO
            if (!historyQueue.isEmpty()) {
                evictKey = historyQueue.remove();
                log.debug("Evict from historyQueue, key: {}", evictKey);
            } else {
                evictKey = cacheQueue.removeTail();
                log.debug("Evict from cacheQueue, key: {}", evictKey);
            }
            V value = cache.remove(evictKey);
            res = new CacheEntry<>(evictKey, value);
        }
        return res;
    }

    /**
     * 访问缓存 更新位置
     *
     * @param key 键
     */
    @Override
    public void updateKey(K key) {
        //不是第一次访问了
        if (historyMap.containsKey(key)) {
            DoublyLinkedList<K>.Node<K> node = historyMap.remove(key);
            historyQueue.remove(node);
            cacheQueue.addToHead(node);
            cacheMap.put(key, node);
        } else if (cacheMap.containsKey(key)) {
            //放到cache队列的头部
            DoublyLinkedList<K>.Node<K> node = cacheMap.get(key);
            cacheQueue.removeToHead(node);
        } else {
            //第一次
            DoublyLinkedList<K>.Node<K> node = historyQueue.add(key);
            historyMap.put(key, node);
        }
    }

    /**
     * 删除缓存
     *
     * @param key 键
     */
    @Override
    public void removeKey(K key) {
        if (historyMap.containsKey(key)) {
            DoublyLinkedList<K>.Node<K> node = historyMap.remove(key);
            historyQueue.remove(node);
        } else {
            DoublyLinkedList<K>.Node<K> remove = cacheMap.remove(key);
            cacheQueue.remove(remove);
        }
    }
}
