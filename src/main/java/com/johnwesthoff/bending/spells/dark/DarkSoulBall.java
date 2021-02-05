
package com.johnwesthoff.bending.spells.dark;

import static com.johnwesthoff.bending.util.network.ResourceLoader.loadIcon;

import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.johnwesthoff.bending.Constants;
import com.johnwesthoff.bending.Session;
import com.johnwesthoff.bending.entity.SoulDrainEntity;
import com.johnwesthoff.bending.logic.World;
import com.johnwesthoff.bending.spells.Spell;
import com.johnwesthoff.bending.util.math.Ops;

public class DarkSoulBall extends Spell {
    public DarkSoulBall() {
        ID = Constants.DARKNESS;
        subID = 1;
        locked = true;
        unlockXP = 2500;
        try {
            icon = loadIcon("shadowBall.png");
        } catch (Exception ex) {
            Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void getAction(Session app) {
        X = app.world.x;
        Y = app.world.y - Constants.HEAD;
        mx = app.world.viewX;
        my = app.world.mouseY - app.world.viewY;
        double direction = Constants.FULL_ANGLE - Ops.pointDir(app.world.x - app.world.viewX,
                app.world.y - Constants.HEAD - app.world.viewY, app.world.mouseX, app.world.mouseY);
        // direction+=180;
        mx = ((int) (Ops.lengthdir_x(8, direction)));
        my = ((int) (Ops.lengthdir_y(8, direction)));
        maker = ID;
        getMessage(app.out);
    }

    @Override
    public int getCoolDown() {
        return (int) (400 * Constants.FPS / 600);
    }

    @Override
    public String getName() {
        return "Soul Sucker";
    }

    @Override
    public void getPassiveAction(Session app) {
    }

    @Override
    public String getTip() {
        return "<html>A basic darkness spell<br>Moderate-High Energy Cost<br>Steal the health of your foe<br>Unaffected by gravity</html>";
    }

    @Override
    public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
        world.entityList.add(new SoulDrainEntity(px, py, mx, my, pid).setID(eid));
    }
}

