package com.johnwesthoff.bending.networking.handlers;

import java.nio.ByteBuffer;

import com.johnwesthoff.bending.Session;
import com.johnwesthoff.bending.logic.PlayerOnline;
import com.johnwesthoff.bending.networking.NetworkEvent;

public class ExpandWorldEvent implements NetworkEvent {
    public static final byte ID = 8;
    @Override
    public byte getId() {
        return ID;
    }

    @Override
    public void clientReceived(Session p, ByteBuffer toRead) {
        p.busy = true;
        final int newx = toRead.getInt();
        final int si = toRead.getInt();
        final byte dir = toRead.get();
        p.world.wIdTh += si;
        final byte list[][] = new byte[p.world.wIdTh][p.world.hEigHt];
        for (int i = 0; i < p.world.ground.w; i++) {
            System.arraycopy(p.world.ground.cellData[i], 0, list[i], 0, p.world.ground.h);
        }
        p.world.ground.cellData = list;
        p.world.ground.w = p.world.wIdTh;
        if (dir == 1) {
            for (int i = newx; i < p.world.wIdTh; i++) {
                toRead.get(p.world.ground.cellData[i]);
            }
        }
        if (dir == 2) {
            for (int i = newx; i < si; i++) {
                toRead.get(p.world.ground.cellData[i]);
            }
            p.world.x += si;
        }
        p.net.readEntityList(toRead);
        p.busy = false;

    }

    @Override
    public void serverReceived(PlayerOnline p, ByteBuffer message) {
        // TODO Auto-generated method stub

    }

}
