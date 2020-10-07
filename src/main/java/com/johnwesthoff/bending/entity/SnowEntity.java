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
import com.johnwesthoff.bending.logic.World;

/**
 * @author John
 */
public class SnowEntity extends Entity {
    // public int maker = 0;
    public int radius = Constants.RADIUS_REGULAR;
    public int gravity = 1;

    public SnowEntity(int x, int y, int hspeed, int vspeed, int ma) {
        X = x;
        Y = y;
        xspeed = hspeed;
        yspeed = vspeed;
        maker = ma;
    }

    @Override
    public void onDraw(Graphics G, int viewX, int viewY) {
        if (X > viewX && X < viewX + Constants.WIDTH_INT && Y > viewY && Y < viewY + Constants.HEIGHT_INT) {
            G.setColor(Color.white);
            G.fillArc((int) X - viewX - 1, (int) Y - viewY - 1, 2, 2, 0, Constants.FULL_ANGLE);
        }
    }

    int timer = 0;

    @Override
    public void onUpdate(World apples) {
        if (!apples.inBounds(X, Y) || apples.checkCollision(X, Y)) {

            alive = false;
            // apples.explode(X, Y, 32, 8, 16);
        }
        if (++timer >= 2) {
            yspeed += gravity;
            timer = 0;
        }

        /*
         * if (yspeed<12) { yspeed++; }
         */
    }

    @Override
    public void onServerUpdate(Server lol) {
        if ((!lol.earth.inBounds(X, Y)) || lol.earth.checkCollision(X, Y)) {
            lol.earth.ground.freeze((int) X, (int) Y, radius * 2);
            lol.sendMessage(Server.FREEZE, ByteBuffer.allocate(40).putInt((int) X).putInt((int) Y).putInt(radius * 4));
            alive = false;
        }

    }

    @Override
    public void checkAndHandleCollision(Client client) {

        if (client.checkCollision(X, Y) && maker != client.ID
                && (client.gameMode <= 0 || client.badTeam.contains(maker))) {
            client.hurt(8);
            client.world.vspeed -= 3;
            client.xspeed += 3 - client.random.nextInt(6);
            client.lastHit = maker;
            alive = false;
            client.killMessage = "~ will need to be thawed out after fighting `!";
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
     * Reconstruct the snow entity
     * @param in
     * @param world World in which the entity should be reconstructed
     */
    public static void reconstruct(ByteBuffer in, World world) {
        try {
            world.entityList.add(new SnowEntity(in.getInt(), in.getInt(), in.getInt(), in.getInt(), in.getInt()));
        } catch (Exception ex) {
            Logger.getLogger(SnowEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
