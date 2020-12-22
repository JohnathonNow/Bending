
package com.johnwesthoff.bending.spells.water;

import com.johnwesthoff.bending.Client;
import com.johnwesthoff.bending.Constants;
import com.johnwesthoff.bending.Server;
import com.johnwesthoff.bending.entity.FreezeEntity;
import com.johnwesthoff.bending.logic.World;
import com.johnwesthoff.bending.spells.Spell;

import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.johnwesthoff.bending.util.network.ResourceLoader.loadIcon;

public class WaterbendingFreeze extends Waterbending {
    public WaterbendingFreeze() {
        ID = Constants.WATERBENDING;
        subID = 1;
        locked = true;
        unlockXP = 400;
        try {
            icon = (loadIcon("https://west-it.webs.com/spells/freezeSpell.png"));
        } catch (Exception ex) {
            Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void getAction(Client app) {
        // throw new UnsupportedOperationException("Not supported yet.");
        X = app.world.x;
        Y = app.world.y - Constants.HEAD;
        if (app.world.isLiquid(app.world.x, app.world.y)) {
            app.energico += 100;
        }
        mx = app.world.viewX;
        my = app.world.mouseY - app.world.viewY;
        double direction = Constants.FULL_ANGLE - Client.pointDir(app.world.x - app.world.viewX,
                app.world.y - Constants.HEAD - app.world.viewY, app.world.mouseX, app.world.mouseY);
        // direction+=180;
        mx = ((int) (Client.lengthdir_x(12, direction)));
        my = ((int) (Client.lengthdir_y(12, direction)));
        maker = ID;
        getMessage(app.out);
    }

    @Override
    public int getCost() {
        return 250;
    }

    @Override
    public String getName() {
        return "Freeze";
    }

    @Override
    public String getTip() {
        return "<html>A basic water spell<br>Low-Moderate Energy Cost<br>Highly affected by gravity<br>Freezes nearby water</html>";
    }

    @Override
    public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
        world.entityList.add(new FreezeEntity(px, py, mx, my, pid).setID(eid));
    }
}

