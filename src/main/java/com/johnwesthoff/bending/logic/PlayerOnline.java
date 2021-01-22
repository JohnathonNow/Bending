/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johnwesthoff.bending.logic;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

import com.johnwesthoff.bending.Server;
import com.johnwesthoff.bending.app.player.PlayerService;
import com.johnwesthoff.bending.app.player.PlayerServiceFactory;
import com.johnwesthoff.bending.entity.Entity;
import com.johnwesthoff.bending.networking.NetworkManager;
import com.johnwesthoff.bending.networking.handlers.EntireWorldEvent;
import com.johnwesthoff.bending.networking.handlers.LeaveEvent;
import com.johnwesthoff.bending.networking.handlers.MoveEvent;
import com.johnwesthoff.bending.util.network.NetworkMessage;
import com.johnwesthoff.bending.util.network.OrderedOutputStream;

/**
 *
 * @author John
 */
public class PlayerOnline extends Player implements Runnable {

    private final PlayerService playerService;

    public Socket playerSocket;
    public InputStream in;
    public OrderedOutputStream out;
    public boolean ready = false, alive = true, loggedIn = false, voted = false;
    public Server handle;

    public PlayerOnline(int X, int Y, Socket s, int ide, Server h) {
        super(X, Y, new byte[] { 1, 1, 1, 1, 1, 1 }, new int[] { 1, 1, 1, 1, 1, 1 }, new int[] { 1, 1, 1, 1, 1, 1 });
        ID = ide;
        playerSocket = s;
        playerService = PlayerServiceFactory.create();
        Thread me = new Thread(this);
        handle = h;
        me.start();
    }

    public void killMe() {
        handle.sendMessage(LeaveEvent.getPacket(this));
        try {
            out.close();
            in.close();
            playerSocket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            alive = false;
            if (handle.team1.contains(ID)) {
                handle.team1.remove(handle.team1.indexOf(ID));
            } else {
                handle.team2.remove(handle.team2.indexOf(ID));
            }
            handle.playerList.remove(this);
            // @TODO : it's the server responsability to emit decrement player event
            playerService.updatePlayerCount(handle.IP, handle.playerList.size());
        }
    }

    public int UDPPORT;

    public void getInput() throws IOException {
        ready = false;
        NetworkMessage m = NetworkMessage.read(in);
        NetworkManager.getInstance().getHandler(m).serverReceived(this, m.getContent());
        ready = true;
    }

    public void writeMovePlayer(int id, int X, int Y, int M, int V, int LA, int RA, short st, short hp, int floatiness) {
        try {
            out.addMessage(MoveEvent.getPacket(X, Y, M, V, LA, RA, st, hp, id, floatiness));
        } catch (Exception ex) {
            ex.printStackTrace();
            killMe();
        }
    }



    public void writeWorld() {
        voted = false;
        try {
            // System.out.println("World sent s");
            ByteBuffer toSend = ByteBuffer.allocate(9600004);
            // out.write(Server.ENTIREWORLD);
            // out.flush();
            toSend.putInt(handle.earth.ground.w);
            toSend.putInt(handle.earth.ground.h);
            toSend.putInt((int) handle.earth.x);
            toSend.putInt((int) handle.earth.y);
            toSend.putInt(handle.mapRotation);
            toSend.putInt(Server.gameMode);
            // System.out.println(handle.earth.ground.w+" x "+handle.earth.ground.h);

            for (int i = 0; i < handle.team1.size(); i++) {
                toSend.putInt(handle.team1.get(i));
            }
            toSend.putInt(-1);
            for (int i = 0; i < handle.team2.size(); i++) {
                toSend.putInt(handle.team2.get(i));
            }
            toSend.putInt(-1);
            ByteBuffer[] chunks = new ByteBuffer[handle.earth.ground.w / 100];
            for (int t = 0; t < handle.earth.ground.w; t += 100) {
                chunks[t / 100] = ByteBuffer.allocate(900000);
                for (int i = t; i < t + 100; i++) {
                    chunks[t / 100].put(handle.earth.ground.cellData[i]);
                    // System.out.println(i);
                }

            }
            for (Entity e : handle.earth.entityList) {
                System.out.println(e.getClass().getName());
                e.cerealize(toSend);
                toSend.putInt(e.MYID);
            }
            toSend.put(Byte.MIN_VALUE);
            toSend.put(Byte.MIN_VALUE);
            out.addMessage(EntireWorldEvent.getPacket(toSend));
            // Server.writeByteBuffer(toSend,out);
            for (int t = 0; t < chunks.length; t += 1) {
                // Server.writeByteBuffer(chunks[t],out);
                out.addMessage(new NetworkMessage(chunks[t], (byte)0));
            }
            // System.out.println("World sent");
        } catch (Exception ex) {
            ex.printStackTrace();
            killMe();
        }
    }

    @Override
    public void run() {
        try {
            in = playerSocket.getInputStream();
            out = new OrderedOutputStream(playerSocket.getOutputStream());
            ready = true;

            while (alive) {
                try {
                    ready = true;
                    getInput();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    killMe();
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            killMe();
        }
    }
}