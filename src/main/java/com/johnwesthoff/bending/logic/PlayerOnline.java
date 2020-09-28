/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johnwesthoff.bending.logic;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

import com.johnwesthoff.bending.Main;
import com.johnwesthoff.bending.Server;
import com.johnwesthoff.bending.app.player.PlayerService;
import com.johnwesthoff.bending.app.player.PlayerServiceFactory;
import com.johnwesthoff.bending.entity.Entity;
import com.johnwesthoff.bending.entity.SandEntity;
import com.johnwesthoff.bending.spells.Spell;
import com.johnwesthoff.bending.spells.earth.EarthbendingSand;
import com.johnwesthoff.bending.util.network.ConnectToDatabase;
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
    public boolean ready = false;
    public Server handle;
    public boolean alive = true;
    public boolean loggedIn = false, voted = false;

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
        handle.sendMessage(Server.LEAVE, ByteBuffer.allocate(6).putInt(ID));
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

    public byte cloth[];
    public int[] colord, colord2;
    public int UDPPORT;

    public void getInput() throws IOException {

        ready = false;
        int read = in.read();
        // System.err.println(Server.MESSAGEIDs[read]);
        switch (read) {
            case Server.LOGIN:
                ByteBuffer bb = Server.readByteBuffer(in);
                long auth = bb.getLong();
                if (auth != Main.getAuth()) {
                    killMe();// HACKER!
                }
                this.username = Server.getString(bb);
                out.addMesssage(ByteBuffer.allocate(4).putInt(ID), Server.ID);
                // handle.newPlayer(ID, username);
                writeWorld();
                byte[] clothing = new byte[] { bb.get(), bb.get(), bb.get(), bb.get(), bb.get(), bb.get() };
                cloth = clothing;
                int[] colors = new int[] { bb.getInt(), bb.getInt(), bb.getInt(), bb.getInt(), bb.getInt(),
                        bb.getInt() };
                int[] colors2 = new int[] { bb.getInt(), bb.getInt(), bb.getInt(), bb.getInt(), bb.getInt(),
                        bb.getInt() };
                colord = colors;
                colord2 = colors2;
                UDPPORT = bb.getInt();
                System.err.println(username + " joined with ID " + ID + ", on UDPPORT " + UDPPORT + ".");
                String gm = "";
                switch (Server.gameMode) {
                    case Server.TEAMDEATHMATCH:
                        gm = "Team Death Match";
                        break;
                    case Server.FREEFORALL:
                        gm = "Free for All";
                        break;
                    case Server.KINGOFTHEHILL:
                        gm = "King of the Hill";
                        break;
                    case Server.THEHIDDEN:
                        gm = "The Hidden";
                        break;
                    case Server.SURVIVAL:
                        gm = "Survival";
                        break;
                    case Server.DEFENDER:
                        gm = "Defender";
                        break;
                }
                gm = "The next game type will be " + gm + ".";
                out.addMesssage(Server.putString(ByteBuffer.allocate(gm.length() * 4 + 4).putInt(0x00FF3C), gm),
                        Server.MESSAGE);
                if (Server.gameMode == Server.DEFENDER) {
                    gm = "You will be a" + (handle.team1.contains(ID) ? " defender." : "n attacker.");
                    out.addMesssage(Server.putString(ByteBuffer.allocate(gm.length() * 4 + 4).putInt(0x00FF3C), gm),
                            Server.MESSAGE);
                }
                for (PlayerOnline p : handle.playerList) {
                    if (p.ID != ID) {
                        byte sameTeam = (byte) ((handle.team1.contains(ID) && handle.team1.contains(p.ID))
                                || (handle.team2.contains(ID) && handle.team2.contains(p.ID)) ? 12 : 0);
                        out.addMesssage(Server
                                .putString(ByteBuffer.allocate(75 + 4 + 4 + 4 * p.username.length()).putInt(p.ID),
                                        p.username)
                                .put(p.cloth).putInt(p.colord[0]).putInt(p.colord[1]).putInt(p.colord[2])
                                .putInt(p.colord[3]).putInt(p.colord[4]).putInt(p.colord[5]).putInt(p.colord2[0])
                                .putInt(p.colord2[1]).putInt(p.colord2[2]).putInt(p.colord2[3]).putInt(p.colord2[4])
                                .putInt(p.colord2[5]).put(sameTeam), Server.LOGIN);
                        p.out.addMesssage(Server
                                .putString(ByteBuffer.allocate(75 + 4 + 4 + 4 * username.length()).putInt(ID), username)
                                .put(clothing).putInt(colors[0]).putInt(colors[1]).putInt(colors[2]).putInt(colors[3])
                                .putInt(colors[4]).putInt(colors[5]).putInt(colors2[0]).putInt(colors2[1])
                                .putInt(colors2[2]).putInt(colors2[3]).putInt(colors2[4]).putInt(colors2[5])
                                .put(sameTeam), Server.LOGIN);
                    }
                }
                loggedIn = true;
                break;
            case Server.LOGOUT:
                killMe();
                break;
            case Server.MOVE:
                ByteBuffer toRead = Server.readByteBuffer(in);

                x = toRead.getShort();
                y = toRead.getShort();
                move = toRead.getShort();
                vspeed = toRead.getShort();
                leftArmAngle = toRead.getShort();
                rightArmAngle = toRead.getShort();
                status = toRead.getShort();
                HP = toRead.getShort();
                // System.out.print(ID+":");
                handle.movePlayer(ID, x, y, move, vspeed, (int) leftArmAngle, (int) rightArmAngle, status, HP);
                break;
            case Server.DIG:
                toRead = Server.readByteBuffer(in);
                int ix, iy, ir;
                ix = toRead.getInt();
                iy = toRead.getInt();
                ir = toRead.getInt();
                handle.earth.ground.ClearCircle(ix, iy, ir);
                toRead.rewind();
                // System.out.println("DIG!");
                ByteBuffer toSend = ByteBuffer.allocate(12);
                handle.sendMessage(Server.DIG, toSend.putInt(ix).putInt(iy).putInt(ir));
                break;
            case Server.FILL:
                toRead = Server.readByteBuffer(in);
                ix = toRead.getInt();
                iy = toRead.getInt();
                ir = toRead.getInt();
                byte etg = toRead.get();
                handle.earth.ground.FillCircleW(ix, iy, ir, etg);
                toRead.rewind();
                // System.out.println("FILL!");
                toSend = ByteBuffer.allocate(12);
                handle.sendMessage(Server.FILL, toSend.putInt(ix).putInt(iy).putInt(ir));
                break;
            case Server.MAP:
                // System.out.println("MAP REQUEST!");
                // out.write(Server.MAP);
                ByteBuffer buf;
                buf = Server.readByteBuffer(in);
                int d = buf.getInt();
                int viewX, viewY;
                viewX = Math.min(Math.max(x - 50, 0), handle.earth.wIdTh - 100);
                // viewY = Math.min(Math.max(p.y-150,0),earth.hEigHt-300);
                toSend = ByteBuffer.allocate(300000);
                toSend.putInt(viewX);
                // p.out.writeInt(viewY);
                for (int i = viewX; i < viewX + 100; i++) {
                    toSend.put(handle.earth.ground.cellData[i]);
                }
                handle.sendMessage(Server.MAP, toSend);
                // System.out.println("SENt");
                // Server.writeByteBuffer(toSend, out);

                break;
            case Server.MESSAGE:
                // System.out.println("MESSAGE RECIEVED!");
                buf = Server.readByteBuffer(in);
                int color = buf.getInt();
                String yes = Server.getString(buf);
                if (yes.contains("/nextmap")) {
                    if (!voted) {
                        handle.nextVote++;
                        yes = (handle.nextVote + ((handle.nextVote > 1 ? " players are " : " player is ")
                                + "voting for ending the match."));
                        voted = true;
                    }
                }
                handle.sendMessage(Server.MESSAGE,
                        Server.putString(ByteBuffer.allocate(yes.length() * 4 + 4).putInt(color), yes));
                // System.out.println(new String(buff).trim());
                break;
            case Server.SPELL:
                buf = Server.readByteBuffer(in);
                int subID = buf.getInt();
                int Xx = buf.getInt();
                int Yy = buf.getInt();
                int mX = buf.getInt();
                int mY = buf.getInt();
                int Iw = Server.getID();
                if (Spell.getSpell(subID) instanceof EarthbendingSand) {
                    //TODO this code is duplicated and should not be
                    int number = handle.earth.ground.sandinate(Xx, Yy, 96);
                    number /= (32);
                    handle.earth.entityList.add(new SandEntity(Xx, Yy, mX, mY, ID).setID(Iw));
                    if (number > 3) {
                        handle.earth.entityList.add(new SandEntity(Xx, Yy, mX + (int) Main.lengthdir_x(4, 30),
                                mY + (int) Main.lengthdir_y(4, 30), ID).setID(Iw + 1));
                        handle.earth.entityList.add(new SandEntity(Xx, Yy, mX + (int) Main.lengthdir_x(4, -30),
                                mY + (int) Main.lengthdir_y(4, -30), ID).setID(Iw + 2));
                        Server.MYID += 2;
                    }
                    if (number > 5) {
                        handle.earth.entityList.add(new SandEntity(Xx, Yy, mX + (int) Main.lengthdir_x(4, 45),
                                mY + (int) Main.lengthdir_y(4, 45), ID).setID(Iw + 3));
                        handle.earth.entityList.add(new SandEntity(Xx, Yy, mX + (int) Main.lengthdir_x(4, -45),
                                mY + (int) Main.lengthdir_y(4, -45), ID).setID(Iw + 4));
                        Server.MYID += 2;
                    }
                    if (number > 7) {
                        handle.earth.entityList.add(new SandEntity(Xx, Yy, mX + (int) Main.lengthdir_x(4, 60),
                                mY + (int) Main.lengthdir_y(4, 60), ID).setID(Iw + 5));
                        handle.earth.entityList.add(new SandEntity(Xx, Yy, mX + (int) Main.lengthdir_x(4, -60),
                                mY + (int) Main.lengthdir_y(4, -60), ID).setID(Iw + 6));
                        Server.MYID += 2;
                    }
                    if (number > 12) {
                        handle.earth.entityList.add(new SandEntity(Xx, Yy, mX + (int) Main.lengthdir_x(4, 15),
                                mY + (int) Main.lengthdir_y(4, 15), ID).setID(Iw + 7));
                        handle.earth.entityList.add(new SandEntity(Xx, Yy, mX + (int) Main.lengthdir_x(4, -15),
                                mY + (int) Main.lengthdir_y(4, -15), ID).setID(Iw + 8));
                        Server.MYID += 2;
                    }
                    if (number > 16) {
                        handle.earth.entityList.add(new SandEntity(Xx, Yy, mX + (int) Main.lengthdir_x(4, 35),
                                mY + (int) Main.lengthdir_y(4, 35), ID).setID(Iw + 9));
                        handle.earth.entityList.add(new SandEntity(Xx, Yy, mX + (int) Main.lengthdir_x(4, -35),
                                mY + (int) Main.lengthdir_y(4, -35), ID).setID(Iw + 10));
                        Server.MYID += 2;
                    }
                    if (number > 20) {
                        handle.earth.entityList.add(new SandEntity(Xx, Yy, mX + (int) Main.lengthdir_x(4, 45),
                                mY + (int) Main.lengthdir_y(4, 45), ID).setID(Iw + 11));
                        handle.earth.entityList.add(new SandEntity(Xx, Yy, mX + (int) Main.lengthdir_x(4, -45),
                                mY + (int) Main.lengthdir_y(4, -45), ID).setID(Iw + 12));
                        Server.MYID += 2;
                    }
                    handle.sendMessage(Server.SPELL, ByteBuffer.allocate(32).putInt(subID).putInt(Xx).putInt(Yy)
                            .putInt(mX).putInt(mY).putInt(ID).putInt(Iw).putInt(number));
                    Server.MYID += 2;
                } else {
                    Spell.getSpell(subID).getActionNetwork(handle.earth, Xx, Yy, mX, mY, ID, Iw, buf);
                    handle.sendMessage(Server.SPELL, ByteBuffer.allocate(28).putInt(subID).putInt(Xx).putInt(Yy)
                            .putInt(mX).putInt(mY).putInt(ID).putInt(Iw));
                }
                break;
            case Server.DEATH:
                buf = Server.readByteBuffer(in);
                subID = buf.getInt();
                if (subID != ID) {
                    // handle.score[subID]++;
                    for (PlayerOnline p : handle.playerList) {
                        if (p.ID == subID) {
                            p.score++;
                        }
                    }
                }
                handle.sendMessage(Server.DEATH, ByteBuffer.allocate(9).putInt(subID).putInt(ID));
                break;
            case Server.DRAIN:
                buf = Server.readByteBuffer(in);
                subID = buf.getInt();
                int hpt = buf.getInt();
                if (subID != ID) {
                    // handle.score[subID]++;
                    for (PlayerOnline p : handle.playerList) {
                        if (p.ID == subID) {
                            p.out.addMesssage(ByteBuffer.allocate(4).putInt(hpt), Server.DRAIN);
                        }
                    }
                }
                break;
        }
    }

    public void writeMovePlayer(int id, int X, int Y, int M, int V, int LA, int RA, short st, short hp) {
        try {
            ByteBuffer toSend = ByteBuffer.allocate(6 * 4);
            // out.write(Server.MOVE);
            toSend.putShort((short) id);
            toSend.putShort((short) X);
            toSend.putShort((short) Y);
            toSend.putShort((short) M);
            toSend.putShort((short) V);
            toSend.putShort((short) LA);
            toSend.putShort((short) RA);
            toSend.putShort((short) st);
            toSend.putShort((short) hp);
            out.addMesssage(toSend, Server.MOVE);
            // Server.writeByteBuffer(toSend, out);
            // System.out.println("("+x+","+y+")");
        } catch (Exception ex) {
            ex.printStackTrace();
            killMe();
        }
    }

    public void writeNewPlayer(int id, String user) {
        try {
            // out.write(Server.LOGIN);
            // out.write(id);
            // out.flush();
            out.addMesssage(Server.putString(ByteBuffer.allocate(4 + user.length() * 4).putInt(id), user),
                    Server.LOGIN);
        } catch (Exception ex) {
            // ex.printStackTrace();
            System.err.println("I LEFT BECAUSE I AM A FATTY! ~ " + ID);
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
            toSend.putInt(handle.gameMode);
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
            out.addMesssage(toSend, Server.ENTIREWORLD);
            // Server.writeByteBuffer(toSend,out);
            for (int t = 0; t < chunks.length; t += 1) {
                // Server.writeByteBuffer(chunks[t],out);
                out.addMesssage(chunks[t], Server.CHUNK);
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
                    // ex.printStackTrace();
                    killMe();
                }
            }

        } catch (Exception ex) {
            // ex.printStackTrace();
            killMe();
        }
    }
}