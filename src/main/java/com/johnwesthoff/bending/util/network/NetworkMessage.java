package com.johnwesthoff.bending.util.network;

import java.io.IOException;
import java.io.InputStream;
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

    public NetworkMessage(ByteBuffer content, byte id) {
        this.id = id;
        this.content = content;
    }

    public void send(OutputStream out) throws IOException {
        final byte toSend[] = new byte[content.position() + 5];
        toSend[0] = this.id;
        System.arraycopy(this.content.array(), 0, toSend, 5, this.content.position());
        final byte[] size = ByteBuffer.allocate(4).putInt(this.content.position()).array();
        System.arraycopy(size, 0, toSend, 1, 4);
        out.write(toSend);
        out.flush();
    }

    public static NetworkMessage read(InputStream in) throws IOException {
        final byte mId = (byte) in.read();
        final ByteBuffer size = ByteBuffer.allocate(4);
        size.put((byte) in.read());
        size.put((byte) in.read());
        size.put((byte) in.read());
        size.put((byte) in.read());
        size.rewind();
        final int howMuchData = size.getInt();
        int total = 0;
        final byte[] buf = new byte[howMuchData];
        while (total < howMuchData) {
            total += in.read(buf, total, howMuchData - total);
        }
        return new NetworkMessage(ByteBuffer.wrap(buf), mId);
    }

}
