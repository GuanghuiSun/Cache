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
     * @return result
     */
    public E getFirst() {
        return first.data;
    }

    /**
     * 获取链表的最后一个元素
     * 如果不存在 返回null
     * @return result
     */
    public E getLast() {
        return last.data;
    }

    /**
     * 获取链表长度
     * @return size
     */
    public int getSize() {
        return size;
    }

    public void add(E e) {
        final Node<E> l = last;
        final Node<E> newNode = new Node<E>(l, e, null);
        last = newNode;
        if (l == null)
            first = newNode;
        else
            l.next = newNode;
        ++size;
    }

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

    private static class Node<E> {
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
