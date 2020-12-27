package com.johnwesthoff.bending.spells.air;

import static com.johnwesthoff.bending.util.network.ResourceLoader.loadIcon;

import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.johnwesthoff.bending.Client;
import com.johnwesthoff.bending.Constants;
import com.johnwesthoff.bending.logic.World;
import com.johnwesthoff.bending.spells.Spell;

public class AirRun extends Airbending {
    boolean recharge = false;

    public AirRun() {
        ID = Constants.AIRBENDING;
        subID = 3;

        try {
            icon = (loadIcon("airrun.png"));
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
        number = 0;
        for (Spell e : me.spellList[me.spellBook]) {
            if (e instanceof Airbending) {
                number++;
            }
        }
    }

    @Override
    public void getPassiveAction(Client app) {
        Client.runningSpeed = 1 + (0.19 * (double) (number));
    }

    @Override
    public int getCost() {
        return 0;
    }

    @Override
    public String getName() {
        return "Air Run";
    }

    @Override
    public String getTip() {
        return "<html>A passive air spell<br>Move faster; Jump higher</html>";
    }

    @Override
    public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
