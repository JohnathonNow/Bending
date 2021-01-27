
package com.johnwesthoff.bending.spells.lightning;

import static com.johnwesthoff.bending.util.network.ResourceLoader.loadIcon;

import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.johnwesthoff.bending.Session;
import com.johnwesthoff.bending.Constants;
import com.johnwesthoff.bending.logic.World;
import com.johnwesthoff.bending.spells.Spell;

public class LightningShield extends Lightning {
    int charge = 0;

    public LightningShield() {
        ID = Constants.LIGHTNING;
        subID = 4;
        locked = true;
        unlockXP = 2000;
        try {
            icon = (loadIcon("lightningshield.png"));
        } catch (Exception ex) {
            Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void getAction(Session app) {
        // throw new UnsupportedOperationException("Not supported yet.");
    }

    int number = 0;

    @Override
    public void onSpawn(Session me) {
        number = 0;
        for (Spell e : me.spellList[me.spellBook]) {
            if (e instanceof Lightning) {
                number++;
            }
        }
        me.shockdrain = number;
    }

    @Override
    public void getPassiveAction(Session app) {

    }

    @Override
    public int getCost() {
        return 0;
    }

    @Override
    public String getName() {
        return "Lightning Shield";
    }

    @Override
    public String getTip() {
        return "<html>A passive lightning spell<br>Have a chance at converting health loss into energy loss</html>";
    }

    @Override
    public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
