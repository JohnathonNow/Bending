package com.johnwesthoff.bending.networking;

import java.nio.ByteBuffer;

import com.johnwesthoff.bending.Session;
import com.johnwesthoff.bending.logic.PlayerOnline;

public interface NetworkEvent {
    public void clientReceived(Session p, ByteBuffer message);
    public void serverReceived(PlayerOnline p, ByteBuffer message);
    public byte getId();
}
