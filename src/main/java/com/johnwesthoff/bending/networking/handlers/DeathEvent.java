package com.johnwesthoff.bending.networking.handlers;

import java.nio.ByteBuffer;

import com.johnwesthoff.bending.Client;
import com.johnwesthoff.bending.logic.Player;
import com.johnwesthoff.bending.logic.PlayerOnline;
import com.johnwesthoff.bending.networking.NetworkEvent;

public class DeathEvent implements NetworkEvent {

    @Override
    public void clientReceived(Client p, ByteBuffer buf) {
        final int fX = buf.getInt();
        final int fX2 = buf.getInt();
        if (fX2 != fX) {
            if (fX == p.ID) {
                p.XP += 25;
                p.gameService.earnExperience(p.XP,p.username);
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
    public void serverReceived(PlayerOnline p, ByteBuffer message) {
        // TODO Auto-generated method stub

    }

}

