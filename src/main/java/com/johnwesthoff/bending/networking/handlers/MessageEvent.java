package com.johnwesthoff.bending.networking.handlers;

import java.awt.Color;
import java.nio.ByteBuffer;

import com.johnwesthoff.bending.Client;
import com.johnwesthoff.bending.Server;
import com.johnwesthoff.bending.logic.PlayerOnline;
import com.johnwesthoff.bending.networking.NetworkEvent;

public class MessageEvent implements NetworkEvent {

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

}
