package com.johnwesthoff.bending.networking;

import java.nio.ByteBuffer;

import com.johnwesthoff.bending.Client;
import com.johnwesthoff.bending.logic.PlayerOnline;

public interface NetworkEvent {
    public void clientReceived(Client p, ByteBuffer message);
    public void serverReceived(PlayerOnline p, ByteBuffer message);
}
