
package com.johnwesthoff.bending.spells.earth;

import static com.johnwesthoff.bending.util.network.ResourceLoader.loadIcon;

import java.awt.Polygon;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.johnwesthoff.bending.Constants;
import com.johnwesthoff.bending.Session;
import com.johnwesthoff.bending.logic.World;
import com.johnwesthoff.bending.spells.Spell;
import com.johnwesthoff.bending.util.math.Ops;

public class EarthbendingSpike extends Earthbending {
    public EarthbendingSpike() {
        ID = Constants.EARTHBENDING;
        subID = 1;
        try {
            icon = (loadIcon("earth.png"));
        } catch (Exception ex) {
            Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void getAction(Session app) {
        mx = app.world.mouseX + app.world.viewX;
        my = app.world.mouseY + app.world.viewY;
        X = app.world.pressX + app.world.viewX;
        Y = app.world.pressY + app.world.viewY;

        double direction = Ops.pointDir(mx, my, X, Y);
        if ((mx == X) && (my == Y)) {
            direction = 90;
        }

        mx = X - ((int) (Ops.lengthdir_x(72, direction)));
        my = Y + ((int) (Ops.lengthdir_y(72, direction)));
        maker = ID;
        if (app.world.countSolidCircle((int)X, (int)Y, 20) == 0 || !app.world.inBounds(mx, my) || !app.world.inBounds(mx - 56, my)
                || !app.world.inBounds(mx + 56, my)) {
            return;
        }
        getMessage(app.out);
    }

    @Override
    public int getCoolDown() {
        return (int) (450 * Constants.FPS / 600);
    }

    @Override
    public String getName() {
        return "EarthSpike";
    }

    @Override
    public String getTip() {
        return "<html>An intermediate earth spell<br>Moderate-High Cooldown<br>Summon a spike from the ground</html>";
    }

    @Override
    public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
        final Polygon P = new Polygon();
        double dir = Ops.pointDir(px, py, mx, my);
        P.addPoint(px - (int) Ops.lengthdir_x(20, dir + 90), py + (int) Ops.lengthdir_y(20, dir + 90));
        P.addPoint(mx, my);
        P.addPoint(px - (int) Ops.lengthdir_x(20, dir - 90), py + (int) Ops.lengthdir_y(20, dir - 90));
        world.ground.FillCircleW(px, py, 40, Constants.STONE);
        world.ground.FillPolygon(P, Constants.STONE);
    }
}
