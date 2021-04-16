package com.johnwesthoff.bending;

import java.io.InputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Random;

import com.johnwesthoff.bending.app.game.GameService;
import com.johnwesthoff.bending.logic.Player;
import com.johnwesthoff.bending.logic.World;
import com.johnwesthoff.bending.spells.Spell;
import com.johnwesthoff.bending.util.network.OrderedOutputStream;
import com.johnwesthoff.bending.util.network.StringLongBoolean;

public class Session {
    public Client client;
    public ClientUI clientui;
    public ClientNetworking net;
    public boolean goodTeam = false;
    public String[] chat = { "", "", "", "", "", "", "", "", "", "" };
    public static StringLongBoolean unlocks = new StringLongBoolean("0");
    public int score = 0;
    public int mapRotation = 0;
    public boolean chatActive = false;
    public String chatMessage = "";
    public int gameMode = 1;
    public int shockdrain = 0;
    public int fireTime = 0;
    public int ticks = 0;
    public Player localPlayer;
    public int whoseTurn = -1;
    public short matchOver = 0, forcedRespawn = 0;
    public ArrayList<Integer> myTeam = new ArrayList<>(), badTeam = new ArrayList<>();
    public static boolean currentlyLoggedIn = false;
    public double maxeng, dpyeng, energico = maxeng = dpyeng = 1000;
    public int port = Constants.PORT;
    public Properties userpassinfo;
    public double engrecharge = 4;
    public Random random = new Random();
    public String serverIP;
    public Thread mainProcess;
    public boolean notDone = true;
    public boolean ignored = true;
    public URL base;
    public String temp;
    public static String username;
    public World world;
    public double killingSpree = 0;
    public boolean loggedOn = false;
    public Server hostingPlace;
    public int spellBook = 0;
    public LinkedList<World> worldList = new LinkedList<>();
    public long lastTime = 0;
    public InputStream input;
    public OrderedOutputStream out;
    public Socket connection;
    public short MAXHP, HP = MAXHP = 100;
    public static boolean gameAlive = true;
    public int maxlungs, lungs = maxlungs = 100;
    public double runningSpeed = 1d, swimmingSpeed = 1d;
    public double knockbackDecay;
    public static Client thisone;
    public static int XP = 0;
    public double prevVspeed = 0;
    public static boolean shortJump = false;
    public String killMessage = "~ was defeated by `.";
    public int timeToHeal = 0;
    public String[] hosts = new String[1];
    public Spell[][] spellList;
    public Spell[] passiveList;
    public int leftClick = 0, rightClick = 1, midClick = 2;
    public double xspeed = 0;
    public double prevMove;
    public short turnVisible = -1, removeAura = -1;
    public GameService gameService;
    public int ID = 0;
    public boolean started = false;
    public short dig = 0;
    public boolean loggedIn = false;
    public boolean failed = false;
    public Thread communication;
    public boolean busy = false;
    public boolean isMyTurn = true;
    public int lastHit = -1;
    public byte sendcount = 0;
    public ArrayList<int[]> stuff = new ArrayList<>();
    public boolean isAlive = true;
    public Thread expander;
    public boolean sendRequest = true;
    public int x_prev = 0, y_prev = 0;
    public int pc = 0;
    public boolean refreshShadows = false;
    public long swagTime = 0;
    public int mana_flow = 0;
    public int mana_drain = 0;
    public int serverVersion = 0;


    private static Session instance = null;

    private Session() {
        // Empty to prevent construction of session outside of this class
    }


    public static Session newInstance() {
        instance = new Session();
        return instance;
    }


    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(final Client client) {
        this.client = client;
    }

    public ClientUI getClientui() {
        return clientui;
    }

    public void setClientui(final ClientUI clientui) {
        this.clientui = clientui;
    }

    public boolean isGoodTeam() {
        return goodTeam;
    }

    public void setGoodTeam(final boolean goodTeam) {
        this.goodTeam = goodTeam;
    }

    public String[] getChat() {
        return chat;
    }

    public void setChat(final String[] chat) {
        this.chat = chat;
    }

    public static StringLongBoolean getUnlocks() {
        return unlocks;
    }

    public static void setUnlocks(final StringLongBoolean unlocks) {
        Session.unlocks = unlocks;
    }

    public int getScore() {
        return score;
    }

    public void setScore(final int score) {
        this.score = score;
    }

    public int getMapRotation() {
        return mapRotation;
    }

    public void setMapRotation(final int mapRotation) {
        this.mapRotation = mapRotation;
    }

    public boolean isChatActive() {
        return chatActive;
    }

    public void setChatActive(final boolean chatActive) {
        this.chatActive = chatActive;
    }

    public String getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(final String chatMessage) {
        this.chatMessage = chatMessage;
    }

    public int getGameMode() {
        return gameMode;
    }

    public void setGameMode(final int gameMode) {
        this.gameMode = gameMode;
    }

    public int getFireTime() {
        return fireTime;
    }

    public void setFireTime(final int fireTime) {
        this.fireTime = fireTime;
    }

    public int getTicks() {
        return ticks;
    }

    public void setTicks(final int ticks) {
        this.ticks = ticks;
    }

    public Player getLocalPlayer() {
        return localPlayer;
    }

    public void setLocalPlayer(final Player localPlayer) {
        this.localPlayer = localPlayer;
    }

    public int getWhoseTurn() {
        return whoseTurn;
    }

    public void setWhoseTurn(final int whoseTurn) {
        this.whoseTurn = whoseTurn;
    }

    public short getMatchOver() {
        return matchOver;
    }

    public void setMatchOver(final short matchOver) {
        this.matchOver = matchOver;
    }

    public short getForcedRespawn() {
        return forcedRespawn;
    }

    public void setForcedRespawn(final short forcedRespawn) {
        this.forcedRespawn = forcedRespawn;
    }

    public ArrayList<Integer> getMyTeam() {
        return myTeam;
    }

    public void setMyTeam(final ArrayList<Integer> myTeam) {
        this.myTeam = myTeam;
    }

    public ArrayList<Integer> getBadTeam() {
        return badTeam;
    }

    public void setBadTeam(final ArrayList<Integer> badTeam) {
        this.badTeam = badTeam;
    }

    public static boolean isCurrentlyLoggedIn() {
        return currentlyLoggedIn;
    }

    public static void setCurrentlyLoggedIn(final boolean currentlyLoggedIn) {
        Session.currentlyLoggedIn = currentlyLoggedIn;
    }

    public double getMaxeng() {
        return maxeng;
    }

    public void setMaxeng(final double maxeng) {
        this.maxeng = maxeng;
    }

    public double getDpyeng() {
        return dpyeng;
    }

    public void setDpyeng(final double dpyeng) {
        this.dpyeng = dpyeng;
    }

    public double getEnergico() {
        return energico;
    }

    public void setEnergico(final double energico) {
        this.energico = energico;
    }

    public int getPort() {
        return port;
    }

    public void setPort(final int port) {
        this.port = port;
    }

    public Properties getUserpassinfo() {
        return userpassinfo;
    }

    public void setUserpassinfo(final Properties userpassinfo) {
        this.userpassinfo = userpassinfo;
    }

    public double getEngrecharge() {
        return engrecharge;
    }

    public void setEngrecharge(final double engrecharge) {
        this.engrecharge = engrecharge;
    }

    public Random getRandom() {
        return random;
    }

    public void setRandom(final Random random) {
        this.random = random;
    }

    public String getServerIP() {
        return serverIP;
    }

    public void setServerIP(final String serverIP) {
        this.serverIP = serverIP;
    }

    public Thread getMainProcess() {
        return mainProcess;
    }

    public void setMainProcess(final Thread mainProcess) {
        this.mainProcess = mainProcess;
    }

    public boolean isNotDone() {
        return notDone;
    }

    public void setNotDone(final boolean notDone) {
        this.notDone = notDone;
    }

    public boolean isIgnored() {
        return ignored;
    }

    public void setIgnored(final boolean ignored) {
        this.ignored = ignored;
    }

    public URL getBase() {
        return base;
    }

    public void setBase(final URL base) {
        this.base = base;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(final String temp) {
        this.temp = temp;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(final String username) {
        Session.username = username;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(final World world) {
        this.world = world;
    }

    public double getKillingSpree() {
        return killingSpree;
    }

    public void setKillingSpree(final double killingSpree) {
        this.killingSpree = killingSpree;
    }

    public boolean isLoggedOn() {
        return loggedOn;
    }

    public void setLoggedOn(final boolean loggedOn) {
        this.loggedOn = loggedOn;
    }

    public Server getHostingPlace() {
        return hostingPlace;
    }

    public void setHostingPlace(final Server hostingPlace) {
        this.hostingPlace = hostingPlace;
    }

    public int getSpellBook() {
        return spellBook;
    }

    public void setSpellBook(final int spellBook) {
        this.spellBook = spellBook;
    }

    public LinkedList<World> getWorldList() {
        return worldList;
    }

    public void setWorldList(final LinkedList<World> worldList) {
        this.worldList = worldList;
    }

    public long getLastTime() {
        return lastTime;
    }

    public void setLastTime(final long lastTime) {
        this.lastTime = lastTime;
    }

    public InputStream getInput() {
        return input;
    }

    public void setInput(final InputStream input) {
        this.input = input;
    }

    public OrderedOutputStream getOut() {
        return out;
    }

    public void setOut(final OrderedOutputStream out) {
        this.out = out;
    }

    public Socket getConnection() {
        return connection;
    }

    public void setConnection(final Socket connection) {
        this.connection = connection;
    }

    public short getMAXHP() {
        return MAXHP;
    }

    public void setMAXHP(final short mAXHP) {
        MAXHP = mAXHP;
    }

    public short getHP() {
        return HP;
    }

    public void setHP(final short hP) {
        HP = hP;
    }

    public static boolean isGameAlive() {
        return gameAlive;
    }

    public static void setGameAlive(final boolean gameAlive) {
        Session.gameAlive = gameAlive;
    }

    public int getMaxlungs() {
        return maxlungs;
    }

    public void setMaxlungs(final int maxlungs) {
        this.maxlungs = maxlungs;
    }

    public int getLungs() {
        return lungs;
    }

    public void setLungs(final int lungs) {
        this.lungs = lungs;
    }

    public double getRunningSpeed() {
        return runningSpeed;
    }

    public void setRunningSpeed(final double runningSpeed) {
        this.runningSpeed = runningSpeed;
    }

    public double getSwimmingSpeed() {
        return swimmingSpeed;
    }

    public void setSwimmingSpeed(final double swimmingSpeed) {
        this.swimmingSpeed = swimmingSpeed;
    }

    public double getKnockbackDecay() {
        return knockbackDecay;
    }

    public void setKnockbackDecay(final double knockbackDecay) {
        this.knockbackDecay = knockbackDecay;
    }

    public static Client getThisone() {
        return thisone;
    }

    public static void setThisone(final Client thisone) {
        Session.thisone = thisone;
    }

    public static int getXP() {
        return XP;
    }

    public static void setXP(final int xP) {
        XP = xP;
    }

    public double getPrevVspeed() {
        return prevVspeed;
    }

    public void setPrevVspeed(final double prevVspeed) {
        this.prevVspeed = prevVspeed;
    }

    public static boolean isShortJump() {
        return shortJump;
    }

    public static void setShortJump(final boolean shortJump) {
        Session.shortJump = shortJump;
    }

    public String getKillMessage() {
        return killMessage;
    }

    public void setKillMessage(final String killMessage) {
        this.killMessage = killMessage;
    }

    public int getTimeToHeal() {
        return timeToHeal;
    }

    public void setTimeToHeal(final int timeToHeal) {
        this.timeToHeal = timeToHeal;
    }

    public String[] getHosts() {
        return hosts;
    }

    public void setHosts(final String[] hosts) {
        this.hosts = hosts;
    }

    public Spell[][] getSpellList() {
        return spellList;
    }

    public void setSpellList(final Spell[][] spellList) {
        this.spellList = spellList;
    }

    public Spell[] getPassiveList() {
        return passiveList;
    }

    public void setPassiveList(final Spell[] passiveList) {
        this.passiveList = passiveList;
    }

    public int getLeftClick() {
        return leftClick;
    }

    public void setLeftClick(final int leftClick) {
        this.leftClick = leftClick;
    }

    public int getRightClick() {
        return rightClick;
    }

    public void setRightClick(final int rightClick) {
        this.rightClick = rightClick;
    }

    public int getMidClick() {
        return midClick;
    }

    public void setMidClick(final int midClick) {
        this.midClick = midClick;
    }

    public double getXspeed() {
        return xspeed;
    }

    public void setXspeed(final double xspeed) {
        this.xspeed = xspeed;
    }

    public double getPrevMove() {
        return prevMove;
    }

    public void setPrevMove(final double prevMove) {
        this.prevMove = prevMove;
    }

    public short getTurnVisible() {
        return turnVisible;
    }

    public void setTurnVisible(final short turnVisible) {
        this.turnVisible = turnVisible;
    }

    public short getRemoveAura() {
        return removeAura;
    }

    public void setRemoveAura(final short removeAura) {
        this.removeAura = removeAura;
    }

    public GameService getGameService() {
        return gameService;
    }

    public void setGameService(final GameService gameService) {
        this.gameService = gameService;
    }

    public int getID() {
        return ID;
    }

    public void setID(final int iD) {
        ID = iD;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(final boolean started) {
        this.started = started;
    }

    public short getDig() {
        return dig;
    }

    public void setDig(final short dig) {
        this.dig = dig;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(final boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public boolean isFailed() {
        return failed;
    }

    public void setFailed(final boolean failed) {
        this.failed = failed;
    }

    public Thread getCommunication() {
        return communication;
    }

    public void setCommunication(final Thread communication) {
        this.communication = communication;
    }

    public boolean isBusy() {
        return busy;
    }

    public void setBusy(final boolean busy) {
        this.busy = busy;
    }

    public boolean isMyTurn() {
        return isMyTurn;
    }

    public void setMyTurn(final boolean isMyTurn) {
        this.isMyTurn = isMyTurn;
    }

    public int getLastHit() {
        return lastHit;
    }

    public void setLastHit(final int lastHit) {
        this.lastHit = lastHit;
    }

    public byte getSendcount() {
        return sendcount;
    }

    public void setSendcount(final byte sendcount) {
        this.sendcount = sendcount;
    }

    public ArrayList<int[]> getStuff() {
        return stuff;
    }

    public void setStuff(final ArrayList<int[]> stuff) {
        this.stuff = stuff;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(final boolean isAlive) {
        this.isAlive = isAlive;
    }

    public Thread getExpander() {
        return expander;
    }

    public void setExpander(final Thread expander) {
        this.expander = expander;
    }

    public boolean isSendRequest() {
        return sendRequest;
    }

    public void setSendRequest(final boolean sendRequest) {
        this.sendRequest = sendRequest;
    }

    public int getX_prev() {
        return x_prev;
    }

    public void setX_prev(final int xp) {
        x_prev = xp;
    }

    public int getYp() {
        return y_prev;
    }

    public void setY_prev(final int yp) {
        y_prev = yp;
    }

    public int getPc() {
        return pc;
    }

    public void setPc(final int pc) {
        this.pc = pc;
    }

    public boolean isRefreshShadows() {
        return refreshShadows;
    }

    public void setRefreshShadows(final boolean refreshShadows) {
        this.refreshShadows = refreshShadows;
    }

    public long getSwagTime() {
        return swagTime;
    }

    public void setSwagTime(final long swagTime) {
        this.swagTime = swagTime;
    }

    public static void setInstance(final Session instance) {
        Session.instance = instance;
    }
}
