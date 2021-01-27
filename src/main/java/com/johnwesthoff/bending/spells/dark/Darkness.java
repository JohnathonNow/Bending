
package com.johnwesthoff.bending.spells.dark;

import static com.johnwesthoff.bending.util.network.ResourceLoader.loadIcon;

import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.johnwesthoff.bending.Constants;
import com.johnwesthoff.bending.Session;
import com.johnwesthoff.bending.logic.World;
import com.johnwesthoff.bending.spells.Spell;

public class Darkness extends Spell {
    public Darkness() {
        ID = Constants.DARKNESS;
        subID = 0;
        locked = true;
        unlockXP = 2000;
        try {
            icon = loadIcon("salt.png");
        } catch (Exception ex) {
            Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void getAction(Session app) {
        app.turnVisible = 40 * 3;
        app.world.status |= Constants.ST_INVISIBLE;
        app.net.sendMovement();
    }

    @Override
    public int getCost() {
        return 300;
    }

    @Override
    public String getName() {
        return "Dark Cloak";
    }

    @Override
    public void getPassiveAction(Session app) {
    }

    @Override
    public String getTip() {
        return "<html>A basic darkness spell<br>Moderate Energy Cost<br>Turn invisible for a short time</html>";
    }

    @Override
    public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

