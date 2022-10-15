package com.cacheCore.support.evict;

import com.cacheCore.api.ICache;
import com.cacheCore.api.ICacheEntry;
import com.cacheCore.api.ICacheEvictContext;
import com.cacheCore.model.CacheEntry;
import com.cacheCore.model.FreqNode;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

/**
 * 驱除策略 —— LFU
 *
 * @author sgh
 * @date 2022/10/15 18:45
 */
public class CacheEvictLfu<K, V> extends AbstractCacheEvict<K, V> {

    /**
     * key映射信息
     */
    private final Map<K, FreqNode<K, V>> keyMap;

    /**
     * 频率map
     */
    private final Map<Integer, LinkedHashSet<FreqNode<K, V>>> freqMap;

    /**
     * 最小频率
     */
    private int minFreq;

    public CacheEvictLfu() {
        this.keyMap = new HashMap<>();
        this.freqMap = new HashMap<>();
        this.minFreq = 1;
    }


    @Override
    protected ICacheEntry<K, V> doEvict(ICacheEvictContext<K, V> context) {
        ICacheEntry<K, V> res = null;
        final ICache<K, V> cache = context.cache();
        if (cache.size() >= context.size()) {
            FreqNode<K, V> evictNode = this.getMinFreqNode();
            if (evictNode != null) {
                K evictKey = evictNode.key();
                V evictValue = cache.remove(evictKey);

                res = new CacheEntry<>(evictKey, evictValue);
            }
        }
        return res;
    }

    /**
     * 获取频率最低的节点
     *
     * @return min freq node
     */
    private FreqNode<K, V> getMinFreqNode() {
        LinkedHashSet<FreqNode<K, V>> freqNodes = freqMap.get(minFreq);
        if (!freqNodes.isEmpty()) {
            return freqNodes.iterator().next();
        }
        return null;
    }

    @Override
    public void updateKey(K key) {
        FreqNode<K, V> node = keyMap.get(key);
        if (node != null) {
            int freq = node.frequency();
            LinkedHashSet<FreqNode<K, V>> freqNodes = freqMap.get(freq);
            freqNodes.remove(node);
            if (minFreq == freq && freqNodes.isEmpty()) {
                minFreq++;
            }
            node.incrFre();
            this.addToFreqMap(freq + 1, node);
        } else {
            FreqNode<K, V> kvFreqNode = new FreqNode<>(key);
            this.addToFreqMap(1, kvFreqNode);
            this.minFreq = 1;
            this.keyMap.put(key, kvFreqNode);
        }
    }

    /**
     * 添加节点至频率map中
     *
     * @param freq     频率
     * @param freqNode 节点
     */
    private void addToFreqMap(final int freq, FreqNode<K, V> freqNode) {
        LinkedHashSet<FreqNode<K, V>> freqNodes = freqMap.getOrDefault(freq, new LinkedHashSet<>());
        freqNodes.add(freqNode);
        freqMap.put(freq, freqNodes);
    }

    @Override
    public void removeKey(K key) {
        FreqNode<K, V> node = this.keyMap.remove(key);

        int freq = node.frequency();
        LinkedHashSet<FreqNode<K, V>> freqNodes = this.freqMap.get(freq);

        freqNodes.remove(node);
        if (freq == minFreq && freqNodes.isEmpty()) {
            minFreq--;
        }
        if (minFreq == 0) {
            minFreq = 1;
        }
    }
}
