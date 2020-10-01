package com.johnwesthoff.bending.entity;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.Graphics;
import java.nio.ByteBuffer;
import java.util.Random;

import com.johnwesthoff.bending.Client;
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

    public boolean getAlive() {
        return alive;
    }

    public void setAlive(boolean a) {
        alive = a;
    }

    public int getX() {
        return (int) X;
    }

    public int getY() {
        return (int) Y;
    }

    public int getXspeed() {
        return (int) xspeed;
    }

    public int getYspeed() {
        return (int) yspeed;
    }

    public void setX(int x) {
        X = x;
    }

    public void setY(int y) {
        Y = y;
    }

    public void setXspeed(int x) {
        xspeed = x;
    }

    public void setYspeed(int y) {
        yspeed = y;
    }

    public abstract void onDraw(Graphics G, int viewX, int viewY);

    public abstract void onUpdate(World apples);

    public abstract void cerealize(ByteBuffer out);

    public static void reconstruct(ByteBuffer in, World world) {

    }

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

    public void checkAndHandleCollision(Client client) {
        // do nothing
    }
}
