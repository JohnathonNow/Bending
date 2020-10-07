package com.johnwesthoff.bending.entity;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import com.johnwesthoff.bending.Client;
import com.johnwesthoff.bending.Constants;
import com.johnwesthoff.bending.Server;
import com.johnwesthoff.bending.logic.Player;
import com.johnwesthoff.bending.logic.World;

import java.awt.*;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author John
 */
public class ShardEntity extends Entity {
    // public int maker = 0;
    public int radius = Constants.RADIUS_REGULAR;
    public int gravity = 0;

    public ShardEntity(int x, int y, int hspeed, int vspeed, int ma) {
        X = x;
        Y = y;
        xspeed = hspeed / 2;
        yspeed = vspeed / 2;
        maker = ma;
    }

    @Override
    public void onDraw(Graphics G, int viewX, int viewY) {
        if (X > viewX && X < viewX + Constants.WIDTH_INT && Y > viewY && Y < viewY + Constants.HEIGHT_INT) {
            G.setColor(Color.DARK_GRAY);
            G.drawLine((int) (X - xspeed) - viewX, (int) (Y - yspeed) - viewY, (int) X - viewX, (int) Y - viewY);

        }
    }

    @Override
    public void onUpdate(World apples) {
        if ((apples.isSolid(X, Y))) {
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
     * Reconstruct the shard entity
     * @param in
     * @param world World in which the entity should be reconstructed
     */
    public static void reconstruct(ByteBuffer in, World world) {
        try {
            world.entityList.add(new ShardEntity(in.getInt(), in.getInt(), in.getInt(), in.getInt(), in.getInt()));
        } catch (Exception ex) {
            Logger.getLogger(ShardEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void onServerUpdate(Server lol) {
        if (collided(lol.earth)) {
            radius *= 3;
            lol.earth.ground.ClearCircle((int) X, (int) Y, radius);
            lol.sendMessage(Server.DIG, ByteBuffer.allocate(40).putInt((int) X).putInt((int) Y).putInt(radius));
            alive = false;
        }
    }


    @Override
    public void checkAndHandleCollision(Client client) {

        if (Client.pointDis(X, Y - World.head, client.world.x, client.world.y) < radius * 4 && maker != client.ID
                && (client.gameMode <= 0 || client.badTeam.contains(maker))) {
            client.hurt(15);
            client.world.vspeed -= 5;
            client.xspeed += 7 - client.random.nextInt(14);
            client.lastHit = maker;
            alive = false;
            client.killMessage = "~ was sniped by `.";
        }
    }
    /**
     * Method to get whether the shard collided with the client
     * @param w World in which this should be tested
     * @return true (if the shard collided with the client) or false (else)
     */
    private boolean collided(World w) {
        double direction = Client.pointDir(previousX, previousY, X, Y);
        int speed = (int) Client.pointDis(previousX, previousY, X, Y);
        for (int i = 0; i < speed; i++) {
            if (w.isSolid(X + (int) Client.lengthdir_x(i, direction), Y + (int) Client.lengthdir_y(i, direction))) {
                X = X + (int) Client.lengthdir_x(i, direction);
                Y = Y + (int) Client.lengthdir_y(i, direction);
                return true;
            }
        }
        return false;
    }
}
