package com.johnwesthoff.bending.networking.handlers;

import java.nio.ByteBuffer;

import com.johnwesthoff.bending.Session;
import com.johnwesthoff.bending.logic.PlayerOnline;
import com.johnwesthoff.bending.networking.NetworkEvent;

public class HurtEvent implements NetworkEvent {
    public static final byte ID = 11;

    @Override
    public byte getId() {
        return ID;
    }

    @Override
    public void clientReceived(Session p, ByteBuffer toRead) {
        p.HP -= toRead.getInt();

    }

    @Override
    public void serverReceived(PlayerOnline p, ByteBuffer message) {
        // TODO Auto-generated method stub

    }

}
