package com.cacheapi.consistentHash;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 带虚拟节点的一致性hash
 * 有序数组实现
 *
 * @author sgh
 * @date 2022/10/17 21:50
 */
public class ConsistentHashingWithoutVirtualNode {
    /**
     * 存储一个有序的哈希环
     */
    private List<Integer> temp = new ArrayList<>();

    /**
     * 虚拟节点倍数，代表每个真实节点代表几个虚拟节点
     */
    private int replicas;

    /**
     * 虚拟节点到真实节点的映射
     */
    private Map<Integer, String> virtualMap = new HashMap<>();

    public ConsistentHashingWithoutVirtualNode(int replicas) {
        this.replicas = replicas;
    }

    /**
     * 添加节点
     *
     * @param addr 地址
     */
    public void add(String addr) {
        // 添加虚拟节点
        for (int i = 0; i < replicas; ++i) {
            int hashPos = getHash(i + addr);
            insert(hashPos);
            virtualMap.put(hashPos, addr);
        }
    }

    /**
     * 有序插入哈希环中
     *
     * @param pos 位置
     */
    private void insert(int pos) {
        if (temp.isEmpty()) {
            temp.add(pos);
        } else if (temp.get(0) > pos) {
            temp.add(0, pos);
        } else {
            for (int i = 0; i < temp.size() - 1; i++) {
                if (temp.get(i) < pos && temp.get(i + 1) > pos) {
                    temp.add(i + 1, pos);
                    return;
                }
            }
            temp.add(pos);
        }
    }

    /**
     * 删除节点
     *
     * @param addr 地址
     */
    public void delete(String addr) {
        for (int i = 0; i < replicas; ++i) {
            int hashPos = getHash(i + addr);
            int index = temp.indexOf(hashPos);
            if (index != -1) {
                temp.remove(index);
                virtualMap.remove(hashPos);
            }
        }
    }

    /**
     * 获取 key 的存储节点地址
     *
     * @param key key
     * @return 节点地址
     */
    public String get(String key) {
        if (StringUtils.isEmpty(key))
            return null;
        int hash = getHash(key);
        int index = getIndex(hash);
        return virtualMap.get(temp.get(index));
    }

    /**
     * 二分查找顺时针最近的节点位置
     *
     * @param pos hash值
     * @return temp下标索引 -1代表当前无节点
     */
    private int getIndex(int pos) {
        if (temp.isEmpty()) return -1;
        int left = 0, right = temp.size() - 1;
        if (pos > temp.get(right)) return left;
        while (left < right) {
            int mid = (right - left) / 2 + left;
            if (temp.get(mid) == pos) {
                return mid;
            } else if (temp.get(mid) > pos) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return right;
    }

    /**
     * FNV1_32_HASH算法
     *
     * @param str 字符串
     * @return hash值
     */
    private int getHash(String str) {
        final int p = 16777619;
        int hash = (int) 2166136261L;
        for (int i = 0; i < str.length(); i++)
            hash = (hash ^ str.charAt(i)) * p;
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;

        // 如果算出来的值为负数则取其绝对值
        if (hash < 0)
            hash = Math.abs(hash);
        return hash;
    }

}
