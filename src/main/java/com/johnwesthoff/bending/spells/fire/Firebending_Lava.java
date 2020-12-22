
package com.johnwesthoff.bending.spells.fire;

import com.johnwesthoff.bending.Client;
import com.johnwesthoff.bending.Constants;
import com.johnwesthoff.bending.Server;
import com.johnwesthoff.bending.entity.LavaBallEntity;
import com.johnwesthoff.bending.logic.World;
import com.johnwesthoff.bending.spells.Spell;

import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.johnwesthoff.bending.util.network.ResourceLoader.loadIcon;

public class Firebending_Lava extends Firebending {
    public Firebending_Lava() {
        ID = Constants.FIREBENDING;
        subID = 1;
        locked = true;
        unlockXP = 800;
        try {
            icon = (loadIcon("https://west-it.webs.com/spells/lava.png"));
        } catch (Exception ex) {
            Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void getAction(Client app) {
        // throw new UnsupportedOperationException("Not supported yet.");
        X = app.world.x;
        Y = app.world.y - Constants.HEAD;
        if (app.world.isLiquid(X, Y)) {
            app.energico += 50;
        }
        mx = app.world.viewX;
        my = app.world.mouseY - app.world.viewY;
        double direction = Constants.FULL_ANGLE - Client.pointDir(app.world.x - app.world.viewX,
                app.world.y - Constants.HEAD - app.world.viewY, app.world.mouseX, app.world.mouseY);
        // direction+=180;
        mx = ((int) (Client.lengthdir_x(8, direction)));
        my = ((int) (Client.lengthdir_y(8, direction)));
        maker = ID;
        getMessage(app.out);
    }

    @Override
    public int getCost() {
        return 450;
    }

    @Override
    public String getName() {
        return "Lavaball";
    }

    @Override
    public String getTip() {
        return "<html>An intermediate fire spell<br>Moderate-High Energy Cost<br>Shoot a lava ball<br>Lightly effected by gravity<br>Creates a pool of lava</html>";
    }

    @Override
    public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
        world.entityList.add(new LavaBallEntity(px, py, mx, my, pid).setID(eid));
    }
}

