package com.cacheCore.support.persist;

import com.cacheCore.api.ICache;
import com.cacheCore.api.ICachePersist;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * 内部缓存持久化
 *
 * @author sgh
 * @date 2022/10/13 19:30
 */
@Slf4j
public class InnerCachePersist<K, V> {

    private final ICache<K, V> cache;

    private final ICachePersist<K, V> persist;

    /**
     * 线程执行类
     */
    private static final ScheduledExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor();

    public InnerCachePersist(ICache<K, V> cache, ICachePersist<K, V> persist) {
        this.cache = cache;
        this.persist = persist;

        this.init();
    }

    /**
     * 初始化
     */
    private void init() {
        EXECUTOR_SERVICE.scheduleAtFixedRate(() -> {
            try {
                log.debug("开始持久化缓存信息");
                persist.persist(cache);
                log.debug("完成持久化缓存信息");
            } catch (Exception e) {
                log.error("文件持久化异常", e);
            }
        }, persist.delay(), persist.period(), persist.timeUnit());
    }
}
