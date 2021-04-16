package com.johnwesthoff.bending.networking.handlers;

import java.nio.ByteBuffer;

import com.johnwesthoff.bending.Constants;
import com.johnwesthoff.bending.Session;
import com.johnwesthoff.bending.logic.PlayerOnline;
import com.johnwesthoff.bending.networking.NetworkEvent;
import com.johnwesthoff.bending.util.network.NetworkMessage;

public class IdEvent implements NetworkEvent {
    public static final byte ID = 12;

    @Override
    public byte getId() {
        return ID;
    }

    @Override
    public void clientReceived(Session p, ByteBuffer message) {
        p.ID = message.getInt();
        p.serverVersion = message.getInt();
        if (p.serverVersion != Constants.VERSION) {
            p.clientui.immaKeepTabsOnYou.setSelectedIndex(5);
            p.clientui.bv.go();
        }
        p.world.ID = p.ID;
    }

    @Override
    public void serverReceived(PlayerOnline p, ByteBuffer message) {
        // TODO Auto-generated method stub

    }

    public static NetworkMessage getPacket(int id) {
        ByteBuffer bb = ByteBuffer.allocate(8);
        bb.putInt(id);
        bb.putInt(Constants.VERSION);
        return new NetworkMessage(bb, ID);
    }

}
