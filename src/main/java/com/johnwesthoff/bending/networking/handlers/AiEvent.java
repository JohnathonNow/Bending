package com.johnwesthoff.bending.networking.handlers;

import java.nio.ByteBuffer;

import com.johnwesthoff.bending.Client;
import com.johnwesthoff.bending.entity.EnemyEntity;
import com.johnwesthoff.bending.entity.Entity;
import com.johnwesthoff.bending.logic.PlayerOnline;
import com.johnwesthoff.bending.networking.NetworkEvent;

public class AiEvent implements NetworkEvent {
    public static final byte ID = 1;

    @Override
    public void clientReceived(Client c, ByteBuffer reader) {
        final int redX = reader.getInt();
        final int redY = reader.getInt();
        final int redmove = reader.getInt();
        final int redyspeed = reader.getInt();
        final int redHP = reader.getInt();
        final int redid = reader.getInt();
        final int tar = reader.getInt();
        for (final Entity p : c.world.entityList) {
            if (!(p instanceof EnemyEntity))
                continue;
            final EnemyEntity e = (EnemyEntity) p;
            if (e.MYID == redid) {
                e.X = redX;
                e.Y = redY;
                e.HP = redHP;
                e.move = redmove;
                e.yspeed = redyspeed;
                e.target = tar;
            }

        }

    }

    @Override
    public void serverReceived(PlayerOnline p, ByteBuffer message) {
        // TODO Auto-generated method stub

    }

}
