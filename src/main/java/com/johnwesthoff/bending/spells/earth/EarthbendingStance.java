
package com.johnwesthoff.bending.spells.earth;

import static com.johnwesthoff.bending.util.network.ResourceLoader.loadIcon;

import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.johnwesthoff.bending.Client;
import com.johnwesthoff.bending.Constants;
import com.johnwesthoff.bending.logic.World;
import com.johnwesthoff.bending.spells.Spell;

public class EarthbendingStance extends Earthbending {
    public EarthbendingStance() {
        ID = Constants.EARTHBENDING;
        subID = 7;
        try {
            icon = (loadIcon("earthstance.png"));
        } catch (Exception ex) {
            Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    int number = 0;

    @Override
    public void onSpawn(Client me) {
        number = 0;
        for (Spell e : me.spellList[me.spellBook]) {
            if (e instanceof Earthbending) {
                number++;
            }
        }
        getPassiveAction(me);
    }

    @Override
    public void getPassiveAction(Client app) {
        app.knockbackDecay = 0.8 - ((double) number / 10d);
        app.world.floatiness = -7 * (number + 1);
    }

    @Override
    public String getName() {
        return "Earth Stance";
    }

    @Override
    public String getTip() {
        return "<html>A passive earth spell<br>Decrease knockback</html>";
    }

    @Override
    public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
