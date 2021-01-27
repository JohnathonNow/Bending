package com.johnwesthoff.bending.networking.handlers;

import java.nio.ByteBuffer;

import com.johnwesthoff.bending.Session;
import com.johnwesthoff.bending.entity.SteamEntity;
import com.johnwesthoff.bending.logic.PlayerOnline;
import com.johnwesthoff.bending.networking.NetworkEvent;
import com.johnwesthoff.bending.util.network.NetworkMessage;

public class SteamEvent implements NetworkEvent {
    public static final byte ID = 22;

    @Override
    public byte getId() {
        return ID;
    }

    @Override
    public void clientReceived(Session p, ByteBuffer buf) {
        final int xxxx = buf.getInt(), yyyy = buf.getInt();
        final int idtokill = buf.getInt();
        for (int i = 0; i < p.world.entityList.size(); i++) {
            if (p.world.entityList.get(i).MYID == idtokill) {
                p.world.entityList.remove(i);
                continue;
            }
        }
        p.world.entityList.add(new SteamEntity(xxxx, yyyy));

    }

    @Override
    public void serverReceived(PlayerOnline p, ByteBuffer message) {
        // TODO Auto-generated method stub

    }

    public static NetworkMessage getPacket(int x, int y, int entity) {
        ByteBuffer bb = ByteBuffer.allocate(12);
        bb.putInt(x).putInt(y).putInt(entity);
        return new NetworkMessage(bb, ID);
    }

}
