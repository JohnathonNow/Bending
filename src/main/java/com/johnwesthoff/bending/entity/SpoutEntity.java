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
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author John
 */
public class SpoutEntity extends Entity {
    // public int maker = 0;
    public int radius = 8;
    public int gravity = 1;
    int a1, a2, a3;
    int s1, s2, s3;

    public SpoutEntity(int x, int y, int hspeed, int vspeed, int ma) {
        X = x;
        Y = y;
        xspeed = hspeed;
        yspeed = vspeed;
        maker = ma;
        Random r = new Random();
        a1 = Constants.SIXTY_DEGREE_ANGLE + r.nextInt(Constants.ONE_THIRD_FULL_ANGLE);
        a2 = Constants.SIXTY_DEGREE_ANGLE + r.nextInt(Constants.ONE_THIRD_FULL_ANGLE);
        a3 = Constants.SIXTY_DEGREE_ANGLE + r.nextInt(Constants.ONE_THIRD_FULL_ANGLE);
        s1 = r.nextInt(Constants.FULL_ANGLE);
        s2 = r.nextInt(Constants.FULL_ANGLE);
        s3 = r.nextInt(Constants.FULL_ANGLE);
    }

    @Override
    public void onDraw(Graphics G, int viewX, int viewY) {
        if (X > viewX && X < viewX + Constants.WIDTH_INT && Y > viewY && Y < viewY + Constants.HEIGHT_INT) {
            G.setColor(Color.blue);
            G.fillArc(((int) X - radius / 2) - viewX, (int) (Y - radius / 2) - viewY, radius, radius, 0, Constants.FULL_ANGLE);

            G.setColor(Color.cyan);
            G.fillArc(((int) X - 2) - viewX, (int) (Y - 2) - viewY, 4, 4, s1, a1);
            G.fillArc(((int) X - 4) - viewX, (int) (Y - 4) - viewY, 8, 8, s2, a2);
            G.fillArc(((int) X - 6) - viewX, (int) (Y - 6) - viewY, 12, 12, s3, a3);
        }
    }

    @Override
    public void onUpdate(World apples) {
        if (!apples.inBounds(X, Y) || apples.checkCollision(X, Y)) {

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
    public void onServerUpdate(Server lol) {
        /*
         * if (!lol.earth.inBounds(X, Y)||lol.earth.checkCollision(X, Y)) {
         * lol.earth.ground.FillCircleW(X, Y, radius, Constants.STONE);
         * lol.sendMessage(Server.FILL,
         * ByteBuffer.allocate(40).putInt(X).putInt(Y).putInt(radius).put(Constants.STONE));
         * }
         */
    }

    @Override
    public void checkAndHandleCollision(Client client) {

        if (client.checkCollision(X, Y)) {
            if (maker != client.ID && (client.gameMode <= 0 || client.badTeam.contains(maker))) {
                client.hurt(5);
                client.lastHit = maker;
            }
            client.world.vspeed -= 5;
            client.killMessage = "~ was kicked out of `'s swimming pool.";
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
     * Reconstruct the spout entity
     * @param in
     * @param world World in which the entity should be reconstructed
     */
    public static void reconstruct(ByteBuffer in, World world) {
        try {
            world.entityList.add(new SpoutEntity(in.getInt(), in.getInt(), in.getInt(), in.getInt(), in.getInt()));
        } catch (Exception ex) {
            Logger.getLogger(SpoutEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
