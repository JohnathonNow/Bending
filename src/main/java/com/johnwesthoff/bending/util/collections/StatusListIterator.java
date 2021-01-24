package com.johnwesthoff.bending.util.collections;

import java.util.Iterator;

/**
 * The iterator for a {@link com.johnwesthoff.bending.util.collections.StatusList}.
 */
public class StatusListIterator<T> implements Iterator<Node<T>> {
    private Node<T> cur;
    
    
    /** 
     * @return boolean whether or not there are more active elements
     */
    @Override
    public boolean hasNext() {
        return this.cur.getNextActive() != null;
    }

    
    /** 
     * @return Node<T> the next active element in the statuslist
     */
    @Override
    public Node<T> next() {
        Node<T> n = this.cur;
        this.cur = this.cur.getNextActive();
        return n;
    }

    public StatusListIterator(StatusList<T> sl) {
        this.cur = sl.getHead();
        while (!this.cur.isActive()) {
            this.cur = this.cur.getNext();
        }
    }
    
}
