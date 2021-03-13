package com.johnwesthoff.bending.entity;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import com.johnwesthoff.bending.Constants;
import com.johnwesthoff.bending.logic.World;

import java.awt.*;
import java.nio.ByteBuffer;


public class ExplosionEntity extends Entity {
    int radius = 1, maxradius, speed;

    public ExplosionEntity(int x, int y, int maxradius, int speed) {
        X = x;
        Y = y;
        xspeed = 0;
        yspeed = 0;
        this.maxradius = maxradius;
        this.speed = speed;
    }

    @Override
    public void onDraw(Graphics G, int viewX, int viewY) {
        if (X > viewX && X < viewX + Constants.WIDTH_INT && Y > viewY && Y < viewY + Constants.HEIGHT_INT) {
            G.setColor(c);
            G.fillArc(((int) X - radius) - viewX, ((int) Y - radius) - viewY, radius * 2, radius * 2, 0, Constants.FULL_ANGLE);
        }
    }

    Color c = Color.RED;

    @Override
    public void onUpdate(World apples) {
        if (radius >= maxradius) {
            alive = false;
        }

        switch (apples.random.nextInt(5)) {
            default:
            case 0:
                c = Color.red;
                break;
            case 1:
                c = Color.orange;
                break;
            case 2:
                c = Color.yellow;
                break;
            case 3:
                c = new Color(252, 111, 16);
                break;
            case 4:
                c = new Color(242, 0, 0);
                break;
        }
        c = new Color(c.getRed(), c.getGreen(), c.getBlue(), (radius / (maxradius > 0 ? maxradius : 1)) * Constants.FULL_COLOR_VALUE);
        radius += speed;
    }

    @Override
    public void cerealize(ByteBuffer out) {

    }

}
