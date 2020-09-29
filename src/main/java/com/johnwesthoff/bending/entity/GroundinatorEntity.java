package com.johnwesthoff.bending.entity;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import com.johnwesthoff.bending.Constants;
import com.johnwesthoff.bending.logic.World;

import java.awt.*;
import java.nio.ByteBuffer;

/**
 * @author John
 */
public class GroundinatorEntity extends Entity {
    public GroundinatorEntity(int x, int y, int hspeed, int vspeed) {
        X = x;
        Y = y;
        xspeed = hspeed;
        yspeed = vspeed;
    }

    @Override
    public void onDraw(Graphics G, int viewX, int viewY) {
        if (X > viewX && X < viewX + Constants.WIDTH_INT && Y > viewY && Y < viewY + Constants.HEIGHT_INT) {
            G.setColor(Color.GREEN);
            G.fillArc((int) (X - 1) - viewX, (int) (Y - 1) - viewY, 2, 2, 0, Constants.FULL_ANGLE);
        }
    }

    @Override
    public void onUpdate(World apples) {
        if (!apples.inBounds(X, Y) || apples.checkCollision(X, Y)) {
            alive = false;
            apples.unexplode((int) X, (int) Y, 32, 8, 16);
        }
        if (yspeed < 12) {
            yspeed++;
        }
    }

    @Override
    public void cerealize(ByteBuffer out) {
        // throw new UnsupportedOperationException("Not supported yet.");
    }

}
