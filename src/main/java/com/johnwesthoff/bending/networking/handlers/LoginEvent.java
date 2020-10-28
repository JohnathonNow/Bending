package com.johnwesthoff.bending.networking.handlers;

import java.nio.ByteBuffer;

import com.johnwesthoff.bending.Client;
import com.johnwesthoff.bending.Server;
import com.johnwesthoff.bending.logic.Player;
import com.johnwesthoff.bending.logic.PlayerOnline;
import com.johnwesthoff.bending.networking.NetworkEvent;
import java.awt.Color;
import java.io.IOException;

public class LoginEvent implements NetworkEvent {
    public static final byte ID = 14;

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
    public void serverReceived(PlayerOnline po, ByteBuffer bb) {
        long auth = bb.getLong();
        if (auth != Client.getAuth()) {
            po.killMe();
        }
        po.username = Server.getString(bb);
        try {
            po.out.addMesssage(IdEvent.getPacket(po.ID));

            po.writeWorld();
            byte[] clothing = new byte[] { bb.get(), bb.get(), bb.get(), bb.get(), bb.get(), bb.get() };
            po.cloth = clothing;
            int[] colors = new int[] { bb.getInt(), bb.getInt(), bb.getInt(), bb.getInt(), bb.getInt(), bb.getInt() };
            int[] colors2 = new int[] { bb.getInt(), bb.getInt(), bb.getInt(), bb.getInt(), bb.getInt(), bb.getInt() };
            po.colord = colors;
            po.colord2 = colors2;
            po.UDPPORT = bb.getInt();
            System.err.println(po.username + " joined with ID " + po.ID + ", on UDPPORT " + po.UDPPORT + ".");
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
            }
            gm = "The next game type will be " + gm + ".";
            po.out.addMesssage(MessageEvent.getPacket(0x00FF3C, gm));
            if (Server.gameMode == Server.DEFENDER) {
                gm = "You will be a" + (po.handle.team1.contains(po.ID) ? " defender." : "n attacker.");
                po.out.addMesssage(MessageEvent.getPacket(0x00FF3C, gm));
            }
            for (PlayerOnline p : po.handle.playerList) {
                if (p.ID != po.ID) {
                    byte sameTeam = (byte) ((po.handle.team1.contains(po.ID) && po.handle.team1.contains(p.ID))
                            || (po.handle.team2.contains(po.ID) && po.handle.team2.contains(p.ID)) ? 12 : 0);
                    // TODO: FIX
                    po.out.addMesssage(Server
                            .putString(ByteBuffer.allocate(75 + 4 + 4 + 4 * p.username.length()).putInt(p.ID),
                                    p.username)
                            .put(p.cloth).putInt(p.colord[0]).putInt(p.colord[1]).putInt(p.colord[2])
                            .putInt(p.colord[3]).putInt(p.colord[4]).putInt(p.colord[5]).putInt(p.colord2[0])
                            .putInt(p.colord2[1]).putInt(p.colord2[2]).putInt(p.colord2[3]).putInt(p.colord2[4])
                            .putInt(p.colord2[5]).put(sameTeam), Server.LOGIN);
                    p.out.addMesssage(
                            Server.putString(ByteBuffer.allocate(75 + 4 + 4 + 4 * po.username.length()).putInt(ID),
                                    po.username).put(clothing).putInt(colors[0]).putInt(colors[1]).putInt(colors[2])
                                    .putInt(colors[3]).putInt(colors[4]).putInt(colors[5]).putInt(colors2[0])
                                    .putInt(colors2[1]).putInt(colors2[2]).putInt(colors2[3]).putInt(colors2[4])
                                    .putInt(colors2[5]).put(sameTeam),
                            Server.LOGIN);
                }
            }
            po.loggedIn = true;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static NetworkMessage getPacket(Player p) {
        //TODO: FIX
        ByteBuffer bb = ByteBuffer.allocate(75 + 4 + 4 + 4 * p.username.length());
        bb.putInt(p.ID);
        Server.putString(bb, p.username);
        bb.put(p.cloth).putInt(p.colord[0]).putInt(p.colord[1]).putInt(p.colord[2])
                            .putInt(p.colord[3]).putInt(p.colord[4]).putInt(p.colord[5]).putInt(p.colord2[0])
                            .putInt(p.colord2[1]).putInt(p.colord2[2]).putInt(p.colord2[3]).putInt(p.colord2[4])
                            .putInt(p.colord2[5]).put(sameTeam), Server.LOGIN);
        
        bb.putInt(subID).putInt(x).putInt(y).putInt(mx).putInt(my);
        return new NetworkMessage(bb, ID);
    }

}
