
package com.johnwesthoff.bending.spells.lightning;

import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;

import com.johnwesthoff.bending.Main;
import com.johnwesthoff.bending.Server;
import com.johnwesthoff.bending.entity.BallLightningEntity;
import com.johnwesthoff.bending.logic.World;
import com.johnwesthoff.bending.spells.Spell;
import com.johnwesthoff.bending.util.network.ResourceLoader;

public class LightningBall extends Lightning {
    public LightningBall() {
        ID = Server.LIGHTNING;
        subID = 2;
        locked = true;
        unlockXP = 750;
        try {
            icon = new ImageIcon(
                    ResourceLoader.loadImage("https://west-it.webs.com/spells/lightningball.png", "lightningball.png"));
        } catch (Exception ex) {
            Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void getAction(Main app) {
        // throw new UnsupportedOperationException("Not supported yet.");
        X = app.world.x;
        Y = app.world.y - World.head;
        mx = app.world.viewX;
        my = app.world.mouseY - app.world.viewY;
        double direction = 360 - Main.pointDir(app.world.x - app.world.viewX,
                app.world.y - World.head - app.world.viewY, app.world.mouseX, app.world.mouseY);
        // direction+=180;
        mx = ((int) (Main.lengthdir_x(8, direction)));
        my = ((int) (Main.lengthdir_y(8, direction)));
        maker = ID;
        app.HP -= 3;
        getMessage(app.out);
    }

    @Override
    public int getCost() {
        return 10;
    }

    @Override
    public String getName() {
        return "Lightning Ball";
    }

    @Override
    public void getPassiveAction(Main app) {
        // throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getTip() {
        return "<html>A basic lightning spell<br>Low Health Cost<br>Lightly affected by gravity<br>Deals low damage<br>High knockback</html>";
    }

    @Override
    public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
        world.entityList.add(new BallLightningEntity(px, py, mx, my, pid).setID(eid));
    }
}
