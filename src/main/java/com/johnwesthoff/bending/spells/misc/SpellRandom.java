
package com.johnwesthoff.bending.spells.misc;

import com.johnwesthoff.bending.Session;
import com.johnwesthoff.bending.Constants;
import com.johnwesthoff.bending.logic.World;
import com.johnwesthoff.bending.spells.Spell;

import java.nio.ByteBuffer;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.johnwesthoff.bending.util.network.ResourceLoader.loadIconBase;

public class SpellRandom extends Spell {
    Spell choices[];
    public SpellRandom() {
        ID = Constants.RANDOM;
        subID = 0;
        choices = new Spell[Constants.SPELL_SLOTS];
        try {
            icon = (loadIconBase("randomSpell2.png"));
        } catch (Exception ex) {
            Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void getAction(Session app) {
        throw new UnsupportedOperationException("This should never be called!");
    }

    @Override
    public int getCoolDown() {
        throw new UnsupportedOperationException("This should never be called!");
    }

    @Override
    public String getName() {
        return "Random (life)";
    }

    @Override
    public String getTip() {
        return "<html>Swapped by a random spell on each life</html>";
    }

    @Override
    public void getPassiveAction(Session app) {
        // throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
        throw new UnsupportedOperationException("This should never be called!");
    }
    

    public void setSpells() {
        Random r = new Random();
        for (int i = 0; i < Constants.SPELL_SLOTS; i++) {
            do {
                this.choices[i] = Spell.getSpell(r.nextInt(Spell.spells.size()));
            } while (this.choices[i] instanceof SpellRandom);
        }
    }

    /**
     * Gets the spell that results from this spell being cast from a given slot
     * 
     * @param slot the slot this spell is being called from
     * @return the spell to be used when the spell is in a given slot, which will be effectively random
     */
    public Spell getEffectiveSpell(int slot) {
        return this.choices[slot];
    }
}

