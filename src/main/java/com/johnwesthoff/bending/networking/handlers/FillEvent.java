package com.johnwesthoff.bending.networking.handlers;

import java.nio.ByteBuffer;

import com.johnwesthoff.bending.Client;
import com.johnwesthoff.bending.logic.PlayerOnline;
import com.johnwesthoff.bending.networking.NetworkEvent;

public class FillEvent implements NetworkEvent {
    public static final byte ID = 9;

    @Override
    public void clientReceived(Client p, ByteBuffer toRead) {
        final int ix = toRead.getInt();
        final int iy = toRead.getInt();
        final int ir = toRead.getInt();
        final byte etg = toRead.get();
        p.world.ground.FillCircleW(ix, iy, ir, etg);

    }

    @Override
    public void serverReceived(PlayerOnline p, ByteBuffer message) {
        // TODO Auto-generated method stub

    }

}
