package com.johnwesthoff.bending.entity;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.Color;
import java.awt.Graphics;
import java.nio.ByteBuffer;

import com.johnwesthoff.bending.Constants;
import com.johnwesthoff.bending.Server;
import com.johnwesthoff.bending.Session;
import com.johnwesthoff.bending.logic.Player;
import com.johnwesthoff.bending.logic.World;
import com.johnwesthoff.bending.networking.handlers.FillEvent;
import com.johnwesthoff.bending.util.math.Ops;

/**
 * @author John
 */
public class SandEntity extends Entity {
    // public int maker = 0;
    public int radius = Constants.RADIUS_REGULAR;
    public int gravity = 1;
    int a1, a2, a3, s1, s2, s3;
    int life = 0;

    public SandEntity(int x, int y, int hspeed, int vspeed, int ma) {
        X = x;
        Y = y;
        xspeed = hspeed;
        yspeed = vspeed;
        maker = ma;
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
            G.setColor(Color.YELLOW);
            G.fillArc((int) (X - 3) - viewX, (int) (Y - 3) - viewY, 6, 6, 0, Constants.FULL_ANGLE);

            G.setColor(Color.GRAY);
            G.fillArc((int) (X - 2) - viewX, (int) (Y - 2) - viewY, 4, 4, s1, a1);
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
        if (life++ > 50) {
            alive = false;
        }
        yspeed += gravity;

        /*
         * if (yspeed<12) { yspeed++; }
         */
    }

    long time = System.currentTimeMillis();

    @Override
    public void onServerUpdate(Server lol) {

        // System.out.println(System.currentTimeMillis()-time);
        time = System.currentTimeMillis();
        if (!lol.earth.inBounds(X, Y) || hasCollided(lol.earth)) {
            radius /= 2;
            lol.earth.ground.FillCircleW((int) X, (int) Y, radius, Constants.SAND);
            lol.sendMessage(FillEvent.getPacket((int) X, (int) Y, radius, Constants.SAND));
        }
    }

    @Override
    public void checkAndHandleCollision(Session client) {

        final double d = Ops.pointDis(X, Y, client.world.x, client.world.y);
        if (d < radius * 3 && maker != client.ID && (client.gameMode <= 0 || client.badTeam.contains(maker))) {
            client.client.hurt(2);
            client.world.vspeed -= 1;
            client.xspeed += (xspeed / 64);
            client.lastHit = maker;
            alive = false;
            client.killMessage = "~ was shredded by `'s shotgun.";
        }
    }

    @Override
    public void cerealize(ByteBuffer out) {
        // throw new UnsupportedOperationException("Not supported yet.");
    }

}
