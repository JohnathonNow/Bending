package com.johnwesthoff.bending.networking.handlers;

import java.nio.ByteBuffer;

import com.johnwesthoff.bending.Client;
import com.johnwesthoff.bending.logic.PlayerOnline;
import com.johnwesthoff.bending.networking.NetworkEvent;

public class PuddleEvent implements NetworkEvent {
    public static final byte ID = 18;

    @Override
    public void clientReceived(Client p, ByteBuffer buf) {
        final int fX = buf.getInt();
        final int fY = buf.getInt();
        final int  fR = buf.getInt();
        p.world.ground.puddle(fX, fY, fR);

    }

    @Override
    public void serverReceived(PlayerOnline p, ByteBuffer message) {
        // TODO Auto-generated method stub

    }

}

