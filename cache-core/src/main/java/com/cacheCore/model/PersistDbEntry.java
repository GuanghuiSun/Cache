package com.cacheCore.model;

/**
 * 缓存持久化明细 —— DB实现
 *
 * @author sgh
 * @date 2022/10/12 10:19
 */
public class PersistDbEntry<K, V> {

    /**
     * key
     */
    private K key;

    /**
     * value
     */
    private V value;

    /**
     * expire
     */
    private Long expire;

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public Long getExpire() {
        return expire;
    }

    public void setExpire(Long expire) {
        this.expire = expire;
    }
}
