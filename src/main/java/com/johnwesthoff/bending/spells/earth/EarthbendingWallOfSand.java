
package com.johnwesthoff.bending.spells.earth;

import static com.johnwesthoff.bending.util.network.ResourceLoader.loadIcon;

import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.johnwesthoff.bending.Client;
import com.johnwesthoff.bending.Server;
import com.johnwesthoff.bending.logic.World;
import com.johnwesthoff.bending.spells.Spell;

public class EarthbendingWallOfSand extends Earthbending {
    public EarthbendingWallOfSand() {
        ID = Server.EARTHBENDING;
        subID = 5;
        try {
            icon = (loadIcon("https://west-it.webs.com/spells/earthwos.png"));
        } catch (Exception ex) {
            Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void getAction(Client app) {
        // throw new UnsupportedOperationException("Not supported yet.");
        X = app.world.pressX + app.world.viewX;
        Y = app.world.pressY + app.world.viewY;
        mx = 12;
        my = 12;
        if (app.world.isSolid(app.world.x, app.world.y + 4)) {
            app.energico += 100;
        }
        if (app.world.inBounds((int) app.world.x, (int) (app.world.y - World.head + 4))
                && app.world.ground.cellData[(int) app.world.x][(int) app.world.y + 4] == World.SAND) {
            app.energico += 200;
        }
        maker = ID;
        getMessage(app.out);
    }

    @Override
    public int getCost() {
        return 425;
    }

    @Override
    public String getName() {
        return "Wall of Sand";
    }

    @Override
    public String getTip() {
        return "<html>A basic earth spell<br>Moderate-High Energy Cost<br>Creates sand where you need it</html>";
    }

    @Override
    public void getPassiveAction(Client app) {
        // throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
        world.ground.FillRectW(px - 12, py - 12, 24, 24, World.SAND);
    }
}

