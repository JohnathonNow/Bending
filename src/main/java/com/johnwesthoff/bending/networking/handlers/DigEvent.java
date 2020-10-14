package com.johnwesthoff.bending.networking.handlers;

import java.nio.ByteBuffer;

import com.johnwesthoff.bending.Client;
import com.johnwesthoff.bending.logic.PlayerOnline;
import com.johnwesthoff.bending.networking.NetworkEvent;

public class DigEvent implements NetworkEvent {

    @Override
    public void clientReceived(Client p, ByteBuffer toRead) {
        int ix, iy, ir;
        ix = toRead.getInt();
        iy = toRead.getInt();
        ir = toRead.getInt();
        p.world.ground.ClearCircle(ix, iy, ir);
    }

    @Override
    public void serverReceived(PlayerOnline p, ByteBuffer message) {
        // TODO Auto-generated method stub

    }

}
