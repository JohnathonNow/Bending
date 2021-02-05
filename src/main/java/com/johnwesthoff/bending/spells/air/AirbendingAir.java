package com.johnwesthoff.bending.spells.air;

import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;

import com.johnwesthoff.bending.Constants;
import com.johnwesthoff.bending.Session;
import com.johnwesthoff.bending.logic.World;
import com.johnwesthoff.bending.spells.Spell;
import com.johnwesthoff.bending.util.math.Ops;
import com.johnwesthoff.bending.util.network.ResourceLoader;

public class AirbendingAir extends Airbending {
    public AirbendingAir() {
        ID = Constants.AIRBENDING;
        subID = 5;
        try {
            icon = new ImageIcon(ResourceLoader.loadImage("airAir.png"));
        } catch (Exception ex) {
            Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void getAction(Session app) {
        // throw new UnsupportedOperationException("Not supported yet.");
        double direction = 360 - Ops.pointDir(app.world.x - app.world.viewX,
                app.world.y - Constants.HEAD - app.world.viewY, app.world.mouseX, app.world.mouseY);
        // direction+=180;
        mx = ((int) (Ops.lengthdir_x(16, direction)));
        my = ((int) (Ops.lengthdir_y(16, direction))) - 8;
        X = app.world.x + mx;
        Y = app.world.y + my;
        maker = ID;
        getMessage(app.out);
    }

    @Override
    public int getCoolDown() {
        return (int) (75 * Constants.FPS / 600);
    }

    @Override
    public String getName() {
        return "Air Clear";
    }

    @Override
    public String getTip() {
        return "<html>A useful air spell<br>Very Low Energy Cost<br>Digs you out of tight spots</html>";
    }

    @Override
    public void getPassiveAction(Session app) {
        // throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
        world.ground.ClearCircle(px, py, 48);
    }
}
