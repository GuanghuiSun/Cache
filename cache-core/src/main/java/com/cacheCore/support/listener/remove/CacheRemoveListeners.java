package com.cacheCore.support.listener.remove;

import com.cacheCore.api.ICacheRemoveListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 缓存删除监听类
 *
 * @author sgh
 * @date 2022/10/13 10:15
 */
public class CacheRemoveListeners {
    private CacheRemoveListeners(){
    }

    /**
     * 默认监听类实现
     * @param <K> key
     * @param <V> value
     * @return listener list result
     */
    @SuppressWarnings("all")
    public static <K, V> List<ICacheRemoveListener<K, V>> defaults() {
        List<ICacheRemoveListener<K, V>> res = new ArrayList<>();
        res.add(new CacheRemoveListener<>());
        return res;
    }
}
