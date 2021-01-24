package com.johnwesthoff.bending.util.collections;

import java.util.Iterator;

public class SkipList<T> implements Iterable<Node<T>> {
    private Node<T> head;
    private Node<T> tail;
    private int id;

    public SkipList() {
        this.head = null;
        this.tail = null;
        this.id = 0;
    }

    public void add(T toAdd) {
        Node<T> n = new Node<>(null, this.tail, null, this.tail, this.id++, true, toAdd);

        if (this.tail != null) {
            this.tail.setAfter(n);
        }

        this.tail = n;

        if (this.head == null) {
            this.head = n;
        }
    }

    public Node<T> getHead() {
        return head;
    }

    public Node<T> getTail() {
        return tail;
    }

    @Override
    public Iterator<Node<T>> iterator() {
        return new SkipListIterator<>(this);
    }

}
