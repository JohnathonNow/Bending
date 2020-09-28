
package com.johnwesthoff.bending.spells.earth;

import static com.johnwesthoff.bending.util.network.ResourceLoader.loadIcon;

import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.johnwesthoff.bending.Main;
import com.johnwesthoff.bending.Server;
import com.johnwesthoff.bending.logic.World;
import com.johnwesthoff.bending.spells.Spell;

public class EarthbendingShield extends Earthbending {
    public EarthbendingShield() {
        ID = Server.EARTHBENDING;
        subID = 3;
        try {
            icon = (loadIcon("https://west-it.webs.com/spells/earthshield.png"));
        } catch (Exception ex) {
            Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    int number = 0;

    @Override
    public void onSpawn(Main me) {
        number = 0;
        for (Spell e : me.spellList[me.spellBook]) {
            if (e instanceof Earthbending) {
                number++;
            }
        }
        getPassiveAction(me);
    }

    @Override
    public void getAction(Main app) {
        if (app.energico > 0 && app.HP < app.MAXHP) {
            app.energico -= 150;
            app.HP += 5;
        }
    }

    @Override
    public void getPassiveAction(Main app) {
        /*
         * if (app.energico>0) { app.energico--; }
         */
        app.MAXHP = (short) (110 + (10 * number));

    }

    @Override
    public int getCost() {
        return 0;
    }

    @Override
    public String getName() {
        return "Earth Shield";
    }

    @Override
    public String getTip() {
        return "<html>A passive earth spell<br>Increase maximum health</html>";
    }

    @Override
    public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

