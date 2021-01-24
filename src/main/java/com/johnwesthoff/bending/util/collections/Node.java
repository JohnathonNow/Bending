package com.johnwesthoff.bending.util.collections;

/**
 * A node in a {@link com.johnwesthoff.bending.util.collections.StatusList}.
 */
public class Node<T> {
    private Node<T> next;
    private final Node<T> prev;
    private Node<T> nextActive;
    private Node<T> prevActive;
    private final int id;
    private boolean active;
    private T data;

    public Node(final Node<T> next, final Node<T> prev, final Node<T> nextActive, final Node<T> prevActive, final int id, final boolean active, final T data) {
        this.next = next;
        this.prev = prev;
        this.nextActive = nextActive;
        this.prevActive = prevActive;
        this.id = id;
        this.active = active;
        this.setData(data);
    }

    
    /**
     * Gets the data object this node wraps 
     * 
     * @return T the data held within this node
     */
    public T getData() {
        return data;
    }

    
    /** 
     * Sets what data we are wrapping
     * 
     * @param data the data to wrap
     */
    public void setData(final T data) {
        this.data = data;
    }

    /*
     * Marks this node as inactive
     */
    public void deactivate() {
        if (!active) return;

        this.prevActive.nextActive = this.nextActive;
        this.nextActive.prevActive = this.prevActive;
        this.active = false;
    }

    
    /** 
     * Marks this node as active, adjusting the pointers in the statuslist accordingly.
     * 
     * Works by advancing down the list until we find an active node that
     * points past this node, and then adjusting the pointers.
     * 
     * @param head the node to start searching from
     */
    public void activate(Node<T> head) {
        if (active) return;

        while (head.nextActive != null && head.nextActive.id < this.id) {
            head = head.nextActive;
        }
        this.prevActive = head;
        this.nextActive = head.nextActive;
        this.nextActive.prevActive = this;
        head.nextActive = this;
        this.active = true;
    }

    
    /**
     * Gets the next node in the list, regardless of status 
     * 
     * @return Node<T> the next node in the statuslist
     */
    public Node<T> getNext() {
        return next;
    }

    
    /** 
     * Gets the previous node in the list, regardless of status
     * 
     * @return Node<T> the previous node in the statuslist
     */
    public Node<T> getPrev() {
        return prev;
    }

    
    /** 
     * Used primarily for creating the statuslist, this method sets the next pointers for this node.
     * 
     * @param n the new next node from this node
     */
    public void setAfter(final Node<T> n) {
        this.next = n;
        this.nextActive = n;
    }

    
    /** 
     * Gets the next active node.
     * 
     * @return Node<T> the next active node after this one in the statuslist
     */
    public Node<T> getNextActive() {
        return nextActive;
    }

    
    /** 
     * Gets the previous active node.
     * 
     * @return Node<T> the previous active node before this one in the statuslist
     */
    public Node<T> getPrevActive() {
        return prevActive;
    }

    
    /** 
     * Returns the status of this node.
     * 
     * @return boolean whether or not this node is active
     */
    public boolean isActive() {
        return active;
    }
}
