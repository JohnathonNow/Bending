package com.johnwesthoff.bending.networking.handlers;

import java.nio.ByteBuffer;

import com.johnwesthoff.bending.Client;
import com.johnwesthoff.bending.logic.Player;
import com.johnwesthoff.bending.logic.PlayerOnline;
import com.johnwesthoff.bending.networking.NetworkEvent;

public class ScoreEvent implements NetworkEvent {
    public static final byte ID = 20;

    @Override
    public byte getId() {
        return ID;
    }

    @Override
    public void clientReceived(Client p, ByteBuffer buf) {
        final int idd = buf.getInt();
        final int scored = buf.getInt();
        if (idd == p.ID) {
            p.XP += 10;
            p.gameService.earnExperience(p.XP, p.username);
            p.score = scored;
            if (p.killingSpree == 0) {
                p.killingSpree = Math.E;
            } else {
                p.killingSpree *= Math.E;
            }
        }
        for (final Player x : p.world.playerList) {
            if (x.ID == idd) {
                x.score = scored;
            }
        }

    }

    @Override
    public void serverReceived(PlayerOnline p, ByteBuffer message) {
        // TODO Auto-generated method stub

    }

}
