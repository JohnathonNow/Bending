package com.johnwesthoff.bending.logic.terrain;

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

    public T getData() {
        return data;
    }

    public void setData(final T data) {
        this.data = data;
    }

    public void deactivate() {
        if (!active) return;

        this.prevActive.nextActive = this.nextActive;
        this.nextActive.prevActive = this.prevActive;
        this.active = false;
    }

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

    public Node<T> getNext() {
        return next;
    }

    public Node<T> getPrev() {
        return prev;
    }

    public void setAfter(final Node<T> n) {
        this.next = n;
        this.nextActive = n;
    }

    public Node<T> getNextActive() {
        return nextActive;
    }

    public Node<T> getPrevActive() {
        return prevActive;
    }

    public boolean isActive() {
        return active;
    }
}
