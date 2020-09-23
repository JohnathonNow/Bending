
package com.johnwesthoff.bending.spells.dark;

import static com.johnwesthoff.bending.util.network.ResourceLoader.loadIcon;

import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.johnwesthoff.bending.Client;
import com.johnwesthoff.bending.Server;
import com.johnwesthoff.bending.logic.World;
import com.johnwesthoff.bending.spells.Spell;

public class DarkSummonBall extends Spell {
    public DarkSummonBall() {
        ID = Server.DARKNESS;
        subID = 2;
        locked = true;
        unlockXP = 3500;
        try {
            icon = loadIcon("https://west-it.webs.com/spells/shadowSummon.png");
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
        mx = ((int) (Client.lengthdir_x(8, direction)));
        my = ((int) (Client.lengthdir_y(8, direction)));
        maker = ID;
        getMessage(app.out);
    }

    @Override
    public int getCost() {
        return 555;
    }

    @Override
    public String getName() {
        return "Necromancy";
    }

    @Override
    public void getPassiveAction(Client app) {
    }

    @Override
    public String getTip() {
        return "<html>An intermediate darkness spell<br>High Energy Cost<br>Summon an undead minion</html>";
    }

    @Override
    public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

