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


public class WaterBallEntity extends Entity {
    // public int maker = 0;
    public int radius = 16;
    public int gravity = 1;
    int a1, a2, a3;
    int s1, s2, s3;

    public WaterBallEntity(int x, int y, int hspeed, int vspeed, int ma) {
        X = x;
        Y = y;
        xspeed = hspeed;
        yspeed = vspeed;
        maker = ma;
        a1 = Constants.SIXTY_DEGREE_ANGLE + r.nextInt(Constants.ONE_THIRD_FULL_ANGLE);
        a2 = Constants.SIXTY_DEGREE_ANGLE + r.nextInt(Constants.ONE_THIRD_FULL_ANGLE);
        a3 = Constants.SIXTY_DEGREE_ANGLE + r.nextInt(Constants.ONE_THIRD_FULL_ANGLE);
        s1 = r.nextInt(Constants.FULL_ANGLE);
        s2 = r.nextInt(Constants.FULL_ANGLE);
        s3 = r.nextInt(Constants.FULL_ANGLE);
    }

    @Override
    public void onDraw(Graphics G, int viewX, int viewY) {
        if (X > viewX && X < viewX + Constants.WIDTH_INT && Y > viewY && Y < viewY + Constants.HEIGHT_INT) {
            G.setColor(Color.blue);
            G.fillArc((int) (X - radius / 2) - viewX, (int) (Y - radius / 2) - viewY, radius, radius, 0,
                    Constants.FULL_ANGLE);

            G.setColor(Color.cyan);
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
        yspeed += gravity;

        /*
         * if (yspeed<12) { yspeed++; }
         */
    }

    @Override
    public void onServerUpdate(Server lol) {
        if (lol.earth.checkCollision((int) X, (int) Y) || lol.earth.isLiquid((int) X, (int) Y)) {
            radius *= 2.85;
            lol.earth.ground.puddle((int) X, (int) Y, radius);
            lol.sendMessage(PuddleEvent.getPacket((int) X, (int) Y, radius));
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

    /**
     * Reconstruct the water ball entity
     * 
     * @param in
     * @param world World in which the entity should be reconstructed
     */
    public static void reconstruct(ByteBuffer in, World world) {
        try {
            world.entityList.add(new WaterBallEntity(in.getInt(), in.getInt(), in.getInt(), in.getInt(), in.getInt()));
        } catch (Exception ex) {
            Logger.getLogger(WaterBallEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
