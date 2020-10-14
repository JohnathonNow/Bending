package com.johnwesthoff.bending.networking.handlers;

import java.nio.ByteBuffer;

import com.johnwesthoff.bending.Client;
import com.johnwesthoff.bending.logic.Player;
import com.johnwesthoff.bending.logic.PlayerOnline;
import com.johnwesthoff.bending.networking.NetworkEvent;

import java.awt.Color;

public class LeaveEvent implements NetworkEvent {

    @Override
    public void clientReceived(Client c, ByteBuffer buf) {
        int id = buf.getInt();
        for (final Player p : c.world.playerList) {
            if (p.ID == id) {
                if (c.myTeam.contains(p.ID)) {
                    c.myTeam.remove(c.myTeam.indexOf(p.ID));
                } else {
                    c.badTeam.remove(c.badTeam.indexOf(p.ID));
                }
                c.addChat(p.username + " has left the game.", Color.RED);
                c.world.playerList.remove(p);
                break;
            }
        }

    }

    @Override
    public void serverReceived(PlayerOnline p, ByteBuffer message) {
        // TODO Auto-generated method stub

    }

}

