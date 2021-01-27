
package com.johnwesthoff.bending.spells.lightning;

import static com.johnwesthoff.bending.util.network.ResourceLoader.loadIcon;

import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.johnwesthoff.bending.Session;
import com.johnwesthoff.bending.Constants;
import com.johnwesthoff.bending.logic.World;
import com.johnwesthoff.bending.spells.Spell;

public class LightningOvercharge extends Lightning {
    int charge = 0;

    public LightningOvercharge() {
        ID = Constants.LIGHTNING;
        subID = 4;
        locked = true;
        unlockXP = 1500;
        try {
            icon = (loadIcon("lightningovercharge.png"));
        } catch (Exception ex) {
            Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void getAction(Session app) {
    }

    int number = 0;

    @Override
    public void onSpawn(Session me) {
        number = 1;
        for (Spell e : me.spellList[me.spellBook]) {
            if (e instanceof Lightning) {
                number++;
            }
        }

    }

    @Override
    public void getPassiveAction(Session app) {
        app.maxeng = 1000 + (number * 166);
    }

    @Override
    public int getCost() {
        return 0;
    }

    @Override
    public String getName() {
        return "Overcharged";
    }

    @Override
    public String getTip() {
        return "<html>A passive lightning spell<br>Have more energy</html>";
    }

    @Override
    public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
