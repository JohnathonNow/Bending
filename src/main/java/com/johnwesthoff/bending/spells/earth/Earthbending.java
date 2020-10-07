
package com.johnwesthoff.bending.spells.earth;

import static com.johnwesthoff.bending.util.network.ResourceLoader.loadIcon;

import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.johnwesthoff.bending.Client;
import com.johnwesthoff.bending.Constants;
import com.johnwesthoff.bending.Server;
import com.johnwesthoff.bending.entity.RockEntity;
import com.johnwesthoff.bending.logic.World;
import com.johnwesthoff.bending.spells.Spell;

public class Earthbending extends Spell {
    public Earthbending() {
        ID = Server.EARTHBENDING;
        subID = 0;
        try {
            icon = (loadIcon("https://west-it.webs.com/spells/earthRock.png"));
        } catch (Exception ex) {
            Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void getAction(Client app) {
        // throw new UnsupportedOperationException("Not supported yet.");
        X = app.world.x;
        Y = app.world.y - Constants.HEAD;
        mx = app.world.viewX;
        my = app.world.mouseY - app.world.viewY;
        double direction = 360 - Client.pointDir(app.world.x - app.world.viewX, app.world.y - app.world.viewY,
                app.world.mouseX, app.world.mouseY);
        // direction+=180;
        if (app.world.isSolid(app.world.x, app.world.y - Constants.HEAD + 1)) {
            app.energico += 50;
        }
        mx = ((int) (Client.lengthdir_x(12, direction)));
        my = ((int) (Client.lengthdir_y(12, direction)));
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
    public void getPassiveAction(Client app) {
        // throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
        world.entityList.add(new RockEntity(px, py, mx, my, pid).setID(eid));
    }
}

