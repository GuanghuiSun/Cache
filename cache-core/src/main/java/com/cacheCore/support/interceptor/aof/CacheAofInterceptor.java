package com.cacheCore.support.interceptor.aof;

import com.alibaba.fastjson.JSON;
import com.cacheCore.api.ICache;
import com.cacheCore.api.ICacheInterceptor;
import com.cacheCore.api.ICacheInterceptorContext;
import com.cacheCore.api.ICachePersist;
import com.cacheCore.model.PersistAofEntry;
import com.cacheCore.model.PersistDbEntry;
import com.cacheCore.support.persist.CachePersistAof;
import lombok.extern.slf4j.Slf4j;

/**
 * 顺序追加模式
 *
 * @author sgh
 * @date 2022/10/13 20:30
 */
@Slf4j
public class CacheAofInterceptor<K, V> implements ICacheInterceptor<K, V> {
    @Override
    public void before(ICacheInterceptorContext<K, V> context) {

    }

    @Override
    public void after(ICacheInterceptorContext<K, V> context) {
        //持久化
        ICache<K, V> cache = context.cache();
        ICachePersist<K, V> persist = cache.persist();

        if (persist instanceof CachePersistAof) {
            CachePersistAof<K, V> cachePersistAof = (CachePersistAof<K, V>) persist;

            String name = context.method().getName();
            PersistAofEntry aofEntity = PersistAofEntry.newInstance();
            aofEntity.setMethodName(name);
            aofEntity.setParams(context.params());

            String line = JSON.toJSONString(aofEntity);

            log.debug("Begin AOF, append to file...");
            cachePersistAof.append(line);
            log.debug("End AOF, finish...");

        }

    }
}
