
package com.johnwesthoff.bending.spells.earth;

import static com.johnwesthoff.bending.util.network.ResourceLoader.loadIcon;

import java.awt.Polygon;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.johnwesthoff.bending.Client;
import com.johnwesthoff.bending.Server;
import com.johnwesthoff.bending.logic.World;
import com.johnwesthoff.bending.spells.Spell;

public class EarthbendingSpike extends Earthbending {
    public EarthbendingSpike() {
        ID = Server.EARTHBENDING;
        subID = 1;
        try {
            icon = (loadIcon("https://west-it.webs.com/spells/earth.png"));
        } catch (Exception ex) {
            Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void getAction(Client app) {
        mx = app.world.mouseX + app.world.viewX;
        my = app.world.mouseY + app.world.viewY;
        X = app.world.pressX + app.world.viewX;
        Y = app.world.pressY + app.world.viewY;

        double direction = Client.pointDir(mx, my, X, Y);
        if ((mx == X) && (my == Y)) {
            direction = 90;
        }

        mx = X - ((int) (Client.lengthdir_x(72, direction)));
        my = Y + ((int) (Client.lengthdir_y(72, direction)));
        maker = ID;
        if (!app.world.isSolid(X, Y) || Client.pointDis(app.world.x, app.world.y, X, Y) > 300
                || !app.world.inBounds(mx, my) || !app.world.inBounds(mx - 56, my)
                || !app.world.inBounds(mx + 56, my)) {
            app.energico += this.getCost();
            return;
        }
        if (app.world.isSolid(app.world.x, app.world.y + 4)) {
            app.energico += 150;
        }
        getMessage(app.out);
    }

    @Override
    public int getCost() {
        return 450;
    }

    @Override
    public String getName() {
        return "EarthSpike";
    }

    @Override
    public String getTip() {
        return "<html>An intermediate earth spell<br>Moderate-High Energy Cost<br>Summon a spike from the ground</html>";
    }

    @Override
    public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
        final Polygon P = new Polygon();
        P.addPoint(px + 28, py);
        P.addPoint(px - 28, py);
        P.addPoint(mx, my);
        world.ground.FillPolygon(P, World.STONE);
    }
}
