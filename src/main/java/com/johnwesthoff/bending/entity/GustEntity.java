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

import com.johnwesthoff.bending.Session;
import com.johnwesthoff.bending.Constants;
import com.johnwesthoff.bending.Server;
import com.johnwesthoff.bending.logic.Player;
import com.johnwesthoff.bending.logic.World;
import com.johnwesthoff.bending.networking.handlers.DigEvent;

/**
 * @author John
 */
public class GustEntity extends Entity {
    // public int maker = 0;
    public int radius = Constants.RADIUS_REGULAR;

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
            int deg = r.nextInt(Constants.FULL_ANGLE);
            G.fillArc((int) (X - 1) - viewX, (int) (Y - 1) - viewY, 2, 2, deg, Constants.FIFTEEN_DEGREE_ANGLE);
            deg = r.nextInt(Constants.FULL_ANGLE);
            G.fillArc((int) (X - 2) - viewX, (int) (Y - 2) - viewY, 4, 4, deg, Constants.FIFTEEN_DEGREE_ANGLE);
            G.setColor(Color.white);
            deg = r.nextInt(Constants.FULL_ANGLE);
            G.fillArc((int) (X - 3) - viewX, (int) (Y - 3) - viewY, 6, 6, deg, Constants.FIFTEEN_DEGREE_ANGLE);
            deg = r.nextInt(Constants.FULL_ANGLE);
            G.fillArc((int) (X - 4) - viewX, (int) (Y - 4) - viewY, 8, 8, deg, Constants.FIFTEEN_DEGREE_ANGLE);

            //TODO: Implement using for loop
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
        if (hasCollided(lol.earth)) {
            lol.earth.ground.ClearCircle((int) X, (int) Y, radius);
            lol.sendMessage(DigEvent.getPacket((int)X, (int)Y, radius));
            alive = false;
        }
    }


    @Override
    public void checkAndHandleCollision(Session client) {

        if (client.client.checkCollision(X, Y)) {
            client.client.hurt(7);
            client.world.vspeed += yspeed;
            client.xspeed += xspeed;
            alive = false;
            client.lastHit = maker;
            client.lungs = client.maxlungs;
            client.killMessage = "~ met `'s gust of air!";
        }
    }
}
