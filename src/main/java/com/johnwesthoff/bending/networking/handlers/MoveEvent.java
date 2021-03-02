package com.johnwesthoff.bending.networking.handlers;

import java.nio.ByteBuffer;

import com.johnwesthoff.bending.Constants;
import com.johnwesthoff.bending.Session;
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
    public void clientReceived(Session p, ByteBuffer reading) {
        final int idtomove = reading.getShort();
        if (idtomove == p.ID) {
            p.world.x = (float)reading.getDouble();
            p.world.y = (float)reading.getDouble();
            p.world.move = reading.getDouble();
            p.world.vspeed = reading.getDouble();
            p.world.leftArmAngle = reading.getShort();
            p.world.rightArmAngle = reading.getShort();
            p.world.status = reading.getShort();
            p.world.floatiness = reading.getInt();
        }
        for (final Player r : p.world.playerList) {
            if (r.ID == idtomove) {
                r.x = reading.getDouble();
                r.y = reading.getDouble();
                r.move = reading.getDouble();
                r.vspeed = reading.getDouble();
                r.leftArmAngle = reading.getShort();
                r.rightArmAngle = reading.getShort();
                r.status = reading.getShort();
                r.floatiness = reading.getInt();
                int ping = reading.getInt();
                p.world.movePlayer((int)(ping * Constants.FPS / 1000), r);
                break;
            }
        }
    }

    @Override
    public void serverReceived(PlayerOnline p, ByteBuffer toRead) {
        toRead.getShort();
        p.x = toRead.getDouble();
        p.y = toRead.getDouble();
        p.move = toRead.getDouble();
        p.vspeed = toRead.getDouble();
        p.leftArmAngle = toRead.getShort();
        p.rightArmAngle = toRead.getShort();
        p.status = toRead.getShort();
        p.HP = toRead.getShort();
        p.floatiness = toRead.getInt();
        p.handle.movePlayer(p.ID, p.x, p.y, p.move, p.vspeed, (int) p.leftArmAngle, (int) p.rightArmAngle, p.status,
                p.HP, p.floatiness, (int)p.ping);
        p.handle.earth.movePlayer((int)(p.ping * Constants.FPS / 1000), p);
    }

    public static NetworkMessage getPacket(double x, double y, double move, double vspeed, double laa, double raa, int status, int hp, int id, int floatiness) {
        return getPacket(x, y, move, vspeed, laa, raa, status, hp, id, floatiness, 0);
    }

    public static NetworkMessage getPacket(double x, double y, double move, double vspeed, double laa, double raa, int status, int hp, int id, int floatiness, int ping) {
        ByteBuffer toSend = ByteBuffer.allocate(10 * 8);
        toSend.putShort((short) id);
        toSend.putDouble(x);
        toSend.putDouble(y);
        toSend.putDouble(move);
        toSend.putDouble(vspeed);
        toSend.putShort((short) laa);
        toSend.putShort((short) raa);
        toSend.putShort((short) status);
        toSend.putShort((short) hp);
        toSend.putInt(floatiness);
        toSend.putInt(ping);
        return new NetworkMessage(toSend, ID);
    }
}
