package com.johnwesthoff.bending.networking.handlers;

import java.nio.ByteBuffer;

import com.johnwesthoff.bending.Constants;
import com.johnwesthoff.bending.Session;
import com.johnwesthoff.bending.entity.EnergyEntity;
import com.johnwesthoff.bending.entity.ShockEffectEntity;
import com.johnwesthoff.bending.entity.StaticShotEntity;
import com.johnwesthoff.bending.logic.PlayerOnline;
import com.johnwesthoff.bending.networking.NetworkEvent;
import com.johnwesthoff.bending.util.math.Ops;
import com.johnwesthoff.bending.util.network.NetworkMessage;

public class ChargeEvent implements NetworkEvent {
    public static final byte ID = 2;

    @Override
    public byte getId() {
        return ID;
    }

    @Override
    public void clientReceived(Session p, ByteBuffer toRead) {
        final int ix = toRead.getInt();
        final int iy = toRead.getInt();
        final int ir = toRead.getInt();
        final int energy = toRead.getInt();
        final int maker = toRead.getInt();
        if (Ops.pointDis(p.world.x, p.world.y, ix, iy) < ir) {
            p.energico += energy;
            if (maker != p.ID && (p.gameMode > 0 ? !p.myTeam.contains(maker) : true)) {
                if (maker != 0) {
                    p.lastHit = maker;
                }
                p.client.hurt(12);
                p.killMessage = "~ was electrified by `.";
                if (p.world.inBounds(p.world.x, p.world.y)
                        && p.world.ground.cellData[(int) p.world.x][(int) p.world.y] == Constants.WATER) {
                    p.client.hurt(12);
                    p.killMessage = "~ will never go in the water during a storm again, thanks to `!";
                }
            }
        }
        p.world.entityList.add(new ShockEffectEntity(ix, iy, ir));

    }

    @Override
    public void serverReceived(PlayerOnline p, ByteBuffer message) {
        // TODO Auto-generated method stub

    }

    public static NetworkMessage getPacket(EnergyEntity e) {
        ByteBuffer bb = ByteBuffer.allocate(40);
        bb.putInt((int) e.X).putInt((int) e.Y).putInt(e.radius).putInt(200).putInt(e.maker);
        return new NetworkMessage(bb, ID);
    }

    public static NetworkMessage getPacket(StaticShotEntity e) {
        ByteBuffer bb = ByteBuffer.allocate(40);
        bb.putInt((int) e.X).putInt((int) e.Y).putInt(e.radius).putInt(200).putInt(e.maker);
        return new NetworkMessage(bb, ID);
    }

}
