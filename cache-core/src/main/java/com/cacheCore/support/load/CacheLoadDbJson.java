package com.cacheCore.support.load;

import com.alibaba.fastjson.JSON;
import com.cacheCore.api.ICache;
import com.cacheCore.api.ICacheLoad;
import com.cacheCore.model.PersistDbEntry;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 缓存加载策略 —— 读db文件
 *
 * @author sgh
 * @date 2022/10/12 16:22
 */
@Slf4j
public class CacheLoadDbJson<K, V> implements ICacheLoad<K, V> {

    /**
     * 文件路径
     */
    private final String path;

    public CacheLoadDbJson(String path) {
        this.path = path;
    }

    @Override
    public void load(ICache<K, V> cache) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path)), StandardCharsets.UTF_8.newDecoder()));
            List<String> lines = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
            if (lines.isEmpty()) {
                log.debug("File is empty!");
                return ;
            }
            for (String s : lines) {
                if (StringUtils.isEmpty(s)) {
                    continue;
                }
                PersistDbEntry<K, V> entry = JSON.parseObject(s, PersistDbEntry.class);
                Long expireTime = entry.getExpire();
                K key = entry.getKey();
                V value = entry.getValue();
                cache.put(key, value);

                if (ObjectUtils.isEmpty(expireTime))
                    cache.expire(key, expireTime);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)
                    br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
