package com.johnwesthoff.bending.networking.handlers;

import java.nio.ByteBuffer;

import com.johnwesthoff.bending.Session;
import com.johnwesthoff.bending.logic.PlayerOnline;
import com.johnwesthoff.bending.networking.NetworkEvent;
import com.johnwesthoff.bending.util.network.NetworkMessage;

public class FillEvent implements NetworkEvent {
    public static final byte ID = 9;

    @Override
    public byte getId() {
        return ID;
    }

    @Override
    public void clientReceived(Session p, ByteBuffer toRead) {
        final int ix = toRead.getInt();
        final int iy = toRead.getInt();
        final int ir = toRead.getInt();
        final byte etg = toRead.get();
        p.world.ground.FillCircleW(ix, iy, ir, etg);

    }

    @Override
    public void serverReceived(PlayerOnline p, ByteBuffer toRead) {
        int ix = toRead.getInt();
        int iy = toRead.getInt();
        int ir = toRead.getInt();
        byte etg = toRead.get();
        p.handle.earth.ground.FillCircleW(ix, iy, ir, etg);
        p.handle.sendMessage(getPacket(ix, iy, ir, etg));

    }

    public static NetworkMessage getPacket(int ix, int iy, int ir, byte etg) {
        ByteBuffer toSend = ByteBuffer.allocate(13);
        toSend.putInt(ix).putInt(iy).putInt(ir).put(etg);
        return new NetworkMessage(toSend, ID);
    }

}
