package com.johnwesthoff.bending.networking.handlers;

import java.nio.ByteBuffer;

import com.johnwesthoff.bending.Client;
import com.johnwesthoff.bending.Server;
import com.johnwesthoff.bending.entity.SandEntity;
import com.johnwesthoff.bending.logic.PlayerOnline;
import com.johnwesthoff.bending.networking.NetworkEvent;
import com.johnwesthoff.bending.spells.Spell;
import com.johnwesthoff.bending.spells.earth.EarthbendingSand;
import com.johnwesthoff.bending.util.network.NetworkMessage;

public class SpellEvent implements NetworkEvent {
    public static final byte ID = 21;

    @Override
    public byte getId() {
        return ID;
    }

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
    public void serverReceived(PlayerOnline p, ByteBuffer buf) {
        int subID = buf.getInt();
        int Xx = buf.getInt();
        int Yy = buf.getInt();
        int mX = buf.getInt();
        int mY = buf.getInt();
        int Iw = Server.getID();
        NetworkMessage nm = getPacket(subID, Xx, Yy, mX, mY);
        nm.getContent().putInt(p.ID).putInt(Iw);
        if (Spell.getSpell(subID) instanceof EarthbendingSand) {
            // TODO this code is duplicated and should not be
            int number = p.handle.earth.ground.sandinate(Xx, Yy, 96);
            number /= (32);
            p.handle.earth.entityList.add(new SandEntity(Xx, Yy, mX, mY, ID).setID(Iw));
            if (number > 3) {
                p.handle.earth.entityList.add(new SandEntity(Xx, Yy, mX + (int) Client.lengthdir_x(4, 30),
                        mY + (int) Client.lengthdir_y(4, 30), ID).setID(Iw + 1));
                p.handle.earth.entityList.add(new SandEntity(Xx, Yy, mX + (int) Client.lengthdir_x(4, -30),
                        mY + (int) Client.lengthdir_y(4, -30), ID).setID(Iw + 2));
                Server.MYID += 2;
            }
            if (number > 5) {
                p.handle.earth.entityList.add(new SandEntity(Xx, Yy, mX + (int) Client.lengthdir_x(4, 45),
                        mY + (int) Client.lengthdir_y(4, 45), ID).setID(Iw + 3));
                p.handle.earth.entityList.add(new SandEntity(Xx, Yy, mX + (int) Client.lengthdir_x(4, -45),
                        mY + (int) Client.lengthdir_y(4, -45), ID).setID(Iw + 4));
                Server.MYID += 2;
            }
            if (number > 7) {
                p.handle.earth.entityList.add(new SandEntity(Xx, Yy, mX + (int) Client.lengthdir_x(4, 60),
                        mY + (int) Client.lengthdir_y(4, 60), ID).setID(Iw + 5));
                p.handle.earth.entityList.add(new SandEntity(Xx, Yy, mX + (int) Client.lengthdir_x(4, -60),
                        mY + (int) Client.lengthdir_y(4, -60), ID).setID(Iw + 6));
                Server.MYID += 2;
            }
            if (number > 12) {
                p.handle.earth.entityList.add(new SandEntity(Xx, Yy, mX + (int) Client.lengthdir_x(4, 15),
                        mY + (int) Client.lengthdir_y(4, 15), ID).setID(Iw + 7));
                p.handle.earth.entityList.add(new SandEntity(Xx, Yy, mX + (int) Client.lengthdir_x(4, -15),
                        mY + (int) Client.lengthdir_y(4, -15), ID).setID(Iw + 8));
                Server.MYID += 2;
            }
            if (number > 16) {
                p.handle.earth.entityList.add(new SandEntity(Xx, Yy, mX + (int) Client.lengthdir_x(4, 35),
                        mY + (int) Client.lengthdir_y(4, 35), ID).setID(Iw + 9));
                p.handle.earth.entityList.add(new SandEntity(Xx, Yy, mX + (int) Client.lengthdir_x(4, -35),
                        mY + (int) Client.lengthdir_y(4, -35), ID).setID(Iw + 10));
                Server.MYID += 2;
            }
            if (number > 20) {
                p.handle.earth.entityList.add(new SandEntity(Xx, Yy, mX + (int) Client.lengthdir_x(4, 45),
                        mY + (int) Client.lengthdir_y(4, 45), ID).setID(Iw + 11));
                p.handle.earth.entityList.add(new SandEntity(Xx, Yy, mX + (int) Client.lengthdir_x(4, -45),
                        mY + (int) Client.lengthdir_y(4, -45), ID).setID(Iw + 12));
                Server.MYID += 2;
            }
            nm.getContent().putInt(number);

            Server.MYID += 2;
        } else {
            Spell.getSpell(subID).getActionNetwork(p.handle.earth, Xx, Yy, mX, mY, ID, Iw, buf);
        }
        p.handle.sendMessage(nm);
    }

    public static NetworkMessage getPacket(int subID, int x, int y, int mx, int my) {
        ByteBuffer bb = ByteBuffer.allocate(32);
        bb.putInt(subID).putInt(x).putInt(y).putInt(mx).putInt(my);
        return new NetworkMessage(bb, ID);
    }

}
