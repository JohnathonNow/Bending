
package com.johnwesthoff.bending.spells.lightning;

import static com.johnwesthoff.bending.util.network.ResourceLoader.loadIcon;

import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.johnwesthoff.bending.Client;
import com.johnwesthoff.bending.Server;
import com.johnwesthoff.bending.logic.World;
import com.johnwesthoff.bending.spells.Spell;

public class LightningShield extends Lightning {
    int charge = 0;

    public LightningShield() {
        ID = Server.LIGHTNING;
        subID = 4;
        try {
            icon = (loadIcon("https://west-it.webs.com/spells/lightningshield.png"));
        } catch (Exception ex) {
            Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void getAction(Client app) {
        // throw new UnsupportedOperationException("Not supported yet.");
    }

    int number = 0;

    @Override
    public void onSpawn(Client me) {
        number = 0;
        for (Spell e : me.spellList[me.spellBook]) {
            if (e instanceof Lightning) {
                number++;
            }
        }
        me.shockdrain = number;
    }

    @Override
    public void getPassiveAction(Client app) {

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

