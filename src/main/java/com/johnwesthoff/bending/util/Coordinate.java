package com.johnwesthoff.bending.util;

public class Coordinate {
    public int x;
    public int y;
    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public Coordinate up() {
        return new Coordinate(this.x, this.y - 1);
    }
    public Coordinate down() {
        return new Coordinate(this.x, this.y + 1);
    }
    public Coordinate left() {
        return new Coordinate(this.x - 1, this.y);
    }
    public Coordinate right() {
        return new Coordinate(this.x + 1, this.y);
    }
    public Coordinate[] getNeighbors() {
        return new Coordinate[] {
            this.up(),
            this.down(),
            this.left(),
            this.right()
        };
    }
}
