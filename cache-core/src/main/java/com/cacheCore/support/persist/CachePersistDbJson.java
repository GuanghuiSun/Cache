package com.cacheCore.support.persist;


import com.alibaba.fastjson.JSON;
import com.cacheCore.api.ICache;
import com.cacheCore.model.PersistDbEntry;
import org.springframework.util.StringUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 缓存持久化策略 ——基于JSON的db策略 类似于RDB
 *
 * @author sgh
 * @date 2022/10/12 9:48
 */
public class CachePersistDbJson<K, V> extends CachePersistAdaptor<K, V> {

    /**
     * 文件存储路径
     */
    private final String path;

    public CachePersistDbJson(String path) {
        this.path = path;
    }

    /**
     * 持久化
     * 格式: key长度 key+value
     * @param cache 缓存
     */
    @Override
    public void persist(ICache<K, V> cache) {
        if (path == null || path.length() == 0) return ;
        Set<Map.Entry<K, V>> entries = cache.entrySet();
        BufferedWriter bw = null;

        try {
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(path)), StandardCharsets.UTF_8.newEncoder()));

            for (Map.Entry<K, V> entry : entries) {
                K key = entry.getKey();
                Long expireTime = cache.expire().expireTime(key);
                PersistDbEntry<K, V> dbEntry = new PersistDbEntry<>();
                dbEntry.setKey(key);
                dbEntry.setValue(entry.getValue());
                dbEntry.setExpire(expireTime);

                String line = JSON.toJSONString(dbEntry);
                bw.append(line);
                bw.newLine();

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null)
                    bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public long delay() {
        return 5;
    }

    @Override
    public long period() {
        return 5;
    }

    @Override
    public TimeUnit timeUnit() {
        return TimeUnit.MINUTES;
    }
}
