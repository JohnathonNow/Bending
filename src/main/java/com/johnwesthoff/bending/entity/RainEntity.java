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
import com.johnwesthoff.bending.networking.handlers.PuddleEvent;


public class RainEntity extends Entity {

    int life = 250;

    // int maker;
    public RainEntity(int x, int y, int ID) {
        X = x;
        Y = y;
        maker = ID;
    }

    @Override
    public void onDraw(Graphics G, int viewX, int viewY) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change
        // body of generated methods, choose Tools | Templates.
        G.setColor(Color.GRAY);
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

        if (r.nextInt(12) == 5) {
            int x = (int) X + 40 - r.nextInt(80);
            // lol.earth.entityList.add((new WaterBallEntity(x,y,0,0,maker).setID(Iw)));
            // lol.sendMessage(Constants.WATERBENDING,ByteBuffer.allocate(28).putInt(0).putInt(x).putInt(y).putInt(0).putInt(5).putInt(-1).putInt(Iw));
            lol.earth.ground.puddle(x, (int) Y, 10);
            lol.sendMessage(PuddleEvent.getPacket(x, (int) Y, 10));
        }
        if (life-- < 0) {
            // lol.earth.ground.FillCircleW(X, Y, radius, Constants.STONE);
            alive = false;
            lol.sendMessage(DestroyEvent.getPacket(this));
        }
    }
}
