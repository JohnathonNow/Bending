
package com.johnwesthoff.bending.spells.water;

import static com.johnwesthoff.bending.util.network.ResourceLoader.loadIcon;

import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.johnwesthoff.bending.Constants;
import com.johnwesthoff.bending.Session;
import com.johnwesthoff.bending.entity.IceShardEntity;
import com.johnwesthoff.bending.logic.World;
import com.johnwesthoff.bending.spells.Spell;
import com.johnwesthoff.bending.util.math.Ops;

public class WaterbendingShard extends Waterbending {
    public WaterbendingShard() {
        ID = Constants.WATERBENDING;
        subID = 4;
        try {
            icon = (loadIcon("watershard.png"));
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
            app.energico += 50;
        }
        mx = app.world.viewX;
        my = app.world.mouseY - app.world.viewY;
        double direction = Constants.FULL_ANGLE - Ops.pointDir(app.world.x - app.world.viewX,
                app.world.y - Constants.HEAD - app.world.viewY, app.world.mouseX, app.world.mouseY);
        // direction+=180;
        mx = ((int) (Ops.lengthdir_x(9, direction)));
        my = ((int) (Ops.lengthdir_y(9, direction)));
        maker = ID;
        getMessage(app.out);
    }

    @Override
    public int getCoolDown() {
        return (int) (250 * Constants.FPS / 600);
    }

    @Override
    public String getName() {
        return "Ice Shard";
    }

    @Override
    public String getTip() {
        return "<html>An intermediate water spell<br>Low-Moderate Cooldown<br>Not affected by gravity<br>Deals light damage<br>Creates an ice block</html>";
    }

    @Override
    public void getPassiveAction(Session app) {
        // throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
        world.entityList.add(new IceShardEntity(px, py, mx, my, pid).setID(eid));
    }
}

