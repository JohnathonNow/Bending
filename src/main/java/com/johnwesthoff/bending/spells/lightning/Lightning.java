
package com.johnwesthoff.bending.spells.lightning;

import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;

import com.johnwesthoff.bending.Client;
import com.johnwesthoff.bending.Server;
import com.johnwesthoff.bending.entity.EnergyEntity;
import com.johnwesthoff.bending.logic.World;
import com.johnwesthoff.bending.spells.Spell;
import com.johnwesthoff.bending.util.network.ResourceLoader;

public class Lightning extends Spell {
    public Lightning() {
        ID = Server.LIGHTNING;
        subID = 0;
        locked = true;
        unlockXP = 250;
        try {
            icon = new ImageIcon(ResourceLoader.loadImage("lightningstrike.png"));
        } catch (Exception ex) {
            Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void getAction(Client app) {
        // throw new UnsupportedOperationException("Not supported yet.");
        ID = Server.LIGHTNING;
        subID = 0;
        X = app.world.x;
        Y = app.world.y - World.head;
        mx = app.world.viewX;
        my = app.world.mouseY - app.world.viewY;
        double direction = 360 - Client.pointDir(app.world.x - app.world.viewX,
                app.world.y - World.head - app.world.viewY, app.world.mouseX, app.world.mouseY);
        // direction+=180;
        mx = ((int) (Client.lengthdir_x(8, direction)));
        my = ((int) (Client.lengthdir_y(8, direction)));
        maker = ID;
        app.HP -= 1;
        getMessage(app.out);
    }

    @Override
    public int getCost() {
        return 0;
    }

    @Override
    public String getName() {
        return "Lightning Strike";
    }

    @Override
    public void getPassiveAction(Client app) {
        // throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getTip() {
        return "<html>A basic lightning spell<br>Low Health Cost<br>Travels in a straight line<br>Deals low damage<br>Restores energy in blast vicinity</html>";
    }

    @Override
    public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
        world.entityList.add(new EnergyEntity(px, py, mx, my, pid).setID(eid));
    }
}

