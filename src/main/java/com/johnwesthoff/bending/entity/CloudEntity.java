/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johnwesthoff.bending.entity;

import java.awt.Color;
import java.awt.Graphics;
import java.nio.ByteBuffer;

import com.johnwesthoff.bending.Constants;
import com.johnwesthoff.bending.Server;
import com.johnwesthoff.bending.logic.World;
import com.johnwesthoff.bending.networking.handlers.DestroyEvent;

/**
 * @author John
 */
public class CloudEntity extends Entity {

    int life = 250, maker;

    public CloudEntity(int x, int y, int ID) {
        X = x;
        Y = y;
        maker = ID;
    }

    @Override
    public void onDraw(Graphics G, int viewX, int viewY) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change
        // body of generated methods, choose Tools | Templates.
        G.setColor(Color.DARK_GRAY);
        for (int i = 0; i < 14; i++) {
            G.fillArc((int) X + 20 - r.nextInt(80) - viewX, (int) Y + 10 - r.nextInt(20) - viewY - 10,
                    20 + r.nextInt(40), 5 + r.nextInt(15), 0, Constants.FULL_ANGLE);
        }
    }

    @Override
    public void onUpdate(World apples) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change
        // body of generated methods, choose Tools | Templates.
    }

    @Override
    public void cerealize(ByteBuffer out) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change
        // body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onServerUpdate(Server lol) {

        if (r.nextInt(50) == 5) {
            int x = (int) X + 40 - r.nextInt(80), y = (int) Y - 15;
            int Iw = Server.getID();
            lol.earth.entityList.add((new EnergyEntity(x, y, 0, 5, maker).setID(Iw)));
            //TODO: CAST LIGHTNING
        }
        if (life-- < 0) {
            // lol.earth.ground.FillCircleW(X, Y, radius, Constants.STONE);
            alive = false;
            lol.sendMessage(DestroyEvent.getPacket(this));
        }
    }
}
