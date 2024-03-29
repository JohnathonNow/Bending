package com.johnwesthoff.bending.entity;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import com.johnwesthoff.bending.Constants;
import com.johnwesthoff.bending.Server;
import com.johnwesthoff.bending.logic.World;
import com.johnwesthoff.bending.networking.handlers.FreezeEvent;

import java.awt.*;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;


public class FreezeEntity extends Entity {
    // public int maker = 0;
    public int radius = Constants.RADIUS_REGULAR;

    public FreezeEntity(int x, int y, int hspeed, int vspeed, int ma) {
        X = x;
        Y = y;
        xspeed = hspeed;
        yspeed = vspeed;
        maker = ma;
        previousX = X;
        previousY = Y;
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
        yspeed += Constants.GRAVITY;

        if (life++ == 10) {
            apples.entityList.add(new SnowEntity((int) X, (int) Y, (int) xspeed + 1, (int) yspeed + 1, maker));
            apples.entityList.add(new SnowEntity((int) X, (int) Y, (int) xspeed + 1, (int) yspeed - 1, maker));
            apples.entityList.add(new SnowEntity((int) X, (int) Y, (int) xspeed - 1, (int) yspeed + 1, maker));
            apples.entityList.add(new SnowEntity((int) X, (int) Y, (int) xspeed - 1, (int) yspeed - 1, maker));
            apples.entityList.add(new SnowEntity((int) X, (int) Y, (int) xspeed, (int) yspeed + 1, maker));
            apples.entityList.add(new SnowEntity((int) X, (int) Y, (int) xspeed + 1, (int) yspeed, maker));
            // lol.sendMessage(Constants.WATERBENDING,ByteBuffer.allocate(24).putInt(5).putInt(X).putInt(Y).putInt(xspeed).putInt(yspeed).putInt(maker));
        }
        /*
         * if (yspeed<12) { yspeed++; }
         */
    }

    @Override
    public void onServerUpdate(Server lol) {
        if ((!lol.earth.inBounds(X, Y)) || lol.earth.checkCollision(X, Y)) {
            lol.earth.ground.freeze((int) X, (int) Y, radius * 2);
            lol.sendMessage(FreezeEvent.getPacket(X, Y, radius * 2));
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


    /**
     * Reconstruct the freeze entity
     * @param in
     * @param world World in which the entity should be reconstructed
     */
    public static void reconstruct(ByteBuffer in, World world) {
        try {
            world.entityList.add(new FreezeEntity(in.getInt(), in.getInt(), in.getInt(), in.getInt(), in.getInt()));
        } catch (Exception ex) {
            Logger.getLogger(FreezeEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
