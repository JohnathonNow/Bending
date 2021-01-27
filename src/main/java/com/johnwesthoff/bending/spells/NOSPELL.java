
package com.johnwesthoff.bending.spells;

import com.johnwesthoff.bending.Session;
import com.johnwesthoff.bending.logic.World;
import com.johnwesthoff.bending.util.network.ResourceLoader;

import javax.swing.*;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NOSPELL extends Spell {
    public NOSPELL() {
        try {
            icon = new ImageIcon(
                    ResourceLoader.loadImage("nospell.png"));
        } catch (Exception ex) {
            Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void getAction(Session app) {

    }

    @Override
    public int getCost() {
        return 0;
    }

    @Override
    public String getName() {
        return "None";
    }

    @Override
    public void getPassiveAction(Session app) {
    }

    @Override
    public String getTip() {
        return "<html>Nothing</html>";
    }

    @Override
    public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

