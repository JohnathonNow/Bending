package com.johnwesthoff.bending;

import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.johnwesthoff.bending.logic.Player;
import com.johnwesthoff.bending.logic.World;
import com.johnwesthoff.bending.networking.NetworkManager;
import com.johnwesthoff.bending.networking.handlers.LoginEvent;
import com.johnwesthoff.bending.networking.handlers.MessageEvent;
import com.johnwesthoff.bending.networking.handlers.MoveEvent;
import com.johnwesthoff.bending.util.network.NetworkMessage;
import com.johnwesthoff.bending.util.network.OrderedOutputStream;

public class ClientNetworking {
    public boolean startAi() {
        Session sess = Session.getInstance();
        try {
            sess.connection = new Socket(sess.serverIP, Constants.PORT);
            sess.isAlive = true;
            sess.world = new World(true, Constants.WIDTH_EXT, Constants.HEIGHT_EXT,
                    sess.clientui.createImage(Constants.WIDTH_EXT, Constants.HEIGHT_EXT), sess.clientui.Grass,
                    sess.clientui.Sand, sess.clientui.Sky, sess.clientui.Stone, sess.clientui.Bark, sess.clientui.Ice,
                    sess.clientui.LavaLand, sess.clientui.Crystal, sess.clientui.ether);
            //sess.world.load(sess.clientui.Clothing, sess.clientui.Colors, sess.clientui.Colors2);
            sess.world.serverWorld = false;
            sess.connection.setKeepAlive(true);
            sess.connection.setTcpNoDelay(true);
            sess.out = new OrderedOutputStream(sess.connection.getOutputStream());
            sess.input = sess.connection.getInputStream();
            sess.localPlayer = new Player(0, 0, sess.clientui.Clothing, sess.clientui.Colors, sess.clientui.Colors2);
            sess.localPlayer.username = sess.username;
            sess.out.addMessage(LoginEvent.getPacket(sess.localPlayer));
            sess.ID = -1;
            sess.world.ID = sess.ID;
            sess.communication = new Thread() {
                @Override
                public void run() {
                    try {
                        while (sess.gameAlive) {
                            NetworkMessage m = NetworkMessage.read(sess.input);
                            NetworkManager.getInstance().getHandler(m).clientReceived(sess, m.getContent());
                            sess.pc++;
                        }
                    } catch (final Exception ex) {
                        Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            };
            sess.communication.start();

            sess.worldList.add(sess.world);
            sess.started = true;
        } catch (final Exception ex) {
            sess.failed = true;
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
    public boolean start() {
        Session sess = Session.getInstance();
        try {
            sess.connection = new Socket(sess.serverIP, Constants.PORT);
            sess.isAlive = true;
            sess.world = new World(false, Constants.WIDTH_EXT, Constants.HEIGHT_EXT,
                    sess.clientui.createImage(Constants.WIDTH_EXT, Constants.HEIGHT_EXT), sess.clientui.Grass,
                    sess.clientui.Sand, sess.clientui.Sky, sess.clientui.Stone, sess.clientui.Bark, sess.clientui.Ice,
                    sess.clientui.LavaLand, sess.clientui.Crystal, sess.clientui.ether);
            sess.world.load(sess.clientui.Clothing, sess.clientui.Colors, sess.clientui.Colors2);

            sess.connection.setKeepAlive(true);
            sess.connection.setTcpNoDelay(true);
            sess.out = new OrderedOutputStream(sess.connection.getOutputStream());
            sess.input = sess.connection.getInputStream();
            sess.localPlayer = new Player(0, 0, sess.clientui.Clothing, sess.clientui.Colors, sess.clientui.Colors2);
            sess.localPlayer.username = sess.username;
            sess.out.addMessage(LoginEvent.getPacket(sess.localPlayer));
            sess.ID = -1;
            sess.world.ID = sess.ID;
            sess.communication = new Thread() {
                @Override
                public void run() {
                    try {
                        while (sess.gameAlive) {
                            NetworkMessage m = NetworkMessage.read(sess.input);
                            NetworkManager.getInstance().getHandler(m).clientReceived(sess, m.getContent());
                            sess.pc++;
                        }
                    } catch (final Exception ex) {
                        Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            };
            sess.communication.start();

            sess.worldList.add(sess.world);
            sess.started = true;
        } catch (final Exception ex) {
            sess.failed = true;
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    public void sendMessage(final String s) {
        sendMessage(s, 0x3333FF);
    }

    public void sendMessage(final String s, final int color) {
        try {
            Session.getInstance().out.addMessage(MessageEvent.getPacket(color, s));
        } catch (final IOException ex) {
            // ex.printStackTrace();
        }
    }

    public void readWorld(ByteBuffer toRead) throws Exception {
        Session sess = Session.getInstance();
        sess.busy = true;
        sess.world.following = null;
        sess.world.status = 0;
        // //system.out.println(toRead.remaining());
        sess.world.ground.w = toRead.getInt();
        sess.world.ground.h = toRead.getInt();

        sess.world.wIdTh = sess.world.ground.w;
        sess.world.hEigHt = sess.world.ground.h;
        sess.world.x = toRead.getInt();
        sess.world.y = toRead.getInt();
        sess.mapRotation = toRead.getInt();
        sess.world.map = sess.mapRotation;
        sess.gameMode = toRead.getInt();
        sess.myTeam.clear();
        sess.badTeam.clear();
        sess.matchOver = 40 * 8;
        int i = 0;
        while (i != -1) {
            i = toRead.getInt();
            if (i != -1) {
                sess.myTeam.add(i);
            }
        }
        i = 0;
        while (i != -1) {
            i = toRead.getInt();
            if (i != -1) {
                sess.badTeam.add(i);
            }
        }
        sess.goodTeam = true;
        if (sess.badTeam.contains(sess.ID)) {
            final ArrayList<Integer> TeamTemp = sess.badTeam;
            sess.badTeam = sess.myTeam;
            sess.myTeam = TeamTemp;
            sess.goodTeam = false;
        }
        for (final Player p : sess.world.playerList) {
            p.myTeam = sess.myTeam.contains(p.ID) && sess.gameMode > 0;
        }
        sess.HP = sess.MAXHP;
        sess.passiveList[sess.spellBook].onSpawn(sess);
        for (final Player p : sess.world.playerList) {
            p.score = 0;
        }
        sess.score = 0;
        sess.world.y = 0;
        sess.world.x = (sess.goodTeam ? sess.world.wIdTh / 2 : 0) + sess.random.nextInt(sess.world.wIdTh / 2);
        sess.lastHit = sess.ID;
        // System.out.println(world.wIdTh+" x "+world.hEigHt);
        sess.world.ground.cellData = new byte[sess.world.ground.w][sess.world.ground.h];
        sess.world.username = sess.username;
        sess.world.idddd = sess.ID;
        // system.out.println(world.ground.w+" x "+world.ground.h);
        // byte buffer[] = new byte[world.ground.h*world.ground.w];
        // toRead.get(buffer);
        // input.read(buffer);
        final ByteBuffer chunks[] = new ByteBuffer[sess.world.wIdTh / 100];
        for (int t = 0; t < chunks.length; t += 1) {
            sess.input.read();
            // System.out.println("CHUNK");
            chunks[t] = Server.readByteBuffer(sess.input);
            for (i = t * 100; i < (t * 100) + 100; i++) {

                chunks[t].get(sess.world.ground.cellData[i], 0, sess.world.ground.h);
                //// system.out.println(read);
                // world.ground.cellData[i] = read;
            }
        }
        // system.out.println(toRead.remaining());
        readEntityList(toRead);
        // System.out.println("Done");
        // system.out.println(toRead.remaining());
        sess.busy = false;
    }

    public boolean expand = false;

    public void sendMovement() {
        Session sess = Session.getInstance();
        if (sess.ID == -1) {
            return;
        }
        try {
            sess.out.addMessage(MoveEvent.getPacket(sess.world.x, sess.world.y, sess.world.move, sess.world.vspeed,
                    sess.world.leftArmAngle, sess.world.rightArmAngle, sess.world.status, sess.HP, sess.world.ID,
                    sess.world.floatiness));

        } catch (final Exception e) {
            // e.printStackTrace();
        }
    }

    public void readEntityList(final ByteBuffer toRead) {
        boolean done;
        Session sess = Session.getInstance();
        sess.world.entityList.clear();
        done = false;
        try {
            while (!done) {
                if (!toRead.hasRemaining())
                    break;
                final String className = Server.getString(toRead);
                try {
                    Class.forName(className).getMethod("reconstruct", ByteBuffer.class, World.class).invoke(null,
                            toRead, sess.world);
                    sess.world.entityList.get(sess.world.entityList.size() - 1).setID(toRead.getInt());
                } catch (ClassNotFoundException | NoSuchMethodException | SecurityException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (final Exception e) {

        }
    }

}
