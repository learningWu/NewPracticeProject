package com.chulan.newtestproject.specialstructure;

/**
 * Created by wuzixuan on 2021/8/27
 * 双向链表
 */
public class SelfLinkedList<T> {
    private Node<T> head;
    private Node<T> tail;
    private int size;

    public Node<T> put(T value) {

        Node<T> node = new Node<T>(value, tail, null);
        if (head == null) {
            head = node;
            tail = node;
        } else {
            tail.next = node;
            node.pre = tail;
            tail = node;
        }
        size++;
        return node;
    }

    @Override
    public String toString() {
        Node<T> temp = head;
        StringBuilder sb = new StringBuilder();
        while (temp != null) {
            sb.append(temp.value);
            temp = temp.next;
        }
        return sb.toString();
    }

    public Node<T> get(T value) {
        Node<T> temp = head;
        while (temp != null) {
            if (temp.value == value) {
                return temp;
            }
            temp = temp.next;
        }
        return null;
    }

    public Node<T> insert(int index, T value) {
        if (index < 0 || index > size) {
            return null;
        }
        if (index == size) {
            // 插入到最后一个节点之后
            return put(value);
        }

        // 插入到目标节点之前
        Node<T> targetNode = head;
        int i = 0;
        while (i < index) {
            targetNode = targetNode.next;
            i++;
        }
        Node<T> preTemp = targetNode.pre;
        Node<T> node = new Node<T>(value, preTemp, targetNode);

        if (preTemp == null) {
            head = node;
        } else {
            preTemp.next = node;
            targetNode.pre = node;
        }

        size++;
        return node;
    }

    public static class Node<E> {
        public E value;
        public Node<E> pre;
        public Node<E> next;

        Node(E value, Node<E> pre, Node<E> next) {
            this.value = value;
            this.pre = pre;
            this.next = next;
        }
    }
}
