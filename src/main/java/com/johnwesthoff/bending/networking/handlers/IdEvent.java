package com.johnwesthoff.bending.networking.handlers;

import java.nio.ByteBuffer;

import com.johnwesthoff.bending.Client;
import com.johnwesthoff.bending.logic.PlayerOnline;
import com.johnwesthoff.bending.networking.NetworkEvent;

public class IdEvent implements NetworkEvent {

    @Override
    public void clientReceived(Client p, ByteBuffer message) {
        p.ID = message.getInt();
        p.world.ID = p.ID;

    }

    @Override
    public void serverReceived(PlayerOnline p, ByteBuffer message) {
        // TODO Auto-generated method stub

    }
    
}
