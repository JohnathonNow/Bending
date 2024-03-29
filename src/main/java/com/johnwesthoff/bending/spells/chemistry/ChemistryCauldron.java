
package com.johnwesthoff.bending.spells.chemistry;

import static com.johnwesthoff.bending.util.network.ResourceLoader.loadIcon;

import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.johnwesthoff.bending.Constants;
import com.johnwesthoff.bending.Session;
import com.johnwesthoff.bending.entity.CauldronBallEntity;
import com.johnwesthoff.bending.logic.World;
import com.johnwesthoff.bending.spells.Spell;
import com.johnwesthoff.bending.util.math.Ops;

public class ChemistryCauldron extends Chemistry {
    public ChemistryCauldron() {
        ID = Constants.CHEMISTRY;
        subID = 0;
        try {
            icon = (loadIcon("cauldron.png"));
        } catch (Exception ex) {
            Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void getAction(Session app) {
        X = app.world.x;
        Y = app.world.y - World.head;
        mx = app.world.viewX;
        my = app.world.mouseY - app.world.viewY;
        double direction = 360 - Ops.pointDir(app.world.x - app.world.viewX, app.world.y - World.head - app.world.viewY,
                app.world.mouseX, app.world.mouseY);
        mx = ((int) (Ops.lengthdir_x(12, direction)));
        my = ((int) (Ops.lengthdir_y(12, direction)));
        maker = ID;
        getMessage(app.out);
    }

    @Override
    public int getCoolDown() {
        return (int) (Constants.FPS);
    }

    @Override
    public String getName() {
        return "Cauldron";
    }

    @Override
    public String getTip() {
        return "<html>An intermediate chemistry spell<br>High Cooldown<br>Makes a stone bubble</html>";
    }

    @Override
    public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
        world.entityList.add(new CauldronBallEntity(px, py, mx, my, pid).setID(eid));
    }
}
