
package com.johnwesthoff.bending.spells.water;

import java.nio.ByteBuffer;

import javax.swing.ImageIcon;

import com.johnwesthoff.bending.Client;
import com.johnwesthoff.bending.Constants;
import com.johnwesthoff.bending.entity.RainEntity;
import com.johnwesthoff.bending.logic.World;
import com.johnwesthoff.bending.util.network.ResourceLoader;

public class WaterStorm extends Waterbending {
    public WaterStorm() {
        ID = Constants.WATERBENDING;
        subID = 6;
        locked = true;
        unlockXP = 600;
        icon = new ImageIcon(ResourceLoader.loadImage("waterStorm.png"));
    }

    @Override
    public void getAction(Client app) {
        // throw new UnsupportedOperationException("Not supported yet.");
        mx = app.world.mouseX + app.world.viewX;
        my = app.world.mouseY + app.world.viewY;
        X = mx;
        Y = my;
        if (app.world.isLiquid(app.world.x, app.world.y)) {
            app.energico += 150;
        }
        maker = ID;
        getMessage(app.out);
    }

    @Override
    public int getCost() {
        return 656;
    }

    @Override
    public String getName() {
        return "Storm";
    }

    @Override
    public String getTip() {
        return "<html>An intermediate water spell<br>Very High Energy Cost<br>Summon a storm cloud</html>";
    }

    @Override
    public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
        world.entityList.add(new RainEntity(px, py, pid).setID(eid));
    }
}
