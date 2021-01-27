package com.johnwesthoff.bending.entity;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.nio.ByteBuffer;

import com.johnwesthoff.bending.Constants;
import com.johnwesthoff.bending.Server;
import com.johnwesthoff.bending.logic.Player;
import com.johnwesthoff.bending.logic.World;
import com.johnwesthoff.bending.networking.handlers.ChargeEvent;
import com.johnwesthoff.bending.networking.handlers.DestroyEvent;
import com.johnwesthoff.bending.util.math.Ops;

/**
 * @author John
 */
public class EnergyEntity extends Entity {
    // public int maker = 0;
    public int radius = Constants.RADIUS_REGULAR, gravity = 0, decay = 0, life = Constants.FULL_COLOR_VALUE, xstart, ystart;
    Polygon P = new Polygon();

    public EnergyEntity(int x, int y, int hspeed, int vspeed, int ma) {
        X = x;
        Y = y;
        xspeed = hspeed * 3;
        yspeed = vspeed * 3;
        xstart = x;
        ystart = y;
        maker = ma;
        P.addPoint(x, y);
    }

    @Override
    public void onDraw(Graphics G, int viewX, int viewY) {

        G.setColor(new Color(Constants.FULL_COLOR_VALUE, Constants.FULL_COLOR_VALUE, 0, life));
        P.translate(-viewX, -viewY);
        G.drawPolygon(P);
        P.translate(viewX, viewY);

        G.setColor(Color.DARK_GRAY);
        for (int i = 0; i < 14; i++) {
            G.fillArc(xstart + 10 - r.nextInt(20) - viewX - 10, ystart + 10 - r.nextInt(20) - viewY - 10,
                    5 + r.nextInt(15), 5 + r.nextInt(15), 0, Constants.FULL_ANGLE);
        }
    }

    @Override
    public void onUpdate(World apples) {
        P.addPoint((int) X + 5 - r.nextInt(10), (int) Y + 5 - r.nextInt(10));
        if (apples.checkCollision(X, Y)) {
            decay = 5;
            xspeed = 0;
            yspeed = 0;
        }

        if ((life -= decay) <= 0) {
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

        // System.out.println(System.currentTimeMillis()-time);
        time = System.currentTimeMillis();
        if (!lol.earth.inBounds(X, Y) || lol.earth.checkCollision(X, Y)) {
            radius *= Constants.MULTIPLIER;
            alive = false;
            // lol.earth.ground.FillCircleW(X, Y, radius, Constants.STONE);
            lol.sendMessage(ChargeEvent.getPacket(this));
                    
        }
        for (Player p : lol.playerList) {
            if (Ops.pointDis(X, Y - Constants.HEAD, p.x, p.y) < 40 && maker != p.ID) {
                alive = false;
                lol.sendMessage(DestroyEvent.getPacket(this));
                lol.sendMessage(ChargeEvent.getPacket(this));
            }
        }
    }

    @Override
    public void cerealize(ByteBuffer out) {
        // throw new UnsupportedOperationException("Not supported yet.");
    }

}
