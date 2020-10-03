package com.johnwesthoff.bending.entity;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.Graphics;
import java.nio.ByteBuffer;
import java.util.Random;

import com.johnwesthoff.bending.Server;
import com.johnwesthoff.bending.logic.World;

/**
 *
 * @author John
 */
public abstract class Entity extends Object {
    public static Random r = new Random();
    public int MYID = 0;
    public int maker;

    public Entity setID(int MYID) {
        this.MYID = MYID;
        return this;
    }

    public static final byte explosion = 0, groundinator = 2, house = 3, missile = 4, water = 5, enemy = 6, effect = 7;
    public float X, Y, yspeed, xspeed, previousX, previousY;
    public boolean alive = true;

    /**
     * Method to get the current state of the Player
     * @return true (if the Player is still alive) or false (else)
     */
    public boolean getAlive() {
        return alive;
    }

    /**
     * Method to set the current state of the Player
     * @param a Boolean to which the state should be changed
     */
    public void setAlive(boolean a) {
        alive = a;
    }

    /**
     * Get the x-coordinate
     * @return x-coordinate
     */
    public int getX() {
        return (int) X;
    }

    /**
     * Get the y-coordinate
     * @return y-coordinate
     */
    public int getY() {
        return (int) Y;
    }

    /**
     * Get the speed on the x-axis
     * @return speed on the x-axis
     */
    public int getXspeed() {
        return (int) xspeed;
    }

    /**
     * Get the speed on the y-axis
     * @return speed on the y-axis
     */
    public int getYspeed() {
        return (int) yspeed;
    }

    /**
     * Set the x-coordinate
     * @param x value to set the x coordinate (integer)
     */
    public void setX(int x) {
        X = x;
    }

    /**
     * Set the y-coordinate
     * @param y value to set the y coordinate (integer)
     */
    public void setY(int y) {
        Y = y;
    }

    /**
     * Set the speed on the x-axis
     * @param x value to set the speed (integer)
     */
    public void setXspeed(int x) {
        xspeed = x;
    }

    /**
     * Set the speed on the y-axis
     * @param y value to set the speed (integer)
     */
    public void setYspeed(int y) {
        yspeed = y;
    }

    public abstract void onDraw(Graphics G, int viewX, int viewY);

    public abstract void onUpdate(World apples);

    public abstract void cerealize(ByteBuffer out);

    public static void reconstruct(ByteBuffer in, World world) {

    }

    /**
     * Move the Player with the current set speed
     */
    public void move() {
        previousX = X;
        previousY = Y;
        X += xspeed * World.deltaTime();
        Y += yspeed * World.deltaTime();
    }

    public double lengthdir_x(double R, double T) {
        return (R * Math.cos(T * Math.PI / 180));
    }

    public double lengthdir_y(double R, double T) {
        return (-R * Math.sin(T * Math.PI / 180));
    }

    public double pointDir(double x1, double y1, double x2, double y2) {
        return Math.toDegrees(Math.atan2(y2 - y1, x2 - x1));
    }

    public double pointDis(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    /**
     * Get the distance to an entity
     * @param e the entity to which the distance should be returned
     * @return distance
     */
    public double distanceToEntity(Entity e) {
        return pointDis(X, Y, e.X, e.Y);
    }

    public void drawOverlay(Graphics g, int viewx, int viewy) {
    }

    public void drawAdditive(Graphics g, int viewx, int viewy) {

    }

    public void onServerUpdate(Server lol) {

    }

    @Override
    public String toString() {
        String whatIam = "~~~" + getClass().getName() + "~~~" + "\nX: " + X + " Y: " + Y + "\nI am "
                + (alive ? "alive" : "dead");
        return whatIam;
    }
}
