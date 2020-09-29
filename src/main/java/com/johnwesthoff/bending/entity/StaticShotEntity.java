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
public class StaticShotEntity extends Entity {
    // public int maker = 0;
    public int radius = 0;

    public StaticShotEntity(int x, int y, int hspeed, int vspeed, int ma) {
        X = x;
        Y = y;
        xspeed = hspeed;
        yspeed = vspeed;
        maker = ma;
    }

    @Override
    public void onDraw(Graphics G, int viewX, int viewY) {
        if (X > viewX && X < viewX + Constants.WIDTH_INT && Y > viewY && Y < viewY + Constants.HEIGHT_INT) {
            G.setColor(radius == 0 ? Color.blue : Color.red);
            G.drawArc(((int) X - 3) - viewX, (int) (Y - 3) - viewY, 6, 6, 0, Constants.FULL_ANGLE);
            G.drawLine((int) (X - 2) - viewX, (int) Y - viewY, (int) (X + 2) - viewX, (int) Y - viewY);
        }
    }

    @Override
    public void onUpdate(World apples) {
        if (apples.checkCollision(X, Y)) {
            xspeed = 0;
            yspeed = 0;
            radius = 96;
            // apples.explode(X, Y, 32, 8, 16);
        }
        if (!apples.inBounds(X, Y)) {
            alive = false;
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

    public static void reconstruct(ByteBuffer in, World world) {
        try {
            world.entityList.add(new StaticShotEntity(in.getInt(), in.getInt(), in.getInt(), in.getInt(), in.getInt()));
        } catch (Exception ex) {
            Logger.getLogger(StaticShotEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void onServerUpdate(Server lol) {
        for (Player p : lol.playerList) {
            if (Client.pointDis(X, Y, p.x, p.y) < radius && maker != p.ID) {
                alive = false;
                lol.sendMessage(Server.DESTROY, ByteBuffer.allocate(30).putInt(MYID));
                lol.sendMessage(Server.CHARGE,
                        ByteBuffer.allocate(40).putInt((int) X).putInt((int) Y).putInt(radius).putInt(200).putInt(0));// maker
                return;
            }
        }
    }
}
