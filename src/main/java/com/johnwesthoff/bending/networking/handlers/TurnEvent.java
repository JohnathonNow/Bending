package com.johnwesthoff.bending.networking.handlers;

import java.nio.ByteBuffer;

import com.johnwesthoff.bending.Session;
import com.johnwesthoff.bending.logic.PlayerOnline;
import com.johnwesthoff.bending.networking.NetworkEvent;
import com.johnwesthoff.bending.util.network.NetworkMessage;

public class TurnEvent implements NetworkEvent {
    public static final byte ID = 24;

    @Override
    public byte getId() {
        return ID;
    }

    @Override
    public void clientReceived(Session p, ByteBuffer buf) {
        p.whoseTurn = buf.getInt();
        p.world.following = p.world.getPlayer(p.whoseTurn);
    }

    @Override
    public void serverReceived(PlayerOnline p, ByteBuffer message) {
        // TODO Auto-generated method stub

    }

    public static NetworkMessage getPacket(int turnId) {
        return new NetworkMessage(ByteBuffer.allocate(24).putInt(turnId), ID);
    }
}
