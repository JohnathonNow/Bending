package com.johnwesthoff.bending.app.spell;

import com.johnwesthoff.bending.util.network.ConnectToDatabase;

public class SpellsServiceFactory {

    /**
     * Create a SpellsService instance
     *
     * @return SpellsService
     */
    public static SpellsService create() {
        return new SpellsService(
            ConnectToDatabase.INSTANCE()
        );
    }
}
