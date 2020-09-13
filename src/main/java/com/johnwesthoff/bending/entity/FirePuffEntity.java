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
import com.johnwesthoff.bending.destruct.Constants;
import com.johnwesthoff.bending.destruct.Server;
import com.johnwesthoff.bending.destruct.World;

/**
 *
 * @author John
 */
public class FirePuffEntity extends Entity {
    // public int maker = 0;
    public int radius = 3;
    public int life = 75;

    public FirePuffEntity(int x, int y, int hspeed, int vspeed, int ma) {
        X = x;
        Y = y;
        xspeed = hspeed;
        yspeed = vspeed;
        maker = ma;
    }

    Color yes = new Color(0xFF0000);

    @Override
    public void drawAdditive(Graphics G, int viewX, int viewY) {
        if (X > viewX && X < viewX + Constants.WIDTH_INT && Y > viewY && Y < viewY + Constants.HEIGHT_INT) {
            G.setColor(yes);
            G.fillArc(((int) (X - radius) - viewX) * 3, (int) ((Y - radius) - viewY) * 3, radius * 6, radius * 6, 0,
                    360);
        }
    }

    @Override
    public void onDraw(Graphics G, int viewX, int viewY) {
        if (X > viewX && X < viewX + Constants.WIDTH_INT && Y > viewY && Y < viewY + Constants.HEIGHT_INT) {
            G.setColor(Color.BLACK);
            G.fillArc((int) (X - radius) - viewX, (int) (Y - radius) - viewY, radius * 2, radius * 2, 0, 360);
        }
    }

    int next = 0;
    boolean nightTime = false;

    @Override
    public void onUpdate(World apples) {
        nightTime = apples.map != 0;
        if (!apples.inBounds(X, Y) || apples.checkCollision(X, Y) || life-- < 0) {
            alive = false;
        }
        if (next++ % 3 == 1) {
            radius += 1;
        }
        if (next % 8 == 0) {
            int Re = yes.getRed(), Bl = yes.getBlue(), Gr = yes.getGreen();
            Gr = (Gr + 0xFF) / 3;
            yes = new Color(Re, Gr, Bl);
        }
        if (next > 36) {
            next = 0;
        }

    }

    @Override
    public void onServerUpdate(Server lol) {
        if (lol.earth.inBounds(X, Y) && collided(lol.earth))// lol.earth.ground.cellData[X][Y]==World.WATER
        {
            alive = false;
            lol.sendMessage(Server.STEAM, ByteBuffer.allocate(40).putInt((int) X).putInt((int) Y).putInt(this.MYID));
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

    private boolean collided(World w) {
        double xx = X;
        double yy = Y;
        for (int i = 0; i < 16; i++) {
            if (w.inBounds((int) xx, (int) yy) && w.ground.cellData[(int) xx][(int) yy] == World.WATER) {
                X = (int) xx;
                Y = (int) yy;
                return true;
            }
            xx += xspeed / 16d;
            yy += yspeed / 16d;
        }
        return false;
    }

    public static void reconstruct(ByteBuffer in, World world) {
        try {
            world.entityList.add(new FirePuffEntity(in.getInt(), in.getInt(), in.getInt(), in.getInt(), in.getInt()));
        } catch (Exception ex) {
            Logger.getLogger(FirePuffEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
