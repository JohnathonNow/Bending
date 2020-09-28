
package com.johnwesthoff.bending.spells.water;

import static com.johnwesthoff.bending.util.network.ResourceLoader.loadIcon;

import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.johnwesthoff.bending.Main;
import com.johnwesthoff.bending.Server;
import com.johnwesthoff.bending.logic.World;
import com.johnwesthoff.bending.spells.Spell;

public class BreathUnderWater extends Waterbending {
    public BreathUnderWater() {
        ID = Server.WATERBENDING;
        subID = 3;
        try {
            icon = (loadIcon("https://west-it.webs.com/spells/BreathUnderWater.png"));
        } catch (Exception ex) {
            Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void getAction(Main app) {
    }

    int number = 0;

    @Override
    public void onSpawn(Main me) {
        number = 0;
        for (Spell e : me.spellList[me.spellBook]) {
            if (e instanceof Waterbending) {
                number++;
            }
        }
    }

    @Override
    public void getPassiveAction(Main app) {
        if (app.world.inBounds(app.world.x, app.world.y) && app.energico > 0
                && app.world.isType((int) app.world.x, (int) app.world.y, World.WATER)) {
            if (app.HP < app.MAXHP) {
                app.energico -= 15 + (5 - number) * 5;
                app.HP++;
            }
            if (number >= 0) {
                app.lungs = app.maxlungs;
            }
        }
    }

    @Override
    public int getCost() {
        return 0;
    }

    @Override
    public String getName() {
        return "Water Heal";
    }

    @Override
    public String getTip() {
        return "<html>A passive water spell<br>High Energy Cost<br>Heal when in water</html>";
    }

    @Override
    public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}

