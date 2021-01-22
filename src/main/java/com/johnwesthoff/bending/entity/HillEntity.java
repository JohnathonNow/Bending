package com.johnwesthoff.bending.entity;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import com.johnwesthoff.bending.Constants;
import com.johnwesthoff.bending.Server;
import com.johnwesthoff.bending.logic.PlayerOnline;
import com.johnwesthoff.bending.logic.World;
import com.johnwesthoff.bending.networking.handlers.ScoreEvent;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author John
 */
public class HillEntity extends Entity {
    // public int maker = 0;
    public int radius = Constants.RADIUS_REGULAR;

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
                    apples.sendMessage(ScoreEvent.getPacket(P));
                }
            }
        }
    }

    @Override
    public void drawOverlay(Graphics g, int viewx, int viewy) {
        Graphics2D G = (Graphics2D) g;
        AffineTransform prevTrans = G.getTransform();
        G.scale(Constants.WIDTH_SCALE, Constants.HEIGHT_SCALE);
        G.translate(-viewx, -viewy);
        G.setColor(Color.BLACK);
        G.drawArc((int) X - 48, (int) Y - 48, 96, 96, 0, Constants.FULL_ANGLE);
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

    /**
     * Reconstruct the hill entity
     * @param in
     * @param world World in which the entity should be reconstructed
     */
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
        G.fillArc((int) X - 48 - viewX, (int) Y - 48 - viewY, 96, 96, 0, Constants.FULL_ANGLE);
    }

    @Override
    public void onUpdate(World apples) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change
        // body of generated methods, choose Tools | Templates.
    }

}
