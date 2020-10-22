package com.johnwesthoff.bending.util.network;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class NetworkMessage {
    private final byte id;
    private final ByteBuffer content;

    public byte getId() {
        return id;
    }

    public ByteBuffer getContent() {
        return content;
    }

    public NetworkMessage(byte id, ByteBuffer content) {
        this.id = id;
        this.content = content;
    }

    public void send(OutputStream out) throws IOException {
        final byte toSend[] = new byte[content.position() + 5];
        toSend[0] = this.id;
        System.arraycopy(this.content.array(), 0, toSend, 4, this.content.position());
        final byte[] size = ByteBuffer.allocate(4).putInt(this.content.position()).array();
        System.arraycopy(size, 0, toSend, 1, 4);
        out.write(toSend);
        out.flush();
    }

}
