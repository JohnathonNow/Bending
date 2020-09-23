
package com.johnwesthoff.bending.spells.earth;

import static com.johnwesthoff.bending.util.network.ResourceLoader.loadIcon;

import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.johnwesthoff.bending.Client;
import com.johnwesthoff.bending.Server;
import com.johnwesthoff.bending.entity.SandEntity;
import com.johnwesthoff.bending.logic.World;
import com.johnwesthoff.bending.spells.Spell;

public class EarthbendingSand extends Earthbending {
    public EarthbendingSand() {
        ID = Server.EARTHBENDING;
        subID = 4;
        try {
            icon = (loadIcon("https://west-it.webs.com/spells/earthSand.png"));
        } catch (Exception ex) {
            Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void getAction(Client app) {
        // throw new UnsupportedOperationException("Not supported yet.");
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
        mx = ((int) (Client.lengthdir_x(18, direction)));
        my = ((int) (Client.lengthdir_y(18, direction)));
        maker = ID;
        getMessage(app.out);
    }

    @Override
    public int getCost() {
        return 325;
    }

    @Override
    public String getName() {
        return "Sand Shotgun";
    }

    @Override
    public String getTip() {
        return "<html>An advanced earth spell<br>Moderate Energy Cost<br>Highly affected by gravity<br>Deals High damage</html>";
    }

    @Override
    public void getPassiveAction(Client app) {
        // throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
        world.ground.sandinate(px, py, 96);
        final int number = buf.getInt();
        world.entityList.add(new SandEntity(px, py, mx, my, pid).setID(eid));
        if (number > 3) {
            world.entityList.add(new SandEntity(px, py, mx + (int) Client.lengthdir_x(4, 30),
                    my + (int) Client.lengthdir_y(4, 30), pid).setID(eid + 1));
            world.entityList.add(new SandEntity(px, py, mx + (int) Client.lengthdir_x(4, -30),
                    my + (int) Client.lengthdir_y(4, -30), pid).setID(eid + 2));
        }
        if (number > 5) {
            world.entityList.add(new SandEntity(px, py, mx + (int) Client.lengthdir_x(4, 45),
                    my + (int) Client.lengthdir_y(4, 45), pid).setID(eid + 3));
            world.entityList.add(new SandEntity(px, py, mx + (int) Client.lengthdir_x(4, -45),
                    my + (int) Client.lengthdir_y(4, -45), pid).setID(eid + 4));
        }
        if (number > 7) {
            world.entityList.add(new SandEntity(px, py, mx + (int) Client.lengthdir_x(4, 60),
                    my + (int) Client.lengthdir_y(4, 60), pid).setID(eid + 5));
            world.entityList.add(new SandEntity(px, py, mx + (int) Client.lengthdir_x(4, -60),
                    my + (int) Client.lengthdir_y(4, -60), pid).setID(eid + 6));
        }
        if (number > 12) {
            world.entityList.add(new SandEntity(px, py, mx + (int) Client.lengthdir_x(4, 15),
                    my + (int) Client.lengthdir_y(4, 15), pid).setID(eid + 7));
            world.entityList.add(new SandEntity(px, py, mx + (int) Client.lengthdir_x(4, -15),
                    my + (int) Client.lengthdir_y(4, -15), pid).setID(eid + 8));
        }
        if (number > 16) {
            world.entityList.add(new SandEntity(px, py, mx + (int) Client.lengthdir_x(4, 35),
                    my + (int) Client.lengthdir_y(4, 35), pid).setID(eid + 9));
            world.entityList.add(new SandEntity(px, py, mx + (int) Client.lengthdir_x(4, -35),
                    my + (int) Client.lengthdir_y(4, -35), pid).setID(eid + 10));
        }
        if (number > 20) {
            world.entityList.add(new SandEntity(px, py, mx + (int) Client.lengthdir_x(4, 45),
                    my + (int) Client.lengthdir_y(4, 45), pid).setID(eid + 11));
            world.entityList.add(new SandEntity(px, py, mx + (int) Client.lengthdir_x(4, -45),
                    my + (int) Client.lengthdir_y(4, -45), pid).setID(eid + 12));
        }
    }
}
