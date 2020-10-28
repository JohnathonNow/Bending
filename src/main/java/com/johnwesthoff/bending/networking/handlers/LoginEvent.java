package com.johnwesthoff.bending.networking.handlers;

import java.nio.ByteBuffer;

import com.johnwesthoff.bending.Client;
import com.johnwesthoff.bending.Server;
import com.johnwesthoff.bending.logic.Player;
import com.johnwesthoff.bending.logic.PlayerOnline;
import com.johnwesthoff.bending.networking.NetworkEvent;
import java.awt.Color;

public class LoginEvent implements NetworkEvent {

    @Override
    public void clientReceived(Client p, ByteBuffer rasputin) {
        int iid;
        iid = rasputin.getInt();
        final String feliceNavidad = Server.getString(rasputin);
        final Player yes = new Player(300, 300,
                new byte[] { rasputin.get(), rasputin.get(), rasputin.get(), rasputin.get(), rasputin.get(),
                        rasputin.get() },
                new int[] { rasputin.getInt(), rasputin.getInt(), rasputin.getInt(), rasputin.getInt(),
                        rasputin.getInt(), rasputin.getInt() },
                new int[] { rasputin.getInt(), rasputin.getInt(), rasputin.getInt(), rasputin.getInt(),
                        rasputin.getInt(), rasputin.getInt() });
        p.world.playerList.add(yes);
        final boolean sameTeam = rasputin.get() == 12;
        if (sameTeam) {
            p.myTeam.add(iid);
        } else {
            p.badTeam.add(iid);
        }
        yes.myTeam = sameTeam;
        yes.ID = iid;
        yes.username = feliceNavidad;
        p.addChat(yes.username + " has joined the game.", Color.RED);
        p.loggedIn = true;
    }

    @Override
    public void serverReceived(PlayerOnline p, ByteBuffer message) {
        // TODO Auto-generated method stub

    }

}
