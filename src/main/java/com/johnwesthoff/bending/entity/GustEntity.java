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

import com.johnwesthoff.bending.Client;
import com.johnwesthoff.bending.Constants;
import com.johnwesthoff.bending.Server;
import com.johnwesthoff.bending.logic.Player;
import com.johnwesthoff.bending.logic.World;

/**
 *
 * @author John
 */
public class GustEntity extends Entity {
    // public int maker = 0;
    public int radius = 16;

    public GustEntity(int x, int y, int hspeed, int vspeed, int ma) {
        X = x;
        Y = y;
        xspeed = hspeed;
        yspeed = vspeed;
        maker = ma;
    }

    @Override
    public void onDraw(Graphics G, int viewX, int viewY) {
        if (X > viewX && X < viewX + Constants.WIDTH_INT && Y > viewY && Y < viewY + Constants.HEIGHT_INT) {
            G.setColor(Color.lightGray);
            int deg = r.nextInt(360);
            G.fillArc((int) (X - 1) - viewX, (int) (Y - 1) - viewY, 2, 2, deg, 15);
            deg = r.nextInt(360);
            G.fillArc((int) (X - 2) - viewX, (int) (Y - 2) - viewY, 4, 4, deg, 15);
            G.setColor(Color.white);
            deg = r.nextInt(360);
            G.fillArc((int) (X - 3) - viewX, (int) (Y - 3) - viewY, 6, 6, deg, 15);
            deg = r.nextInt(360);
            G.fillArc((int) (X - 4) - viewX, (int) (Y - 4) - viewY, 8, 8, deg, 15);
        }
    }

    @Override
    public void onUpdate(World apples) {
        if (!apples.inBounds(X, Y) || apples.checkCollision(X, Y)) {
            alive = false;
            // apples.explode(X, Y, 32, 8, 16);
        }
        for (Player p : apples.playerList) {
            if (maker != p.ID && p.checkCollision((int) X, (int) Y)) {
                alive = false;
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
            out.putInt((int) yspeed);
            out.putInt(maker);
        } catch (Exception ex) {
            Logger.getLogger(ExplosionEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    /**
     * Reconstruct the gust entity
     * @param in
     * @param world World in which the entity should be reconstructed
     */
    public static void reconstruct(ByteBuffer in, World world) {
        try {
            world.entityList.add(new GustEntity(in.getInt(), in.getInt(), in.getInt(), in.getInt(), in.getInt()));
        } catch (Exception ex) {
            Logger.getLogger(GustEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void onServerUpdate(Server lol) {
        if (collided(lol.earth)) {
            lol.earth.ground.ClearCircle((int) X, (int) Y, radius);
            lol.sendMessage(Server.DIG, ByteBuffer.allocate(40).putInt((int) X).putInt((int) Y).putInt(radius));
            alive = false;
        }
    }


    /**
     * Method to get whether the gust collided with water
     * @param w World in which this should be tested
     * @return true (if the gust collided with water) or false (else)
     */
    private boolean collided(World w) {
        double direction = Client.pointDir(previousX, previousY, X, Y);
        int speed = (int) Client.pointDis(previousX, previousY, X, Y);
        for (int i = 0; i <= speed; i++) {
            if (w.checkCollision(X + (int) Client.lengthdir_x(i, direction),
                    Y + (int) Client.lengthdir_y(i, direction))) {
                X = X + (int) Client.lengthdir_x(i, direction);
                Y = Y + (int) Client.lengthdir_y(i, direction);
                return true;
            }
        }
        return false;
    }
}
