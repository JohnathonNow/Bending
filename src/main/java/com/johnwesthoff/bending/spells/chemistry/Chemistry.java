
package com.johnwesthoff.bending.spells.chemistry;

import static com.johnwesthoff.bending.util.network.ResourceLoader.loadIcon;

import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.johnwesthoff.bending.Constants;
import com.johnwesthoff.bending.Session;
import com.johnwesthoff.bending.entity.JuiceBallEntity;
import com.johnwesthoff.bending.logic.World;
import com.johnwesthoff.bending.spells.Spell;
import com.johnwesthoff.bending.util.math.Ops;

public class Chemistry extends Spell {
    public Chemistry() {
        ID = Constants.CHEMISTRY;
        subID = 0;
        try {
            icon = (loadIcon("healingjuice.png"));
        } catch (Exception ex) {
            Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void getAction(Session app) {
        // throw new UnsupportedOperationException("Not supported yet.");
        X = app.world.x;
        Y = app.world.y - World.head;
        mx = app.world.viewX;
        my = app.world.mouseY - app.world.viewY;
        double direction = 360 - Ops.pointDir(app.world.x - app.world.viewX,
                app.world.y - World.head - app.world.viewY, app.world.mouseX, app.world.mouseY);
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
        return "Healing Potion";
    }

    @Override
    public String getTip() {
        return "<html>A basic chemistry spell<br>Low-Moderate Energy Cost<br>Makes healing potion</html>";
    }

    @Override
    public void getPassiveAction(Session app) {
        // throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
        world.entityList.add(new JuiceBallEntity(px, py, mx, my, pid).setID(eid));
    }
}

