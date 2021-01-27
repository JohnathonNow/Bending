package com.johnwesthoff.bending.networking.handlers;

import java.nio.ByteBuffer;

import com.johnwesthoff.bending.Session;
import com.johnwesthoff.bending.logic.Player;
import com.johnwesthoff.bending.logic.PlayerOnline;
import com.johnwesthoff.bending.networking.NetworkEvent;
import com.johnwesthoff.bending.util.network.NetworkMessage;

public class DeathEvent implements NetworkEvent {
    public static final byte ID = 3;

    @Override
    public byte getId() {
        return ID;
    }

    @Override
    public void clientReceived(Session p, ByteBuffer buf) {
        final int fX = buf.getInt();
        final int fX2 = buf.getInt();
        if (fX2 != fX) {
            if (fX == p.ID) {
                p.XP += 25;
                p.gameService.earnExperience(p.XP, p.username);
                p.score++;
                if (p.killingSpree == 0) {
                    p.killingSpree = Math.E;
                } else {
                    p.killingSpree *= Math.E;
                }
            }
            for (final Player x : p.world.playerList) {
                if (x.ID == fX) {
                    x.score++;
                }
            }
        }

    }

    @Override
    public void serverReceived(PlayerOnline po, ByteBuffer buf) {
        int subID = buf.getInt();
        if (subID != po.ID) {
            for (PlayerOnline p : po.handle.playerList) {
                if (p.ID == subID) {
                    p.score++;
                }
            }
        }
        po.handle.sendMessage(getPacket(subID, po.ID));
    }

    public static NetworkMessage getPacket(int killer, int victim) {
        ByteBuffer bb = ByteBuffer.allocate(9);
        bb.putInt(killer).putInt(victim);
        return new NetworkMessage(bb, ID);
    }
}