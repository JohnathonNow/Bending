package com.johnwesthoff.bending.app.player;

import com.johnwesthoff.bending.util.network.ConnectToDatabase;

public class PlayerServiceFactory {

    /**
     * Create a PlayerService instance
     *
     * @return PlayerService
     */
    public static PlayerService create() {
        return new PlayerService(
            ConnectToDatabase.INSTANCE()
        );
    }

}
