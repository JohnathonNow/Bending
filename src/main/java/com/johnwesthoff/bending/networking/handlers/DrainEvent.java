package com.johnwesthoff.bending.networking.handlers;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.johnwesthoff.bending.Client;
import com.johnwesthoff.bending.logic.PlayerOnline;
import com.johnwesthoff.bending.networking.NetworkEvent;
import com.johnwesthoff.bending.util.network.NetworkMessage;

public class DrainEvent implements NetworkEvent {
    public static final byte ID = 6;

    @Override
    public byte getId() {
        return ID;
    }

    @Override
    public void clientReceived(Client p, ByteBuffer buf) {
        buf.getInt(); //ignore the target for now
        final int hpt = buf.getInt();
        p.HP += hpt;

    }

    @Override
    public void serverReceived(PlayerOnline po, ByteBuffer buf) {
        int subID = buf.getInt();
        int hpt = buf.getInt();
        if (subID != ID) {
            for (PlayerOnline p : po.handle.playerList) {
                if (p.ID == subID) {
                    try {
                        p.out.addMessage(getPacket(subID, hpt));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static NetworkMessage getPacket(int target, int hpbuff) {
        ByteBuffer bb = ByteBuffer.allocate(8).putInt(target).putInt(hpbuff);
        return new NetworkMessage(bb, ID);
    }

}

