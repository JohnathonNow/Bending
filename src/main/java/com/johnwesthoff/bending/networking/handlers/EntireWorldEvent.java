package com.johnwesthoff.bending.networking.handlers;

import java.nio.ByteBuffer;

import com.johnwesthoff.bending.Session;
import com.johnwesthoff.bending.logic.PlayerOnline;
import com.johnwesthoff.bending.networking.NetworkEvent;
import com.johnwesthoff.bending.util.network.NetworkMessage;

public class EntireWorldEvent implements NetworkEvent {
    public static final byte ID = 7;
    @Override
    public byte getId() {
        return ID;
    }
    
    @Override
    public void clientReceived(Session p, ByteBuffer reading) {
        try {
            p.net.readWorld(reading);
        } catch (Exception e) {
            e.printStackTrace();
        }
        p.busy = false;

    }

    @Override
    public void serverReceived(PlayerOnline p, ByteBuffer message) {
        // This is never sent to the server, only to the client
        throw new UnsupportedOperationException("Server should never receive entire world packet");
    }

    public static NetworkMessage getPacket(ByteBuffer bb) {
        return new NetworkMessage(bb, ID);
    }

}
