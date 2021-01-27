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
import com.johnwesthoff.bending.logic.World;
import com.johnwesthoff.bending.networking.handlers.SteamEvent;

/**
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
            G.fillArc(((int) (X - radius) - viewX) * Constants.MULTIPLIER,
                    (int) ((Y - radius) - viewY) * Constants.MULTIPLIER, radius * 6, radius * 6, 0,
                    Constants.FULL_ANGLE);
        }
    }

    @Override
    public void onDraw(Graphics G, int viewX, int viewY) {
        if (X > viewX && X < viewX + Constants.WIDTH_INT && Y > viewY && Y < viewY + Constants.HEIGHT_INT) {
            G.setColor(Color.BLACK);
            G.fillArc((int) (X - radius) - viewX, (int) (Y - radius) - viewY, radius * 2, radius * 2, 0,
                    Constants.FULL_ANGLE);
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
            lol.sendMessage(SteamEvent.getPacket((int) X, (int) Y, this.MYID));
        }
    }

    @Override
    public void checkAndHandleCollision(Session client) {

        if (client.client.checkCollision(X, Y) && maker != client.ID
                && (client.gameMode <= 0 || client.badTeam.contains(maker))) {
            client.client.hurt(2);
            client.world.status |= Constants.ST_FLAMING;
            client.world.vspeed -= 2;
            client.xspeed += 2 - client.random.nextInt(4);
            client.lastHit = maker;
            alive = false;
            client.world.status |= Constants.ST_FLAMING;
            client.killMessage = "~ was set ablaze by `.";
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
     * Method to get whether the fire puff entity collided with water
     * 
     * @param w World in which this should be tested
     * @return true (if the fire puff entity collided with water) or false (else)
     */
    private boolean collided(World w) {
        double xx = X;
        double yy = Y;
        for (int i = 0; i < 16; i++) {
            if (w.inBounds((int) xx, (int) yy) && w.ground.cellData[(int) xx][(int) yy] == Constants.WATER) {
                X = (int) xx;
                Y = (int) yy;
                return true;
            }
            xx += xspeed / 16d;
            yy += yspeed / 16d;
        }
        return false;
    }

    /**
     * Reconstruct the fire puff entity
     * 
     * @param in
     * @param world World in which the entity should be reconstructed
     */
    public static void reconstruct(ByteBuffer in, World world) {
        try {
            world.entityList.add(new FirePuffEntity(in.getInt(), in.getInt(), in.getInt(), in.getInt(), in.getInt()));
        } catch (Exception ex) {
            Logger.getLogger(FirePuffEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
