package com.cacheCore.support.evict;


import com.cacheCore.api.ICacheEntry;
import com.cacheCore.api.ICacheEvict;
import com.cacheCore.api.ICacheEvictContext;

/**
 * @author sgh
 * @date 2022/10/11 16:57
 */
public abstract class AbstractCacheEvict<K, V> implements ICacheEvict<K, V> {

    @Override
    public ICacheEntry<K, V> evict(ICacheEvictContext<K, V> context) {
        return doEvict(context);
    }

    /**
     * 执行移除操作
     *
     * @param context 驱除策略上下文
     * @return 结果
     */
    protected abstract ICacheEntry<K, V> doEvict(ICacheEvictContext<K, V> context);

    @Override
    public void updateKey(K key) {

    }

    @Override
    public void removeKey(K key) {

    }
}
