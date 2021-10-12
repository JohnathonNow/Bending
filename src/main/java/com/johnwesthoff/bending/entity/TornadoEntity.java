package com.johnwesthoff.bending.entity;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import com.johnwesthoff.bending.Constants;
import com.johnwesthoff.bending.Server;
import com.johnwesthoff.bending.Session;
import com.johnwesthoff.bending.logic.World;

import java.awt.*;
import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author John
 */
public class TornadoEntity extends Entity {
    // public int maker = 0;
    public int radius = Constants.RADIUS_REGULAR;

    public TornadoEntity(int x, int y, int hspeed, int ma) {
        X = x;
        Y = y;
        xspeed = hspeed;
        maker = ma;
    }

    @Override
    public void onDraw(Graphics G, int viewX, int viewY) {
        if (X > viewX && X < viewX + Constants.WIDTH_INT && Y > viewY && Y < viewY + Constants.HEIGHT_INT) {
            G.setColor(Color.white);
            int deg = r.nextInt(Constants.FULL_ANGLE);
            G.fillArc((int) (X - 1) - viewX, (int) (Y) - viewY, 2, 1, deg, Constants.SIXTY_DEGREE_ANGLE);
            deg = r.nextInt(Constants.FULL_ANGLE);
            G.fillArc((int) (X - 2) - viewX, (int) (Y - 4) - viewY, 4, 3, deg, Constants.SIXTY_DEGREE_ANGLE);
            deg = r.nextInt(Constants.FULL_ANGLE);
            G.fillArc((int) (X - 3) - viewX, (int) (Y - 7) - viewY, 6, 5, deg, Constants.SIXTY_DEGREE_ANGLE);
            deg = r.nextInt(Constants.FULL_ANGLE);
            G.fillArc((int) (X - 4) - viewX, (int) (Y - 11) - viewY, 8, 7, deg, Constants.SIXTY_DEGREE_ANGLE);
            deg = r.nextInt(Constants.FULL_ANGLE);
            G.fillArc((int) (X - 5) - viewX, (int) (Y - 14) - viewY, 10, 9, deg, Constants.SIXTY_DEGREE_ANGLE);
            deg = r.nextInt(Constants.FULL_ANGLE);
            G.fillArc((int) (X - 6) - viewX, (int) (Y - 17) - viewY, 12, 11, deg, Constants.SIXTY_DEGREE_ANGLE);
            deg = r.nextInt(Constants.FULL_ANGLE);
            G.fillArc((int) (X - 7) - viewX, (int) (Y - 20) - viewY, 14, 13, deg, Constants.SIXTY_DEGREE_ANGLE);
            deg = r.nextInt(Constants.FULL_ANGLE);
            G.fillArc((int) (X - 8) - viewX, (int) (Y - 23) - viewY, 16, 15, deg, Constants.SIXTY_DEGREE_ANGLE);
            deg = r.nextInt(Constants.FULL_ANGLE);
            G.fillArc((int) (X - 9) - viewX, (int) (Y - 26) - viewY, 18, 17, deg, Constants.SIXTY_DEGREE_ANGLE);
            deg = r.nextInt(Constants.FULL_ANGLE);
            G.fillArc((int) (X - 10) - viewX, (int) (Y - 29) - viewY, 20, 19, deg, Constants.SIXTY_DEGREE_ANGLE);
            deg = r.nextInt(Constants.FULL_ANGLE);
            G.fillArc((int) (X - 11) - viewX, (int) (Y - 32) - viewY, 22, 21, deg, Constants.SIXTY_DEGREE_ANGLE);

            //TODO: implement using a for loop
        }
    }

    public int life = 100;

    @Override
    public void onUpdate(World apples) {
        if (!apples.inBounds(X, Y) || life-- < 0) {
            alive = false;
            // apples.explode(X, Y, 32, 8, 16);
        }
        if (!apples.isSolid(X, Y - 10)) {
            Y -= 10;
        }
        if (apples.isSolid(X + xspeed, Y)) {
            xspeed = 0;
        }
        for (int i = 1; i < 10; i++) {
            if (!apples.isSolid(X, Y + i)) {
                Y += i;
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
            out.putInt(maker);
        } catch (Exception ex) {
            Logger.getLogger(ExplosionEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Reconstruct the tornado entity
     * @param in
     * @param world World in which the entity should be reconstructed
     */
    public static void reconstruct(ByteBuffer in, World world) {
        try {
            world.entityList.add(new TornadoEntity(in.getInt(), in.getInt(), in.getInt(), in.getInt()));
        } catch (Exception ex) {
            Logger.getLogger(TornadoEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void checkAndHandleCollision(Session client) {

        if (client.client.checkCollision(X, Y) && life < 80) {
            client.client.hurt(1);
            client.xspeed += 1 - client.random.nextInt(2);
            client.xspeed *= -1;
            client.world.x = (int) X + (int) client.xspeed;
            client.world.move = 0;
            client.lastHit = maker;
            client.killMessage = "~ was sucked into `'s Tornado.";
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TornadoEntity that = (TornadoEntity) o;
        return radius == that.radius && life == that.life;
    }

    @Override
    public int hashCode() {
        return Objects.hash(radius, life);
    }
}
