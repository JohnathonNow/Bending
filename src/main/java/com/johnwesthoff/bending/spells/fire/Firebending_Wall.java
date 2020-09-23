
package com.johnwesthoff.bending.spells.fire;

import static com.johnwesthoff.bending.util.network.ResourceLoader.loadIcon;

import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.johnwesthoff.bending.Client;
import com.johnwesthoff.bending.Server;
import com.johnwesthoff.bending.logic.World;
import com.johnwesthoff.bending.spells.Spell;

public class Firebending_Wall extends Firebending {
    public Firebending_Wall() {
        ID = Server.FIREBENDING;
        subID = 4;
        try {
            icon = (loadIcon("https://west-it.webs.com/spells/fireWall.png"));
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
        maker = ID;
        getMessage(app.out);
        app.world.leftArmAngle = 225;
        app.world.rightArmAngle = 315;
    }

    @Override
    public int getCost() {
        return 750;
    }

    @Override
    public String getName() {
        return "Fire Wall";
    }

    @Override
    public String getTip() {
        return "<html>An advanced fire spell<br>Very Very High Energy Cost<br>Projects two separating columns of fire<br>Not effected by gravity</html>";
    }

    @Override
    public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

