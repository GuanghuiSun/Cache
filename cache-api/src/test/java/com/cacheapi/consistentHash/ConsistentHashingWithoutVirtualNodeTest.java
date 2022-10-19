package com.cacheapi.consistentHash;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author sgh
 * @date 2022/10/18 18:41
 */
class ConsistentHashingWithoutVirtualNodeTest {

    @Test
    void demo() {
        ConsistentHashingWithoutVirtualNode node = new ConsistentHashingWithoutVirtualNode(2);
        node.add("192.168.1.1");
        node.add("192.168.1.2");
        node.add("192.168.1.3");
//        System.out.println(node.get("thisIsTest"));
//        System.out.println(node.get("thisIsTest"));
//        System.out.println(node.get("thisIsTest2"));
//        System.out.println(node.get("thisIsTest3"));
        System.out.println(node.get("192.168.1.2"));
    }

}