package com.johnwesthoff.bending.networking.handlers;

import java.nio.ByteBuffer;

import com.johnwesthoff.bending.Constants;
import com.johnwesthoff.bending.Session;
import com.johnwesthoff.bending.logic.PlayerOnline;
import com.johnwesthoff.bending.networking.NetworkEvent;
import com.johnwesthoff.bending.util.network.NetworkMessage;

public class PuddleEvent implements NetworkEvent {
    public static final byte ID = 18;

    @Override
    public byte getId() {
        return ID;
    }

    @Override
    public void clientReceived(Session p, ByteBuffer buf) {
        final int fX = buf.getInt();
        final int fY = buf.getInt();
        final int fR = buf.getInt();
        final int fT = buf.getInt();
        p.world.ground.puddle(fX, fY, fR, (byte)fT);

    }

    @Override
    public void serverReceived(PlayerOnline p, ByteBuffer message) {
        // TODO Auto-generated method stub

    }

    public static NetworkMessage getPacket(int x, int y, int r) {
        return getPacket(x, y, r, Constants.WATER);
    }

	public static NetworkMessage getPacket(int x, int y, int r, int type) {
        ByteBuffer b = ByteBuffer.allocate(40).putInt(x).putInt(y).putInt(r).putInt(type);
        return new NetworkMessage(b, ID);
	}
}
