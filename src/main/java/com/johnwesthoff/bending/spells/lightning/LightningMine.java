
package com.johnwesthoff.bending.spells.lightning;

import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;

import com.johnwesthoff.bending.Main;
import com.johnwesthoff.bending.Server;
import com.johnwesthoff.bending.entity.StaticShotEntity;
import com.johnwesthoff.bending.logic.World;
import com.johnwesthoff.bending.spells.Spell;
import com.johnwesthoff.bending.util.network.ResourceLoader;

public class LightningMine extends Lightning {
    public LightningMine() {
        ID = Server.LIGHTNING;
        subID = 3;
        locked = true;
        unlockXP = 1000;
        try {
            icon = new ImageIcon(
                    ResourceLoader.loadImage("https://west-it.webs.com/spells/lightningmine.png", "lightningmine.png"));
        } catch (Exception ex) {
            Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void getAction(Main app) {
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
        getMessage(app.out);
        app.HP -= 5;
    }

    @Override
    public int getCost() {
        return 100;
    }

    @Override
    public String getName() {
        return "Static Charge";
    }

    @Override
    public void getPassiveAction(Main app) {
    }

    @Override
    public String getTip() {
        return "<html>An intermediate lightning spell<br>Low Health Cost<br>Low Energy Cost<br>Creates a static charge<br>Deals low damage<br></html>";
    }

    @Override
    public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
        world.entityList.add(new StaticShotEntity(px, py, mx, my, pid).setID(eid));
    }
}

