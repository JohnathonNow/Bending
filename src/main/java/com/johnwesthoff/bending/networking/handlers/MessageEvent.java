package com.johnwesthoff.bending.networking.handlers;

import java.awt.Color;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.johnwesthoff.bending.Server;
import com.johnwesthoff.bending.Session;
import com.johnwesthoff.bending.logic.PlayerOnline;
import com.johnwesthoff.bending.networking.NetworkEvent;
import com.johnwesthoff.bending.util.network.NetworkMessage;

public class MessageEvent implements NetworkEvent {
    public static final byte ID = 16;

    @Override
    public byte getId() {
        return ID;
    }

    @Override
    public void clientReceived(Session p, ByteBuffer gotten) {
        final int color = gotten.getInt();
        final String message = Server.getString(gotten);
        p.clientui.addChat(message, new Color(color));

    }

    @Override
    public void serverReceived(PlayerOnline p, ByteBuffer buf) {
        int color = buf.getInt();
        String yes = Server.getString(buf);
        if (yes.contains("/nextmap") && !p.voted) {
            p.handle.nextVote++;
            yes = (p.handle.nextVote
                    + ((p.handle.nextVote > 1 ? " players are " : " player is ") + "voting for ending the match."));
            p.voted = true;
            Logger.getLogger(getClass().getName()).log(Level.INFO, p.username + "[" + p.ID + "] voted to skip.");
        } else {
            Logger.getLogger(getClass().getName()).log(Level.INFO, p.username + "[" + p.ID + "]: " + yes);
        }
        p.handle.sendMessage(getPacket(color, yes));
        
    }

    public static NetworkMessage getPacket(int color, String message) {
        ByteBuffer bb = ByteBuffer.allocate(message.length() * 4 + 4);
        bb.putInt(color);
        Server.putString(bb, message);
        return new NetworkMessage(bb, ID);
    }

}
