package com.cacheCore.support.expire;


import com.cacheCore.api.ICache;
import com.cacheCore.api.ICacheExpire;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 缓存过期策略 —— 普通策略
 *
 * @author sgh
 * @date 2022/10/11 18:43
 */
public class CacheExpire<K, V> implements ICacheExpire<K, V> {

    /**
     * 单次清空的数量限制
     */
    private static final int LIMIT = 100;

    /**
     * 过期map
     */
    private final Map<K, Long> expireMap = new HashMap<>();

    /**
     * 排序过期map —— 红黑树实现
     * 辅助定时任务 避免空遍历
     * 时间戳为 sort key
     */
    private final Map<Long, List<K>> expireSortedMap = new TreeMap<>((o1, o2) -> (int) (o1-o2));

    /**
     * 缓存实现
     */
    private final ICache<K, V> cache;

    /**
     * 单线程定时执行清理任务 每次上限100
     */
    private static final ScheduledExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor();

    public CacheExpire(ICache<K, V> cache) {
        this.cache = cache;
        this.init();
    }

    @Override
    public void expire(K key, long expireAt) {
        expireMap.put(key, expireAt);
        List<K> keys = expireSortedMap.getOrDefault(expireAt, new ArrayList<>());
        keys.add(key);
        expireSortedMap.put(expireAt, keys);
    }

    @Override
    public void refreshExpire(Collection<K> keyList) {
        if (keyList.isEmpty())
            return ;
        final int expireSize = expireMap.size();
        if (expireSize <= keyList.size()) {
            for (Map.Entry<K, Long> entry : expireMap.entrySet()) {
                this.expireKey(entry.getKey(), entry.getValue());
            }
        } else {
            for(K key : keyList) {
                Long expireAt = expireMap.get(key);
                expireKey(key, expireAt);
            }
        }

    }

    @Override
    public Long expireTime(K key) {
        return expireMap.get(key);
    }

    /**
     * 初始化任务
     */
    public void init() {
        EXECUTOR_SERVICE.scheduleAtFixedRate(new ExpireThread(), 100, 100, TimeUnit.MILLISECONDS);
    }

    /**
     * 清理线程 定时执行任务
     */
    private class ExpireThread implements Runnable {
        @Override
        public void run() {
            //判断过期map是否为空
            if (expireMap.isEmpty() || expireSortedMap.isEmpty())
                return ;

            //获取key并处理
            int count = 0;
            for (Map.Entry<Long, List<K>> entry : expireSortedMap.entrySet()) {
                final Long expireAt = entry.getKey();
                List<K> expireKeys = entry.getValue();

                if (expireKeys.isEmpty()) {
                    expireSortedMap.remove(expireAt);
                    continue;
                }
                if (count >= LIMIT) return ;

                long currentTime = System.currentTimeMillis();
                if (currentTime >= expireAt) {
                    Iterator<K> iterator = expireKeys.iterator();
                    while (iterator.hasNext()) {
                        K key = iterator.next();
                        iterator.remove();
                        expireMap.remove(key);

                        cache.remove(key);

                        ++count;
                    }
                } else {
                    return ;
                }
            }
        }
    }

    public void expireKey(final K key, final Long expireAt) {
        if (expireAt == null) return;

        long currentTime = System.currentTimeMillis();
        if (currentTime >= expireAt) {
            expireMap.remove(key);
            expireSortedMap.remove(expireAt);
            //移除缓存
            cache.remove(key);
        }
    }
}
