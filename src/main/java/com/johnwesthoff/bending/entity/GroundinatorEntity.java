package com.johnwesthoff.bending.entity;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.Color;
import java.awt.Graphics;
import java.nio.ByteBuffer;
import com.johnwesthoff.bending.destruct.Constants;
import com.johnwesthoff.bending.destruct.World;

/**
 *
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
            G.fillArc((int) (X - 1) - viewX, (int) (Y - 1) - viewY, 2, 2, 0, 360);
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
