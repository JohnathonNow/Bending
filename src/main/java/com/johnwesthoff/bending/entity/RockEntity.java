package com.johnwesthoff.bending.entity;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.Color;
import java.awt.Graphics;
import java.nio.ByteBuffer;
import java.util.Random;

import com.johnwesthoff.bending.Session;
import com.johnwesthoff.bending.Constants;
import com.johnwesthoff.bending.Server;
import com.johnwesthoff.bending.logic.Player;
import com.johnwesthoff.bending.logic.World;
import com.johnwesthoff.bending.networking.handlers.FillEvent;


public class RockEntity extends Entity {
    public int radius = Constants.RADIUS_REGULAR;
    int a1, a2, a3, s1, s2, s3;

    public RockEntity(int x, int y, int hspeed, int vspeed, int ma) {
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
            G.setColor(Color.DARK_GRAY);
            G.fillArc((int) (X - radius / 2) - viewX, (int) (Y - radius / 2) - viewY, radius, radius, 0,
                    Constants.FULL_ANGLE);

            G.setColor(Color.GRAY);
            G.fillArc(((int) X - 2) - viewX, (int) (Y - 2) - viewY, 4, 4, s1, a1);
            G.fillArc((int) (X - 4) - viewX, (int) (Y - 4) - viewY, 8, 8, s2, a2);
            G.fillArc((int) (X - 6) - viewX, (int) (Y - 6) - viewY, 12, 12, s3, a3);
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
        yspeed += Constants.GRAVITY;

        /*
         * if (yspeed<12) { yspeed++; }
         */
    }

    long time = System.currentTimeMillis();

    @Override
    public void onServerUpdate(Server lol) {

        // System.out.println(System.currentTimeMillis()-time);
        time = System.currentTimeMillis();
        if (!lol.earth.inBounds(X + xspeed, Y + yspeed) || lol.earth.checkCollision(X + xspeed, Y + yspeed)) {
            radius *= 3;
            lol.earth.ground.fillCircleW((int) (X + xspeed), (int) (Y + yspeed), 48, Constants.STONE);
            lol.sendMessage(FillEvent.getPacket((int) (X + xspeed), (int) (Y + yspeed), 48, Constants.STONE));
        }
    }

    @Override
    public void checkAndHandleCollision(Session client) {

        if (client.client.checkCollision(X, Y) && maker != client.ID
                && (client.gameMode <= 0 || client.badTeam.contains(maker))) {
            client.client.hurt(18);
            client.world.vspeed -= 5;
            client.xspeed += 7 - client.random.nextInt(14);
            client.lastHit = maker;
            alive = false;
            client.killMessage = "~ was built into a bridge by `.";
        }
    }

    @Override
    public void cerealize(ByteBuffer out) {
        // throw new UnsupportedOperationException("Not supported yet.");
    }

}
