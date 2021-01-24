package com.johnwesthoff.bending.util.collections;

import java.util.Iterator;

public class SkipListIterator<T> implements Iterator<Node<T>> {
    private Node<T> cur;
    
    @Override
    public boolean hasNext() {
        return this.cur.getNextActive() != null;
    }

    @Override
    public Node<T> next() {
        Node<T> n = this.cur;
        this.cur = this.cur.getNextActive();
        return n;
    }

    public SkipListIterator(SkipList<T> sl) {
        this.cur = sl.getHead();
        while (!this.cur.isActive()) {
            this.cur = this.cur.getNext();
        }
    }
    
}
