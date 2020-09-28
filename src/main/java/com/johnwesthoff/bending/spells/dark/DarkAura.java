
package com.johnwesthoff.bending.spells.dark;

import static com.johnwesthoff.bending.util.network.ResourceLoader.loadIcon;

import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.johnwesthoff.bending.Main;
import com.johnwesthoff.bending.Server;
import com.johnwesthoff.bending.logic.World;
import com.johnwesthoff.bending.spells.Spell;

public class DarkAura extends Spell {
    public DarkAura() {
        ID = Server.DARKNESS;
        subID = 2;
        locked = true;
        unlockXP = 3000;
        try {
            icon = loadIcon("https://west-it.webs.com/spells/aura.png");
        } catch (Exception ex) {
            Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void getAction(Main app) {
        app.removeAura = 40 * 5;
        app.world.status |= World.ST_DRAIN;
        app.sendMovement();
    }

    @Override
    public int getCost() {
        return 500;
    }

    @Override
    public String getName() {
        return "Dark Aura";
    }

    @Override
    public void getPassiveAction(Main app) {
    }

    @Override
    public String getTip() {
        return "<html>An intermediate darkness spell<br>High Energy Cost<br>For five seconds, nearby players are hurt</html>";
    }

    @Override
    public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

