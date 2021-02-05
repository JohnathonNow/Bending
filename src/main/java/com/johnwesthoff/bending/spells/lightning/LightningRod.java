
package com.johnwesthoff.bending.spells.lightning;

import com.johnwesthoff.bending.Session;
import com.johnwesthoff.bending.Constants;
import com.johnwesthoff.bending.Server;
import com.johnwesthoff.bending.entity.RodEntity;
import com.johnwesthoff.bending.logic.World;
import com.johnwesthoff.bending.spells.Spell;

import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.johnwesthoff.bending.util.network.ResourceLoader.loadIcon;

public class LightningRod extends Lightning {
    public LightningRod() {
        ID = Constants.LIGHTNING;
        subID = 5;
        locked = true;
        unlockXP = 1250;
        try {
            icon = loadIcon("shocktower.png");
        } catch (Exception ex) {
            Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void getAction(Session app) {

        X = app.world.x;
        Y = app.world.y - Constants.HEAD;
        mx = 0;
        my = 0;
        maker = ID;
        getMessage(app.out);
    }

    @Override
    public int getCoolDown() {
        return (int) (200 * Constants.FPS / 600);
    }

    @Override
    public String getName() {
        return "Lightning Rod";
    }

    @Override
    public void getPassiveAction(Session app) {
    }

    @Override
    public String getTip() {
        return "<html>An intermediate lightning spell<br>Low-Moderate Cooldown<br>Strongly affected by gravity<br>Drains enemy energy</html>";
    }

    @Override
    public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
        world.entityList.add(new RodEntity(px, py, mx, pid).setID(eid));
    }
}