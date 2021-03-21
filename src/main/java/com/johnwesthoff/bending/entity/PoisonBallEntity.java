package com.johnwesthoff.bending.entity;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.Color;
import java.awt.Graphics;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.johnwesthoff.bending.Constants;
import com.johnwesthoff.bending.Server;
import com.johnwesthoff.bending.logic.World;
import com.johnwesthoff.bending.networking.handlers.PuddleEvent;

/**
 *
 * @author John
 */
public class PoisonBallEntity extends Entity {
    // public int maker = 0;
    public int radius = 16;
    int a1, a2, a3;
    int s1, s2, s3;

    public PoisonBallEntity(int x, int y, int hspeed, int vspeed, int ma) {
        X = x;
        Y = y;
        xspeed = hspeed;
        yspeed = vspeed;
        maker = ma;
        a1 = 60 + r.nextInt(120);
        a2 = 60 + r.nextInt(120);
        a3 = 60 + r.nextInt(120);
        s1 = r.nextInt(360);
        s2 = r.nextInt(360);
        s3 = r.nextInt(360);
    }

    @Override
    public void onDraw(Graphics G, int viewX, int viewY) {
        if (X > viewX && X < viewX + Constants.WIDTH_INT && Y > viewY && Y < viewY + Constants.HEIGHT_INT) {
            G.setColor(Color.green);
            G.fillArc((int) (X - radius / 2) - viewX, (int) (Y - radius / 2) - viewY, radius, radius, 0, 360);

            G.setColor(Color.LIGHT_GRAY);
            G.fillArc((int) (X - 2) - viewX, (int) (Y - 2) - viewY, 4, 4, s1, a1);
            G.fillArc((int) (X - 4) - viewX, (int) (Y - 4) - viewY, 8, 8, s2, a2);
            G.fillArc((int) (X - 6) - viewX, (int) (Y - 6) - viewY, 12, 12, s3, a3);
        }
    }

    @Override
    public void onUpdate(World apples) {
        if (!apples.inBounds(X, Y) || apples.checkCollision(X, Y)) {

            alive = false;
            // apples.explode(X, Y, 32, 8, 16);
        }
        yspeed += Constants.GRAVITY;

        /*
         * if (yspeed<12) { yspeed++; }
         */
    }

    @Override
    public void onServerUpdate(Server lol) {
        if (lol.earth.checkCollision(X, Y)) {
            radius = 48;
            lol.earth.ground.puddle((int) X, (int) Y, radius, Constants.GAS);
            lol.sendMessage(PuddleEvent.getPacket((int) (X), (int) (Y), radius, Constants.GAS));
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
            world.entityList.add(new JuiceBallEntity(in.getInt(), in.getInt(), in.getInt(), in.getInt(), in.getInt()));
        } catch (Exception ex) {
            Logger.getLogger(JuiceBallEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
