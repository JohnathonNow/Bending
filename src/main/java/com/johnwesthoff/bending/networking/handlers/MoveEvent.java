package com.johnwesthoff.bending.networking.handlers;

import java.nio.ByteBuffer;

import com.johnwesthoff.bending.Client;
import com.johnwesthoff.bending.logic.Player;
import com.johnwesthoff.bending.logic.PlayerOnline;
import com.johnwesthoff.bending.networking.NetworkEvent;
import com.johnwesthoff.bending.util.network.NetworkMessage;

public class MoveEvent implements NetworkEvent {
    public static final byte ID = 17;

    @Override
    public byte getId() {
        return ID;
    }

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
        toRead.getShort();
        p.x = toRead.getShort();
        p.y = toRead.getShort();
        p.move = toRead.getShort();
        p.vspeed = toRead.getShort();
        p.leftArmAngle = toRead.getShort();
        p.rightArmAngle = toRead.getShort();
        p.status = toRead.getShort();
        p.HP = toRead.getShort();
        p.handle.movePlayer(p.ID, (int)p.x, (int)p.y, (int)p.move, (int)p.vspeed, (int) p.leftArmAngle, (int) p.rightArmAngle, p.status,
                p.HP);
    }

    public static NetworkMessage getPacket(float x, float y, double move, double vspeed, double laa, double raa, int status, int hp, int id) {
        ByteBuffer toSend = ByteBuffer.allocate(6 * 4);
        toSend.putShort((short) id);
        toSend.putShort((short) x);
        toSend.putShort((short) y);
        toSend.putShort((short) move);
        toSend.putShort((short) vspeed);
        toSend.putShort((short) laa);
        toSend.putShort((short) raa);
        toSend.putShort((short) status);
        toSend.putShort((short) hp);
        return new NetworkMessage(toSend, ID);
    }
}
