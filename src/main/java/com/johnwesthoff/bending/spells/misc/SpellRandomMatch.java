
package com.johnwesthoff.bending.spells.misc;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.johnwesthoff.bending.Client;
import com.johnwesthoff.bending.spells.Spell;

import static com.johnwesthoff.bending.util.network.ResourceLoader.loadIconBase;

public class SpellRandomMatch extends SpellRandom {
    public SpellRandomMatch() {
        super();
        try {
            icon = (loadIconBase("randomSpell1.png"));
        } catch (Exception ex) {
            Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getName() {
        return "Random (match)";
    }

    @Override
    public String getTip() {
        return "<html>Swapped by a random spell for each match</html>";
    }

    @Override
    public void getPassiveAction(Client app) {
        // throw new UnsupportedOperationException("Not supported yet.");
    }

}

