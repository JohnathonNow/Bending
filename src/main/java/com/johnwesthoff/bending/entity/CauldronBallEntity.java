package com.johnwesthoff.bending.entity;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.Color;
import java.awt.Graphics;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.johnwesthoff.bending.Constants;
import com.johnwesthoff.bending.Server;
import com.johnwesthoff.bending.logic.World;
import com.johnwesthoff.bending.networking.handlers.DigEvent;
import com.johnwesthoff.bending.networking.handlers.FillEvent;
import com.johnwesthoff.bending.util.math.Ops;

/**
 *
 * @author John
 */
public class CauldronBallEntity extends Entity {
    private enum Containing {
        NOTHING, POISON
    }

    public int radius = 16;
    int a1, a2, a3;
    int s1, s2, s3;
    Containing substance;

    public CauldronBallEntity(int x, int y, int hspeed, int vspeed, int ma) {
        X = x;
        Y = y;
        xspeed = hspeed;
        yspeed = vspeed;
        maker = ma;
        a1 = 60 + r.nextInt(120);
        a2 = 60 + r.nextInt(120);
        a3 = 60 + r.nextInt(120);
        s1 = r.nextInt(360);
        s2 = r.nextInt(360);
        s3 = r.nextInt(360);
        substance = Containing.NOTHING;
    }

    @Override
    public void onDraw(Graphics G, int viewX, int viewY) {
        if (X > viewX && X < viewX + Constants.WIDTH_INT && Y > viewY && Y < viewY + Constants.HEIGHT_INT) {
            G.setColor(Color.DARK_GRAY);
            G.fillArc((int) (X - radius / 2) - viewX, (int) (Y - radius / 2) - viewY, radius, radius, 0, 360);

            G.setColor(Color.LIGHT_GRAY);
            G.fillArc((int) (X - 2) - viewX, (int) (Y - 2) - viewY, 4, 4, s1, a1);
            G.fillArc((int) (X - 4) - viewX, (int) (Y - 4) - viewY, 8, 8, s2, a2);
            G.fillArc((int) (X - 6) - viewX, (int) (Y - 6) - viewY, 12, 12, s3, a3);
        }
    }

    @Override
    public void onUpdate(World apples) {
        if (!apples.inBounds(X, Y) || apples.checkCollision(X, Y)) {

            alive = false;
        }
        yspeed += Constants.GRAVITY;

    }

    @Override
    public void onServerUpdate(Server lol) {
        if (lol.earth.checkCollision(X, Y)) {
            radius = 68;
            lol.earth.ground.fillCircleW((int) X, (int) Y, radius, Constants.STONE);
            lol.sendMessage(FillEvent.getPacket((int) (X), (int) (Y), radius, Constants.STONE));
            radius -= 8;
            if (substance == Containing.POISON) {
                lol.earth.ground.fillCircleW((int) X, (int) Y, radius, Constants.GAS);
                lol.sendMessage(FillEvent.getPacket((int) (X), (int) (Y), radius, Constants.GAS));
            } else {
                lol.earth.ground.clearCircle((int) X, (int) Y, radius);
                lol.sendMessage(DigEvent.getPacket((int) (X), (int) (Y), radius));
            }
            alive = false;
        }
        for (Entity e : lol.earth.entityList) {
            if (e instanceof PoisonBallEntity && Ops.pointDis(X, Y, e.getX(), e.getY()) < 24
                    && substance == Containing.NOTHING) {
                substance = Containing.POISON;
                e.destroy(lol);
            }
        }
    }

    @Override
    public void cerealize(ByteBuffer out) {
        try {
            Server.putString(out, this.getClass().getName());
            out.putInt((int) X);
            out.putInt((int) Y);
            out.putInt((int) xspeed);
            out.putInt((int) yspeed);
            out.putInt(maker);
        } catch (Exception ex) {
            Logger.getLogger(ExplosionEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void reconstruct(ByteBuffer in, World world) {
        try {
            world.entityList.add(new JuiceBallEntity(in.getInt(), in.getInt(), in.getInt(), in.getInt(), in.getInt()));
        } catch (Exception ex) {
            Logger.getLogger(JuiceBallEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
