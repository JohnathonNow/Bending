
package com.johnwesthoff.bending.spells.lightning;

import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;

import com.johnwesthoff.bending.Session;
import com.johnwesthoff.bending.Constants;
import com.johnwesthoff.bending.entity.CloudEntity;
import com.johnwesthoff.bending.logic.World;
import com.johnwesthoff.bending.spells.Spell;
import com.johnwesthoff.bending.util.network.ResourceLoader;

public class LightningStorm extends Lightning {
    public LightningStorm() {
        ID = Constants.LIGHTNING;
        subID = 1;
        locked = true;
        unlockXP = 500;
        try {
            icon = new ImageIcon(ResourceLoader.loadImage("lightningstorm.png"));
        } catch (Exception ex) {
            Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void getAction(Session app) {
        // throw new UnsupportedOperationException("Not supported yet.");
        X = app.world.mouseX + app.world.viewX;
        Y = app.world.mouseY + app.world.viewY;
        mx = -Math.min(Math.max(((app.world.x - app.world.viewX) - app.world.mouseX) / 10, -2), 2);
        my = 0;
        maker = ID;
        app.HP -= 3;
        getMessage(app.out);
    }

    @Override
    public int getCost() {
        return 250;
    }

    @Override
    public String getName() {
        return "Lightning Storm";
    }

    @Override
    public void getPassiveAction(Session app) {
        // throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getTip() {
        return "<html>An intermediate lightning spell<br>Low-Moderate Energy Cost<br>Low Health Cost<br>Fires from overhead<br>Deals low damage<br>Restores energy in blast vicinity</html>";
    }

    @Override
    public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
        world.entityList.add((new CloudEntity(px, py, ID).setID(eid)));
    }
}

