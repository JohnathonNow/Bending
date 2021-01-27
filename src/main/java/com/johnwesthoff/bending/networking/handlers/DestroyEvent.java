package com.johnwesthoff.bending.networking.handlers;

import java.nio.ByteBuffer;

import com.johnwesthoff.bending.Session;
import com.johnwesthoff.bending.entity.Entity;
import com.johnwesthoff.bending.logic.PlayerOnline;
import com.johnwesthoff.bending.networking.NetworkEvent;
import com.johnwesthoff.bending.util.network.NetworkMessage;

public class DestroyEvent implements NetworkEvent {
    public static final byte ID = 4;
    @Override
    public byte getId() {
        return ID;
    }
    
    @Override
    public void clientReceived(Session p, ByteBuffer buf) {
        int idtokill = buf.getInt();
        for (int i = 0; i < p.world.entityList.size(); i++) {
            if (p.world.entityList.get(i).MYID == idtokill) {
                p.world.entityList.remove(i);
                continue;
            }
        }

    }

    @Override
    public void serverReceived(PlayerOnline p, ByteBuffer message) {
        // TODO Auto-generated method stub

    }

    public static NetworkMessage getPacket(Entity e) {
        ByteBuffer bb = ByteBuffer.allocate(8);
        bb.putInt(e.MYID);
        return new NetworkMessage(bb, ID);
    }

}

