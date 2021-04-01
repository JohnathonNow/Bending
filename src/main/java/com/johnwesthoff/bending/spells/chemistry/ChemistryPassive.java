
package com.johnwesthoff.bending.spells.chemistry;

import static com.johnwesthoff.bending.util.network.ResourceLoader.loadIcon;

import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.johnwesthoff.bending.Session;
import com.johnwesthoff.bending.Constants;
import com.johnwesthoff.bending.logic.World;
import com.johnwesthoff.bending.spells.Spell;

public class ChemistryPassive extends Chemistry {
    private int effect[] = {
        Constants.ST_CHEM0,
        Constants.ST_CHEM1,
        Constants.ST_CHEM2,
        Constants.ST_CHEM3,
        Constants.ST_CHEM4,
        Constants.ST_CHEM5,
    };
    public ChemistryPassive() {
        ID = Constants.CHEMISTRY;
        subID = 3;
        try {
            icon = (loadIcon("earthshield.png"));
        } catch (Exception ex) {
            Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    int number = 0;

    @Override
    public void onSpawn(Session me) {
        number = 0;
        for (Spell e : me.spellList[me.spellBook]) {
            if (e instanceof Chemistry) {
                number++;
            }
        }
        getPassiveAction(me);
    }

    @Override
    public void getPassiveAction(Session app) {
        app.world.status |= this.effect[number];
    }

    @Override
    public String getName() {
        return "Toxic Skin";
    }

    @Override
    public String getTip() {
        return "<html>A passive chemistry spell<br>Damage nearby players who touch you</html>";
    }

    @Override
    public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
