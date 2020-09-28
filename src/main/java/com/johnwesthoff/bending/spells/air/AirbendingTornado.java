package com.johnwesthoff.bending.spells.air;

import static com.johnwesthoff.bending.util.network.ResourceLoader.loadIcon;

import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.johnwesthoff.bending.Main;
import com.johnwesthoff.bending.Server;
import com.johnwesthoff.bending.entity.TornadoEntity;
import com.johnwesthoff.bending.logic.World;
import com.johnwesthoff.bending.spells.Spell;

public class AirbendingTornado extends Spell {
    public AirbendingTornado() {
        ID = Server.AIRBENDING;
        subID = 2;
        try {
            icon = (loadIcon("https://west-it.webs.com/spells/airTornado.png"));
        } catch (Exception ex) {
            Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void getAction(Main app) {
        // throw new UnsupportedOperationException("Not supported yet.");
        X = app.world.mouseX + app.world.viewX;
        Y = app.world.mouseY + app.world.viewY;
        mx = -Math.min(Math.max(((app.world.x - app.world.viewX) - app.world.mouseX) / 2, -2), 2);
        my = 0;
        getMessage(app.out);
    }

    @Override
    public int getCost() {
        return 500;
    }

    @Override
    public String getName() {
        return "Tornado";
    }

    @Override
    public String getTip() {
        return "<html>An intermediate air spell<br>High Energy Cost<br>Summons a tornado</html>";
    }

    @Override
    public void getPassiveAction(Main app) {
        // throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
        world.entityList.add(new TornadoEntity(px, py, mx, pid).setID(eid));
    }
}

