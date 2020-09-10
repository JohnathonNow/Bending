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
import java.util.logging.Level;
import java.util.logging.Logger;

import com.johnwesthoff.bending.destruct.PlayerOnline;
import com.johnwesthoff.bending.destruct.Server;
import com.johnwesthoff.bending.destruct.World;

/**
 *
 * @author John
 */
public class HillEntity extends Entity {
    // public int maker = 0;
    public int radius = 16;

    public HillEntity(int x, int y, int hspeed, int ma) {
        X = x;
        Y = y;
        xspeed = hspeed;
        maker = ma;
    }

    int swag = 0;

    @Override
    public void onServerUpdate(Server apples) {
        if (swag++ > 80) {
            swag = 0;
            for (PlayerOnline P : apples.playerList) {
                if (P.x > X - 48 && P.x < X + 48 && P.y > Y - 48 && P.y < Y + 48) {
                    P.score += 10;
                    apples.sendMessage(Server.SCORE, ByteBuffer.allocate(24).putInt(P.ID).putInt(P.score));
                }
            }
        }
    }

    @Override
    public void drawOverlay(Graphics g, int viewx, int viewy) {
        Graphics2D G = (Graphics2D) g;
        AffineTransform prevTrans = G.getTransform();
        G.scale(3, 3);
        G.translate(-viewx, -viewy);
        G.setColor(Color.BLACK);
        G.drawArc((int) X - 48, (int) Y - 48, 96, 96, 0, 360);
        G.setTransform(prevTrans);
    }

    @Override
    public void cerealize(ByteBuffer out) {
        try {
            Server.putString(out, this.getClass().getName());
            out.putInt((int) X);
            out.putInt((int) Y);
            out.putInt((int) xspeed);
            out.putInt(maker);
        } catch (Exception ex) {
            Logger.getLogger(ExplosionEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void reconstruct(ByteBuffer in, World world) {
        try {
            world.entityList.add(new HillEntity(in.getInt(), in.getInt(), in.getInt(), in.getInt()));
        } catch (Exception ex) {
            Logger.getLogger(HillEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void onDraw(Graphics G, int viewX, int viewY) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change
        // body of generated methods, choose Tools | Templates.
        G.setColor(Color.WHITE);
        G.fillArc((int) X - 48 - viewX, (int) Y - 48 - viewY, 96, 96, 0, 360);
    }

    @Override
    public void onUpdate(World apples) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change
        // body of generated methods, choose Tools | Templates.
    }

}
