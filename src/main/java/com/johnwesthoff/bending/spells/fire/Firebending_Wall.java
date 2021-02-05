
package com.johnwesthoff.bending.spells.fire;

import static com.johnwesthoff.bending.util.network.ResourceLoader.loadIcon;

import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.johnwesthoff.bending.Constants;
import com.johnwesthoff.bending.Session;
import com.johnwesthoff.bending.entity.WallofFireEntity;
import com.johnwesthoff.bending.logic.World;
import com.johnwesthoff.bending.spells.Spell;

public class Firebending_Wall extends Firebending {
    public Firebending_Wall() {
        ID = Constants.FIREBENDING;
        subID = 4;
        locked = true;
        unlockXP = 900;
        try {
            icon = (loadIcon("fireWall.png"));
        } catch (Exception ex) {
            Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void getAction(Session app) {
        // throw new UnsupportedOperationException("Not supported yet.");
        X = app.world.x;
        Y = app.world.y - Constants.HEAD;
        mx = app.world.viewX;
        my = app.world.mouseY - app.world.viewY;
        maker = ID;
        getMessage(app.out);
        app.world.leftArmAngle = 225;
        app.world.rightArmAngle = 315;
    }

    @Override
    public int getCoolDown() {
        return (int) (750 * Constants.FPS / 600);
    }

    @Override
    public String getName() {
        return "Fire Wall";
    }

    @Override
    public String getTip() {
        return "<html>An advanced fire spell<br>Very Very High Cooldown<br>Projects two separating columns of fire<br>Not effected by gravity</html>";
    }

    @Override
    public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
        world.entityList.add(new WallofFireEntity(px, py, 8, 0, pid).setID(eid));
        world.entityList.add(new WallofFireEntity(px, py, -8, 0, pid).setID(eid));
    }
}
