package com.johnwesthoff.bending.spells.air;

import static com.johnwesthoff.bending.util.network.ResourceLoader.loadIcon;

import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.johnwesthoff.bending.Constants;
import com.johnwesthoff.bending.Session;
import com.johnwesthoff.bending.entity.GustEntity;
import com.johnwesthoff.bending.logic.World;
import com.johnwesthoff.bending.spells.Spell;
import com.johnwesthoff.bending.util.math.Ops;

public class AirbendingGust extends Airbending {
    public AirbendingGust() {
        ID = Constants.AIRBENDING;
        try {
            icon = (loadIcon("airGust.png"));
        } catch (Exception ex) {
            Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void getAction(Session app) {
        ID = Constants.AIRBENDING;
        // throw new UnsupportedOperationException("Not supported yet.");
        mx = app.world.mouseX + app.world.viewX;
        my = app.world.mouseY + app.world.viewY;
        X = app.world.pressX + app.world.viewX;
        Y = app.world.pressY + app.world.viewY;
        if (app.world.isSolid(X, Y) || Ops.pointDis(app.world.x, app.world.y, X, Y) > 300) {
            return;
        }
        double direction = Ops.pointDir(mx, my, X, Y);
        // direction+=180;
        mx = -((int) (Ops.lengthdir_x(12, direction)));
        my = ((int) (Ops.lengthdir_y(12, direction)));
        maker = ID;
        getMessage(app.out);
    }

    @Override
    public int getCoolDown() {
        return (int) (200 * Constants.FPS / 600);
    }

    @Override
    public String getName() {
        return "AirGust";
    }

    @Override
    public String getTip() {
        return "<html>An advanced air spell<br>Low-Moderate Energy Cost<br>Summons a gust</html>";
    }

    @Override
    public void getPassiveAction(Session app) {
        // throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
        double dir = Ops.pointDir(0, 0, mx, my);
        double dis = Ops.pointDis(0, 0, mx, my);
        for (int i = -30; i <= 30; i+= 30) {
            mx = (int)Ops.lengthdir_x(dis, dir + i);
            my = -(int)Ops.lengthdir_y(dis, dir + i);
            world.entityList.add(new GustEntity(px, py, mx, my, pid).setID(eid));
        }
    }
}

