package com.johnwesthoff.bending.app.game;

import com.johnwesthoff.bending.util.network.ConnectToDatabase;

import java.util.ArrayList;

public class GameService {

    public static final String DEFAULT_SERVER_NAME = "SERVER";

    private final Game game;

    GameService(
        ConnectToDatabase connection
    ) {
        game = new Game(connection);
    }

    public boolean login(String username, String password) {
        return game.getConnection()
            .logIn(username, password);
    }

    public int getPlayerExperience(String username, String password) {
        return game.getConnection()
                .getXP(username, password);
    }

    public void earnExperience(int xp, String username) {
        game.getConnection()
            .postXP(xp, username);
    }

    /**
     * @TODO : what is an "Unlock"?
     * @param username
     * @param password
     */
    public void getUnlocks(String username, String password) {
        game.getConnection()
            .getUnlocks(username, password);
    }

    public void postUnlocks(String username) {
        game.getConnection()
            .postUnlocks(username);
    }

    public void feedRss(String title, String description) {
        game.getConnection()
            .postRSSfeed(title, description);
    }

    public void tryToCreateServer(String serverName, String ip) {
        game.setServerName(
                serverName.length() > 0 ? serverName : DEFAULT_SERVER_NAME
            )
            .setHostIp(ip);

        game.getConnection()
            .addServer(
                game.getServerName(),
                game.getHostIp()
            );
    }

    public void tryToRemoveServer(String hostIP) {
        if (!"".equals(hostIP)) {
            game.getConnection()
                .removeServer(hostIP);
        }
    }

    public ArrayList<String> retrieveServers() {
        return game.getConnection()
                    .getServers();
    }

    public String getServerName() {
        return game.getServerName();
    }

    public String getHostIp() {
        return game.getHostIp();
    }

    public String getPort() {
        return Integer.toString(
                game.getPort()
        );
    }
}
