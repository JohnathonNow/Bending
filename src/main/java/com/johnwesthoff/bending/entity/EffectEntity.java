/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johnwesthoff.bending.entity;

import com.johnwesthoff.bending.logic.World;

import java.awt.*;
import java.nio.ByteBuffer;


public class EffectEntity extends Entity {
    Color c;
    int life;
    public int gravity;

    public EffectEntity(int x, int y, int xs, int ys, int lifee, Color ce) {
        X = x;
        Y = y;
        xspeed = xs;
        yspeed = ys;
        life = lifee;
        c = ce;
    }

    @Override
    public void onDraw(Graphics G, int viewX, int viewY) {
        G.setColor(c);
        G.drawLine((int) X - viewX, (int) Y - viewY, (int) (X + 2 * xspeed) - viewX, (int) (Y + 2 * yspeed) - viewY);
    }

    @Override
    public void onUpdate(World apples) {
        if (life-- <= 0) {
            alive = false;
        }
        this.move();
        yspeed += gravity;
    }

    @Override
    public void cerealize(ByteBuffer out) {
        /*
         * try { Server.putString(out, this.getClass().getName()); out.putInt(X);
         * out.putInt(Y); out.putInt(xspeed); out.putInt(yspeed);
         * out.putInt(c.getRGB()); out.putInt(life); } catch (Exception ex) {
         * Logger.getLogger(ExplosionEntity.class.getName()).log(Level.SEVERE, null,
         * ex); }
         */
    }
}
