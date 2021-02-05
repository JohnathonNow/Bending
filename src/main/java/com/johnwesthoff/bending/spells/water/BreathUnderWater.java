
package com.johnwesthoff.bending.spells.water;

import static com.johnwesthoff.bending.util.network.ResourceLoader.loadIcon;

import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.johnwesthoff.bending.Session;
import com.johnwesthoff.bending.Constants;
import com.johnwesthoff.bending.logic.World;
import com.johnwesthoff.bending.spells.Spell;

public class BreathUnderWater extends Waterbending {
    public BreathUnderWater() {
        ID = Constants.WATERBENDING;
        subID = 3;
        try {
            icon = (loadIcon("BreathUnderWater.png"));
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
        number = 0;
        for (Spell e : me.spellList[me.spellBook]) {
            if (e instanceof Waterbending) {
                number++;
            }
        }
    }

    @Override
    public void getPassiveAction(Session app) {
        if (app.world.inBounds(app.world.x, app.world.y) && app.energico > 0
                && app.world.isType((int) app.world.x, (int) app.world.y, Constants.WATER)) {
            if (app.HP < app.MAXHP) {
                app.energico -= 30 + (5 - number) * 5;
                app.HP++;
            }
            if (number >= 0) {
                app.lungs = app.maxlungs;
            }
        }
    }

    @Override
    public int getCoolDown() {
        return (int) (0 * Constants.FPS / 600);
    }

    @Override
    public String getName() {
        return "Water Heal";
    }

    @Override
    public String getTip() {
        return "<html>A passive water spell<br>High Cooldown<br>Heal when in water</html>";
    }

    @Override
    public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}

