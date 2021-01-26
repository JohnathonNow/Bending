package com.johnwesthoff.bending;

import java.awt.Rectangle;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JPanel;

import com.johnwesthoff.bending.logic.Player;
import com.johnwesthoff.bending.logic.World;
import com.johnwesthoff.bending.networking.NetworkManager;
import com.johnwesthoff.bending.networking.handlers.LoginEvent;
import com.johnwesthoff.bending.networking.handlers.MessageEvent;
import com.johnwesthoff.bending.networking.handlers.MoveEvent;
import com.johnwesthoff.bending.util.network.NetworkMessage;
import com.johnwesthoff.bending.util.network.OrderedOutputStream;

/**
 *
 * @author Family
 */
public class ClientNetworking extends JPanel implements Runnable {
    public boolean start() {
        try {
            connection = new Socket(serverIP, 25565);
            isAlive = true;
            world = new World(false, Constants.WIDTH_EXT, Constants.HEIGHT_EXT,
                    createImage(Constants.WIDTH_EXT, Constants.HEIGHT_EXT), Grass, Sand, Sky, Stone, Bark, Ice,
                    LavaLand, Crystal, ether);
            world.load(Clothing, Colors, Colors2);
            // entityList.add(new HouseEntity(750,300,200,300));

            connection.setKeepAlive(true);
            connection.setTcpNoDelay(true);
            out = new OrderedOutputStream(connection.getOutputStream());
            input = connection.getInputStream();
            localPlayer = new Player(0, 0, Clothing, Colors, Colors2);
            localPlayer.username = username;
            out.addMessage(LoginEvent.getPacket(localPlayer));
            ID = -1;
            world.ID = ID;
            playerHitbox = new Rectangle(0, 0, 20, 40);
            Client c = this; // this is big sad
            communication = new Thread() {
                @Override
                public void run() {
                    try {
                        while (gameAlive) {
                            NetworkMessage m = NetworkMessage.read(input);
                            NetworkManager.getInstance().getHandler(m).clientReceived(c, m.getContent());
                            pc++;
                        }
                    } catch (final Exception ex) {
                        // Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            };
            communication.start();

            worldList.add(world);
            repaint();
            started = true;
        } catch (

        final Exception ex) {

            failed = true;
            return false;
        }
        return true;
        // terrain.getGraphics().drawImage(Grass, 0,0,null);
    }

  

    public void sendMessage(final String s) {
        sendMessage(s, 0x3333FF);
    }

    public void sendMessage(final String s, final int color) {
        try {
            out.addMessage(MessageEvent.getPacket(color, s));
        } catch (final IOException ex) {
            // ex.printStackTrace();
        }
    }

    public void readWorld(ByteBuffer toRead) throws Exception {
        busy = true;
        world.following = null;
        world.status = 0;
        // //system.out.println(toRead.remaining());
        world.ground.w = toRead.getInt();
        world.ground.h = toRead.getInt();

        world.wIdTh = world.ground.w;
        world.hEigHt = world.ground.h;
        world.x = toRead.getInt();
        world.y = toRead.getInt();
        mapRotation = toRead.getInt();
        world.map = mapRotation;
        gameMode = toRead.getInt();
        myTeam.clear();
        badTeam.clear();
        matchOver = 40 * 8;
        int i = 0;
        while (i != -1) {
            i = toRead.getInt();
            if (i != -1) {
                myTeam.add(i);
            }
        }
        i = 0;
        while (i != -1) {
            i = toRead.getInt();
            if (i != -1) {
                badTeam.add(i);
            }
        }
        goodTeam = true;
        if (badTeam.contains(ID)) {
            final ArrayList<Integer> TeamTemp = badTeam;
            badTeam = myTeam;
            myTeam = TeamTemp;
            goodTeam = false;
        }
        for (final Player p : world.playerList) {
            p.myTeam = myTeam.contains(p.ID) && gameMode > 0;
        }
        HP = MAXHP;
        passiveList[spellBook].onSpawn(this);
        for (final Player p : world.playerList) {
            p.score = 0;
        }
        score = 0;
        world.y = 0;
        world.x = (goodTeam ? world.wIdTh / 2 : 0) + random.nextInt(world.wIdTh / 2);
        lastHit = ID;
        // System.out.println(world.wIdTh+" x "+world.hEigHt);
        world.ground.cellData = new byte[world.ground.w][world.ground.h];
        world.username = username;
        world.idddd = ID;
        // system.out.println(world.ground.w+" x "+world.ground.h);
        // byte buffer[] = new byte[world.ground.h*world.ground.w];
        // toRead.get(buffer);
        // input.read(buffer);
        final ByteBuffer chunks[] = new ByteBuffer[world.wIdTh / 100];
        for (int t = 0; t < chunks.length; t += 1) {
            input.read();
            // System.out.println("CHUNK");
            chunks[t] = Server.readByteBuffer(input);
            for (i = t * 100; i < (t * 100) + 100; i++) {

                chunks[t].get(world.ground.cellData[i], 0, world.ground.h);
                //// system.out.println(read);
                // world.ground.cellData[i] = read;
            }
        }
        // system.out.println(toRead.remaining());
        readEntityList(toRead);
        // System.out.println("Done");
        // system.out.println(toRead.remaining());
        busy = false;
    }

    public boolean expand = false;

    public void sendMovement() {
        if (ID == -1) {
            return;
        }
        try {
            out.addMessage(MoveEvent.getPacket(world.x, world.y, world.move, world.vspeed, world.leftArmAngle,
                    world.rightArmAngle, world.status, HP, world.ID, world.floatiness));

        } catch (final Exception e) {
            // e.printStackTrace();
        }
    }

    public void readEntityList(final ByteBuffer toRead) {
        boolean done;
        world.entityList.clear();
        done = false;
        try {
            while (!done) {
                if (!toRead.hasRemaining())
                    break;
                final String className = Server.getString(toRead);
                try {
                    Class.forName(className).getMethod("reconstruct", ByteBuffer.class, World.class).invoke(null,
                            toRead, world);
                    world.entityList.get(world.entityList.size() - 1).setID(toRead.getInt());
                } catch (ClassNotFoundException | NoSuchMethodException | SecurityException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (final Exception e) {

        }
    }

}
