package com.johnwesthoff.Entity;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.Color;
import java.awt.Graphics;
import java.nio.ByteBuffer;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.johnwesthoff.Destruct.Player;
import com.johnwesthoff.Destruct.Server;
import com.johnwesthoff.Destruct.World;

/**
 *
 * @author John
 */
public class SpoutEntity extends Entity {
    // public int maker = 0;
    public int radius = 8;
    public int gravity = 1;
    int a1, a2, a3;
    int s1, s2, s3;

    public SpoutEntity(int x, int y, int hspeed, int vspeed, int ma) {
        X = x;
        Y = y;
        xspeed = hspeed;
        yspeed = vspeed;
        maker = ma;
        Random r = new Random();
        a1 = 60 + r.nextInt(120);
        a2 = 60 + r.nextInt(120);
        a3 = 60 + r.nextInt(120);
        s1 = r.nextInt(360);
        s2 = r.nextInt(360);
        s3 = r.nextInt(360);
    }

    @Override
    public void onDraw(Graphics G, int viewX, int viewY) {
        if (X > viewX && X < viewX + 300 && Y > viewY && Y < viewY + 300) {
            G.setColor(Color.blue);
            G.fillArc(((int) X - radius / 2) - viewX, (int) (Y - radius / 2) - viewY, radius, radius, 0, 360);

            G.setColor(Color.cyan);
            G.fillArc(((int) X - 2) - viewX, (int) (Y - 2) - viewY, 4, 4, s1, a1);
            G.fillArc(((int) X - 4) - viewX, (int) (Y - 4) - viewY, 8, 8, s2, a2);
            G.fillArc(((int) X - 6) - viewX, (int) (Y - 6) - viewY, 12, 12, s3, a3);
        }
    }

    @Override
    public void onUpdate(World apples) {
        if (!apples.inBounds(X, Y) || apples.checkCollision(X, Y)) {

            alive = false;
            // apples.explode(X, Y, 32, 8, 16);
        }
        for (Player p : apples.playerList) {
            if (apples.pointDis(X, Y, p.x, p.y) < radius && maker != p.ID) {
                alive = false;
            }
        }
        yspeed += gravity;

        /*
         * if (yspeed<12) { yspeed++; }
         */
    }

    @Override
    public void onServerUpdate(Server lol) {
        /*
         * if (!lol.earth.inBounds(X, Y)||lol.earth.checkCollision(X, Y)) {
         * lol.earth.ground.FillCircleW(X, Y, radius, World.STONE);
         * lol.sendMessage(Server.FILL,
         * ByteBuffer.allocate(40).putInt(X).putInt(Y).putInt(radius).put(World.STONE));
         * }
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

    public static void reconstruct(ByteBuffer in, World world) {
        try {
            world.entityList.add(new SpoutEntity(in.getInt(), in.getInt(), in.getInt(), in.getInt(), in.getInt()));
        } catch (Exception ex) {
            Logger.getLogger(SpoutEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
