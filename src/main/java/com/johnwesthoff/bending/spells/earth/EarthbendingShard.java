
package com.johnwesthoff.bending.spells.earth;

import static com.johnwesthoff.bending.util.network.ResourceLoader.loadIcon;

import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.johnwesthoff.bending.Client;
import com.johnwesthoff.bending.Server;
import com.johnwesthoff.bending.entity.ShardEntity;
import com.johnwesthoff.bending.logic.World;
import com.johnwesthoff.bending.spells.Spell;

public class EarthbendingShard extends Earthbending {
    public EarthbendingShard() {
        ID = Server.EARTHBENDING;
        subID = 2;
        try {
            icon = (loadIcon("https://west-it.webs.com/spells/earthShard.png"));
        } catch (Exception ex) {
            Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void getAction(Client app) {
        X = app.world.x;
        Y = app.world.y - World.head;
        mx = app.world.viewX;
        my = app.world.mouseY - app.world.viewY;
        double direction = 360 - Client.pointDir(app.world.x - app.world.viewX,
                app.world.y - World.head - app.world.viewY, app.world.mouseX, app.world.mouseY);
        // direction+=180;
        if (app.world.isSolid(app.world.x, app.world.y + 4)) {
            app.energico += 50;
        }
        mx = ((int) (Client.lengthdir_x(32, direction)));
        my = ((int) (Client.lengthdir_y(32, direction)));
        maker = ID;
        getMessage(app.out);
    }

    @Override
    public int getCost() {
        return 200;
    }

    @Override
    public String getName() {
        return "EarthShard";
    }

    @Override
    public String getTip() {
        return "<html>A basic earth spell<br>Low-Moderate Energy Cost<br>Travels quickly in a straight line<br>Deals moderate damage</html>";
    }

    @Override
    public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
        world.entityList.add(new ShardEntity(px, py, mx, my, pid).setID(eid));
    }
}

