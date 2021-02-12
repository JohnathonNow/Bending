package com.johnwesthoff.bending.app.game;

import com.johnwesthoff.bending.Constants;
import com.johnwesthoff.bending.util.network.ConnectToDatabase;

public class Game {

    //@TODO : connection must be a part of a service or a repository, not a business object...
    private ConnectToDatabase connection;

    private String serverName;
    private String hostIp;
    private int port = Constants.PORT;

    Game(ConnectToDatabase connection) {
        this.connection = connection;
    }

    public ConnectToDatabase getConnection() {
        return connection;
    }

    public Game setServerName(String serverName) {
        this.serverName = serverName;

        return this;
    }

    public String getServerName() {
        return serverName;
    }

    public Game setHostIp(String hostIp) {
        this.hostIp = hostIp;

        return this;
    }

    public String getHostIp() {
        return hostIp;
    }

    public int getPort() {
        return port;
    }
}
