package com.johnwesthoff.bending.app.avatar;

import com.johnwesthoff.bending.util.network.ConnectToDatabase;

public class AvatarService {

    private ConnectToDatabase connection;

    AvatarService (ConnectToDatabase connection) {
        this.connection = connection;
    }

    public void changesAppearance(String outfit, String username) {
        connection.postOutfit(outfit, username);
    }

    public void getAppearance(String username, String password) {
        connection.getOutfit(username, password);
    }
}
