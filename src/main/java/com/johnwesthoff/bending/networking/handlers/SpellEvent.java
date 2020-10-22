package com.johnwesthoff.bending.networking.handlers;

import java.nio.ByteBuffer;

import com.johnwesthoff.bending.Client;
import com.johnwesthoff.bending.logic.PlayerOnline;
import com.johnwesthoff.bending.networking.NetworkEvent;
import com.johnwesthoff.bending.spells.Spell;
import com.johnwesthoff.bending.util.network.OrderedOutputStream.Message;

public class SpellEvent implements NetworkEvent {
    public static final byte ID = 4;

    @Override
    public void clientReceived(Client p, ByteBuffer buf) {
        int subID = buf.getInt();
        int px = buf.getInt();
        int py = buf.getInt();
        int mx = buf.getInt();
        int my = buf.getInt();
        int pid = buf.getInt();
        int eid = buf.getInt();
        Spell.getSpell(subID).getActionNetwork(p.world, px, py, mx, my, pid, eid, buf);

    }

    @Override
    public void serverReceived(PlayerOnline p, ByteBuffer message) {
        // TODO Auto-generated method stub

    }

    public static Message sendPacket(int subID, int x, int y, int mx, int my) {
        ByteBuffer bb = ByteBuffer.allocate(24);
        bb.putInt(subID).putInt(x).putInt(y).putInt(mx).putInt(my);
        return new Message(bb, ID);
    }

}
