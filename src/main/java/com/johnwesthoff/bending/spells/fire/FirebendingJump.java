
package com.johnwesthoff.bending.spells.fire;

import static com.johnwesthoff.bending.util.network.ResourceLoader.loadIcon;

import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.johnwesthoff.bending.Constants;
import com.johnwesthoff.bending.Session;
import com.johnwesthoff.bending.entity.FireJumpEntity;
import com.johnwesthoff.bending.logic.World;
import com.johnwesthoff.bending.spells.Spell;

public class FirebendingJump extends Spell {
    public FirebendingJump() {
        ID = Constants.FIREBENDING;
        subID = 2;
        locked = true;
        unlockXP = 1000;
        try {
            icon = (loadIcon("firejump.png"));
        } catch (Exception ex) {
            Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void getAction(Session app) {
        // throw new UnsupportedOperationException("Not supported yet.");
        X = app.world.x;
        Y = app.world.y - Constants.HEAD;
        mx = (app.world.mouseX - app.world.viewX);
        my = (app.world.mouseY - app.world.viewY);
        app.xspeed = -Math.min(Math.max(((app.world.x - app.world.viewX) - app.world.mouseX), -16), 16);
        app.world.vspeed = -Math.min(Math.max(((app.world.y - Constants.HEAD - app.world.viewY) - app.world.mouseY), -16),
                16);
        my = (int) app.world.vspeed;
        app.world.keepMoving = false;
        mx = (int) app.xspeed;
        // my=-my;mx=-mx;
        getMessage(app.out);
    }

    @Override
    public int getCost() {
        return 400;
    }

    @Override
    public String getName() {
        return "FireJump";
    }

    @Override
    public String getTip() {
        return "<html>An agile fire spell<br>Moderate-High Energy Cost<br>Fly in a chosen direction<br>Burn those in your path</html>";
    }

    @Override
    public void getPassiveAction(Session app) {
        // throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
        world.entityList.add(new FireJumpEntity(px, py, mx, my, pid).setID(eid));
    }
}

