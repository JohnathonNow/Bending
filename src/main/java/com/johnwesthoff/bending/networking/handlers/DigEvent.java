package com.johnwesthoff.bending.networking.handlers;

import java.nio.ByteBuffer;

import com.johnwesthoff.bending.Session;
import com.johnwesthoff.bending.logic.PlayerOnline;
import com.johnwesthoff.bending.networking.NetworkEvent;
import com.johnwesthoff.bending.util.network.NetworkMessage;

public class DigEvent implements NetworkEvent {
    public static final byte ID = 5;
    @Override
    public byte getId() {
        return ID;
    }

    @Override
    public void clientReceived(Session p, ByteBuffer toRead) {
        int ix, iy, ir;
        ix = toRead.getInt();
        iy = toRead.getInt();
        ir = toRead.getInt();
        p.world.ground.ClearCircle(ix, iy, ir);
    }

    @Override
    public void serverReceived(PlayerOnline p, ByteBuffer toRead) {
        int ix, iy, ir;
        ix = toRead.getInt();
        iy = toRead.getInt();
        ir = toRead.getInt();
        p.handle.earth.ground.ClearCircle(ix, iy, ir);
        p.handle.sendMessage(getPacket(ix, iy, ir));
    }

    public static NetworkMessage getPacket(int ix, int iy, int ir) {
        ByteBuffer toSend = ByteBuffer.allocate(12).putInt(ix).putInt(iy).putInt(ir);
        return new NetworkMessage(toSend, ID);
    }

}
