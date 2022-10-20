package com.cacheCore.support.expire;

import com.cacheCore.api.ICache;
import com.cacheCore.api.ICacheExpire;
import com.cacheCore.api.ICacheRemoveListener;
import com.cacheCore.support.listener.remove.CacheRemoveListenerContext;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * 缓存过期 —— 普通策略 随机获取
 *
 * @author sgh
 * @date 2022/10/20 11:03
 */
public class CacheExpireRandom<K, V> implements ICacheExpire<K, V> {

    /**
     * 单次清空的数量限制
     */
    private static final int COUNT_LIMIT = 100;

    /**
     * 过期 map
     */
    private final Map<K, Long> expireMap = new HashMap<>();

    /**
     * 缓存实现
     */
    private final ICache<K, V> cache;

    /**
     * 是否启用快模式
     */
    private volatile boolean fastMode = false;

    /**
     * 线程执行类
     */
    private static final ScheduledExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor();

    public CacheExpireRandom(ICache<K, V> cache) {
        this.cache = cache;
        this.init();
    }

    private void init() {
        EXECUTOR_SERVICE.scheduleAtFixedRate(new ExpireThreadRandom(), 10, 10, TimeUnit.SECONDS);
    }

    private class ExpireThreadRandom implements Runnable {

        @Override
        public void run() {
            //判断是否为空
            if (expireMap.isEmpty())
                return;
            //是否启用快模式
            if (fastMode) {
                expireKeys(10L);
            }

            expireKeys(100L);
        }
    }

    private void expireKeys(long timeoutMills) {
        //设置超时时间
        final long timeLimit = System.currentTimeMillis() + timeoutMills;
        //恢复为慢模式
        this.fastMode = false;

        int count = 0;
        while (true) {
            if (count >= COUNT_LIMIT) {
                return;
            }
            if (System.currentTimeMillis() >= timeLimit) {
                this.fastMode = true;
                return;
            }
            K randomKey = getRandomKey();
            expireKey(randomKey, expireMap.get(randomKey));

            ++count;
        }
    }

    /**
     * 过期处理key
     *
     * @param key      key
     * @param expireAt 过期时间
     * @return 是否成功
     */
    private boolean expireKey(final K key, Long expireAt) {
        if (expireAt == null || expireAt <= 0)
            return false;
        long currentTime = System.currentTimeMillis();
        if (currentTime >= expireAt) {
            expireMap.remove(key);
            V remove = cache.remove(key);

            CacheRemoveListenerContext<K, V> context = CacheRemoveListenerContext.<K, V>newInstance();
            for (ICacheRemoveListener<K, V> removeListener : cache.removeListeners()) {
                removeListener.listen(context);
            }
            return true;
        }
        return false;
    }

    /**
     * 随机获取key
     *
     * @return key
     */
    private K getRandomKey() {
        Random random = ThreadLocalRandom.current();

        List<K> list = new ArrayList<>(expireMap.keySet());
        int index = random.nextInt(list.size());
        return list.get(index);
    }

    @Override
    public void expire(K key, long expireAt) {
        expireMap.put(key, expireAt);
    }

    @Override
    public void refreshExpire(Collection<K> keyList) {
        if (keyList.isEmpty())
            return ;
        if (keyList.size() <= expireMap.size()) {
            for (K key : keyList) {
                Long expireAt = expireMap.get(key);
                expireKey(key, expireAt);
            }
        } else {
            for (Map.Entry<K, Long> entry : expireMap.entrySet()) {
                expireKey(entry.getKey(), entry.getValue());
            }
        }
    }

    @Override
    public Long expireTime(K key) {
        return expireMap.get(key);
    }
}
