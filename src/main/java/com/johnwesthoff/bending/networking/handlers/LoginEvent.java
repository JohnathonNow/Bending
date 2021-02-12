package com.johnwesthoff.bending.networking.handlers;

import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.johnwesthoff.bending.Session;
import com.johnwesthoff.bending.Server;
import com.johnwesthoff.bending.logic.Player;
import com.johnwesthoff.bending.logic.PlayerOnline;
import com.johnwesthoff.bending.networking.NetworkEvent;
import com.johnwesthoff.bending.util.network.NetworkMessage;

import java.awt.Color;
import java.io.IOException;

public class LoginEvent implements NetworkEvent {
    public static final byte ID = 14;

    @Override
    public byte getId() {
        return ID;
    }

    @Override
    public void clientReceived(Session p, ByteBuffer rasputin) {
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
        p.clientui.addChat(yes.username + " has joined the game.", Color.RED);
        p.loggedIn = true;
        yes.sameTeam = sameTeam;
    }

    @Override
    public void serverReceived(PlayerOnline po, ByteBuffer bb) {
        bb.getInt();
        po.username = Server.getString(bb);
        try {
            po.out.addMessage(IdEvent.getPacket(po.ID));
            po.writeWorld();
            byte[] clothing = new byte[] { bb.get(), bb.get(), bb.get(), bb.get(), bb.get(), bb.get() };
            po.partss = clothing;
            int[] colors = new int[] { bb.getInt(), bb.getInt(), bb.getInt(), bb.getInt(), bb.getInt(), bb.getInt() };
            int[] colors2 = new int[] { bb.getInt(), bb.getInt(), bb.getInt(), bb.getInt(), bb.getInt(), bb.getInt() };
            po.colorss = colors;
            po.colorss2 = colors2;
            po.UDPPORT = 0;
            Logger.getLogger(getClass().getName()).log(Level.INFO, po.username + " joined with ID " + po.ID + ".");
            String gm = "";
            switch (Server.gameMode) {
                default:
                case Server.TEAMDEATHMATCH:
                    gm = "Team Death Match";
                    break;
                case Server.FREEFORALL:
                    gm = "Free for All";
                    break;
                case Server.KINGOFTHEHILL:
                    gm = "King of the Hill";
                    break;
                case Server.THEHIDDEN:
                    gm = "The Hidden";
                    break;
                case Server.SURVIVAL:
                    gm = "Survival";
                    break;
                case Server.DEFENDER:
                    gm = "Defender";
                    break;
                case Server.TURNBASED:
                    gm = "Turn-based Free for All";
                    break;
            }
            gm = "The next game type will be " + gm + ".";
            po.out.addMessage(MessageEvent.getPacket(0x00FF3C, gm));
            if (Server.gameMode == Server.DEFENDER) {
                gm = "You will be a" + (po.handle.team1.contains(po.ID) ? " defender." : "n attacker.");
                po.out.addMessage(MessageEvent.getPacket(0x00FF3C, gm));
            }
            for (PlayerOnline p : po.handle.playerList) {
                if (p.ID != po.ID) {
                    boolean sameTeam = ((po.handle.team1.contains(po.ID) && po.handle.team1.contains(p.ID))
                            || (po.handle.team2.contains(po.ID) && po.handle.team2.contains(p.ID)));
                    // TODO: FIX
                    p.sameTeam = sameTeam;
                    po.sameTeam = sameTeam;
                    po.out.addMessage(getPacket(p));
                    p.out.addMessage(getPacket(po));
                }
            }
            po.loggedIn = true;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static NetworkMessage getPacket(Player p) {
        // TODO: FIX
        ByteBuffer bb = ByteBuffer.allocate(75 + 4 + 4 + 4 * p.username.length());
        bb.putInt(p.ID);
        Server.putString(bb, p.username);
        bb.put(p.partss).putInt(p.colorss[0]).putInt(p.colorss[1]).putInt(p.colorss[2]).putInt(p.colorss[3])
                .putInt(p.colorss[4]).putInt(p.colorss[5]).putInt(p.colorss2[0]).putInt(p.colorss2[1])
                .putInt(p.colorss2[2]).putInt(p.colorss2[3]).putInt(p.colorss2[4]).putInt(p.colorss2[5])
                .put((byte) (p.sameTeam ? 12 : 0));
        return new NetworkMessage(bb, ID);
    }

}
