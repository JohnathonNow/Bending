
package com.johnwesthoff.bending.spells.earth;

import static com.johnwesthoff.bending.util.network.ResourceLoader.loadIcon;

import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.johnwesthoff.bending.Constants;
import com.johnwesthoff.bending.Session;
import com.johnwesthoff.bending.entity.RockEntity;
import com.johnwesthoff.bending.logic.World;
import com.johnwesthoff.bending.spells.Spell;
import com.johnwesthoff.bending.util.math.Ops;

public class Earthbending extends Spell {
    public Earthbending() {
        ID = Constants.EARTHBENDING;
        subID = 0;
        try {
            icon = (loadIcon("earthRock.png"));
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
        double direction = 360 - Ops.pointDir(app.world.x - app.world.viewX, app.world.y - app.world.viewY,
                app.world.mouseX, app.world.mouseY);
        // direction+=180;
        if (app.world.isSolid(app.world.x, app.world.y - Constants.HEAD + 1)) {
            app.energico += 50;
        }
        mx = ((int) (Ops.lengthdir_x(12, direction)));
        my = ((int) (Ops.lengthdir_y(12, direction)));
        maker = ID;
        getMessage(app.out);
    }

    @Override
    public int getCost() {
        return 150;
    }

    @Override
    public String getName() {
        return "EarthBall";
    }

    @Override
    public String getTip() {
        return "<html>A basic earth spell<br>Low Energy Cost<br>Highly affected by gravity<br>Deals moderate damage</html>";
    }

    @Override
    public void getPassiveAction(Session app) {
        // throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
        world.entityList.add(new RockEntity(px, py, mx, my, pid).setID(eid));
    }
}

