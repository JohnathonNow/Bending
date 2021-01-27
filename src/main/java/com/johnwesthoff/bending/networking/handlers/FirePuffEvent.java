package com.johnwesthoff.bending.networking.handlers;

import java.nio.ByteBuffer;

import com.johnwesthoff.bending.Session;
import com.johnwesthoff.bending.entity.FirePuffEntity;
import com.johnwesthoff.bending.logic.PlayerOnline;
import com.johnwesthoff.bending.networking.NetworkEvent;
import com.johnwesthoff.bending.util.network.NetworkMessage;

public class FirePuffEvent implements NetworkEvent {
    public static final byte ID = 23;

    @Override
    public byte getId() {
        return ID;
    }

    @Override
    public void clientReceived(Session p, ByteBuffer buf) {
        int px = buf.getInt();
        int py = buf.getInt();
        int mx = buf.getInt();
        int my = buf.getInt();
        int pid = buf.getInt();
        int eid = buf.getInt();
        p.world.entityList.add(new FirePuffEntity(px, py, mx, my, pid).setID(eid));
    }

    @Override
    public void serverReceived(PlayerOnline p, ByteBuffer buf) {
        throw new UnsupportedOperationException("Not available on the server!");
    }

    public static NetworkMessage getPacket(int x, int y, int mx, int my, int pid, int eid) {
        ByteBuffer bb = ByteBuffer.allocate(32);
        bb.putInt(x).putInt(y).putInt(mx).putInt(my).putInt(pid).putInt(eid);
        return new NetworkMessage(bb, ID);
    }

}
