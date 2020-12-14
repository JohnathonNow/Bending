
package com.johnwesthoff.bending.spells.misc;

import com.johnwesthoff.bending.Client;

public class SpellRandomMatch extends SpellRandom {
    public SpellRandomMatch() {
        super();
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

