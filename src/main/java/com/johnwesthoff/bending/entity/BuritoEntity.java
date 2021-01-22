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
public class BuritoEntity extends Entity {
    // public int maker = 0;
    public int radius = Constants.RADIUS_REGULAR;
    public int gravity = 1;

    public BuritoEntity(final int x, final int y, final int hspeed, final int vspeed, final int ma) {
        X = x;
        Y = y;
        xspeed = hspeed;
        yspeed = vspeed;
        maker = ma;
    }

    @Override
    public void onDraw(final Graphics G, final int viewX, final int viewY) {
        if (X > viewX && X < viewX + Constants.WIDTH_INT && Y > viewY && Y < viewY + Constants.HEIGHT_INT) {
            // g.setComposite(new Additive());
            G.setColor(Color.ORANGE);
            for (int i = 0; i < 4; i++) {
                G.fillArc((int) X - viewX + (i * 2) - 4, (int) Y - viewY, 4, 4, 0, Constants.FULL_ANGLE);
            }
            G.setColor(Color.RED);
            G.fillArc(4 + (int) X - viewX, (int) Y - viewY, 3, 3, 0, Constants.FULL_ANGLE);
        }
    }

    int next = 0;

    @Override
    public void onUpdate(final World apples) {
        if (!apples.inBounds((int) X, (int) Y) || apples.checkCollision((int) X, (int) Y)) {

            alive = false;
            // apples.explode(X, Y, 32, 8, 16);
        }
        apples.ground.FillCircleW((int) X, (int) Y, (int) Math.sqrt(xspeed * xspeed + yspeed * yspeed), Constants.LAVA);
        if (next++ > 4) {
            next = 0;
            yspeed += gravity;
        }
        /*
         * if (yspeed<12) { yspeed++; }
         */
    }

    @Override
    public void onServerUpdate(final Server lol) {
    }

    @Override
    public void cerealize(final ByteBuffer out) {
        try {
            Server.putString(out, this.getClass().getName());
            out.putInt((int) X);
            out.putInt((int) Y);
            out.putInt((int) xspeed);
            out.putInt((int) yspeed);
            out.putInt(maker);
        } catch (final Exception ex) {
            Logger.getLogger(ExplosionEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Method to reconstruct the BuritoEntity
     * 
     * @param in
     * @param world World in which the entity should be reconstructed
     */
    public static void reconstruct(final ByteBuffer in, final World world) {
        try {
            world.entityList.add(new BuritoEntity(in.getInt(), in.getInt(), in.getInt(), in.getInt(), in.getInt()));
        } catch (final Exception ex) {
            Logger.getLogger(BuritoEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void checkAndHandleCollision(Client client) {

        if (client.checkCollision(X, Y) && maker != client.ID
                && (client.gameMode <= 0 || client.badTeam.contains(maker))) {
            client.hurt(65);
            client.world.status |= Constants.ST_FLAMING;
            client.world.vspeed -= 39;
            client.xspeed += 47 - client.random.nextInt(94);
            client.lastHit = maker;
            alive = false;
            client.world.status |= Constants.ST_FLAMING;
            client.killMessage = "~ shouldn't have stolen `'s burito...";
        }
    }
}
