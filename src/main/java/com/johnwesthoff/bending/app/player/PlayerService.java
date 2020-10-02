package com.johnwesthoff.bending.app.player;

import com.johnwesthoff.bending.util.network.ConnectToDatabase;

public class PlayerService {

    private ConnectToDatabase connection;

    private Player currentPlayer;

    PlayerService (ConnectToDatabase connection) {
        this.connection = connection;
    }

    public Player getCurrentPlayer() {
        //@TODO : enougth for now, but make strategy to retrieve player and memoize it
        return currentPlayer;
    }

    public void updatePlayerCount(String ip, int size) {
        connection.updateCount(ip, size);
    }

    public String sanitizeAndRegister(String username, String password, String email) {
        username = username.replaceAll("'", "\'");
        password = password.replaceAll("'", "\'");
        email = email.replaceAll("'", "\'");
        String verify = connection.uniqueID();

        if (connection.register(username, password, email, verify)) {
            return verify;
        }

        return null;
    }

    public boolean verify(String username) {
        return connection.verify(username);
    }
}
