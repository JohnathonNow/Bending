package com.johnwesthoff.bending.entity;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.nio.ByteBuffer;

import com.johnwesthoff.bending.Server;
import com.johnwesthoff.bending.logic.World;

/**
 *
 * @author John
 */
public class SteamEntity extends Entity {
    int life = 100;

    public SteamEntity(int x, int y) {
        X = x;
        Y = y;
        life = 100;
    }

    int bigness = 30;

    @Override
    public void onDraw(Graphics G, int viewX, int viewY) {

        // double scale = (life/100d);
        // scale = 2*(0.6-Math.abs(0.5d-scale));
        // for (int i = 0; i < 30; i++)
        // {
        // G.setColor(Color.white);
        // double xx =
        // X+(bigness*scale)-r.nextInt((int)(bigness*2*scale))-viewX-(bigness*2*scale);
        // double yy =
        // Y+(bigness*scale)-r.nextInt((int)(bigness*2*scale))-viewY-(bigness*scale);
        // double xxx = (bigness*.5*scale)+r.nextInt((int)((bigness*3/2)*scale));
        // double yyy = (bigness*.5*scale)+r.nextInt((int)((bigness*3/2)*scale));
        // G.fillArc((int)xx,(int)yy,(int)xxx,(int)yyy, 0, 360);
        // G.setColor(Color.BLACK);
        // G.drawArc((int)xx,(int)yy,(int)xxx,(int)yyy, 0, 360);
        // }
    }

    @Override
    public void drawOverlay(Graphics g, int viewx, int viewy) {
        Graphics2D G = (Graphics2D) g;
        AffineTransform prevTrans = G.getTransform();
        G.scale(3, 3);
        G.translate(-viewx, -viewy);
        double scale = (life / 100d);
        scale = 2 * (0.52d - Math.abs(0.5d - scale));
        for (int i = 0; i < 20; i++) {
            G.setColor(Color.white);
            double xx = X + (bigness * scale) - r.nextInt((int) (bigness * 2 * scale)) - (bigness * 2 * scale);
            double yy = Y + (bigness * scale) - r.nextInt((int) (bigness * 2 * scale)) - (bigness * scale);
            double xxx = (bigness * .5 * scale) + r.nextInt((int) ((bigness * 3 / 2) * scale));
            double yyy = (bigness * .5 * scale) + r.nextInt((int) ((bigness * 3 / 2) * scale));
            G.fillArc((int) (xx + (xxx / 2)), (int) yy, (int) xxx, (int) yyy, 0, 360);
            G.setColor(Color.BLACK);
            G.drawArc((int) (xx + (xxx / 2)), (int) yy, (int) xxx, (int) yyy, 0, 360);
        }
        G.setTransform(prevTrans);
    }

    @Override
    public void onUpdate(World apples) {

        if ((life -= 3) <= 4) {
            alive = false;
        }
        // yspeed+=gravity;

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
