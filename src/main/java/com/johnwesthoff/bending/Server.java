package com.johnwesthoff.bending;

import java.awt.Polygon;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.johnwesthoff.bending.entity.BallLightningEntity;
import com.johnwesthoff.bending.entity.BuritoEntity;
import com.johnwesthoff.bending.entity.EnemyEntity;
import com.johnwesthoff.bending.entity.Entity;
import com.johnwesthoff.bending.entity.FireBallEntity;
import com.johnwesthoff.bending.entity.FireJumpEntity;
import com.johnwesthoff.bending.entity.FirePuffEntity;
import com.johnwesthoff.bending.entity.GuardianEntity;
import com.johnwesthoff.bending.entity.GustEntity;
import com.johnwesthoff.bending.entity.HillEntity;
import com.johnwesthoff.bending.entity.IceShardEntity;
import com.johnwesthoff.bending.entity.LavaBallEntity;
import com.johnwesthoff.bending.entity.MissileEntity;
import com.johnwesthoff.bending.entity.PumpkinEntity;
import com.johnwesthoff.bending.entity.RockEntity;
import com.johnwesthoff.bending.entity.SandEntity;
import com.johnwesthoff.bending.entity.ShardEntity;
import com.johnwesthoff.bending.entity.SnowEntity;
import com.johnwesthoff.bending.entity.SoulDrainEntity;
import com.johnwesthoff.bending.entity.SpoutEntity;
import com.johnwesthoff.bending.entity.TornadoEntity;
import com.johnwesthoff.bending.entity.WallofFireEntity;
import com.johnwesthoff.bending.logic.Player;
import com.johnwesthoff.bending.logic.PlayerOnline;
import com.johnwesthoff.bending.logic.World;
import com.johnwesthoff.bending.util.network.ConnectToDatabase;
/**
 *
 * @author John
 */
public final class Server implements Runnable {
    public static final byte // MESSAGE IDs
    LOGIN = 0, MOVE = 1, LEAVE = 2, MAP = 3, ENTITY = 4, AI = 5, MESSAGE = 6, WORLDEXPAND = 7, ENTIREWORLD = 8, DIG = 9,
            FILL = 10, ID = 11, CHUNK = 12, AIRBENDING = 13, EARTHBENDING = 14, WATERBENDING = 15, FIREBENDING = 16,
            FREEZE = 17, PUDDLE = 18, DEATH = 19, CHARGE = 20, LIGHTNING = 21, DESTROY = 22, STEAM = 23, HURT = 24,
            SANDINATE = 25, LOGOUT = 26, SCORE = 27, DARKNESS = 28, DRAIN = 29, SPELL = 30;
    public static final int TEAMDEATHMATCH = 1, FREEFORALL = -1, KINGOFTHEHILL = -2, THEHIDDEN = 2, SURVIVAL = 3,
            DEFENDER = 4;
    public static final byte // MESSAGE IDs
    UDPMOVE = 0;
    public static int MYID = 0;
    public int nextVote = 0;

    public static int getID() {
        return MYID++;
    }

    public static final String MESSAGEIDs[] = new String[] { "Player Login", "Player Move", "Player Left",
            "Map Request", "Entity Request", "AI Request", "Chat Message", "World Expansion", "Entire World", "Dig",
            "Fill", "ID", "Chunk", "Airbending", "Earthbending", "Boats Float In The Ocean!", "Firebending", "Freeze",
            "BOATS_IN_THE_KINDOM", "Death", "Charge", "Lightning", "Entity Kill", "Entity Steam", "Hurt", "SANDINATE",
            "QUITTER", "POINTS", "DARKNESS", "DRAIN", "SPELL" };
    int port = 25565;
    public ArrayList<Integer> team1 = new ArrayList<>(), team2 = new ArrayList<>();
    public ArrayList<PlayerOnline> playerList = new ArrayList<>();
    static ServerSocket SocialSecurity;
    Thread playerAcceptor = new Thread(this);
    int spawnX, spawnY, pID = 1, eID = 0;
    public World earth = new World();
    boolean goLeft = false, goRight = false;
    public int score[] = new int[256];
    public static int gameMode = 1;
    static Random random = new Random();

    public static int choose(final int... e) {
        return (e[random.nextInt(e.length)]);
    }

    public void kill() {
        try {
            this.playerAcceptor.interrupt();
            worldHandle.interrupt();
            expander.interrupt();
            SocialSecurity.close();
            try {
                if (!playerList.isEmpty()) {
                    for (final PlayerOnline p : playerList)
                        p.killMe();
                }
            } catch (final Exception e) {

            }
            gameRunning = false;
            playerList.clear();
        } catch (final IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    static long oldTime = 0;

    public static void setTime() {
        oldTime = System.nanoTime();
    }

    public static float deltaTime() {
        final float lol = ((System.nanoTime() - oldTime)) / (25f * 1000000f);
        System.out.println("Delta Time: " + lol);
        return lol;
    }

    int index = 0;
    long oldTim;
    Thread worldHandle, udplistener;

    public Server() {
        try {
            SocialSecurity = new ServerSocket(25565);
            playerAcceptor.start();
            spawnX = (int) earth.x;
            spawnY = (int) earth.y;
            earth.lol = this;
            loadMap(1);
            startExpander();
            // udplistener = new Thread(new UDPThread());
            // udplistener.start();

            worldHandle = new Thread() {
                @Override
                public void run() {
                    long lastTime = System.nanoTime();
                    double delta = 0;
                    while (gameRunning) {
                        try {
                            Thread.sleep(1);
                        } catch (final InterruptedException e2) {
                            e2.printStackTrace();
                        }
                        final long l = System.nanoTime();
                        delta += (l - lastTime) / (1000000000 / 40.0d);

                        swagTime = l;
                        lastTime = l;

                        if (earth == null) {
                            delta = 0;
                            continue;
                        }
                        while (delta >= 1) {
                            delta -= 1;
                            index++;
                            // System.out.println(""+World.deltaTime());
                            earth.onUpdate();
                            if (nextVote * 3 > playerList.size() * 2) {
                                expander.interrupt();// muwahahaa
                            }
                            if (gameMode == THEHIDDEN) {
                                if (playerList.size() > 1 && playerList.get(0).score >= (playerList.size() - 1)) {
                                    expander.interrupt();
                                }
                            }
                            if (gameMode == SURVIVAL) {
                                final long tim = System.currentTimeMillis();
                                if (tim - oldTim > 1000 * 20) {
                                    oldTim = tim;
                                    for (int i = 0; i <= 5; i++) {
                                        final int yay = getID(), xxxx = earth.random.nextInt(earth.wIdTh);

                                        earth.entityList.add(new EnemyEntity(xxxx, 0, 0, 0, -2).setID(yay));
                                        sendMessage(Server.DARKNESS, ByteBuffer.allocate(28).putInt(10).putInt(xxxx)
                                                .putInt(0).putInt(0).putInt(0).putInt(-2).putInt(yay));
                                    }
                                }
                            }
                            // fixStuff();
                            // System.out.println(lastTime);
                            try {
                                earth.ground.handleWater();
                            } catch (final Exception ex) {
                                // Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            // earth.ground.ShowData();
                            handleAI();
                        }
                        World.setTime();
                    }
                }
            };
            worldHandle.start();
        } catch (final IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static Server main2(final String[] args) {
        final Server me = new Server();
        me.IP = args[0];
        return me;
        // me.port = Integer.getInteger(WhyMustJavaBeSoArguementative[0]);
        // me.earth.ground.ClearCircle(100, 100, 400);
    }

    public static void main(final String[] args) {
        final Server me = new Server();
        me.IP = args[0];
        // me.port = Integer.getInteger(WhyMustJavaBeSoArguementative[0]);
        // me.earth.ground.ClearCircle(100, 100, 400);
    }

    int runThrough = 0;
    public String IP = "";
    boolean gameRunning = true, accepting = true;
    ConnectToDatabase INSTANCE = ConnectToDatabase.INSTANCE();
    long swagTime = 0;

    @Override
    public void run() {
        while (gameRunning && accepting) {

            try {
                final Socket toAdd = (SocialSecurity.accept());
                toAdd.setKeepAlive(false);
                toAdd.setTcpNoDelay(true);
                playerList.add(new PlayerOnline(spawnX, spawnY, toAdd, pID, this));
                INSTANCE.updateCount(IP, playerList.size());
                if ((gameMode == THEHIDDEN)) {
                    if ((team1.isEmpty())) {
                        team1.add(pID);
                    } else {
                        team2.add(pID);
                    }
                } else {
                    if (gameMode == SURVIVAL) {
                        team1.add(pID);
                    } else {
                        if ((team1.size() > team2.size())) {
                            team2.add(pID);
                        } else {
                            team1.add(pID);
                        }
                    }
                }
                pID++;
                // Thread.sleep(10);
                // newPlayer();

                Thread.sleep(100);
            } catch (IOException | InterruptedException ex) {
                // return;
            }
        }
    }

    public void newPlayer(final int id, final String user) {
        for (final PlayerOnline p : playerList) {
            if (id != p.ID) {
                p.writeNewPlayer(id, user);
            }
        }

    }

    public void sendMessage(final byte id, final ByteBuffer mes) {
        for (final PlayerOnline p : playerList) {
            try {
                // p.out.write(id);
                // Server.writeByteBuffer(mes, p.out);
                p.out.addMesssage(mes, id);
            } catch (final Exception ex) {
                // Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void movePlayer(final int id, final int x, final int y, final int m, final int v, final int la, final int ra,
            final short st, final short hp) {
        for (final PlayerOnline p : playerList) {
            if (p.ID != id) {
                p.writeMovePlayer(id, x, y, m, v, la, ra, st, hp);
            }
        }
    }

    public void moveRelative(final int x, final int y) {
        for (final PlayerOnline p : playerList) {
            p.writeMovePlayer(p.ID, p.x + x, p.y + y, p.move, p.vspeed, (int) p.leftArmAngle, (int) p.rightArmAngle,
                    p.status, p.HP);
        }
    }

    int call = 0;

    public void fixStuff() {
        call++;
        for (final Entity p : earth.entityList) {
            if (p instanceof EnemyEntity) {
                final EnemyEntity e = (EnemyEntity) p;
                e.onServerUpdate(this);
                if (call > 100) {
                    call = 0;
                    final ByteBuffer aIMessage = ByteBuffer.allocate(1000);
                    aIMessage.putInt((int) e.X).putInt((int) e.Y).putInt(e.HP).putInt((int) e.move)
                            .putInt((int) e.yspeed).putInt(e.target).putInt(e.id);
                    sendMessage(AI, aIMessage);
                }
            }
        }

    }

    public Thread expander;

    public void startExpander() {
        expander = new Thread(new Runnable() {
            @Override
            public void run() {
                while (gameRunning) {
                    loadMap(mapRotation);
                    try {
                        Thread.sleep(5 * 60 * 1000);
                    } catch (final InterruptedException ex) {
                        // Up! Time for a new map!
                    }
                }
            }
        });
        expander.start();
    }

    public int mapRotation = 0;
    public int maxMap = 1;

    public static void writeByteBuffer(final ByteBuffer toSend, final OutputStream output) throws IOException {

        final byte yay[] = new byte[toSend.position()];// toSend.slice().array();
        // S System.a
        System.arraycopy(toSend.array(), 0, yay, 0, toSend.position());
        // output.write(toSend.position());
        final byte[] size = ByteBuffer.allocate(4).putInt(yay.length).array();
        // System.err.println(yay.length);
        output.write(size);
        output.write(yay);
        output.flush();
    }

    public static ByteBuffer putString(final ByteBuffer yes, final String y) {
        yes.putInt(y.length());
        yes.put(y.getBytes());
        return yes;
    }

    public static String getString(final ByteBuffer yes) {
        final byte[] dst = new byte[yes.getInt()];
        yes.get(dst);
        return new String(dst);
    }

    public static ByteBuffer readByteBuffer(final InputStream in) throws IOException {
        final ByteBuffer size = ByteBuffer.allocate(4);

        size.put((byte) in.read());
        size.put((byte) in.read());
        size.put((byte) in.read());
        size.put((byte) in.read());
        size.rewind();
        final int howMuchData = size.getInt();
        // System.err.println(howMuchData);
        int total = 0;
        final byte[] buf = new byte[howMuchData];
        while (total < howMuchData) {
            total += in.read(buf, total, howMuchData - total);
        }
        return ByteBuffer.wrap(buf);
    }

    public String getKiller(final int i) {
        for (final Player p : playerList) {
            if (p.ID == i) {
                return p.username;
            }
        }
        return "NULL";
    }

    public PlayerOnline getPlayer(final int i) {
        for (final PlayerOnline p : playerList) {
            if (p.ID == i) {
                return p;
            }
        }
        return null;
    }

    String dir = System.getenv("APPDATA") + File.separator + "Bending" + File.separator;

    public void loadMap(final int i) {
        nextVote = 0;
        if (++mapRotation > maxMap) {
            mapRotation = 0;
        }

        String yes;
        if (gameMode == TEAMDEATHMATCH) {
            int score1 = 0, score2 = 0;
            String teamname1 = "", teamname2 = "";
            for (final PlayerOnline p : playerList) {
                if (team1.contains(p.ID)) {
                    score1 += p.score;
                    teamname1 += p.username + " ";
                } else {
                    score2 += p.score;
                    teamname2 += p.username + " ";
                }
                p.score = 0;
            }
            if (score1 > score2) {
                yes = "This match's winners were: " + teamname1;
                sendMessage(MESSAGE, Server.putString(ByteBuffer.allocate(yes.length() * 4 + 4).putInt(0xFF0000), yes));
            } else if (score1 < score2) {
                yes = "This match's winners were: " + teamname2;
                sendMessage(MESSAGE, Server.putString(ByteBuffer.allocate(yes.length() * 4 + 4).putInt(0xFF0000), yes));
            } else {
                yes = "The matched ended in... a tie!";
                sendMessage(MESSAGE, Server.putString(ByteBuffer.allocate(yes.length() * 4 + 4).putInt(0xFF0000), yes));
            }
        } else {
            int max = -1;
            int winner = 0;
            for (final Entity e : earth.entityList) {
                e.setAlive(false);
            }
            earth.entityList.clear();
            for (final PlayerOnline P : playerList) {
                if (P.score > max) {
                    max = P.score;
                    winner = P.ID;
                }
            }
            yes = getKiller(winner) + " won the round!";
            sendMessage(MESSAGE, Server.putString(ByteBuffer.allocate(yes.length() * 4 + 4).putInt(0xFF0000), yes));
        }
        gameMode = choose(FREEFORALL, TEAMDEATHMATCH, KINGOFTHEHILL, THEHIDDEN, DEFENDER);
        Collections.shuffle(playerList);
        team1.clear();
        team2.clear();
        for (final PlayerOnline P : playerList) {
            P.score = 0;
        }
        int id;
        if (gameMode == THEHIDDEN) {
            if (playerList.size() > 0) {
                team1.add(playerList.get(0).ID);
                System.out.println(playerList.get(0).username + " joined red");
                for (int index = 1; index < playerList.size(); index++) {
                    id = playerList.get(index).ID;
                    team2.add(id);
                    System.out.println(playerList.get(index).username + " joined red");
                }
            }
        } else {
            if (gameMode == THEHIDDEN) {
                for (int index = 0; index < playerList.size(); index++) {
                    id = playerList.get(index).ID;
                    team1.add(id);
                    System.out.println(playerList.get(index).username + " joined red");

                }
            } else {
                for (int index = 0; index < playerList.size(); index++) {
                    id = playerList.get(index).ID;
                    if (index % 2 == 0) {
                        team1.add(id);
                        System.out.println(playerList.get(index).username + " joined red");
                    } else {
                        team2.add(id);
                        System.out.println(playerList.get(index).username + " joined blue");
                    }
                }
            }
        }

        String gm = "";
        switch (gameMode) {
            case TEAMDEATHMATCH:
                gm = "Team Death Match";
                break;
            case FREEFORALL:
                gm = "Free for All";
                break;
            case KINGOFTHEHILL:
                gm = "King of the Hill";
                break;
            case THEHIDDEN:
                gm = "The Hidden";
                break;
            case SURVIVAL:
                gm = "Survival";
                break;
            case DEFENDER:
                gm = "Defender";
                break;
        }
        yes = "The next game type will be " + gm + ".";

        sendMessage(MESSAGE, Server.putString(ByteBuffer.allocate(yes.length() * 4 + 4).putInt(0x00FF3C), yes));
        if (gameMode == Server.DEFENDER) {
            for (final PlayerOnline P : playerList) {
                gm = "You will be a" + (team1.contains(P.ID) ? " defender." : "n attacker.");
                try {
                    P.out.addMesssage(Server.putString(ByteBuffer.allocate(gm.length() * 4 + 4).putInt(0x00FF3C), gm),
                            Server.MESSAGE);
                } catch (final IOException ex) {
                }
            }
        }
        Arrays.fill(score, 0);
        earth.ground.ClearCircleStrong(150, 150, 9000);
        final File mapsFolder = new File(dir + "maps");
        System.out.println(mapsFolder.getPath());
        final File[] mapFiles = mapsFolder.listFiles();
        if (mapFiles == null || mapFiles.length == 0) {
            switch (i) {
                default:
                case 0:
                    earth.ground.FillCircle(150, 900, 300);
                    earth.ground.FillCircle(450, 900, 300);
                    earth.ground.FillCircle(750, 900, 300);
                    break;
                case 1:
                    earth.ground.FillRectW(0, 800, 100, 900, World.LAVA);
                    for (int x = 0; x <= 900; x += 100) {
                        earth.ground.FillCircleW(x, 800, 100, World.STONE);
                    }
                    break;
                case 2:
                    final Polygon P = new Polygon();
                    P.addPoint(0, 900);
                    P.addPoint(25, 800);
                    P.addPoint(100, 600);
                    P.addPoint(300, 750);
                    P.addPoint(400, 700);
                    P.addPoint(500, 500);
                    P.addPoint(600, 720);
                    P.addPoint(700, 820);
                    P.addPoint(800, 860);
                    P.addPoint(900, 900);
                    earth.ground.FillRectW(0, 700, 200, 900, World.WATER);
                    earth.ground.FillPolygon(P, World.ICE);
                    break;
            }
        } else {
            final Random r = new Random();
            boolean invalid = true;
            while (invalid) {
                final int toPick = r.nextInt(mapFiles.length);
                final File chosen = mapFiles[toPick];
                if (chosen != null && chosen.isFile() && chosen.canRead() && chosen.getName().endsWith(".ter")) {
                    try {
                        loadMap(chosen);
                        invalid = false;
                    } catch (final Exception e) {

                    }
                }
            }
        }
        // earth.entityList.add(new EnemyEntity(300,300,0,0,500).setID(Server.getID()));
        switch (gameMode) {
            default:
                earth.entityList.clear();
                break;
            case KINGOFTHEHILL:
                earth.entityList.add(new HillEntity(earth.wIdTh / 2, earth.hEigHt / 2, 0, 0).setID(Server.getID()));
                break;
            case DEFENDER:
                earth.entityList
                        .add(new GuardianEntity(earth.wIdTh / 4, earth.hEigHt / 2, 0, 0, -2).setID(Server.getID()));
                break;
        }
        earth.entityList.add(new PumpkinEntity(earth.wIdTh / 2, earth.hEigHt / 2).floor(earth).setID(Server.getID()));
        for (final PlayerOnline p : playerList) {
            p.writeWorld();
        }
    }

    public void loadMap(final File f) throws Exception {
        DataInputStream fos;
        System.out.println("LOADING MAP: " + f.getName());
        fos = new DataInputStream(new FileInputStream(f));
        final int xxxxxx = fos.readInt(), yyyyy = fos.readInt();
        earth.ground.cellData = new byte[xxxxxx][yyyyy];
        earth.ground.w = xxxxxx;
        earth.ground.h = yyyyy;
        earth.wIdTh = xxxxxx;
        earth.hEigHt = yyyyy;
        for (int i = 0; i < earth.ground.cellData.length; i++) {
            final byte in[] = new byte[yyyyy];
            fos.read(in);
            earth.ground.cellData[i] = in;
        }
        fos.close();

    }

    public void handleAI() {
        for (final Entity ai : earth.entityList) {
            if (ai instanceof EnemyEntity) {
                final EnemyEntity AI = (EnemyEntity) ai;
                for (final Entity e : earth.entityList) {
                    if (ai.distanceToEntity(e) < 16 && ai != e && e.alive) {
                        if (e instanceof MissileEntity) {
                            e.setAlive(false);
                            AI.lastHit = e.maker;
                            AI.HP -= 70;
                            sendMessage(Server.DESTROY, ByteBuffer.allocate(30).putInt(e.MYID));
                        }
                        if (e instanceof TornadoEntity) {
                            AI.lastHit = e.maker;
                            e.setAlive(false);
                            AI.HP -= 100;
                            sendMessage(Server.DESTROY, ByteBuffer.allocate(30).putInt(e.MYID));
                        }
                        if (e instanceof GustEntity) {
                            AI.lastHit = e.maker;
                            e.setAlive(false);
                            AI.HP -= 40;
                            sendMessage(Server.DESTROY, ByteBuffer.allocate(30).putInt(e.MYID));
                        }
                        if (e instanceof RockEntity) {
                            AI.lastHit = e.maker;
                            e.setAlive(false);
                            AI.HP -= 120;
                            sendMessage(Server.DESTROY, ByteBuffer.allocate(30).putInt(e.MYID));
                        }
                        if (e instanceof FireBallEntity) {
                            AI.lastHit = e.maker;
                            e.setAlive(false);
                            AI.HP -= 200;
                            sendMessage(Server.DESTROY, ByteBuffer.allocate(30).putInt(e.MYID));
                        }
                        if (e instanceof FirePuffEntity) {
                            AI.lastHit = e.maker;
                            e.setAlive(false);
                            AI.HP -= 20;
                            sendMessage(Server.DESTROY, ByteBuffer.allocate(30).putInt(e.MYID));
                        }
                        if (e instanceof BuritoEntity) {
                            AI.lastHit = e.maker;
                            e.setAlive(false);
                            AI.HP -= 500;
                            sendMessage(Server.DESTROY, ByteBuffer.allocate(30).putInt(e.MYID));
                        }
                        if (e instanceof LavaBallEntity) {
                            AI.lastHit = e.maker;
                            e.setAlive(false);
                            AI.HP -= 300;
                            sendMessage(Server.DESTROY, ByteBuffer.allocate(30).putInt(e.MYID));
                        }
                        if (e instanceof SoulDrainEntity) {
                            AI.lastHit = e.maker;
                            e.setAlive(false);
                            AI.HP -= 100;
                            sendMessage(Server.DESTROY, ByteBuffer.allocate(30).putInt(e.MYID));
                        }
                        if (e instanceof FireJumpEntity) {
                            AI.lastHit = e.maker;
                            e.setAlive(false);
                            AI.HP -= 200;
                            sendMessage(Server.DESTROY, ByteBuffer.allocate(30).putInt(e.MYID));
                        }
                        if (e instanceof ShardEntity) {
                            AI.lastHit = e.maker;
                            e.setAlive(false);
                            AI.HP -= 160;
                            sendMessage(Server.DESTROY, ByteBuffer.allocate(30).putInt(e.MYID));
                        }
                        if (e instanceof SandEntity) {
                            AI.lastHit = e.maker;
                            e.setAlive(false);
                            AI.HP -= 200;
                            sendMessage(Server.DESTROY, ByteBuffer.allocate(30).putInt(e.MYID));
                        }
                        if (e instanceof IceShardEntity) {
                            AI.lastHit = e.maker;
                            e.setAlive(false);
                            AI.HP -= 100;
                            sendMessage(Server.DESTROY, ByteBuffer.allocate(30).putInt(e.MYID));
                        }
                        if (e instanceof SnowEntity) {
                            AI.lastHit = e.maker;
                            e.setAlive(false);
                            AI.HP -= 30;
                            sendMessage(Server.DESTROY, ByteBuffer.allocate(30).putInt(e.MYID));
                        }
                        if (e instanceof SpoutEntity) {
                            AI.lastHit = e.maker;
                            e.setAlive(false);
                            AI.HP -= 30;
                            sendMessage(Server.DESTROY, ByteBuffer.allocate(30).putInt(e.MYID));
                        }
                        if (e instanceof BallLightningEntity) {
                            AI.lastHit = e.maker;
                            e.setAlive(false);
                            AI.HP -= 100;
                            sendMessage(Server.DESTROY, ByteBuffer.allocate(30).putInt(e.MYID));
                        }
                        if (e instanceof WallofFireEntity) {
                            AI.lastHit = e.maker;
                            e.setAlive(false);
                            AI.HP -= 222;
                            sendMessage(Server.DESTROY, ByteBuffer.allocate(30).putInt(e.MYID));
                        }
                    }
                }
            } else {
                continue;
            }
        }
    }
}
