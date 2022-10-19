package com.cacheapi.model;

import lombok.Data;

/**
 * 请求实体
 *
 * @author sgh
 * @date 2022/10/18 17:12
 */
@Data
public class CacheEntity {
    private String key;
    private String value;
    private Long expire;

    @Override
    public String toString() {
        return "CacheEntity{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                ", expire=" + expire +
                '}';
    }
}
