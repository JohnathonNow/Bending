package com.johnwesthoff.bending.entity;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.Color;
import java.awt.Graphics;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.johnwesthoff.bending.Session;
import com.johnwesthoff.bending.Constants;
import com.johnwesthoff.bending.Server;
import com.johnwesthoff.bending.logic.Player;
import com.johnwesthoff.bending.logic.World;
import com.johnwesthoff.bending.networking.handlers.DrainEvent;


/**
 * @author John
 */
public class SoulDrainEntity extends Entity {
    // public int maker = 0;
    public int radius = Constants.RADIUS_REGULAR;

    public SoulDrainEntity(int x, int y, int hspeed, int vspeed, int ma) {
        X = x;
        Y = y;
        xspeed = hspeed;
        yspeed = vspeed;
        maker = ma;
    }

    @Override
    public void onDraw(Graphics G, int viewX, int viewY) {
        if (X > viewX && X < viewX + Constants.WIDTH_INT && Y > viewY && Y < viewY + Constants.HEIGHT_INT) {
            G.setColor(Color.BLACK);

            int deg = r.nextInt(Constants.FULL_ANGLE);
            G.fillArc((int) (X - 1) - viewX, (int) (Y - 1) - viewY, 2, 2, deg, Constants.SIXTY_DEGREE_ANGLE);
            deg = r.nextInt(Constants.FULL_ANGLE);
            G.fillArc((int) (X - 2) - viewX, (int) (Y - 2) - viewY, 4, 4, deg, Constants.SIXTY_DEGREE_ANGLE);
            deg = r.nextInt(Constants.FULL_ANGLE);
            G.fillArc((int) (X - 3) - viewX, (int) (Y - 3) - viewY, 6, 6, deg, Constants.SIXTY_DEGREE_ANGLE);
            deg = r.nextInt(Constants.FULL_ANGLE);
            G.fillArc((int) (X - 4) - viewX, (int) (Y - 4) - viewY, 8, 8, deg, Constants.SIXTY_DEGREE_ANGLE);
            deg = r.nextInt(Constants.FULL_ANGLE);
            G.fillArc((int) (X - 5) - viewX, (int) (Y - 5) - viewY, 10, 10, deg, Constants.SIXTY_DEGREE_ANGLE);
            deg = r.nextInt(Constants.FULL_ANGLE);
            G.fillArc((int) (X - 8) - viewX, (int) (Y - 8) - viewY, 16, 16, deg, Constants.SIXTY_DEGREE_ANGLE);
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
     * Reconstruct the soul drown entity
     * @param in
     * @param world World in which the entity should be reconstructed
     */
    public static void reconstruct(ByteBuffer in, World world) {
        try {
            world.entityList.add(new SoulDrainEntity(in.getInt(), in.getInt(), in.getInt(), in.getInt(), in.getInt()));
        } catch (Exception ex) {
            Logger.getLogger(SoulDrainEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void checkAndHandleCollision(Session client) {

        if (client.client.checkCollision(X, Y) && maker != client.ID
                && (client.gameMode <= 0 || client.badTeam.contains(maker))) {
            client.lastHit = maker;
            client.killMessage = "~'s soul was stolen by `!";
            alive = false;
            client.world.vspeed -= 5;
            client.xspeed += 7 - client.random.nextInt(14);
            try {
                client.out.addMessage(DrainEvent.getPacket(client.lastHit, client.client.hurt(21)));
            } catch (final IOException ex) {
                // Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
