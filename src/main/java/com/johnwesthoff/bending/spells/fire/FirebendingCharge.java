
package com.johnwesthoff.bending.spells.fire;

import com.johnwesthoff.bending.Client;
import com.johnwesthoff.bending.Server;
import com.johnwesthoff.bending.logic.World;
import com.johnwesthoff.bending.spells.Spell;

import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.johnwesthoff.bending.util.network.ResourceLoader.loadIcon;

public class FirebendingCharge extends Firebending {

    public FirebendingCharge() {
        ID = Server.FIREBENDING;
        subID = 3;
        try {
            icon = (loadIcon("https://west-it.webs.com/spells/firecharge.png"));
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
            if (e instanceof Firebending) {
                number++;
            }
        }
        Client.inputer.doublecast = number;
    }

    @Override
    public void getPassiveAction(Client app) {

        // throw new UnsupportedOperationException("Not supported yet.");
        /*
         * if (app.energico<app.maxeng) { if (charge++>1) { app.HP--; charge = 0; }
         * app.energico+=app.engrecharge*3; }
         */
    }

    @Override
    public int getCost() {
        return 0;
    }

    @Override
    public String getName() {
        return "Fire Charge";
    }

    @Override
    public String getTip() {
        return "<html>A passive fire spell<br>Have a chance at casting twice as many fire spells</html>";
    }

    @Override
    public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

