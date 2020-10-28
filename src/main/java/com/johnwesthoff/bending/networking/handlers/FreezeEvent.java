package com.johnwesthoff.bending.networking.handlers;

import java.nio.ByteBuffer;

import com.johnwesthoff.bending.Client;
import com.johnwesthoff.bending.logic.PlayerOnline;
import com.johnwesthoff.bending.networking.NetworkEvent;

public class FreezeEvent implements NetworkEvent {
    public static final byte ID = 10;

    @Override
    public byte getId() {
        return ID;
    }

    @Override
    public void clientReceived(Client p, ByteBuffer buf) {
        int fX = buf.getInt();
        int fY = buf.getInt();
        int fR = buf.getInt();
        p.world.ground.freeze(fX, fY, fR);

    }

    @Override
    public void serverReceived(PlayerOnline p, ByteBuffer message) {
        // TODO Auto-generated method stub

    }

}
