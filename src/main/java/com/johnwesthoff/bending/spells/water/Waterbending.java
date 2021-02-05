
package com.johnwesthoff.bending.spells.water;

import static com.johnwesthoff.bending.util.network.ResourceLoader.loadIcon;

import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.johnwesthoff.bending.Constants;
import com.johnwesthoff.bending.Session;
import com.johnwesthoff.bending.entity.WaterBallEntity;
import com.johnwesthoff.bending.logic.World;
import com.johnwesthoff.bending.spells.Spell;
import com.johnwesthoff.bending.util.math.Ops;

public class Waterbending extends Spell {
    public Waterbending() {
        ID = Constants.WATERBENDING;
        subID = 0;
        try {
            icon = (loadIcon("waterSpell.png"));
        } catch (Exception ex) {
            Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void getAction(Session app) {
        // throw new UnsupportedOperationException("Not supported yet.");
        X = app.world.x;
        Y = app.world.y - Constants.HEAD;
        if (app.world.isLiquid(app.world.x, app.world.y)) {
            app.energico += 125;
        }
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
        return (int) (150 * Constants.FPS / 600);
    }

    @Override
    public String getName() {
        return "WaterBall";
    }

    @Override
    public String getTip() {
        return "<html>An essential water spell<br>Low Cooldown<br>Highly affected by gravity<br>Creates a pool of water</html>";
    }

    @Override
    public void getPassiveAction(Session app) {
        // throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
        System.out.format("%d %d\n", px, py);
        world.entityList.add(new WaterBallEntity(px, py, mx, my, pid).setID(eid));
    }
}

