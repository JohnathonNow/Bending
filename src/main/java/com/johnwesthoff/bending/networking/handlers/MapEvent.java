package com.johnwesthoff.bending.networking.handlers;

import java.nio.ByteBuffer;

import com.johnwesthoff.bending.Client;
import com.johnwesthoff.bending.logic.PlayerOnline;
import com.johnwesthoff.bending.networking.NetworkEvent;

public class MapEvent implements NetworkEvent {
    public static final byte ID = 15;

    @Override
    public void clientReceived(Client p, ByteBuffer reading) {
        int x;
        x = reading.getInt();
        for (int i = x; i < x + 100; i++) {
            reading.get(p.world.ground.cellData[i]);
        }
        p.sendRequest = true;

    }

    @Override
    public void serverReceived(PlayerOnline p, ByteBuffer message) {
        // TODO Auto-generated method stub

    }
    
}
