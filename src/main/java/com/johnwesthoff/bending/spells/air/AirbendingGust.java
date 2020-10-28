package com.johnwesthoff.bending.spells.air;

import static com.johnwesthoff.bending.util.network.ResourceLoader.loadIcon;

import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.johnwesthoff.bending.Client;
import com.johnwesthoff.bending.Constants;
import com.johnwesthoff.bending.entity.GustEntity;
import com.johnwesthoff.bending.logic.World;
import com.johnwesthoff.bending.spells.Spell;

public class AirbendingGust extends Airbending {
    public AirbendingGust() {
        ID = Constants.AIRBENDING;
        try {
            icon = (loadIcon("https://west-it.webs.com/spells/airGust.png"));
        } catch (Exception ex) {
            Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void getAction(Client app) {
        ID = Constants.AIRBENDING;
        // throw new UnsupportedOperationException("Not supported yet.");
        mx = app.world.mouseX + app.world.viewX;
        my = app.world.mouseY + app.world.viewY;
        X = app.world.pressX + app.world.viewX;
        Y = app.world.pressY + app.world.viewY;
        if (app.world.isSolid(X, Y) || Client.pointDis(app.world.x, app.world.y, X, Y) > 300) {
            app.energico += this.getCost();
            return;
        }
        double direction = Client.pointDir(mx, my, X, Y);
        // direction+=180;
        mx = -((int) (Client.lengthdir_x(12, direction)));
        my = ((int) (Client.lengthdir_y(12, direction)));
        maker = ID;
        getMessage(app.out);
    }

    @Override
    public int getCost() {
        return 200;
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
    public void getPassiveAction(Client app) {
        // throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
        world.entityList.add(new GustEntity(px, py, mx, my, pid).setID(eid));
    }
}

