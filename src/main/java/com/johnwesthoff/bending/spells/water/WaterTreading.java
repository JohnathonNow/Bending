
package com.johnwesthoff.bending.spells.water;

import static com.johnwesthoff.bending.util.network.ResourceLoader.loadIcon;

import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.johnwesthoff.bending.Session;
import com.johnwesthoff.bending.Constants;
import com.johnwesthoff.bending.logic.World;
import com.johnwesthoff.bending.spells.Spell;

public class WaterTreading extends Waterbending {
    public WaterTreading() {
        ID = Constants.WATERBENDING;
        subID = 23;
        try {
            icon = (loadIcon("waterswim.png"));
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
        app.swimmingSpeed = 1 + (0.23 * (double) (number));
        if (app.world.inBounds(app.world.x, app.world.y)
                && app.world.ground.cellData[(int) app.world.x][(int) app.world.y] == Constants.WATER) {
            app.lungs = app.maxlungs;
        }
    }

    @Override
    public int getCost() {
        return 0;
    }

    @Override
    public String getName() {
        return "Water Treader";
    }

    @Override
    public String getTip() {
        return "<html>A passive water spell<br>Move faster through fluids</html>";
    }

    @Override
    public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
