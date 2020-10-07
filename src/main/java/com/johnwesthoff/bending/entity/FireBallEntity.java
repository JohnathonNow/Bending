package com.johnwesthoff.bending.entity;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

<<<<<<< HEAD
=======
import com.johnwesthoff.bending.Client;
>>>>>>> 3cd39e5d65d1097a087bdc621ea89b579cb4a1aa
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
public class FireBallEntity extends Entity {
    // public int maker = 0;
    public int radius = Constants.RADIUS_REGULAR;
    public int gravity = 1;

    public FireBallEntity(int x, int y, int hspeed, int vspeed, int ma) {
        X = x;
        Y = y;
        xspeed = hspeed;
        yspeed = vspeed;
        maker = ma;
    }

    @Override
    public void onDraw(Graphics G, int viewX, int viewY) {
        if (X > viewX && X < viewX + Constants.WIDTH_INT && Y > viewY && Y < viewY + Constants.HEIGHT_INT) {
            Graphics2D g = (Graphics2D) G;
            Composite c = g.getComposite();
            // g.setComposite(new Additive());
            g.setColor(new Color(Constants.FULL_COLOR_VALUE, r.nextInt(Constants.FULL_COLOR_VALUE), 0, r.nextInt(Constants.FULL_COLOR_VALUE)));
            g.fillArc((int) (X - 6) - viewX, (int) (Y - 6) - viewY, 12, 12, 0, Constants.FULL_ANGLE);
            for (int i = 0; i < 4; i++) {
                int e1 = 6 - r.nextInt(12), e2 = 6 - r.nextInt(12);
                g.setColor(new Color(Constants.FULL_COLOR_VALUE, r.nextInt(Constants.FULL_COLOR_VALUE), 0, r.nextInt(Constants.FULL_COLOR_VALUE)));
                g.fillArc((int) (X + e1) - viewX, (int) (Y + e2) - viewY, e1, e2, 0, Constants.FULL_ANGLE);
            }
            g.setComposite(c);
        }
    }

    int next = 0;

    @Override
    public void onUpdate(World apples) {
        if (!apples.inBounds(X, Y) || apples.checkCollision(X, Y)) {

            alive = false;
            // apples.explode(X, Y, 32, 8, 16);
        }
        if (next++ > 2) {
            next = 0;
            yspeed += gravity;
        }
        /*
         * if (yspeed<12) { yspeed++; }
         */
    }

    @Override
    public void onServerUpdate(Server lol) {
        if (lol.earth.checkCollision(X, Y)) {
            radius *= 4;
            lol.earth.ground.ClearCircle((int) X, (int) Y, radius);
            lol.sendMessage(Server.DIG, ByteBuffer.allocate(40).putInt((int) X).putInt((int) Y).putInt(radius));
            alive = false;
        }
        if (lol.earth.inBounds(X, Y) && collided(lol.earth))// lol.earth.ground.cellData[X][Y]==World.WATER
        {
            alive = false;
            lol.sendMessage(Server.STEAM, ByteBuffer.allocate(40).putInt((int) X).putInt((int) Y).putInt(this.MYID));
        }
    }

    @Override
    public void checkAndHandleCollision(Client client) {

        if (client.checkCollision(X, Y) && maker != client.ID
                && (client.gameMode <= 0 || client.badTeam.contains(maker))) {
            client.hurt(15);
            client.world.status |= World.ST_FLAMING;
            client.world.vspeed -= 7;
            client.xspeed += 9 - client.random.nextInt(18);
            client.lastHit = maker;
            alive = false;
            client.world.status |= World.ST_FLAMING;
            client.killMessage = "~ was burninated by `.";
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
     * Method to get whether the fire ball collided with water
     * @param w World in which this should be tested
     * @return true (if the fire ball collided with water) or false (else)
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
     * Reconstruct the fire ball entity
     * @param in
     * @param world World in which the entity should be reconstructed
     */
    public static void reconstruct(ByteBuffer in, World world) {
        try {
            world.entityList.add(new FireBallEntity(in.getInt(), in.getInt(), in.getInt(), in.getInt(), in.getInt()));
        } catch (Exception ex) {
            Logger.getLogger(FireBallEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
