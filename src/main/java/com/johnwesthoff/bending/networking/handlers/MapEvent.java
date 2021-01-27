package com.johnwesthoff.bending.networking.handlers;

import java.nio.ByteBuffer;

import com.johnwesthoff.bending.Session;
import com.johnwesthoff.bending.logic.PlayerOnline;
import com.johnwesthoff.bending.logic.World;
import com.johnwesthoff.bending.networking.NetworkEvent;
import com.johnwesthoff.bending.util.network.NetworkMessage;

public class MapEvent implements NetworkEvent {
    public static final byte ID = 15;

    @Override
    public byte getId() {
        return ID;
    }

    @Override
    public void clientReceived(Session p, ByteBuffer reading) {
        int x;
        x = reading.getInt();
        for (int i = x; i < x + 100; i++) {
            reading.get(p.world.ground.cellData[i]);
        }
        p.sendRequest = true;

    }

    @Override
    public void serverReceived(PlayerOnline p, ByteBuffer buf) {
        int viewX = Math.min(Math.max((int)p.x - 50, 0), p.handle.earth.wIdTh - 100);
        p.handle.sendMessage(getPacket(p.handle.earth, viewX));
    }

    public static NetworkMessage getPacket(World w, int viewX) {
        ByteBuffer toSend = ByteBuffer.allocate(300000);
        toSend.putInt(viewX);
        for (int i = viewX; i < viewX + 100; i++) {
            toSend.put(w.ground.cellData[i]);
        }
        return new NetworkMessage(toSend, ID);
    }

    public static NetworkMessage getPacketClient() {
        ByteBuffer toSend = ByteBuffer.allocate(1);
        return new NetworkMessage(toSend, ID);
    }

}
