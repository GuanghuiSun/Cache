package com.cacheCore.support.evict;

import com.cacheCore.api.ICacheEntry;
import com.cacheCore.api.ICacheEvictContext;

/**
 * 驱除策略 —— 无策略
 *
 * @author sgh
 * @date 2022/10/13 9:09
 */
public class CacheEvictNone<K, V> extends AbstractCacheEvict<K, V> {

    @Override
    protected ICacheEntry<K, V> doEvict(ICacheEvictContext<K, V> context) {
        return null;
    }
}
