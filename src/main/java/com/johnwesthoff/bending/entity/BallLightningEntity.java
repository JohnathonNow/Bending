package com.johnwesthoff.bending.entity;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.Color;
import java.awt.Graphics;
import java.nio.ByteBuffer;

import com.johnwesthoff.bending.Client;
import com.johnwesthoff.bending.Constants;
import com.johnwesthoff.bending.Server;
import com.johnwesthoff.bending.logic.Player;
import com.johnwesthoff.bending.logic.World;


/**
 *
 * @author John
 */
public class BallLightningEntity extends Entity {
    public int maker = 0;
    public int radius = 16;
    public int gravity = 1;
    int a1, a2, a3;
    int s1, s2, s3;

    public BallLightningEntity(int x, int y, int hspeed, int vspeed, int ma) {
        X = x;
        Y = y;
        xspeed = hspeed * 3;
        yspeed = vspeed * 3;
        maker = ma;

    }

    @Override
    public void onDraw(Graphics G, int viewX, int viewY) {
        if (X > viewX && X < viewX + Constants.WIDTH_INT && Y > viewY && Y < viewY + Constants.HEIGHT_INT) {
            G.setColor(Color.yellow);
            int x = (int) X + 5 - r.nextInt(10) - viewX;
            int y = (int) Y + 5 - r.nextInt(10) - viewY;
            G.drawLine(x, y, x + 5 - r.nextInt(10), y + 5 - r.nextInt(10));
            G.drawLine(x + 5 - r.nextInt(10), y + 5 - r.nextInt(10), (int) X + 5 - r.nextInt(10) - viewX,
                    (int) Y + 5 - r.nextInt(10) - viewY);

            G.setColor(Color.white);
            G.drawLine((int) X + 5 - r.nextInt(10) - viewX, (int) Y + 5 - r.nextInt(10) - viewY,
                    (int) X + 5 - r.nextInt(10) - viewX, (int) Y + 5 - r.nextInt(10) - viewY);
        }
    }

    @Override
    public void onUpdate(World apples) {
        if (!apples.inBounds((int) X, (int) Y) || apples.checkCollision((int) X, (int) Y)) {

            alive = false;
            // apples.explode(X, Y, 32, 8, 16);
        }
        for (Player p : apples.playerList) {
            if (apples.pointDis(X, Y, p.x, p.y) < radius && maker != p.ID) {
                alive = false;
            }
        }
        if (gravity++ > 3) {
            yspeed += 1;
            gravity = 0;
        }

        /*
         * if (yspeed<12) { yspeed++; }
         */
    }

    long time = System.currentTimeMillis();

    @Override
    public void onServerUpdate(Server lol) {

    }

    @Override
    public void handleCollision(Client client) {

        if (client.checkCollision(X, Y) && (maker != client.ID && (client.gameMode <= 0 || client.badTeam.contains(maker)))) {
            client.hurt(10);
            client.lastHit = maker;
            client.world.vspeed -= client.random.nextInt(22);
            client.xspeed += 18 - client.random.nextInt(36);
            client.killMessage = "~ was shockingly killed by `!";
        }
    }

    @Override
    public void cerealize(ByteBuffer out) {
        // throw new UnsupportedOperationException("Not supported yet.");
    }

}
