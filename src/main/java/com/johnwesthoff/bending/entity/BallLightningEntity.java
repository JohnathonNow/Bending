package com.johnwesthoff.bending.entity;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import com.johnwesthoff.bending.Constants;
import com.johnwesthoff.bending.Server;
import com.johnwesthoff.bending.logic.Player;
import com.johnwesthoff.bending.logic.World;

import java.awt.*;
import java.nio.ByteBuffer;

/**
 * @author John
 */
public class BallLightningEntity extends Entity {
    public int maker = 0, radius = Constants.RADIUS_REGULAR, gravity = 1;
    public int BOUND = 10;

    public BallLightningEntity(int x, int y, int hspeed, int vspeed, int ma) {
        X = x;
        Y = y;
        xspeed = hspeed * Constants.MULTIPLIER;
        yspeed = vspeed * Constants.MULTIPLIER;
        maker = ma;
    }

    @Override
    public void onDraw(Graphics G, int viewX, int viewY) {
        if (X > viewX && X < viewX + Constants.WIDTH_INT && Y > viewY && Y < viewY + Constants.HEIGHT_INT) {
            G.setColor(Color.yellow);
            int x = (int) X + 5 - r.nextInt(BOUND) - viewX;
            int y = (int) Y + 5 - r.nextInt(BOUND) - viewY;
            G.drawLine(x, y, x + 5 - r.nextInt(BOUND), y + 5 - r.nextInt(BOUND));
            G.drawLine(x + 5 - r.nextInt(BOUND), y + 5 - r.nextInt(BOUND),
                    (int) X + 5 - r.nextInt(BOUND) - viewX, (int) Y + 5 - r.nextInt(BOUND) - viewY);

            G.setColor(Color.white);
            G.drawLine((int) X + 5 - r.nextInt(BOUND) - viewX, (int) Y + 5 - r.nextInt(BOUND) - viewY,
                    (int) X + 5 - r.nextInt(BOUND) - viewX, (int) Y + 5 - r.nextInt(BOUND) - viewY);
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
    public void cerealize(ByteBuffer out) {
        // throw new UnsupportedOperationException("Not supported yet.");
    }

}
