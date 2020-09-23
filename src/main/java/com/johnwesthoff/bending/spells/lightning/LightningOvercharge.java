
package com.johnwesthoff.bending.spells.lightning;

import static com.johnwesthoff.bending.util.network.ResourceLoader.loadIcon;

import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.johnwesthoff.bending.Client;
import com.johnwesthoff.bending.Server;
import com.johnwesthoff.bending.logic.World;
import com.johnwesthoff.bending.spells.Spell;

public class LightningOvercharge extends Lightning {
    int charge = 0;

    public LightningOvercharge() {
        ID = Server.LIGHTNING;
        subID = 4;
        try {
            icon = (loadIcon("https://west-it.webs.com/spells/lightningovercharge.png"));
        } catch (Exception ex) {
            Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void getAction(Client app) {
    }

    int number = 0;

    @Override
    public void onSpawn(Client me) {
        number = 1;
        for (Spell e : me.spellList[me.spellBook]) {
            if (e instanceof Lightning) {
                number++;
            }
        }

    }

    @Override
    public void getPassiveAction(Client app) {
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
