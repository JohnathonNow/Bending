package com.johnwesthoff.bending.networking.handlers;

import java.nio.ByteBuffer;

import com.johnwesthoff.bending.Client;
import com.johnwesthoff.bending.logic.Player;
import com.johnwesthoff.bending.logic.PlayerOnline;
import com.johnwesthoff.bending.networking.NetworkEvent;

public class MoveEvent implements NetworkEvent {
    public static final byte ID = 17;

    @Override
    public void clientReceived(Client p, ByteBuffer reading) {
        final int idtomove = reading.getShort();
        if (idtomove == p.ID) {
            p.world.x = reading.getShort();
            p.world.y = reading.getShort();
            p.world.move = reading.getShort();
            p.world.vspeed = reading.getShort();
            p.world.leftArmAngle = reading.getShort();
            p.world.rightArmAngle = reading.getShort();
            p.world.status = reading.getShort();
        }
        for (final Player r : p.world.playerList) {
            if (r.ID == idtomove) {
                r.x = reading.getShort();
                r.y = reading.getShort();
                r.move = reading.getShort();
                r.vspeed = reading.getShort();
                r.leftArmAngle = reading.getShort();
                r.rightArmAngle = reading.getShort();
                r.status = reading.getShort();
                break;
            }
        }
    }

    @Override
    public void serverReceived(PlayerOnline p, ByteBuffer toRead) {
        p.x = toRead.getShort();
        p.y = toRead.getShort();
        p.move = toRead.getShort();
        p.vspeed = toRead.getShort();
        p.leftArmAngle = toRead.getShort();
        p.rightArmAngle = toRead.getShort();
        p.status = toRead.getShort();
        p.HP = toRead.getShort();
        p.handle.movePlayer(p.ID, p.x, p.y, p.move, p.vspeed, (int) p.leftArmAngle, (int) p.rightArmAngle, p.status, p.HP);
    }
}
