package com.johnwesthoff.Entity;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.johnwesthoff.Destruct.Server;
import com.johnwesthoff.Destruct.World;

/**
 *
 * @author John
 */
public class SnowEntity extends Entity {
    // public int maker = 0;
    public int radius = 16;
    public int gravity = 1;

    public SnowEntity(int x, int y, int hspeed, int vspeed, int ma) {
        X = x;
        Y = y;
        xspeed = hspeed;
        yspeed = vspeed;
        maker = ma;
    }

    @Override
    public void onDraw(Graphics G, int viewX, int viewY) {
        if (X > viewX && X < viewX + 300 && Y > viewY && Y < viewY + 300) {
            Graphics2D g2d = (Graphics2D) G;
            G.setColor(Color.white);
            G.fillArc((int) X - viewX - 1, (int) Y - viewY - 1, 2, 2, 0, 360);
        }
    }

    int timer = 0;

    @Override
    public void onUpdate(World apples) {
        if (!apples.inBounds(X, Y) || apples.checkCollision(X, Y)) {

            alive = false;
            // apples.explode(X, Y, 32, 8, 16);
        }
        if (++timer >= 2) {
            yspeed += gravity;
            timer = 0;
        }

        /*
         * if (yspeed<12) { yspeed++; }
         */
    }

    @Override
    public void onServerUpdate(Server lol) {
        if ((!lol.earth.inBounds(X, Y)) || lol.earth.checkCollision(X, Y)) {
            lol.earth.ground.freeze((int) X, (int) Y, radius * 2);
            lol.sendMessage(Server.FREEZE, ByteBuffer.allocate(40).putInt((int) X).putInt((int) Y).putInt(radius * 4));
            alive = false;
        }

    }

    @Override
    public void cerealize(ByteBuffer out) {
        try {

            Server.putString(out, this.getClass().getName());
            out.putInt((int) X);
            out.putInt((int) Y);
            out.putInt((int) xspeed);
            out.putInt((int) yspeed);
            out.putInt(maker);
        } catch (Exception ex) {
            Logger.getLogger(ExplosionEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void reconstruct(ByteBuffer in, World world) {
        try {
            world.entityList.add(new SnowEntity(in.getInt(), in.getInt(), in.getInt(), in.getInt(), in.getInt()));
        } catch (Exception ex) {
            Logger.getLogger(SnowEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
