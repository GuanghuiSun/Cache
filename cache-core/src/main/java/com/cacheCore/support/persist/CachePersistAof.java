package com.cacheCore.support.persist;

import com.cacheCore.api.ICache;
import com.cacheCore.support.util.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.FileUpload;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 缓存持久化策略 —— AOF
 *
 * @author sgh
 * @date 2022/10/13 20:35
 */
@Slf4j
public class CachePersistAof<K, V> extends CachePersistAdaptor<K, V> {

    /**
     * 缓存列表
     */
    private final List<String> bufferList = new ArrayList<>();

    private final String path;

    public CachePersistAof(String path) {
        this.path = path;
    }

    /**
     * 持久化
     * <p>
     * 格式： key长度 key+value
     *
     * @param cache 缓存信息
     */
    @Override
    public void persist(ICache<K, V> cache) {
        log.debug("Begin AOF persisting to file");
        FileUtils.writeAll(path, bufferList);
        log.debug("Finished AOF persisting to file!");
        super.persist(cache);
    }

    @Override
    public long delay() {
        return 1;
    }

    @Override
    public long period() {
        return 1;
    }

    @Override
    public TimeUnit timeUnit() {
        return TimeUnit.SECONDS;
    }

    /**
     * 添加缓存信息到buffer列表中
     *
     * @param json 缓存信息
     */
    public void append(final String json) {
        if (StringUtils.isNotEmpty(json)) {
            bufferList.add(json);
        }
    }
}
