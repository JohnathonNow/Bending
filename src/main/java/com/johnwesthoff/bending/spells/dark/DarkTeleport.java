
package com.johnwesthoff.bending.spells.dark;

import static com.johnwesthoff.bending.util.network.ResourceLoader.loadIcon;

import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.johnwesthoff.bending.Client;
import com.johnwesthoff.bending.Server;
import com.johnwesthoff.bending.logic.World;
import com.johnwesthoff.bending.spells.Spell;

public class DarkTeleport extends Spell {
    public DarkTeleport() {
        ID = Server.DARKNESS;
        subID = 5;
        locked = true;
        unlockXP = 5000;
        try {
            icon = loadIcon("https://west-it.webs.com/spells/teleport.png");
        } catch (Exception ex) {
            Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void getAction(Client app) {
        mx = app.world.mouseX + app.world.viewX;
        my = app.world.mouseY + app.world.viewY;
        if (Client.pointDis(app.world.x, app.world.y, mx, my) < 600) {
            app.world.x = mx;
            app.world.y = my;
            app.sendMovement();
            app.energico = 0;
        } else {
            app.energico += getCost();
        }
    }

    @Override
    public int getCost() {
        return 1000;
    }

    @Override
    public String getName() {
        return "Dark Gate";
    }

    @Override
    public void getPassiveAction(Client app) {
    }

    @Override
    public String getTip() {
        return "<html>An intermediate darkness spell<br>Total Energy Cost<br>Teleport to a point of your choice</html>";
    }

    @Override
    public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

