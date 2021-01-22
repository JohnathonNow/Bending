package com.johnwesthoff.bending.entity;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import com.johnwesthoff.bending.Constants;
import com.johnwesthoff.bending.Server;
import com.johnwesthoff.bending.logic.World;

import java.awt.*;
import java.nio.ByteBuffer;

/**
 * @author John
 */
public class ShockEffectEntity extends Entity {
    public int radius = Constants.RADIUS_REGULAR;
    public int life = 3;

    public ShockEffectEntity(int x, int y, int radius) {
        X = x;
        Y = y;
        this.radius = radius;

    }

    @Override
    public void onDraw(Graphics G, int viewX, int viewY) {
        if (X > viewX && X < viewX + Constants.WIDTH_INT && Y > viewY && Y < viewY + Constants.HEIGHT_INT) {
            G.setColor(Color.yellow);
            int x = (int) X + radius - r.nextInt(radius * 2) - viewX;
            int y = (int) Y + radius - r.nextInt(radius * 2) - viewY;
            G.drawLine(x, y, x + radius - r.nextInt(radius * 2), y + radius - r.nextInt(radius * 2));
            G.drawLine((int) x + radius - r.nextInt(radius * 2), y + radius - r.nextInt(radius * 2),
                    (int) X + radius - r.nextInt(radius * 2) - viewX, (int) Y + radius - r.nextInt(radius * 2) - viewY);

            G.setColor(Color.white);
            G.drawLine((int) X + radius - r.nextInt(radius * 2) - viewX,
                    (int) Y + radius - r.nextInt(radius * 2) - viewY, (int) X + radius - r.nextInt(radius * 2) - viewX,
                    (int) Y + radius - r.nextInt(radius * 2) - viewY);
        }
    }

    @Override
    public void onUpdate(World apples) {
        if (life-- < 0) {
            alive = false;
        }

        /*
         * if (yspeed<12) { yspeed++; }
         */
    }

    @Override
    public void onServerUpdate(Server lol) {

    }

    @Override
    public void cerealize(ByteBuffer out) {
        // throw new UnsupportedOperationException("Not supported yet.");
    }

}
