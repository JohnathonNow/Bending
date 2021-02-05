
package com.johnwesthoff.bending.spells.fire;

import static com.johnwesthoff.bending.util.network.ResourceLoader.loadIcon;

import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.johnwesthoff.bending.Constants;
import com.johnwesthoff.bending.Session;
import com.johnwesthoff.bending.entity.BuritoEntity;
import com.johnwesthoff.bending.logic.World;
import com.johnwesthoff.bending.spells.Spell;
import com.johnwesthoff.bending.util.math.Ops;

public class Burito extends Spell {
    public Burito() {
        ID = Constants.FIREBENDING;
        subID = 9;
        locked = true;
        try {
            icon = (loadIcon("fireball.png"));
        } catch (Exception ex) {
            Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void getAction(Session app) {
        X = app.world.x;
        Y = app.world.y - Constants.HEAD;
        app.HP -= 15;
        mx = app.world.viewX;
        my = app.world.mouseY - app.world.viewY;
        double direction = 360 - Ops.pointDir(app.world.x - app.world.viewX,
                app.world.y - Constants.HEAD - app.world.viewY, app.world.mouseX, app.world.mouseY);
        mx = ((int) (Ops.lengthdir_x(12, direction)));
        my = ((int) (Ops.lengthdir_y(12, direction)));
        maker = ID;
        getMessage(app.out);
    }

    @Override
    public int getCoolDown() {
        return (int) (0 * Constants.FPS / 600);
    }

    @Override
    public String getName() {
        return "Burito";
    }

    @Override
    public String getTip() {
        return "<html>HAX<br>Moderate Health Cost</html>";
    }

    @Override
    public void getPassiveAction(Session app) {
    }

    @Override
    public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
        world.entityList.add(new BuritoEntity(px, py, mx, my, pid).setID(eid));
    }
}

