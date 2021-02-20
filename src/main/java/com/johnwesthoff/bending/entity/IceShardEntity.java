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

import com.johnwesthoff.bending.Constants;
import com.johnwesthoff.bending.Server;
import com.johnwesthoff.bending.Session;
import com.johnwesthoff.bending.logic.Player;
import com.johnwesthoff.bending.logic.World;
import com.johnwesthoff.bending.networking.handlers.FillEvent;
import com.johnwesthoff.bending.networking.handlers.FreezeEvent;
import com.johnwesthoff.bending.util.math.Ops;

/**
 * @author John
 */
public class IceShardEntity extends Entity {
    // public int maker = 0;
    public int radius = Constants.RADIUS_REGULAR;
    public int gravity = 0;

    public IceShardEntity(int x, int y, int hspeed, int vspeed, int ma) {
        X = x;
        Y = y;
        xspeed = hspeed;
        yspeed = vspeed;
        maker = ma;
    }

    @Override
    public void onDraw(Graphics G, int viewX, int viewY) {
        if (X > viewX && X < viewX + Constants.WIDTH_INT && Y > viewY && Y < viewY + Constants.HEIGHT_INT) {
            G.setColor(Color.CYAN);
            G.drawLine((int) (X - xspeed) - viewX, (int) (Y - yspeed) - viewY, (int) X - viewX, (int) Y - viewY);

        }
    }

    @Override
    public void onUpdate(World apples) {
        if (collided(apples)) {
            // apples.ground.FillCircleW(X, Y, radius, Constants.STONE);
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
     * Reconstruct the ice shard entity
     * 
     * @param in
     * @param world World in which the entity should be reconstructed
     */
    public static void reconstruct(ByteBuffer in, World world) {
        try {
            world.entityList.add(new IceShardEntity(in.getInt(), in.getInt(), in.getInt(), in.getInt(), in.getInt()));
        } catch (Exception ex) {
            Logger.getLogger(IceShardEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    int time = 0;

    @Override
    public void onServerUpdate(Server lol) {
        if (lol.earth.checkCollision(X, Y)) {
            radius = Constants.RADIUS_REGULAR;
            lol.earth.ground.fillCircleW((int) X, (int) Y, 27, Constants.ICE);
            lol.sendMessage(FillEvent.getPacket((int) X, (int) Y, 27, Constants.ICE));
            alive = false;
        }
        if (time++ > 1) {
            time = 0;
            lol.earth.ground.freeze((int) (previousX - (xspeed * 4)), (int) (previousY - (yspeed * 4)), 54);
            lol.sendMessage(FreezeEvent.getPacket((previousX - (xspeed * 4)), (previousY - (yspeed * 4)), 54 * 4));
        }
    }

    public void checkAndHandleCollision(Session client) {

        if (client.client.checkCollision(X, Y) && maker != client.ID
                && (client.gameMode <= 0 || client.badTeam.contains(maker))) {
            client.client.hurt(15);
            client.world.vspeed -= 5;
            client.xspeed += 7 - client.random.nextInt(14);
            client.lastHit = maker;
            alive = false;
            client.killMessage = "~ was hit by `'s icey attack!";
        }
    }

    /**
     * Method to get whether the ice shard collided with the client
     * 
     * @param w World in which this should be tested
     * @return true (if the ice shard collided with the client) or false (else)
     */
    private boolean collided(World w) {
        double direction = Ops.pointDir(previousX, previousY, X, Y);
        int speed = (int) Ops.pointDis(previousX, previousY, X, Y);
        for (int i = 0; i <= speed; i++) {
            if (w.checkCollision(X + (int) Ops.lengthdir_x(i, direction),
                    Y + (int) Ops.lengthdir_y(i, direction))) {
                X = X + (int) Ops.lengthdir_x(i, direction);
                Y = Y + (int) Ops.lengthdir_y(i, direction);
                return true;
            }
        }
        return false;
    }
}
