package com.johnwesthoff.bending.util.collections;

import java.util.Iterator;
/**
 * A doubly linked list data structure in which certain elements can be marked as active or not.
 * When iterating over the collection, only active elements are returned.
 * 
 * Marking a node as inactive is an O(1) operation, though marking a node as active may be O(n) in the worst case.
 * In certain applications marking as active can be more efficient.
 */
public class StatusList<T> implements Iterable<Node<T>> {
    private Node<T> head;
    private Node<T> tail;
    private int id;

    public StatusList() {
        this.head = null;
        this.tail = null;
        this.id = 0;
    }

    
    /** 
     * Adds a new node to the statuslist, marking it as active
     * 
     * @param toAdd the piece of data to add to the statuslist
     */
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

    
    /** 
     * Gets the head node of the list.
     * 
     * @return Node<T> the head of the statuslist
     */
    public Node<T> getHead() {
        return head;
    }

    
    /** 
     * Gets the tail node of the list.
     * 
     * @return Node<T> the tail of the statuslist
     */
    public Node<T> getTail() {
        return tail;
    }

    
    /** 
     * Gets an iterator for the statuslist that iterates over the active nodes of the list.
     * 
     * @return Iterator<Node<T>> the active iterator of the statuslist
     */
    @Override
    public Iterator<Node<T>> iterator() {
        return new StatusListIterator<>(this);
    }

}
