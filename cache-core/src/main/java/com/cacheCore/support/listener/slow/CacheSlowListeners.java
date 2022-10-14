package com.cacheCore.support.listener.slow;

import com.cacheCore.api.ICacheSlowListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 慢日志监听工具类
 *
 * @author sgh
 * @date 2022/10/13 19:58
 */
public final class CacheSlowListeners {

    private CacheSlowListeners() {
    }

    /**
     * 无
     *
     * @return 空监听列表
     */
    public static List<ICacheSlowListener> none() {
        return new ArrayList<>();
    }

    /**
     * 默认实现
     *
     * @return 默认
     */
    public static ICacheSlowListener defaults() {
        return new CacheSlowListener();
    }
}
