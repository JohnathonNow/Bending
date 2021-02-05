package com.johnwesthoff.bending.spells.air;

import static com.johnwesthoff.bending.util.network.ResourceLoader.loadIcon;

import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.johnwesthoff.bending.Session;
import com.johnwesthoff.bending.Constants;
import com.johnwesthoff.bending.logic.World;
import com.johnwesthoff.bending.spells.Spell;

public class AirAffinity extends Airbending {
    boolean recharge = false;

    public AirAffinity() {
        ID = Constants.AIRBENDING;
        subID = 7;

        try {
            icon = (loadIcon("airfloat.png"));
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
            if (e instanceof Airbending) {
                number++;
            }
        }
    }

    @Override
    public void getPassiveAction(Session app) {
        app.maxlungs = 150 + (75 * number);
        app.world.floatiness = 2;
    }

    @Override
    public int getCoolDown() {
        return (int) (0 * Constants.FPS / 600);
    }

    @Override
    public String getName() {
        return "Air Affinity";
    }

    @Override
    public String getTip() {
        return "<html>A passive air spell<br>Fall more slowly<br>Have more lung capacity</html>";
    }

    @Override
    public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}