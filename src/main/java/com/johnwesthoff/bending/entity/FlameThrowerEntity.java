/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johnwesthoff.bending.entity;

import static com.johnwesthoff.bending.entity.Entity.r;

import java.awt.Graphics;
import java.nio.ByteBuffer;

import com.johnwesthoff.bending.destruct.Player;
import com.johnwesthoff.bending.destruct.Server;
import com.johnwesthoff.bending.destruct.World;

/**
 *
 * @author John
 */
public class FlameThrowerEntity extends Entity {

    int life = 60;
    // int maker;
    int MX, MY;

    public FlameThrowerEntity(int x, int y, int mx, int my, int ID) {
        X = x;
        Y = y;
        MX = mx;
        MY = my;
        maker = ID;
    }

    @Override
    public void onDraw(Graphics G, int viewX, int viewY) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change
        // body of generated methods, choose Tools | Templates.
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

    Player master = null;

    @Override
    public void onServerUpdate(Server lol) {

        if (life % 4 == 0) {
            if (master == null) {
                master = lol.getPlayer(maker);
            }
            int x = (int) X + 5 - r.nextInt(10), y = (int) Y + 5 - r.nextInt(10);
            int Iw = Server.getID();
            int mx = MX, my = MY;
            lol.earth.entityList.add((new FirePuffEntity(x, y, mx, my, maker).setID(Iw)));
            lol.sendMessage(Server.FIREBENDING, ByteBuffer.allocate(28).putInt(6).putInt(x).putInt(y).putInt(mx)
                    .putInt(my).putInt(maker).putInt(Iw));
        }
        if (life-- < 0) {
            // lol.earth.ground.FillCircleW(X, Y, radius, World.STONE);
            alive = false;
            lol.sendMessage(Server.DESTROY, ByteBuffer.allocate(40).putInt(MYID));
        }
    }
}
