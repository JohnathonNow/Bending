package com.johnwesthoff.bending.networking.handlers;

import java.nio.ByteBuffer;

import com.johnwesthoff.bending.Client;
import com.johnwesthoff.bending.logic.PlayerOnline;
import com.johnwesthoff.bending.networking.NetworkEvent;

public class DestroyEvent implements NetworkEvent {

    @Override
    public void clientReceived(Client p, ByteBuffer buf) {
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

}

