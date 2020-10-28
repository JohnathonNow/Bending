
package com.johnwesthoff.bending.spells.water;

import com.johnwesthoff.bending.Client;
import com.johnwesthoff.bending.Constants;
import com.johnwesthoff.bending.Server;
import com.johnwesthoff.bending.entity.SpoutSourceEntity;
import com.johnwesthoff.bending.logic.World;
import com.johnwesthoff.bending.spells.Spell;

import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.johnwesthoff.bending.util.network.ResourceLoader.loadIcon;

public class WaterSpout extends Waterbending {
    public WaterSpout() {
        ID = Constants.WATERBENDING;
        subID = 2;
        try {
            icon = (loadIcon("https://west-it.webs.com/spells/waterspout.png"));
        } catch (Exception ex) {
            Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void getAction(Client app) {
        // throw new UnsupportedOperationException("Not supported yet.");
        mx = app.world.mouseX + app.world.viewX;
        my = app.world.mouseY + app.world.viewY;
        X = mx;
        Y = my;
        if (app.world.ground.cellData[(int) X][(int) Y] != Constants.WATER) {
            app.energico += this.getCost();
            return;
        }
        if (app.world.isLiquid(app.world.x, app.world.y)) {
            app.energico += 150;
        }
        maker = ID;
        getMessage(app.out);
    }

    @Override
    public int getCost() {
        return 450;
    }

    @Override
    public String getName() {
        return "WaterSpout";
    }

    @Override
    public String getTip() {
        return "<html>An intermediate water spell<br>Moderate-High Energy Cost<br>Summon a waterspout from a water source</html>";
    }

    @Override
    public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
        world.entityList.add(new SpoutSourceEntity(px, py, 50, pid).setID(eid));
    }
}

