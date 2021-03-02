package com.johnwesthoff.bending.networking.handlers;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.johnwesthoff.bending.Session;
import com.johnwesthoff.bending.logic.PlayerOnline;
import com.johnwesthoff.bending.networking.NetworkEvent;
import com.johnwesthoff.bending.util.network.NetworkMessage;

public class PingEvent implements NetworkEvent {
    public static final byte ID = 26;

    @Override
    public byte getId() {
        return ID;
    }

    @Override
    public void clientReceived(Session p, ByteBuffer reading) {
        try {
            p.out.addMessage(getPacket());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void serverReceived(PlayerOnline p, ByteBuffer toRead) {
        p.ping = System.currentTimeMillis() - p.pingSendTime;
    }

    public static NetworkMessage getPacket() {
        ByteBuffer toSend = ByteBuffer.allocate(4);
        return new NetworkMessage(toSend, ID);
    }
}
