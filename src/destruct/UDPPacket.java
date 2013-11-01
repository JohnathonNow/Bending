/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package destruct;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author John
 */
public class UDPPacket{
    public byte ID = 0;
    DatagramPacket packet = null;
    ByteBuffer buffer = null;
    public static UDPPacket build(DatagramPacket dp)
    {
        byte lols[] = dp.getData();
        int length = dp.getLength();
        UDPPacket yes = allocate(lols.length);
        yes.put(lols);
        yes.rewind();
        byte[] ne = new byte[length];
        yes.buffer.get(ne);
        yes.buffer = ByteBuffer.wrap(ne);
        System.out.println(yes.buffer.capacity());
        yes.ID = yes.get();
        yes.packet = dp;
        return yes;
    }
    public static UDPPacket allocate(int space)
    {
        UDPPacket yes = new UDPPacket();
        yes.buffer = ByteBuffer.allocate(space);
        return yes;
    }
    public static UDPPacket allocate(int space, byte id)
    {
        UDPPacket yes = new UDPPacket();
        yes.buffer = ByteBuffer.allocate(space);
        return yes.put(id);
    }
    public static UDPPacket allocate(int space, int id)
    {
        return allocate(space,(byte)id);
    }
    public UDPPacket putInt(int i)
    {
        buffer.putInt(i);
        return this;
    }
    public UDPPacket putShort(short i)
    {
        buffer.putShort(i);
        return this;
    }
    public UDPPacket put(byte i)
    {
        buffer.put(i);
        return this;
    }
    public UDPPacket put(int i)
    {
        buffer.put((byte)i);
        return this;
    }
    public UDPPacket put(byte i[])
    {
        buffer.put(i);
        return this;
    }
    public UDPPacket putDouble(double i)
    {
        buffer.putDouble(i);
        return this;
    }
    public UDPPacket putString(String s)
    {
        buffer.putInt(s.length());
        buffer.put(s.getBytes());
        return this;
    }
    public int getInt()
    {
        return buffer.getInt();
    }
    public short geShortt()
    {
        return buffer.getShort();
    }
    public byte get()
    {
        return buffer.get();
    }
    public byte[] get(int length)
    {
        byte[] bytes = new byte[length];
        buffer.get(bytes);
        return bytes;
    }
    public double getDouble()
    {
        return buffer.getDouble();
    }
    public String getString()
    {
        return (new String(get(getInt())));
    }
    public DatagramPacket getPacket()
    {
        return packet;
    }
    public static UDPPacket read(DatagramSocket ds)
    {
        try {
            DatagramPacket pack = new DatagramPacket(new byte[2400000],2400000);
            ds.receive(pack);
            return build(pack);
        } catch (IOException ex) {
            return null;
        }
    }
    public UDPPacket rewind()
    {
        buffer.rewind();
        return this;
    }
    public UDPPacket shrinkPacket()
    {
        byte yay[] = new byte[buffer.position()];//toSend.slice().array();
        System.arraycopy(buffer.array(), 0, yay, 0, buffer.position());
        buffer = ByteBuffer.wrap(yay);
        return this;
    }
    public UDPPacket write(DatagramSocket ds, InetAddress address, int port)
    {
        try {
            packet = new DatagramPacket(new byte[buffer.capacity()],buffer.capacity());
            packet.setPort(port);
            packet.setAddress((address));
            shrinkPacket();
            packet.setData(buffer.array());
            ds.send(packet);
        } catch (Exception ex) {
        }
        return this;
    }
    
    public UDPPacket write(DatagramSocket ds, String address, int port)
    {
        try {
            packet = new DatagramPacket(new byte[buffer.capacity()],buffer.capacity());
            packet.setPort(port);
            packet.setAddress(InetAddress.getByName(address));
            shrinkPacket();
            packet.setData(buffer.array());
            ds.send(packet);
        } catch (Exception ex) {
        }
        return this;
    }
}
