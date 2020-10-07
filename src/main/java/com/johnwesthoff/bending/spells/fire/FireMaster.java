
package com.johnwesthoff.bending.spells.fire;

import com.johnwesthoff.bending.Client;
import com.johnwesthoff.bending.Constants;
import com.johnwesthoff.bending.Server;
import com.johnwesthoff.bending.entity.FireDoom;
import com.johnwesthoff.bending.logic.World;
import com.johnwesthoff.bending.spells.Spell;

import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.johnwesthoff.bending.util.network.ResourceLoader.loadIcon;

public class FireMaster extends Spell {
    public FireMaster() {
        ID = Server.FIREBENDING;
        subID = 10;
        locked = true;
        try {
            icon = (loadIcon("https://west-it.webs.com/spells/fireball.png"));
        } catch (Exception ex) {
            Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void getAction(Client app) {
        X = app.world.x;
        Y = app.world.y - Constants.HEAD;
        if (app.world.isLiquid(X, Y)) {
            app.energico += 50;
        }
        mx = app.world.viewX;
        my = app.world.mouseY - app.world.viewY;
        double direction = 360 - Client.pointDir(app.world.x - app.world.viewX,
                app.world.y - Constants.HEAD - app.world.viewY, app.world.mouseX, app.world.mouseY);
        mx = ((int) (Client.lengthdir_x(12, direction)));
        my = ((int) (Client.lengthdir_y(12, direction)));
        maker = ID;
        getMessage(app.out);
    }

    @Override
    public int getCost() {
        return 100;
    }

    @Override
    public String getName() {
        return "Fire Master";
    }

    @Override
    public String getTip() {
        return "<html>HAX</html>";
    }

    @Override
    public void getPassiveAction(Client app) {
    }

    @Override
    public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
        world.entityList.add(new FireDoom(px, py, mx, my, pid).setID(eid));
    }
}