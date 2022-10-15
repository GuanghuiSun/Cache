package com.cacheCore.model;

/**
 * 携带频率信息的节点
 *
 * @author sgh
 * @date 2022/10/15 18:46
 */
public class FreqNode<K, V> {

    private K key;

    private V value;

    private int frequency = 1;

    public FreqNode(K key) {
        this.key = key;
    }

    public K key() {
        return this.key;
    }

    public FreqNode<K, V> key(K key) {
        this.key = key;
        return this;
    }

    public V value() {
        return this.value;
    }

    public FreqNode<K, V> value(V value) {
        this.value = value;
        return this;
    }

    public int frequency() {
        return this.frequency;
    }

    public void incrFre() {
        this.frequency += 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FreqNode)) return false;

        FreqNode<?, ?> feqNode = (FreqNode<?, ?>) o;

        if (frequency != feqNode.frequency) return false;
        if (key != null ? !key.equals(feqNode.key) : feqNode.key != null) return false;
        return value != null ? value.equals(feqNode.value) : feqNode.value == null;
    }

    @Override
    public int hashCode() {
        int result = key != null ? key.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + frequency;
        return result;
    }

    @Override
    public String toString() {
        return "FeqNode{" +
                "key=" + key +
                ", value=" + value +
                ", frequency=" + frequency +
                '}';
    }
}
