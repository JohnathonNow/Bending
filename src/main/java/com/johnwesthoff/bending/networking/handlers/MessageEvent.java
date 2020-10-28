package com.johnwesthoff.bending.networking.handlers;

import java.awt.Color;
import java.nio.ByteBuffer;

import com.johnwesthoff.bending.Client;
import com.johnwesthoff.bending.Server;
import com.johnwesthoff.bending.logic.PlayerOnline;
import com.johnwesthoff.bending.networking.NetworkEvent;
import com.johnwesthoff.bending.util.network.NetworkMessage;

public class MessageEvent implements NetworkEvent {
    public static final byte ID = 16;

    @Override
    public void clientReceived(Client p, ByteBuffer gotten) {
        final int color = gotten.getInt();
        final String message = Server.getString(gotten);
        p.addChat(message, new Color(color));

    }

    @Override
    public void serverReceived(PlayerOnline p, ByteBuffer message) {
        // TODO Auto-generated method stub

    }

    public static NetworkMessage getPacket(int color, String message) {
        ByteBuffer bb = ByteBuffer.allocate(message.length() * 4 + 4);
        bb.putInt(color);
        Server.putString(bb, message);
        return new NetworkMessage(bb, ID);
    }

}
