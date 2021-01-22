cat > $1Event.java <<EOF
package com.johnwesthoff.bending.networking.handlers;

import java.nio.ByteBuffer;

import com.johnwesthoff.bending.client.Client;
import com.johnwesthoff.bending.logic.PlayerOnline;
import com.johnwesthoff.bending.networking.NetworkEvent;

public class $1Event implements NetworkEvent {

    @Override
    public void clientReceived(Client p, ByteBuffer reading) {
        // TODO Auto-generated method stub

    }

    @Override
    public void serverReceived(PlayerOnline p, ByteBuffer message) {
        // TODO Auto-generated method stub

    }

}

EOF
