package com.cacheCore.support.struct;

/**
 * 双向链表实现
 *
 * @author sgh
 * @date 2022/10/11 17:48
 */
public class DoublyLinkedList<E> {
    private Node<E> first;
    private Node<E> last;

    private int size = 0;

    /**
     * 获取链表的第一个元素
     * 如果不存在 返回null
     *
     * @return result
     */
    public E getFirst() {
        return first.data;
    }

    /**
     * 获取链表的最后一个元素
     * 如果不存在 返回null
     *
     * @return result
     */
    public E getLast() {
        return last.data;
    }

    /**
     * 获取链表长度
     *
     * @return size
     */
    public int getSize() {
        return size;
    }

    /**
     * 添加到队尾 —— FIFO
     *
     * @param e 元素
     * @return 元素节点
     */
    public Node<E> add(E e) {
        final Node<E> l = last;
        final Node<E> newNode = new Node<>(l, e, null);
        last = newNode;
        if (l == null)
            first = newNode;
        else
            l.next = newNode;
        ++size;
        return newNode;
    }

    /**
     * 移除队头的元素 —— FIFO
     *
     * @return 元素数据
     */
    public E remove() {
        final Node<E> f = first;
        final E res = f.data;
        final Node<E> next = f.next;
        //help GC
        f.data = null;
        f.next = null;
        first = next;
        if (next == null)
            last = null;
        else
            next.prev = null;
        --size;
        return res;
    }

    /**
     * 移除队尾节点 —— LRU
     *
     * @return 元素数据
     */
    public E removeTail() {
        final Node<E> l = last;
        final E res = l.data;
        final Node<E> pre = l.prev;
        l.prev = null;
        l.data = null;
        last = pre;
        if (pre != null) {
            pre.next = null;
        }
        --size;
        return res;
    }

    /**
     * 移除指定节点
     *
     * @param node 节点
     * @return 元素数据
     */
    public E remove(Node<E> node) {
        final E res = node.data;
        final Node<E> next = node.next;
        final Node<E> prev = node.prev;
        if (prev == null) {
            first = next;
        } else {
            prev.next = next;
            node.prev = null;
        }
        if (next == null) {
            last = prev;
        } else {
            next.prev = prev;
            node.next = null;
        }
        node.data = null;
        --size;
        return res;
    }

    /**
     * 添加到队头
     *
     * @param node 节点
     */
    public void addToHead(Node<E> node) {
        final Node<E> f = first;
        first = node;
        if (f == null) {
            last = node;
        } else {
            f.prev = node;
        }
        ++size;
    }

    /**
     * 移动指定节点到队头
     *
     * @param node 节点
     */
    public void removeToHead(Node<E> node) {
        remove(node);
        addToHead(node);
    }

    /**
     * 返回链表的大小
     *
     * @return size
     */
    public int size() {
        return size;
    }

    /**
     * 判读队列是否为空
     *
     * @return empty
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    public class Node<E> {
        E data;
        Node<E> prev;
        Node<E> next;

        Node(Node<E> prev, E element, Node<E> next) {
            this.prev = prev;
            this.data = element;
            this.next = next;
        }

    }
}
