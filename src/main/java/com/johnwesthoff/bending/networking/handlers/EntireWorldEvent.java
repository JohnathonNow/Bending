package com.johnwesthoff.bending.networking.handlers;

import java.nio.ByteBuffer;

import com.johnwesthoff.bending.Client;
import com.johnwesthoff.bending.logic.PlayerOnline;
import com.johnwesthoff.bending.networking.NetworkEvent;

public class EntireWorldEvent implements NetworkEvent {
    public static final byte ID = 7;
    
    @Override
    public void clientReceived(Client p, ByteBuffer reading) {
        try {
            p.readWorld(reading);
        } catch (Exception e) {
            e.printStackTrace();
        }
        p.busy = false;

    }

    @Override
    public void serverReceived(PlayerOnline p, ByteBuffer message) {
        // TODO Auto-generated method stub

    }

}
