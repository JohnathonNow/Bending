
package com.johnwesthoff.bending.spells.fire;

import static com.johnwesthoff.bending.util.network.ResourceLoader.loadIcon;

import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.johnwesthoff.bending.Constants;
import com.johnwesthoff.bending.Session;
import com.johnwesthoff.bending.entity.FlameThrowerEntity;
import com.johnwesthoff.bending.logic.World;
import com.johnwesthoff.bending.spells.Spell;
import com.johnwesthoff.bending.util.math.Ops;

public class Firebending_Thrower extends Firebending {
    public Firebending_Thrower() {
        ID = Constants.FIREBENDING;
        subID = 5;
        locked = true;
        unlockXP = 700;
        try {
            icon = (loadIcon("flamer.png"));
        } catch (Exception ex) {
            Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void getAction(Session app) {
        // throw new UnsupportedOperationException("Not supported yet.");
        X = app.world.x;
        Y = app.world.y - Constants.HEAD;
        mx = app.world.viewX;
        my = app.world.mouseY - app.world.viewY;
        double direction = Constants.FULL_ANGLE - Ops.pointDir(app.world.x - app.world.viewX,
                app.world.y - Constants.HEAD - app.world.viewY, app.world.mouseX, app.world.mouseY);
        // direction+=180;
        mx = ((int) (Ops.lengthdir_x(4, direction)));
        my = ((int) (Ops.lengthdir_y(4, direction)));
        maker = ID;
        getMessage(app.out);
    }

    @Override
    public int getCoolDown() {
        return (int) (250 * Constants.FPS / 600);
    }

    @Override
    public String getName() {
        return "Flame Thrower";
    }

    @Override
    public String getTip() {
        return "<html>A basic fire spell<br>Low-Moderate Energy Cost<br>Set your foes on fire!<br>Lightly effected by gravity</html>";
    }

    @Override
    public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
        world.entityList.add(new FlameThrowerEntity(px, py, mx, my, pid).setID(eid));
    }
}

