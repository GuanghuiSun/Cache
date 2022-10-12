package com.cacheCore.support.load;

import com.cacheCore.api.ICache;
import com.cacheCore.api.ICacheLoad;

/**
 * 加载策略 —— 无
 *
 * @author sgh
 * @date 2022/10/12 16:19
 */
public class CacheLoadNone<K, V> implements ICacheLoad<K, V> {
    @Override
    public void load(ICache<K, V> cache) {

    }
}
