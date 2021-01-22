package com.johnwesthoff.bending.entity;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import com.johnwesthoff.bending.Client;
import com.johnwesthoff.bending.Constants;
import com.johnwesthoff.bending.Server;
import com.johnwesthoff.bending.logic.World;

import java.awt.*;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author John
 */
public class RodEntity extends Entity {
    // public int maker = 0;
    public int radius = Constants.RADIUS_REGULAR;

    public RodEntity(int x, int y, int hspeed, int ma) {
        X = x;
        Y = y;
        xspeed = hspeed;
        maker = ma;
    }

    @Override
    public void onDraw(Graphics G, int viewX, int viewY) {
        if (X > viewX && X < viewX + Constants.WIDTH_INT && Y > viewY && Y < viewY + Constants.HEIGHT_INT) {
            G.setColor(Color.gray);
            G.fillRect((int) X - 2 - viewX, (int) Y - 32 - viewY, 4, 32);
            G.setColor(Color.darkGray);
            G.fillRect((int) X - 16 - viewX, (int) Y - 4 - viewY, 32, 4);
            G.setColor(Color.lightGray);
            G.fillArc((int) X - 4 - viewX, (int) Y - 36 - viewY - 4, 8, 8, 0, Constants.FULL_ANGLE);
            G.setColor(Color.black);
            G.drawRect((int) X - 2 - viewX, (int) Y - 32 - viewY, 4, 32);
            G.drawRect((int) X - 16 - viewX, (int) Y - 4 - viewY, 32, 4);
            G.drawArc((int) X - 4 - viewX, (int) Y - 36 - viewY - 4, 8, 8, 0, Constants.FULL_ANGLE);
            G.setColor(Color.YELLOW);
            int dir = r.nextInt(Constants.FULL_ANGLE), dis = 8 + r.nextInt(Constants.RADIUS_REGULAR);
            int xx = (int) X + (int) Client.lengthdir_x(dis, dir) - viewX,
                    yy = (int) Y + (int) Client.lengthdir_y(dis, dir) - 34 - viewY;
            dir = r.nextInt(Constants.FULL_ANGLE);
            dis = 8 + r.nextInt(Constants.RADIUS_REGULAR);
            int xxx = xx + (int) Client.lengthdir_x(dis, dir), yyy = yy + (int) Client.lengthdir_y(dis, dir);
            G.drawLine((int) X - viewX, (int) Y - 34 - viewY, xx, yy);
            G.drawLine(xx, yy, xxx, yyy);
        }
    }

    public int life = 300;

    @Override
    public void onUpdate(World apples) {
        if (!apples.inBounds(X, Y) || life-- < 0) {
            alive = false;
            // apples.explode(X, Y, 32, 8, 16);
        }
        if (!apples.isSolid(X, Y - 10)) {
            Y -= 10;
        }
        for (int i = 1; i < 10; i++) {
            if (!apples.isSolid(X, Y + i)) {
                Y += i;
            }
        }
        if (apples.x > X - 48 && apples.x < X + 48) {
            if (apples.y > Y - 48 && apples.y < Y + 48) {
                apples.status |= Constants.ST_SHOCKED;
            }
        }
        /*
         * if (yspeed<12) { yspeed++; }
         */
    }

    @Override
    public void cerealize(ByteBuffer out) {
        try {
            Server.putString(out, this.getClass().getName());
            out.putInt((int) X);
            out.putInt((int) Y);
            out.putInt((int) xspeed);
            out.putInt(maker);
        } catch (Exception ex) {
            Logger.getLogger(ExplosionEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Reconstruct the rock entity
     * @param in
     * @param world World in which the entity should be reconstructed
     */
    public static void reconstruct(ByteBuffer in, World world) {
        try {
            world.entityList.add(new RodEntity(in.getInt(), in.getInt(), in.getInt(), in.getInt()));
        } catch (Exception ex) {
            Logger.getLogger(RodEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
