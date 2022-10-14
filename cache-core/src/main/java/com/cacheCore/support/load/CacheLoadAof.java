package com.cacheCore.support.load;

import com.alibaba.fastjson.JSON;
import com.cacheCore.annotation.CacheInterceptor;
import com.cacheCore.api.ICache;
import com.cacheCore.api.ICacheLoad;
import com.cacheCore.core.Cache;
import com.cacheCore.model.PersistAofEntry;
import com.cacheCore.support.util.FileUtils;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 缓存加载策略 —— 读 AOF文件
 *
 * @author sgh
 * @date 2022/10/14 15:51
 */
@Slf4j
public class CacheLoadAof<K,V> implements ICacheLoad<K, V> {

    /**
     * 方法映射
     */
    public static final Map<String, Method> METHOD_MAP = new HashMap<>();

    /**
     * 初始化
     */
    static {
        Method[] methods = Cache.class.getMethods();

        for (Method method : methods) {
            CacheInterceptor annotation = method.getAnnotation(CacheInterceptor.class);

            if (annotation != null)
                if (annotation.aof())
                    METHOD_MAP.put(method.getName(), method);

        }
    }

    /**
     * 文件路径
     */
    private final String path;

    public CacheLoadAof(String path) {
        this.path = path;
    }

    @Override
    public void load(ICache<K, V> cache) throws InvocationTargetException, IllegalAccessException {
        List<String> lines = FileUtils.readAll(path);
        log.debug("Loading, Begin AOF load from file, path: {}", path);
        if (lines.isEmpty()) {
            log.info("Loading File is empty!");
        }
        for (String line : lines) {
            if (line == null) continue;

            PersistAofEntry entry = JSON.parseObject(line, PersistAofEntry.class);
            final String methodName = entry.getMethodName();
            final Object[] params = entry.getParams();

            Method method = METHOD_MAP.get(methodName);

            //反射调用
            method.invoke(cache, params);
        }
        log.debug("Loading, End AOF load from file, path: {}", path);
    }
}
