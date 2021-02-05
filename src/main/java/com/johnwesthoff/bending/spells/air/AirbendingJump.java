package com.johnwesthoff.bending.spells.air;

import static com.johnwesthoff.bending.util.network.ResourceLoader.loadIcon;

import java.awt.Color;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.johnwesthoff.bending.Session;
import com.johnwesthoff.bending.Constants;
import com.johnwesthoff.bending.entity.EffectEntity;
import com.johnwesthoff.bending.logic.World;
import com.johnwesthoff.bending.spells.Spell;

public class AirbendingJump extends Spell {
    public AirbendingJump() {
        ID = Constants.AIRBENDING;
        subID = 1;
        try {
            icon = (loadIcon("airJump.png"));
        } catch (Exception ex) {
            Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void getAction(Session app) {
        // throw new UnsupportedOperationException("Not supported yet.");
        X = app.world.x;
        Y = app.world.y;
        mx = (app.world.mouseX - app.world.viewX);
        my = (app.world.mouseY - app.world.viewY);
        app.xspeed = Math.min(Math.max(((app.world.x - app.world.viewX) - app.world.mouseX), -16), 16);
        app.world.vspeed = Math.min(Math.max(((app.world.y - app.world.viewY) - app.world.mouseY), -16), 16);
        my = (int) app.world.vspeed;
        app.world.keepMoving = false;
        mx = (int) app.xspeed;
        my = -my / 7;
        mx = -mx / 7;
        getMessage(app.out);
    }

    @Override
    public int getCoolDown() {
        return (int) (200 * Constants.FPS / 600);
    }

    @Override
    public String getName() {
        return "AirJump";
    }

    @Override
    public String getTip() {
        return "<html>An agile air spell<br>Low-Moderate Cooldown<br>Fly in a chosen direction</html>";
    }

    @Override
    public void getPassiveAction(Session app) {
        // throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
        world.entityList.add(new EffectEntity(px, py, mx, my, world.random.nextInt(40), Color.WHITE).setID(eid));
    }

}