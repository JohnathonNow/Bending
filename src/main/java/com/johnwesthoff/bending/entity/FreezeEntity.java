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
import com.johnwesthoff.bending.game.Constants;
import com.johnwesthoff.bending.game.Server;
import com.johnwesthoff.bending.game.World;

/**
 *
 * @author John
 */
public class FreezeEntity extends Entity {
    // public int maker = 0;
    public int radius = 16;
    public int gravity = 1;

    public FreezeEntity(int x, int y, int hspeed, int vspeed, int ma) {
        X = x;
        Y = y;
        xspeed = hspeed;
        yspeed = vspeed;
        maker = ma;
    }

    @Override
    public void onDraw(Graphics G, int viewX, int viewY) {
        if (X > viewX && X < viewX + Constants.WIDTH_INT && Y > viewY && Y < viewY + Constants.HEIGHT_INT) {
            G.setColor(Color.cyan);
            G.drawLine((int) previousX - viewX, (int) previousY - viewY, (int) X - viewX, (int) Y - viewY);
            G.setColor(Color.white);
            G.drawLine((int) previousX - viewX, (int) (previousY + 1) - viewY, (int) X - viewX, (int) Y + 1 - viewY);
        }
    }

    @Override
    public void onUpdate(World apples) {
        if (!apples.inBounds(X, Y) || apples.checkCollision(X, Y)) {

            alive = false;
            // apples.explode(X, Y, 32, 8, 16);
        }
        yspeed += gravity;
        if (life++ == 10) {
            apples.entityList.add(new SnowEntity((int) X, (int) Y, (int) xspeed + 1, (int) yspeed + 1, maker));
            apples.entityList.add(new SnowEntity((int) X, (int) Y, (int) xspeed + 1, (int) yspeed - 1, maker));
            apples.entityList.add(new SnowEntity((int) X, (int) Y, (int) xspeed - 1, (int) yspeed + 1, maker));
            apples.entityList.add(new SnowEntity((int) X, (int) Y, (int) xspeed - 1, (int) yspeed - 1, maker));
            apples.entityList.add(new SnowEntity((int) X, (int) Y, (int) xspeed, (int) yspeed + 1, maker));
            apples.entityList.add(new SnowEntity((int) X, (int) Y, (int) xspeed + 1, (int) yspeed, maker));
            // lol.sendMessage(Server.WATERBENDING,ByteBuffer.allocate(24).putInt(5).putInt(X).putInt(Y).putInt(xspeed).putInt(yspeed).putInt(maker));
        }
        /*
         * if (yspeed<12) { yspeed++; }
         */
    }

    @Override
    public void onServerUpdate(Server lol) {
        if ((!lol.earth.inBounds(X, Y)) || lol.earth.checkCollision(X, Y)) {
            lol.earth.ground.freeze((int) X, (int) Y, radius * 4);
            lol.sendMessage(Server.FREEZE, ByteBuffer.allocate(40).putInt((int) X).putInt((int) Y).putInt(radius * 4));
            alive = false;
        }
    }

    int life = 0;

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
            world.entityList.add(new FreezeEntity(in.getInt(), in.getInt(), in.getInt(), in.getInt(), in.getInt()));
        } catch (Exception ex) {
            Logger.getLogger(FreezeEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
