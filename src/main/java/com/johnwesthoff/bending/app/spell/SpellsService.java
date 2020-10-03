package com.johnwesthoff.bending.app.spell;

import com.johnwesthoff.bending.util.network.ConnectToDatabase;

public class SpellsService {

    private ConnectToDatabase connection;

    SpellsService(ConnectToDatabase connection) {
        this.connection = connection;
    }


    public void persistSpellsForUser(String post, String username) {
        connection.postSpells(post, username);
    }

    public int[][] getSpellsFromUser(String username, String password) {
        return connection.getSpells(username, password);
    }
}
