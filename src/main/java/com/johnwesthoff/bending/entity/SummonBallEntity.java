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

/**
 *
 * @author John
 */
public class SummonBallEntity extends Entity {
    // public int maker = 0;
    public int radius = 16;

    public SummonBallEntity(int x, int y, int hspeed, int vspeed, int ma) {
        X = x;
        Y = y;
        xspeed = hspeed;
        yspeed = vspeed;
        maker = ma;
    }

    @Override
    public void onDraw(Graphics G, int viewX, int viewY) {
        if (X > viewX && X < viewX + Constants.WIDTH_INT && Y > viewY && Y < viewY + Constants.HEIGHT_INT) {
            G.setColor(Color.darkGray);

            int deg = r.nextInt(360);
            G.fillArc((int) (X - 1) - viewX, (int) (Y - 1) - viewY, 2, 2, deg, 60);
            deg = r.nextInt(360);
            G.fillArc((int) (X - 2) - viewX, (int) (Y - 2) - viewY, 4, 4, deg, 60);
            deg = r.nextInt(360);
            G.fillArc((int) (X - 3) - viewX, (int) (Y - 3) - viewY, 6, 6, deg, 60);
            deg = r.nextInt(360);
            G.fillArc((int) (X - 4) - viewX, (int) (Y - 4) - viewY, 8, 8, deg, 60);
            deg = r.nextInt(360);
            G.fillArc((int) (X - 5) - viewX, (int) (Y - 5) - viewY, 10, 10, deg, 60);
            deg = r.nextInt(360);
            G.fillArc(((int) X - 8) - viewX, (int) (Y - 8) - viewY, 16, 16, deg, 60);
        }
    }

    @Override
    public void onUpdate(World apples) {
        if (!apples.inBounds(X, Y) || apples.checkCollision(X, Y)) {
            alive = false;
            // apples.explode(X, Y, 32, 8, 16);
        }
    }

    @Override
    public void onServerUpdate(Server lol) {
        if (!lol.earth.inBounds(X, Y) || lol.earth.checkCollision(X, Y)) {
            alive = false;
            int yay = Server.getID();
            lol.earth.entityList.add(new EnemyEntity((int) X, (int) Y, 0, 0, maker).setID(yay));
            lol.sendMessage(Server.DARKNESS, ByteBuffer.allocate(28).putInt(10).putInt((int) X).putInt((int) Y)
                    .putInt(0).putInt(0).putInt(maker).putInt(yay));
            // apples.explode(X, Y, 32, 8, 16);
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
     * Reconstruct the summon ball entity
     * @param in
     * @param world World in which the entity should be reconstructed
     */
    public static void reconstruct(ByteBuffer in, World world) {
        try {
            world.entityList.add(new SummonBallEntity(in.getInt(), in.getInt(), in.getInt(), in.getInt(), in.getInt()));
        } catch (Exception ex) {
            Logger.getLogger(SummonBallEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
