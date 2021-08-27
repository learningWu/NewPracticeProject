package com.chulan.newtestproject.specialstructure;

/**
 * Created by wuzixuan on 2021/8/27
 * 双向链表
 */
public class SelfLinkedList<T> {
    private Node<T> head;
    private Node<T> tail;
    private int size;

    public Boolean add(T value) {
        Node<T> last = tail;
        if (last != null) {
            linkAfter(value, last);
        } else {
            Node<T> node = new Node<T>(null, value, null);
            head = node;
            tail = node;
        }
        size++;
        return true;
    }

    @Override
    public String toString() {
        Node<T> temp = head;
        StringBuilder sb = new StringBuilder();
        while (temp != null) {
            sb.append(temp.value);
            sb.append(" ");
            temp = temp.next;
        }
        return sb.toString().trim();
    }

    void linkBefore(T value, Node<T> target) {
        Node<T> pre = target.pre;
        Node<T> node = new Node<>(pre, value, target);
        if (pre == null) {
            head = node;
        } else {
            pre.next = node;
        }
        target.pre = node;
    }

    void linkAfter(T value, Node<T> target) {
        Node<T> next = target.next;
        Node<T> node = new Node<>(target, value, next);
        if (next == null) {
            tail = node;
        } else {
            next.pre = node;
        }
        target.next = node;
    }

    public Node<T> search(T value) {
        Node<T> temp = head;
        while (temp != null) {
            if (temp.value == value) {
                return temp;
            }
            temp = temp.next;
        }
        return null;
    }

    public Boolean insert(int index, T value) {
        if (index < 0 || index > size) {
            return null;
        }
        if (index == size) {
            // 插入到最后一个节点之后
            return add(value);
        }

        // 插入到目标节点之前
        Node<T> targetNode = node(index);
        linkBefore(value, targetNode);
        size++;
        return true;
    }

    Node<T> node(int index) {
        // index 在前半部分。就从 head 开始
        // index 在后半部分。就从 head 开始
        if (index < (size >> 1)) {
            Node<T> temp = head;
            for (int i = 0; i < index; i++) {
                temp = temp.next;
            }
            return temp;
        } else {
            Node<T> temp = tail;
            for (int i = size - 1; i > index; i--) {
                temp = temp.pre;
            }
            return temp;
        }
    }

    public static class Node<E> {
        public E value;
        public Node<E> pre;
        public Node<E> next;

        Node(Node<E> pre, E value, Node<E> next) {
            this.pre = pre;
            this.value = value;
            this.next = next;
        }
    }
}
