package com.johnwesthoff.bending.app.game;

import com.johnwesthoff.bending.util.network.ConnectToDatabase;

public class GameServiceFactory {

    /**
     * Create a GameService instance
     *
     * @return GameService
     */
    public static GameService create() {
        return new GameService(
            ConnectToDatabase.INSTANCE()
        );
    }

}
