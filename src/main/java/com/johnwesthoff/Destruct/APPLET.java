package com.johnwesthoff.Destruct;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//package destructableterrain;
/*TodoList:
* */
import com.johnwesthoff.BlendModes.Additive;
import com.johnwesthoff.Entity.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Family
 */
public class APPLET extends JPanel implements Runnable {
    public boolean goodTeam = false;
    String chat[] = { "", "", "", "", "", "", "", "", "", "" };
    Color chatcolor[] = new Color[] { Color.PINK, Color.PINK, Color.PINK, Color.PINK, Color.PINK, Color.PINK,
            Color.PINK, Color.PINK, Color.PINK, Color.PINK, Color.PINK };
    public static StringLongBoolean unlocks = new StringLongBoolean("0");
    int score = 0;
    int mapRotation = 0;
    boolean chatActive = false;
    String chatMessage = "";
    public int gameMode = 1;
    public int fireTime = 0;
    Color purple = new Color(0xA024C2), backgroundChat = new Color(0, 0, 0, 200),
            deadbg = new Color(255, 255, 255, 127), dark = new Color(0, 0, 0, 128);
    public short matchOver = 0, forcedRespawn = 0;
    static AppletActionListener actioner;
    static AppletInputListener inputer;
    ArrayList<Integer> myTeam = new ArrayList<>(), badTeam = new ArrayList<>();
    static boolean currentlyLoggedIn = false;
    public double maxeng, dpyeng, energico = maxeng = dpyeng = 1000;
    int port = 25565;
    Properties userpassinfo;
    ClothingChooser1 cc = new ClothingChooser1(this);
    double engrecharge = 4;
    Random random = new Random();
    String serverIP = "LocalHost";
    Thread mainProcess, udpthread;
    DatagramSocket udpconnection;
    boolean notDone = true;
    boolean ignored = true;
    Image doubleBuffer;
    BufferedImage Grass, Sky, Sand, Stone, screenBuffer, Bark, Ice, LavaLand, Crystal, ether, bigscreenBuffer;
    static BufferedImage bimage;
    Graphics2D graphicsBuffer, biggraphicsBuffer;
    URL base;// = getDocumentBase();
    String temp;
    public static String username;
    World world;
    double killingSpree = 0;
    boolean loggedOn = false;
    Server hostingPlace;
    public int spellBook = 0;
    LinkedList<World> worldList = new LinkedList<>();
    public long lastTime = 0;
    InputStream input;
    OrderedOutputStream out;
    Socket connection;
    public JFrame owner;
    short MAXHP, HP = MAXHP = 100;
    static boolean gameAlive = true;
    int maxlungs, lungs = maxlungs = 100;
    static double runningSpeed = 1d, swimmingSpeed = 1d;
    static APPLET thisone;
    public static int XP = 0;
    public double prevVspeed = 0;
    public static boolean shortJump = false;
    String killMessage = "~ was defeated by `.";
    int timeToHeal = 0;
    public JComboBox menu;
    public JButton connect, hosting, refresh, register, verify, ChooseSpells, chooseclothing, mapMaker;
    public JCheckBox JRB;
    String[] hosts = new String[1];
    static JTextField jtb = new JTextField();
    JPasswordField jtp = new JPasswordField();
    JLabel jUs = new JLabel("Username:"), jPa = new JLabel("Password:");
    public static ConnectToDatabase CTD;
    Register form = new Register();
    Verify exactly = new Verify();
    SystemTray ST;
    TrayIcon trayIcon;
    Spell[][] spellList;
    Spell[] passiveList;
    int leftClick = 0, rightClick = 1, midClick = 2;
    double xspeed = 0;
    SpellList1 spellselection;
    protected static JFrame container;
    protected static JTabbedPane immaKeepTabsOnYou;
    double prevMove;
    short turnVisible = -1, removeAura = -1;

    public APPLET() {
        super();
        // bimage = ImageIO.read(new URL("https://west-it.webs.com/AgedPaper.png"));
        new File(ResourceLoader.dir).mkdirs();
        new File(ResourceLoader.dir + "images").mkdirs();
        new File(ResourceLoader.dir + "sounds").mkdirs();
        try {
            bimage = ResourceLoader.loadImage("https://west-it.webs.com/Bending/AgedPaper.png", "AgedPaper.png");
            Thread.sleep(100);
        } catch (Exception ex) {
            // ex.printStackTrace();
        }
    }

    // RadialGradientPaint cantSee = new RadialGradientPaint(150f,150f,150f,new
    // float[]{0f,1f},new Color[]{new Color(0,0,0,0),new Color(0,0,0,255)});
    Polygon lineOfSight = new Polygon();

    public static void main(String args[]) {
        System.out.println("Loading BENDING v 2.013.12.24" + System.getProperty("os.name") + File.separator);

        gameAlive = true;
        Spell.init();
        final APPLET me = new APPLET();
        immaKeepTabsOnYou = new JTabbedPane();
        actioner = new AppletActionListener(me);
        inputer = new AppletInputListener(me);
        thisone = me;
        me.CTD = new ConnectToDatabase();
        me.setSize(600, 600);
        me.setPreferredSize(me.getSize());
        // JPanel e = new JPanel();
        // e.setSize(300,300);
        container = new JFrame() {

            @Override
            public void paintComponents(Graphics g) {
                // super.paintComponent(g);

                g.drawImage(APPLET.bimage, 0, 0, getWidth(), getHeight(), null);
                super.paintComponents(g);
            }
        };
        me.owner = container;
        // container.setUndecorated(true);
        me.form.setVisible(false);
        container.setSize(me.getSize());
        container.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        me.cc.setVisible(false);
        me.spellList = new Spell[10][5];// {{Spell.spells.get(0),Spell.spells.get(1),Spell.spells.get(2),Spell.spells.get(3),Spell.spells.get(5)},{Spell.spells.get(0),Spell.spells.get(1),Spell.spells.get(2),Spell.spells.get(3),Spell.spells.get(5)},{Spell.spells.get(0),Spell.spells.get(1),Spell.spells.get(2),Spell.spells.get(3),Spell.spells.get(5)},{Spell.spells.get(0),Spell.spells.get(1),Spell.spells.get(2),Spell.spells.get(3),Spell.spells.get(5)},{Spell.spells.get(0),Spell.spells.get(1),Spell.spells.get(2),Spell.spells.get(3),Spell.spells.get(5)}});
        me.spellList[0] = (new Spell[] { Spell.spells.get(0), Spell.spells.get(0), Spell.spells.get(0),
                Spell.spells.get(0), Spell.spells.get(0) });
        me.spellList[1] = (new Spell[] { Spell.spells.get(0), Spell.spells.get(0), Spell.spells.get(0),
                Spell.spells.get(0), Spell.spells.get(0) });
        me.spellList[2] = (new Spell[] { Spell.spells.get(0), Spell.spells.get(0), Spell.spells.get(0),
                Spell.spells.get(0), Spell.spells.get(0) });
        me.spellList[3] = (new Spell[] { Spell.spells.get(0), Spell.spells.get(0), Spell.spells.get(0),
                Spell.spells.get(0), Spell.spells.get(0) });
        me.spellList[4] = (new Spell[] { Spell.spells.get(0), Spell.spells.get(0), Spell.spells.get(0),
                Spell.spells.get(0), Spell.spells.get(0) });

        me.spellList[5] = (new Spell[] { Spell.spells.get(1), Spell.spells.get(11), Spell.spells.get(18),
                Spell.spells.get(19), Spell.spells.get(7) });
        // container.add(me);
        me.passiveList = (new Spell[] { Spell.noSpell, Spell.noSpell, Spell.noSpell, Spell.noSpell, Spell.noSpell,
                Spell.noSpell });
        // container.add(me);
        me.JRB = new JCheckBox() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bimage, getX(), getY(), getWidth(), getHeight(), null);
            }
        };

        me.userpassinfo = new Properties();
        if (new File(ResourceLoader.dir + "properties.xyz").exists()) {
            try {
                me.userpassinfo.load(new FileInputStream(new File(ResourceLoader.dir + "properties.xyz")));
            } catch (Exception ex) {
                Logger.getLogger(APPLET.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (me.userpassinfo.isEmpty()) {
            me.userpassinfo.setProperty("username", "");
            me.userpassinfo.setProperty("password", "");
            me.userpassinfo.setProperty("remember", "");
            try {
                me.userpassinfo.store(new FileOutputStream(new File(ResourceLoader.dir + "properties.xyz")), "");
            } catch (Exception ex) {
                Logger.getLogger(APPLET.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            if (!me.userpassinfo.getProperty("remember", "").equals("")) {
                APPLET.jtb.setText(me.userpassinfo.getProperty("username", ""));
                me.jtp.setText(me.userpassinfo.getProperty("password", ""));
                me.JRB.setSelected(true);
            }
        }
        me.setBackground(Color.white);
        me.setLayout(null);
        me.menu = new JComboBox() {
            @Override
            public Dimension getSize() {
                Dimension dim = super.getSize();
                dim.width = 256;
                return dim;
            }
        };
        me.menu.setRenderer(new RowCellRenderer());
        me.connect = new JButton("Connect");
        me.hosting = new JButton("Host");
        me.refresh = new JButton("Refresh");
        me.register = new JButton("Register");
        me.verify = new JButton("Log In");
        me.ChooseSpells = new JButton("Loadouts");
        me.chooseclothing = new JButton("Gear");
        me.mapMaker = new JButton("Map Editor");
        container.setMaximumSize(me.getSize());
        me.addMouseListener(inputer);
        me.addMouseMotionListener(inputer);
        container.addKeyListener(inputer);
        container.addMouseListener(inputer);
        container.setResizable(true);
        if (!(args.length > 0 && args[0].equals("APPLET"))) {
            container.setVisible(true);
        }
        me.requestFocus();
        me.add(me.jUs);
        me.jUs.setLocation(16, 16);
        me.jUs.setSize(64, 16);
        me.add(APPLET.jtb);
        APPLET.jtb.setLocation(96, 16);
        me.add(me.jPa);
        me.jPa.setLocation(16, 32);
        me.jPa.setSize(80, 16);
        me.add(me.jtp);
        me.jtp.setLocation(96, 32);
        me.add(me.register);
        me.register.setLocation(32, 54 + 32);
        me.register.setSize(96, 16);
        me.add(me.verify);
        me.verify.setLocation(160, 54 + 32);
        me.verify.setSize(96, 16);
        me.add(me.menu);
        me.menu.setLocation(24, 64 + 8 + 6 + 32);
        me.menu.setSize(120, 32);
        me.add(me.connect);
        me.connect.setLocation(160, 80 + 6 + 32);
        me.connect.setSize(100, 16);
        me.add(me.refresh);
        me.refresh.setLocation(24, 104 + 6 + 32);
        me.refresh.setSize(120, 16);
        me.add(me.hosting);
        me.hosting.setLocation(160, 104 + 6 + 32);
        me.hosting.setSize(100, 16);
        me.add(me.ChooseSpells);
        me.ChooseSpells.setLocation(80, 104 + 6 + 32 + 32);
        me.ChooseSpells.setSize(140, 16);
        me.add(me.chooseclothing);
        me.chooseclothing.setLocation(80, 104 + 6 + 32 + 32 + 32);
        me.chooseclothing.setSize(140, 16);

        me.add(me.mapMaker);
        me.mapMaker.setLocation(16, 104 + 6 + 32 + 32 + 32 + 32);
        me.mapMaker.setSize(240, 16);
        /// me.JRB.setText("Remember me?");

        me.JRB.setActionCommand("RM");
        me.spellselection = new SpellList1(me);
        me.spellselection.setVisible(false);
        me.ChooseSpells.addActionListener(actioner);
        me.ChooseSpells.setActionCommand(me.ChooseSpells.getText());
        me.chooseclothing.addActionListener(actioner);
        me.chooseclothing.setActionCommand(me.chooseclothing.getText());
        me.mapMaker.addActionListener(actioner);
        me.mapMaker.setActionCommand(me.mapMaker.getText());
        me.add(me.JRB);
        me.JRB.setSize(18, 16);
        me.JRB.setLocation(80, 54);
        JLabel cheese = new JLabel("Remember me?");
        cheese.setSize(110, 30);
        cheese.setLocation(110, 48);
        cheese.setVisible(true);
        me.add(cheese);
        // me.JRB.setBackground(Color.white);
        me.JRB.setVisible(true);

        APPLET.jtb.setSize(300 - 128, 16);
        APPLET.jtb.setPreferredSize(APPLET.jtb.getSize());
        // me.jtb.setLocation(16, 16);dfsdfsdf
        APPLET.jtb.setVisible(true);
        APPLET.jtb.requestFocus();
        APPLET.jtb.addKeyListener(inputer);
        me.jtp.setSize(300 - 128, 16);
        me.jtp.setPreferredSize(APPLET.jtb.getSize());
        // me.jtp.setLocation(16, 16);
        me.jtp.setVisible(true);
        me.jtp.requestFocus();
        me.jtp.addKeyListener(inputer);
        me.connect.setActionCommand(me.connect.getText());
        me.connect.addActionListener(actioner);
        me.hosting.setActionCommand(me.hosting.getText());
        me.hosting.addActionListener(actioner);
        me.refresh.setActionCommand(me.refresh.getText());
        me.refresh.addActionListener(actioner);
        me.register.setActionCommand(me.register.getText());
        me.register.addActionListener(actioner);
        me.verify.setActionCommand(me.verify.getText());
        me.verify.addActionListener(actioner);
        me.setFocusable(true);
        me.addKeyListener(inputer);
        me.setVisible(true);
        UIManager.getDefaults().put("TabbedPane.contentBorderInsets", new Insets(0, 0, 0, 0));
        UIManager.getDefaults().put("TabbedPane.tabsOverlapBorder", true);
        immaKeepTabsOnYou.setUI(new BasicTabbedPaneUI() {
            @Override
            protected int calculateTabAreaHeight(int tab_placement, int run_count, int max_tab_height) {
                return 0;
            }
        });
        immaKeepTabsOnYou.addKeyListener(inputer);
        immaKeepTabsOnYou.addMouseListener(inputer);
        immaKeepTabsOnYou.addMouseMotionListener(inputer);
        immaKeepTabsOnYou.addTab("THEGAME", me);
        immaKeepTabsOnYou.addTab("SPELLSELECT", me.spellselection);
        immaKeepTabsOnYou.addTab("SPELLLIST", me.spellselection.choochootrain);
        immaKeepTabsOnYou.addTab("PASSIVELIST", me.spellselection.choochootrain2);
        immaKeepTabsOnYou.addTab("ClothingChooser", me.cc);
        immaKeepTabsOnYou.setLocation(-1, -1);
        immaKeepTabsOnYou.setBackground(Color.black);
        immaKeepTabsOnYou.setBorder(BorderFactory.createEmptyBorder());
        container.setContentPane(immaKeepTabsOnYou);
        me.repaint();
        me.mainProcess = new Thread(me);
        me.mainProcess.start();
        me.getHosts();
        me.chooseclothing.setEnabled(false);
        me.ChooseSpells.setEnabled(false);
        me.connect.setEnabled(false);
        me.ST = SystemTray.getSystemTray();
        try {
            // container.getContentPane().setB

            final PopupMenu pop = new PopupMenu();
            me.trayIcon = new TrayIcon(
                    ResourceLoader.loadImage("https://west-it.webs.com/Bending/GrassTexture.jpg", "GrassTexture.png"));
            me.trayIcon.setToolTip("DestructibleTerrain");
            MenuItem exitItem = new MenuItem("Exit");
            MenuItem hideItem = new MenuItem("Hide");
            MenuItem showItem = new MenuItem("Show");
            MenuItem restartItem = new MenuItem("Restart");
            pop.add(showItem);
            pop.add(hideItem);
            pop.add(exitItem);
            pop.add(restartItem);
            hideItem.addActionListener(actioner);
            exitItem.addActionListener(actioner);
            showItem.addActionListener(actioner);
            restartItem.addActionListener(actioner);
            me.trayIcon.setPopupMenu(pop);
            me.trayIcon.setActionCommand("Tray");
            me.trayIcon.addActionListener(actioner);
            final Frame frame = new Frame("");
            frame.setUndecorated(true);
            frame.setResizable(false);
            frame.setVisible(false);
            me.trayIcon.addMouseListener(new MouseListener() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    // throw new UnsupportedOperationException("Not supported yet.");
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    // throw new UnsupportedOperationException("Not supported yet.");
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    // throw new UnsupportedOperationException("Not supported yet.");
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        frame.setVisible(true);
                        frame.add(pop);
                        pop.show(frame, e.getXOnScreen(), e.getYOnScreen());
                        frame.setVisible(false);
                    }
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    // throw new UnsupportedOperationException("Not supported yet.");
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    // throw new UnsupportedOperationException("Not supported yet.");
                }
            });
            {

            }
            if (args.length > 0 && args[0].equals("APPLET")) {
                container.setVisible(false);
            } else {
                // me.ST.add(me.trayIcon);
            }
            container.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        } catch (Exception ex) {
            Logger.getLogger(APPLET.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    static JApples master;

    public static APPLET getMe(JApples yes) {
        master = yes;
        main(new String[] { "APPLET" });
        return thisone;
    }

    // @Override
    public void init() {
        try {
            loggedOn = true;
            temp = "C:";// (System.getenv("TEMP"));
            Grass = ResourceLoader.loadImage("https://west-it.webs.com/Bending/GrassTexture.jpg", "GrassTexture.jpg");
            Sky = ResourceLoader.loadImage("https://west-it.webs.com/Bending/SkyTexture.jpg", "SkyTexture.jpg");
            Sand = ResourceLoader.loadImage("https://west-it.webs.com/Bending/SandTexture.jpg", "SandTexture.jpg");
            Stone = ResourceLoader.loadImage("https://west-it.webs.com/Bending/StoneTexture.jpg", "StoneTexture.jpg");
            Bark = ResourceLoader.loadImage("https://west-it.webs.com/Bending/BarkTexture.jpg", "BarkTexture.jpg");
            Ice = ResourceLoader.loadImage("https://west-it.webs.com/Bending/iceTexture.png", "iceTexture.png");
            Crystal = ResourceLoader.loadImage("https://west-it.webs.com/Bending/crystalTexture.png",
                    "crystalTexture.png");
            LavaLand = ResourceLoader.loadImage("https://west-it.webs.com/Bending/lavalandTexture.png",
                    "lavalandTexture.png");
            ether = ResourceLoader.loadImage("https://west-it.webs.com/Bending/ether.png", "ether.png");
            // me.getGraphics().clearRect(0, 0, me.getWidth(), me.getHeight());
            // container.requestFocus();
            // me.transferFocus();
            // me.requestFocus();
            // me.validate();
        } catch (Exception ex) {
            Logger.getLogger(APPLET.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int ID = 0;
    public boolean started = false;
    // @Override
    short dig = 0;
    boolean loggedIn = false;
    public boolean failed = false;
    Thread communication;
    static byte[] Clothing = new byte[] { 1, 1, 1, 1, 1, 1 };
    static int[] Colors = new int[] { Color.red.getRGB(), Color.orange.getRGB(), Color.red.getRGB(),
            Color.orange.getRGB(), Color.black.getRGB(), Color.orange.getRGB() };
    static int[] Colors2 = new int[] { Color.red.getRGB(), Color.orange.getRGB(), Color.red.getRGB(),
            Color.orange.getRGB(), Color.black.getRGB(), Color.orange.getRGB() };

    public boolean start() {
        try {
            connection = new Socket(serverIP, 25565);
            isAlive = true;
            world = new World(false, 900, 900, createImage(900, 900), Grass, Sand, Sky, Stone, Bark, Ice, LavaLand,
                    Crystal, ether);
            world.load(Clothing, Colors, Colors2);
            // entityList.add(new HouseEntity(750,300,200,300));

            connection.setKeepAlive(true);
            connection.setTcpNoDelay(true);
            out = new OrderedOutputStream(connection.getOutputStream());
            input = connection.getInputStream();
            // out.write(Server.LOGIN);
            // System.out.println("!!!!!!!!!!!!"+Clothing[1]);
            ByteBuffer tt = Server
                    .putString(ByteBuffer.allocate(username.length() * 4 + 92 + 16).putLong(getAuth()), username)
                    .put(Clothing);
            for (int i = 0; i < Colors.length; i++) {
                tt.putInt(Colors[i]);
            }
            for (int i = 0; i < Colors2.length; i++) {
                tt.putInt(Colors2[i]);
            }
            // int e = udpconnection.getLocalPort();
            // System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"+e);
            // tt.putInt(e);
            tt.putInt(7);
            out.addMesssage(tt, Server.LOGIN);
            // Server.writeByteBuffer(Server.putString(ByteBuffer.allocate(4+username.getBytes().length),username),
            // out);
            // out.flush();
            ID = -1;
            world.ID = ID;
            // system.out.println("My ID is "+ID+".");
            playerHitbox = new Rectangle(0, 0, 20, 40);
            communication = new Thread() {
                @Override
                public void run() {
                    try {
                        while (gameAlive) {
                            int read = input.read();
                            // System.out.println(read);
                            // System.out.println("MR: "+Server.MESSAGEIDs[read]);
                            pc++;
                            switch (read) {
                                case Server.ID:
                                    ID = Server.readByteBuffer(input).getInt();
                                    world.ID = ID;
                                    break;
                                case Server.MESSAGE:
                                    ByteBuffer gotten = Server.readByteBuffer(input);
                                    int color = gotten.getInt();
                                    String message = Server.getString(gotten);
                                    addChat(message, new Color(color));
                                    break;
                                case Server.LOGIN:
                                    int iid;
                                    ByteBuffer rasputin = Server.readByteBuffer(input);
                                    iid = rasputin.getInt();
                                    // iid = input.read();
                                    String feliceNavidad = Server.getString(rasputin);
                                    Player yes = new Player(300, 300,
                                            new byte[] { rasputin.get(), rasputin.get(), rasputin.get(), rasputin.get(),
                                                    rasputin.get(), rasputin.get() },
                                            new int[] { rasputin.getInt(), rasputin.getInt(), rasputin.getInt(),
                                                    rasputin.getInt(), rasputin.getInt(), rasputin.getInt() },
                                            new int[] { rasputin.getInt(), rasputin.getInt(), rasputin.getInt(),
                                                    rasputin.getInt(), rasputin.getInt(), rasputin.getInt() });
                                    world.playerList.add(yes);
                                    boolean sameTeam = rasputin.get() == 12;
                                    // yes.username = Server.getString(rasputin);
                                    // system.out.println("Player joined with ID:"+iid);
                                    if (sameTeam) {
                                        myTeam.add(iid);
                                    } else {
                                        badTeam.add(iid);
                                    }
                                    yes.myTeam = sameTeam;
                                    yes.ID = iid;
                                    yes.username = feliceNavidad;
                                    addChat(yes.username + " has joined the game.", Color.RED);
                                    loggedIn = true;
                                    break;
                                case Server.MOVE:
                                    ByteBuffer reading = Server.readByteBuffer(input);
                                    int idtomove = reading.getShort();
                                    // system.out.println("ID: "+idtomove);
                                    if (idtomove == ID) {
                                        world.x = reading.getShort();
                                        world.y = reading.getShort();
                                        world.move = reading.getShort();
                                        world.vspeed = reading.getShort();
                                        world.leftArmAngle = reading.getShort();
                                        world.rightArmAngle = reading.getShort();
                                        world.status = reading.getShort();
                                    }
                                    for (Player r : world.playerList) {
                                        if (r.ID == idtomove) {
                                            // system.out.println("hi");
                                            r.x = reading.getShort();
                                            r.y = reading.getShort();
                                            r.move = reading.getShort();
                                            r.vspeed = reading.getShort();
                                            r.leftArmAngle = reading.getShort();
                                            r.rightArmAngle = reading.getShort();
                                            r.status = reading.getShort();
                                            break;
                                        }
                                    }
                                    break;
                                case Server.MAP:
                                    int x;
                                    reading = Server.readByteBuffer(input);
                                    x = reading.getInt();
                                    // y = input.readInt();
                                    // System.out.println("SDFSDF"+x);
                                    for (int i = x; i < x + 100; i++) {
                                        reading.get(world.ground.cellData[i]);
                                    }
                                    // System.out.println("yay");
                                    sendRequest = true;
                                    break;
                                case Server.AI:
                                    ByteBuffer reader = Server.readByteBuffer(input);
                                    // putInt(e.X).putInt(e.Y).putInt(e.HP).putInt(e.move).putInt(e.yspeed).putInt(e.target).putInt(e.id);
                                    int redX = reader.getInt();
                                    int redY = reader.getInt();
                                    int redmove = reader.getInt();
                                    int redyspeed = reader.getInt();
                                    int redHP = reader.getInt();
                                    int redid = reader.getInt();
                                    int tar = reader.getInt();
                                    for (Entity p : world.entityList) {
                                        if (!(p instanceof EnemyEntity))
                                            continue;
                                        EnemyEntity e = (EnemyEntity) p;
                                        if (e.MYID == redid) {
                                            e.X = redX;
                                            e.Y = redY;
                                            e.HP = redHP;
                                            e.move = redmove;
                                            e.yspeed = redyspeed;
                                            e.target = tar;
                                        }

                                    }
                                    break;
                                case Server.ENTIREWORLD:
                                    // system.out.println("WOAH");
                                    readWorld();
                                    // system.out.println("SCORE!");
                                    busy = false;
                                    break;
                                case Server.DIG:
                                    ByteBuffer toRead = Server.readByteBuffer(input);
                                    int ix, iy, ir;
                                    ix = toRead.getInt();
                                    iy = toRead.getInt();
                                    ir = toRead.getInt();
                                    world.ground.ClearCircle(ix, iy, ir);
                                    // system.out.println("DIG!");
                                    break;
                                case Server.FILL:
                                    toRead = Server.readByteBuffer(input);
                                    ix = toRead.getInt();
                                    iy = toRead.getInt();
                                    ir = toRead.getInt();
                                    byte etg = toRead.get();
                                    world.ground.FillCircleW(ix, iy, ir, etg);
                                    // system.out.println("FILL!");
                                    break;
                                case Server.CHARGE:
                                    toRead = Server.readByteBuffer(input);
                                    ix = toRead.getInt();
                                    iy = toRead.getInt();
                                    ir = toRead.getInt();
                                    int energy = toRead.getInt();
                                    int maker = toRead.getInt();
                                    if (APPLET.pointDis(world.x, world.y, ix, iy) < ir) {
                                        energico += energy;
                                        if (maker != ID && (gameMode > 0 ? !myTeam.contains(maker) : true)) {
                                            if (maker != 0) {
                                                lastHit = maker;
                                            }
                                            hurt(12);
                                            killMessage = "~ was electrified by `.";
                                            if (world.inBounds(world.x, world.y)
                                                    && world.ground.cellData[(int) world.x][(int) world.y] == World.WATER) {
                                                hurt(12);
                                                killMessage = "~ will never go in the water during a storm again, thanks to `!";
                                            }
                                        }
                                    }
                                    world.entityList.add(new ShockEffectEntity(ix, iy, ir));
                                    // system.out.println("FILL!");
                                    break;
                                case Server.HURT:
                                    toRead = Server.readByteBuffer(input);
                                    HP -= toRead.getInt();
                                    // system.out.println("FILL!");
                                    break;
                                case Server.WORLDEXPAND:
                                    // system.out.println("IT's getting bigger!");
                                    busy = true;
                                    toRead = Server.readByteBuffer(input);
                                    int newx = toRead.getInt();
                                    int si = toRead.getInt();
                                    byte dir = toRead.get();
                                    world.wIdTh += si;
                                    byte list[][] = new byte[world.wIdTh][world.hEigHt];
                                    for (int i = 0; i < world.ground.w; i++) {
                                        System.arraycopy(world.ground.cellData[i], 0, list[i], 0, world.ground.h);
                                    }
                                    world.ground.cellData = list;
                                    world.ground.w = world.wIdTh;
                                    if (dir == 1) {
                                        for (int i = newx; i < world.wIdTh; i++) {
                                            toRead.get(world.ground.cellData[i]);
                                        }
                                    }
                                    if (dir == 2) {
                                        for (int i = newx; i < si; i++) {
                                            toRead.get(world.ground.cellData[i]);
                                        }
                                        world.x += si;
                                    }
                                    readEntityList(toRead);
                                    busy = false;
                                    break;
                                case Server.AIRBENDING:
                                    ByteBuffer buf;
                                    buf = Server.readByteBuffer(input);
                                    int subID = buf.getInt();
                                    int Xx = buf.getInt();
                                    int Yy = buf.getInt();
                                    int mX = buf.getInt();
                                    int mY = buf.getInt();
                                    int ma = buf.getInt();
                                    int iw = buf.getInt();
                                    switch (subID) {
                                        case 0:// Air ball
                                            world.entityList.add(new MissileEntity(Xx, Yy, mX, mY, ma).setID(iw));
                                            break;
                                        case 1:// Air jump
                                            world.entityList.add(
                                                    new EffectEntity(Xx, Yy, mX, mY, random.nextInt(40), Color.WHITE)
                                                            .setID(iw));
                                            break;
                                        case 2:// Tornado
                                            world.entityList.add(new TornadoEntity(Xx, Yy, mX, ma).setID(iw));
                                            break;
                                        case 4:// Air gust
                                            world.entityList.add(new GustEntity(Xx, Yy, mX, mY, ma).setID(iw));
                                            break;
                                        case 5:
                                            world.ground.ClearCircle(Xx, Yy, 48);
                                            break;
                                    }
                                    airCast.start(-30f);
                                    break;
                                case Server.LIGHTNING:
                                    // ByteBuffer buf;
                                    buf = Server.readByteBuffer(input);
                                    subID = buf.getInt();
                                    Xx = buf.getInt();
                                    Yy = buf.getInt();
                                    mX = buf.getInt();
                                    mY = buf.getInt();
                                    ma = buf.getInt();
                                    iw = buf.getInt();
                                    switch (subID) {
                                        case 0:// Fire ball
                                            world.entityList.add(new EnergyEntity(Xx, Yy, mX, mY, ma).setID(iw));
                                            break;
                                        case 1:
                                            world.entityList.add((new CloudEntity(Xx, Yy, ID).setID(iw)));
                                            break;
                                        case 2:
                                            world.entityList.add(new BallLightningEntity(Xx, Yy, mX, mY, ma).setID(iw));
                                            break;
                                        case 3:
                                            world.entityList.add(new StaticShotEntity(Xx, Yy, mX, mY, ma).setID(iw));
                                            break;
                                        case 5:
                                            world.entityList.add(new RodEntity(Xx, Yy, mX, ma).setID(iw));
                                            break;
                                    }
                                    break;
                                case Server.EARTHBENDING:
                                    // ByteBuffer buf;
                                    buf = Server.readByteBuffer(input);
                                    subID = buf.getInt();
                                    Xx = buf.getInt();
                                    Yy = buf.getInt();
                                    mX = buf.getInt();
                                    mY = buf.getInt();
                                    ma = buf.getInt();
                                    iw = buf.getInt();
                                    switch (subID) {
                                        case 0:// Earth ball
                                            world.entityList.add(new RockEntity(Xx, Yy, mX, mY, ma).setID(iw));
                                            break;
                                        case 1:// Earth Spike
                                            Polygon P = new Polygon();
                                            P.addPoint(Xx + 28, Yy);
                                            P.addPoint(Xx - 28, Yy);
                                            P.addPoint(mX, mY);
                                            world.ground.FillPolygon(P, World.STONE);
                                            if (P.contains(world.x, world.y)) {

                                                world.vspeed = -4;
                                                xspeed = mX - Xx;
                                                world.x = mX + (int) xspeed;
                                                world.y = mY + (int) world.vspeed;
                                                // HP-=15;
                                                sendMovement();
                                            }
                                            // world.entityList.add(new
                                            // EffectEntity(Xx,Yy,mX,mY,random.nextInt(40),Color.WHITE));
                                            break;
                                        case 2:// EarthShard
                                            world.entityList.add(new ShardEntity(Xx, Yy, mX, mY, ma).setID(iw));
                                            break;
                                        case 4:// EarthSand
                                            world.ground.sandinate(Xx, Yy, 96);
                                            int number = buf.getInt();
                                            world.entityList.add(new SandEntity(Xx, Yy, mX, mY, ma).setID(iw));
                                            if (number > 3) {
                                                world.entityList.add(
                                                        new SandEntity(Xx, Yy, mX + (int) APPLET.lengthdir_x(4, 30),
                                                                mY + (int) APPLET.lengthdir_y(4, 30), ma)
                                                                        .setID(iw + 1));
                                                world.entityList.add(
                                                        new SandEntity(Xx, Yy, mX + (int) APPLET.lengthdir_x(4, -30),
                                                                mY + (int) APPLET.lengthdir_y(4, -30), ma)
                                                                        .setID(iw + 2));
                                            }
                                            if (number > 5) {
                                                world.entityList.add(
                                                        new SandEntity(Xx, Yy, mX + (int) APPLET.lengthdir_x(4, 45),
                                                                mY + (int) APPLET.lengthdir_y(4, 45), ma)
                                                                        .setID(iw + 3));
                                                world.entityList.add(
                                                        new SandEntity(Xx, Yy, mX + (int) APPLET.lengthdir_x(4, -45),
                                                                mY + (int) APPLET.lengthdir_y(4, -45), ma)
                                                                        .setID(iw + 4));
                                            }
                                            if (number > 7) {
                                                world.entityList.add(
                                                        new SandEntity(Xx, Yy, mX + (int) APPLET.lengthdir_x(4, 60),
                                                                mY + (int) APPLET.lengthdir_y(4, 60), ma)
                                                                        .setID(iw + 5));
                                                world.entityList.add(
                                                        new SandEntity(Xx, Yy, mX + (int) APPLET.lengthdir_x(4, -60),
                                                                mY + (int) APPLET.lengthdir_y(4, -60), ma)
                                                                        .setID(iw + 6));
                                            }
                                            if (number > 12) {
                                                world.entityList.add(
                                                        new SandEntity(Xx, Yy, mX + (int) APPLET.lengthdir_x(4, 15),
                                                                mY + (int) APPLET.lengthdir_y(4, 15), ma)
                                                                        .setID(iw + 7));
                                                world.entityList.add(
                                                        new SandEntity(Xx, Yy, mX + (int) APPLET.lengthdir_x(4, -15),
                                                                mY + (int) APPLET.lengthdir_y(4, -15), ma)
                                                                        .setID(iw + 8));
                                            }
                                            if (number > 16) {
                                                world.entityList.add(
                                                        new SandEntity(Xx, Yy, mX + (int) APPLET.lengthdir_x(4, 35),
                                                                mY + (int) APPLET.lengthdir_y(4, 35), ma)
                                                                        .setID(iw + 9));
                                                world.entityList.add(
                                                        new SandEntity(Xx, Yy, mX + (int) APPLET.lengthdir_x(4, -35),
                                                                mY + (int) APPLET.lengthdir_y(4, -35), ma)
                                                                        .setID(iw + 10));
                                            }
                                            if (number > 20) {
                                                world.entityList.add(
                                                        new SandEntity(Xx, Yy, mX + (int) APPLET.lengthdir_x(4, 45),
                                                                mY + (int) APPLET.lengthdir_y(4, 45), ma)
                                                                        .setID(iw + 11));
                                                world.entityList.add(
                                                        new SandEntity(Xx, Yy, mX + (int) APPLET.lengthdir_x(4, -45),
                                                                mY + (int) APPLET.lengthdir_y(4, -45), ma)
                                                                        .setID(iw + 12));
                                            }
                                            break;
                                        case 5:
                                            // handle.earth.entityList.add(new EffectEntity(Xx,Yy,mX,mY,ma));

                                            world.ground.FillRectW(Xx - 12, Yy - 12, 24, 24, World.SAND);
                                            break;
                                    }
                                    break;
                                case Server.WATERBENDING:
                                    // ByteBuffer buf;
                                    buf = Server.readByteBuffer(input);
                                    subID = buf.getInt();
                                    Xx = buf.getInt();
                                    Yy = buf.getInt();
                                    mX = buf.getInt();
                                    mY = buf.getInt();
                                    ma = buf.getInt();
                                    iw = buf.getInt();
                                    switch (subID) {
                                        case 0:// Water ball
                                            world.entityList.add(new WaterBallEntity(Xx, Yy, mX, mY, ma).setID(iw));
                                            break;
                                        case 1:// Water ball
                                            world.entityList.add(new FreezeEntity(Xx, Yy, mX, mY, ma).setID(iw));
                                            break;
                                        case 2:// Water spout
                                            world.entityList.add(new SpoutSourceEntity(Xx, Yy, 50, ma).setID(iw));
                                            break;
                                        case 4:// Water spout
                                            world.entityList.add(new IceShardEntity(Xx, Yy, mX, mY, ma).setID(iw));
                                            break;
                                        case 5: // Snow Gun
                                            world.entityList.add(new SnowEntity(Xx, Yy, mX + 1, mY + 1, ma).setID(iw));
                                            world.entityList.add(new SnowEntity(Xx, Yy, mX + 1, mY - 1, ma).setID(iw));
                                            world.entityList.add(new SnowEntity(Xx, Yy, mX - 1, mY + 1, ma).setID(iw));
                                            world.entityList.add(new SnowEntity(Xx, Yy, mX - 1, mY - 1, ma).setID(iw));
                                            world.entityList.add(new SnowEntity(Xx, Yy, mX, mY + 1, ma).setID(iw));
                                            world.entityList.add(new SnowEntity(Xx, Yy, mX + 1, mY, ma).setID(iw));
                                            break;
                                        case 6:// Water spout
                                            world.entityList.add(new RainEntity(Xx, Yy, ma).setID(iw));
                                            break;
                                    }
                                    break;
                                case Server.FIREBENDING:
                                    // ByteBuffer buf;
                                    buf = Server.readByteBuffer(input);
                                    subID = buf.getInt();
                                    Xx = buf.getInt();
                                    Yy = buf.getInt();
                                    mX = buf.getInt();
                                    mY = buf.getInt();
                                    ma = buf.getInt();
                                    iw = buf.getInt();
                                    switch (subID) {
                                        case 0:// Fire ball
                                            world.entityList.add(new FireBallEntity(Xx, Yy, mX, mY, ma).setID(iw));
                                            fireCast.start(-3f);
                                            break;
                                        case 1:// Lava ball
                                            world.entityList.add(new LavaBallEntity(Xx, Yy, mX, mY, ma).setID(iw));
                                            fireCast.start(-3f);
                                            break;
                                        case 2:// Fire leap
                                            world.entityList.add(new FireJumpEntity(Xx, Yy, mX, mY, ma).setID(iw));
                                            fireCast.start(-3f);
                                            break;
                                        case 4:// Fire wall
                                            world.entityList.add(new WallofFireEntity(Xx, Yy, 8, 0, ma).setID(iw));
                                            world.entityList.add(new WallofFireEntity(Xx, Yy, -8, 0, ma).setID(iw));
                                            fireCast.start(-3f);
                                            break;
                                        case 5:// Flamethrower
                                            world.entityList.add(new FlameThrowerEntity(Xx, Yy, mX, mY, ma).setID(iw));
                                            fireCast.start(-3f);
                                            break;
                                        case 6:// Flames
                                            world.entityList.add(new FirePuffEntity(Xx, Yy, mX, mY, ma).setID(iw));
                                            break;
                                        case 9:// Fire ball
                                            world.entityList.add(new BuritoEntity(Xx, Yy, mX, mY, ma).setID(iw));
                                            fireCast.start(-3f);
                                            break;
                                        case 10:// Fire ball
                                            world.entityList.add(new FireDoom(Xx, Yy, mX, mY, ma).setID(iw));
                                            fireCast.start(-3f);
                                            break;
                                    }
                                    break;
                                case Server.DARKNESS:
                                    buf = Server.readByteBuffer(input);
                                    subID = buf.getInt();
                                    Xx = buf.getInt();
                                    Yy = buf.getInt();
                                    mX = buf.getInt();
                                    mY = buf.getInt();
                                    ma = buf.getInt();
                                    iw = buf.getInt();
                                    switch (subID) {
                                        case 1:
                                            world.entityList.add(new SoulDrainEntity(Xx, Yy, mX, mY, ma).setID(iw));
                                            break;
                                        case 2:
                                            world.entityList.add(new SummonBallEntity(Xx, Yy, mX, mY, ma).setID(iw));
                                            break;
                                        case 3:
                                            world.entityList.add(new SpoutSourceEntity(Xx, Yy, 50, ma).setID(iw));
                                            break;
                                        case 10:
                                            world.entityList.add(new EnemyEntity(Xx, Yy, mX, mY, ma).setID(iw));
                                            break;
                                    }
                                    break;
                                case Server.FREEZE:
                                    // ByteBuffer buf;
                                    buf = Server.readByteBuffer(input);
                                    int fX = buf.getInt();
                                    int fY = buf.getInt();
                                    int fR = buf.getInt();
                                    world.ground.freeze(fX, fY, fR);
                                    break;
                                case Server.SANDINATE:
                                    // ByteBuffer buf;
                                    buf = Server.readByteBuffer(input);
                                    fX = buf.getInt();
                                    fY = buf.getInt();
                                    fR = buf.getInt();
                                    world.ground.sandinate(fX, fY, fR);
                                    break;
                                case Server.PUDDLE:
                                    // ByteBuffer buf;
                                    buf = Server.readByteBuffer(input);
                                    fX = buf.getInt();
                                    fY = buf.getInt();
                                    fR = buf.getInt();
                                    world.ground.puddle(fX, fY, fR);
                                    break;
                                case Server.DESTROY:
                                    // ByteBuffer buf;
                                    buf = Server.readByteBuffer(input);
                                    int idtokill = buf.getInt();
                                    for (int i = 0; i < world.entityList.size(); i++) {
                                        if (world.entityList.get(i).MYID == idtokill) {
                                            world.entityList.remove(i);
                                            continue;
                                        }
                                    }
                                    break;
                                case Server.DRAIN:
                                    buf = Server.readByteBuffer(input);
                                    int hpt = buf.getInt();
                                    HP += hpt;
                                    break;
                                case Server.STEAM:
                                    // ByteBuffer buf;
                                    buf = Server.readByteBuffer(input);
                                    int xxxx = buf.getInt(), yyyy = buf.getInt();
                                    idtokill = buf.getInt();
                                    for (int i = 0; i < world.entityList.size(); i++) {
                                        if (world.entityList.get(i).MYID == idtokill) {
                                            world.entityList.remove(i);
                                            continue;
                                        }
                                    }
                                    world.entityList.add(new SteamEntity(xxxx, yyyy));
                                    break;
                                case Server.DEATH:
                                    // ByteBuffer buf;
                                    buf = Server.readByteBuffer(input);
                                    fX = buf.getInt();
                                    int fX2 = buf.getInt();
                                    if (fX2 != fX) {
                                        if (fX == ID) {
                                            XP += 25;
                                            CTD.postXP(XP, username);
                                            score++;
                                            if (killingSpree == 0) {
                                                killingSpree = Math.E;
                                            } else {
                                                killingSpree *= Math.E;
                                            }
                                        }
                                        for (Player p : world.playerList) {
                                            if (p.ID == fX) {
                                                p.score++;
                                            }
                                        }
                                    }
                                    break;
                                case Server.SCORE:
                                    // ByteBuffer buf;
                                    buf = Server.readByteBuffer(input);
                                    int idd = buf.getInt();
                                    int scored = buf.getInt();
                                    if (idd == ID) {
                                        XP += 10;
                                        CTD.postXP(XP, username);
                                        score = scored;
                                        if (killingSpree == 0) {
                                            killingSpree = Math.E;
                                        } else {
                                            killingSpree *= Math.E;
                                        }
                                    }
                                    for (Player p : world.playerList) {
                                        if (p.ID == idd) {
                                            p.score = scored;
                                        }
                                    }
                                    break;
                                case Server.LEAVE:
                                    // ByteBuffer buf;
                                    buf = Server.readByteBuffer(input);
                                    fX = buf.getInt();
                                    for (Player p : world.playerList) {
                                        if (p.ID == fX) {
                                            if (myTeam.contains(p.ID)) {
                                                myTeam.remove(myTeam.indexOf(p.ID));
                                            } else {
                                                badTeam.remove(badTeam.indexOf(p.ID));
                                            }
                                            addChat(p.username + " has left the game.", Color.RED);
                                            world.playerList.remove(p);
                                            break;
                                        }
                                    }
                                    break;
                            }
                            // Thread.sleep(25);
                        }
                    } catch (Exception ex) {
                        // Logger.getLogger(APPLET.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            };
            communication.start();

            worldList.add(world);
            repaint();
            started = true;
        } catch (Exception ex) {

            failed = true;
            return false;
        }
        return true;
        // terrain.getGraphics().drawImage(Grass, 0,0,null);
    }

    public Rectangle playerHitbox;

    public boolean checkCollision(float px, float py) {
        playerHitbox.setLocation((int) world.x - playerHitbox.width / 2, (int) world.y - (World.head + 10));
        return (playerHitbox.contains(px, py));
    }

    public void addChat(String message, Color color) {
        for (int i = 0; i < chat.length - 1; i++) {
            chat[i] = chat[i + 1];
            chatcolor[i] = chatcolor[i + 1];
        }
        chat[chat.length - 1] = message;
        chatcolor[chat.length - 1] = color;
    }

    boolean busy = false;
    int lastHit = -1;
    byte sendcount = 0;
    ArrayList<int[]> stuff = new ArrayList<>();
    boolean isAlive = true;
    Area hideFromMe = new Area(), box = new Area(new Rectangle(0, 0, 300, 300));
    Thread expander;

    // @Override
    public void destroy() {
        isAlive = false;
        gameAlive = false;
    }

    boolean sendRequest = true;
    int Xp = 0, Yp = 0;
    int pc = 0;
    Color Clear = new Color(0, 0, 0, 0);

    public void calculateLoS() {
        lineOfSight.reset();
        int Xx;
        int Yy;
        int d;
        int dr;
        int dx;
        int dy;
        boolean yes;
        int resolution = 18;

        for (int i = 0; i < resolution; i++) {
            Xx = (int) world.x - world.viewX;
            Yy = (int) world.y - World.head - world.viewY;
            yes = true;
            d = 0;
            dr = i * (360 / resolution);
            dx = (int) world.lengthdir_x(8, dr);
            dy = (int) world.lengthdir_y(8, dr);
            while (yes) {
                d++;
                Xx += dx;
                Yy += dy;
                if (world.isSolid(Xx + world.viewX, Yy + world.viewY)) {
                    lineOfSight.addPoint(Xx, Yy);
                    yes = false;
                }
                if (d >= 40) {
                    lineOfSight.addPoint(Xx, Yy);
                    yes = false;
                }
            }
        }
        hideFromMe.reset();
        if (!world.dead) {
            // cantSee = new
            // RadialGradientPaint(world.x-world.viewX,world.y-world.viewY,250f,new
            // float[]{0f,1f},new Color[]{new Color(0,0,0,0),new Color(0,0,0,255)});
            Area swag = new Area(lineOfSight);
            hideFromMe.add(box);
            hideFromMe.subtract(swag);
        }

    }

    boolean refreshShadows = false;
    long swagTime = 0;

    @Override
    public void run() {
        int counting = 150;
        Thread.currentThread().setPriority(Thread.NORM_PRIORITY);
        lastTime = System.currentTimeMillis();
        while (gameAlive) {
            long l = System.currentTimeMillis();
            try {
                owner.setTitle(" Packet Count: " + pc + " FPS: " + (1000 / (l - swagTime)));
            } catch (Exception e) {

            }
            swagTime = l;
            try {
                l = System.currentTimeMillis() - lastTime;
                while (l < 25)// 25
                {
                    Thread.sleep(1);
                    l = System.currentTimeMillis() - lastTime;
                }
            } catch (InterruptedException ex) {
            }
            lastTime = System.currentTimeMillis();
            if (!owner.isVisible()) {
                if (!SystemTray.isSupported()) {
                    if (!"".equals(hostIP)) {
                        CTD.removeServer(hostIP);
                    }
                    System.exit(0);
                }

                // System.out.println(started);
            }
            if (!started) {
                continue;
            }

            if (removeAura > 0) {
                removeAura--;
                world.status |= World.ST_DRAIN;
                if (removeAura == 0) {
                    world.status &= ~World.ST_DRAIN;
                    sendMovement();
                }
            }
            if (turnVisible > 0) {
                turnVisible--;
                world.status |= World.ST_INVISIBLE;
                if (turnVisible == 0) {
                    world.status &= ~World.ST_INVISIBLE;
                    sendMovement();
                }
            }
            if (gameMode == Server.THEHIDDEN) {
                if (goodTeam) {
                    world.status |= World.ST_INVISIBLE;
                    spellBook = 5;// Force use of TheHidden book
                } else {
                    if (!badTeam.isEmpty()) {
                        lastHit = badTeam.get(0);
                    }
                }
            }

            if (timeToHeal++ > 30 && HP < MAXHP) {
                timeToHeal = 0;
                HP++;
            }
            if (!"Air Run".equals(passiveList[spellBook].getName())) {
                runningSpeed = 1;
            }
            if ((!"Air Affinity".equals(passiveList[spellBook].getName()))
                    && (!"Earth Stance".equals(passiveList[spellBook].getName()))) {
                world.floatiness = 0;
                maxlungs = 100;
            }
            if (!"Water Treader".equals(passiveList[spellBook].getName())) {
                swimmingSpeed = 1;
            }
            if (!"Earth Shield".equals(passiveList[spellBook].getName())) {
                MAXHP = 100;
            }
            if (!"Overcharged".equals(passiveList[spellBook].getName())) {
                maxeng = 1000;
            }
            if (HP > MAXHP) {
                HP = MAXHP;
            }
            if (!"Earth Stance".equals(passiveList[spellBook].getName())) {
                knockbackDecay = 1;
            }
            passiveList[spellBook].getPassiveAction(this);
            if (world.x > world.wIdTh) {
                world.x = world.wIdTh;
            }
            if (world.checkCollision(world.x, world.y - World.head) || world.isLiquid(world.x, world.y - World.head)) {
                if (lungs-- < 0) {
                    HP--;
                    killMessage = "~ suffocated after fighting `...";
                }
            } else {
                lungs = maxlungs;
            }
            if (energico < 0) {
                energico = 0;
            }
            if (world.isType((int) world.x, (int) world.y, World.LAVA)) {
                world.status |= World.ST_FLAMING;
                killMessage = "~ burned to a crisp after fighting `!";
            }
            if ((world.status & World.ST_FLAMING) != 0) {
                HP -= random.nextInt(2);
                if ((passiveList[spellBook].getName().equals("Fireproof"))) {
                    energico += (inputer.doublecast * 3);
                }
                if (random.nextInt(10) == 0) {
                    world.status &= ~World.ST_FLAMING;// Stop being on fire
                }
            }
            if ((world.status & World.ST_SHOCKED) != 0) {
                energico -= 25;
                if (random.nextInt(10) == 0) {
                    world.status &= ~World.ST_SHOCKED;// Stop being on fire
                }
            }
            if (world.isIce((int) world.x, (int) world.y + 6)) {
                xspeed += world.move;
                xspeed *= 1.4;
                if (xspeed > 15) {
                    xspeed = 15;
                }
                if (xspeed < -15) {
                    xspeed = -15;
                }
            }
            if (world.keys[KeyEvent.VK_S]) {
                // world.move = 0;
                if ((dig += 2) >= 100) {
                    dig = 0;
                    // world.keys[KeyEvent.VK_S] = false;
                    ByteBuffer bb = ByteBuffer.allocate(24);
                    bb.putInt(5).putInt((int) world.x).putInt((int) world.y).putInt(0).putInt(0);
                    try {
                        out.addMesssage(bb, Server.AIRBENDING);
                    } catch (IOException ex) {
                        System.err.println(ex.getMessage());
                    }
                }
            } else {
                dig = 0;
            }
            for (Player p : world.playerList) {
                if ((p.status & World.ST_DRAIN) != 0) {
                    if (Math.abs(p.x - world.x) < World.AURA_RADIUS / 2) {
                        if (Math.abs(p.y - world.y) < World.AURA_RADIUS / 2) {
                            lastHit = p.ID;
                            killMessage = "~'s soul was corrupted by `'s Aura of Darkness.";
                            HP--;// Lose health from aura
                        }
                    }
                }
            }
            for (Entity e : world.entityList) {
                if (e instanceof MissileEntity) {
                    MissileEntity me = (MissileEntity) e;
                    // if (pointDis(me.X, me.Y, world.x, world.y)<me.radius*2&&me.maker!=ID)
                    if (checkCollision(me.X, me.Y) && me.maker != ID
                            && (gameMode > 0 ? badTeam.contains(me.maker) : true)) {
                        me.alive = false;
                        hurt(10);
                        xspeed += 7 - random.nextInt(14);
                        world.vspeed -= 5;
                        lastHit = me.maker;
                        killMessage = "~ was blown away by `.";
                    }
                }
                if (e instanceof TornadoEntity) {
                    TornadoEntity me2 = (TornadoEntity) e;
                    if (checkCollision(me2.X, me2.Y) && me2.life < 80) {
                        hurt(1);
                        // world.vspeed-=1;
                        xspeed += 1 - random.nextInt(2);
                        xspeed *= -1;
                        world.x = (int) me2.X + (int) xspeed;
                        world.move = 0;
                        lastHit = me2.maker;
                        killMessage = "~ was sucked into `'s Tornado.";
                    }
                }
                if (e instanceof GustEntity) {
                    GustEntity me2 = (GustEntity) e;
                    if (checkCollision(me2.X, me2.Y)) {
                        hurt(7);
                        world.vspeed += me2.yspeed * 2;
                        xspeed += me2.xspeed * 2;
                        me2.alive = false;
                        lastHit = me2.maker;
                        lungs = maxlungs;
                        killMessage = "~ met `'s gust of air!";
                    }
                }
                if (e instanceof RockEntity) {
                    RockEntity me3 = (RockEntity) e;
                    if (checkCollision(me3.X, me3.Y) && me3.maker != ID
                            && (gameMode > 0 ? badTeam.contains(me3.maker) : true)) {
                        hurt(18);
                        world.vspeed -= 5;
                        xspeed += 7 - random.nextInt(14);
                        lastHit = me3.maker;
                        me3.alive = false;
                        killMessage = "~ was built into a bridge by `.";
                    }
                }
                if (e instanceof FireBallEntity) {
                    FireBallEntity me3 = (FireBallEntity) e;
                    if (checkCollision(me3.X, me3.Y) && me3.maker != ID
                            && (gameMode > 0 ? badTeam.contains(me3.maker) : true)) {
                        hurt(15);
                        world.status |= World.ST_FLAMING;
                        world.vspeed -= 7;
                        xspeed += 9 - random.nextInt(18);
                        lastHit = me3.maker;
                        me3.alive = false;
                        world.status |= World.ST_FLAMING;
                        killMessage = "~ was burninated by `.";
                    }
                }
                if (e instanceof FirePuffEntity) {
                    FirePuffEntity me3 = (FirePuffEntity) e;
                    if (checkCollision(me3.X, me3.Y) && me3.maker != ID
                            && (gameMode > 0 ? badTeam.contains(me3.maker) : true)) {
                        hurt(2);
                        world.status |= World.ST_FLAMING;
                        world.vspeed -= 2;
                        xspeed += 2 - random.nextInt(4);
                        lastHit = me3.maker;
                        me3.alive = false;
                        world.status |= World.ST_FLAMING;
                        killMessage = "~ was set ablaze by `.";
                    }
                }
                if (e instanceof EnemyEntity) {
                    EnemyEntity me3 = (EnemyEntity) e;
                    if (checkCollision(me3.X, me3.Y) && me3.master != ID
                            && (gameMode > 0 ? !myTeam.contains(me3.master) : true)) {
                        hurt(7);
                        world.vspeed -= 4;
                        xspeed += 4 - random.nextInt(8);
                        lastHit = me3.master;
                        killMessage = "~ was defeated by `'s dark minion.";
                    }
                }
                if (e instanceof BuritoEntity) {
                    BuritoEntity me3 = (BuritoEntity) e;
                    if (checkCollision(me3.X, me3.Y) && me3.maker != ID
                            && (gameMode > 0 ? badTeam.contains(me3.maker) : true)) {
                        hurt(65);
                        world.status |= World.ST_FLAMING;
                        world.vspeed -= 39;
                        xspeed += 47 - random.nextInt(94);
                        lastHit = me3.maker;
                        me3.alive = false;
                        world.status |= World.ST_FLAMING;
                        killMessage = "~ shouldn't have stolen `'s burito...";
                    }
                }
                if (e instanceof LavaBallEntity) {
                    LavaBallEntity me3 = (LavaBallEntity) e;
                    if (checkCollision(me3.X, me3.Y) && me3.maker != ID
                            && (gameMode > 0 ? badTeam.contains(me3.maker) : true)) {
                        lastHit = me3.maker;
                        killMessage = "How did ` beat ~?";
                        me3.alive = false;
                    }
                }
                if (e instanceof SoulDrainEntity) {
                    SoulDrainEntity me3 = (SoulDrainEntity) e;
                    if (checkCollision(me3.X, me3.Y) && me3.maker != ID
                            && (gameMode > 0 ? badTeam.contains(me3.maker) : true)) {
                        lastHit = me3.maker;
                        killMessage = "~'s soul was stolen by `!";
                        me3.alive = false;
                        ByteBuffer bb = ByteBuffer.allocate(8);
                        bb.putInt(lastHit).putInt(hurt(21));
                        world.vspeed -= 5;
                        xspeed += 7 - random.nextInt(14);
                        try {
                            out.addMesssage(bb, Server.DRAIN);
                        } catch (IOException ex) {
                            // Logger.getLogger(APPLET.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                if (e instanceof FireJumpEntity) {
                    FireJumpEntity me3 = (FireJumpEntity) e;
                    if (pointDis(me3.X, me3.Y, world.x, world.y) < me3.radius * 4 && me3.maker != ID
                            && (gameMode > 0 ? badTeam.contains(me3.maker) : true)) {
                        hurt(15);
                        world.status |= World.ST_FLAMING;
                        world.vspeed += me3.yspeed * 2;
                        xspeed += me3.xspeed;
                        lastHit = me3.maker;
                        me3.alive = false;
                        killMessage = "~ was flung into orbit by `'s falcon pawnch!";
                    }
                }
                if (e instanceof ShardEntity) {
                    ShardEntity me3 = (ShardEntity) e;
                    if (pointDis(me3.X, me3.Y - World.head, world.x, world.y) < me3.radius * 4 && me3.maker != ID
                            && (gameMode > 0 ? badTeam.contains(me3.maker) : true)) {
                        hurt(15);
                        world.vspeed -= 5;
                        xspeed += 7 - random.nextInt(14);
                        lastHit = me3.maker;
                        me3.alive = false;
                        killMessage = "~ was sniped by `.";
                    }
                }
                if (e instanceof SandEntity) {
                    SandEntity me3 = (SandEntity) e;
                    double d = pointDis(me3.X, me3.Y, world.x, world.y);
                    // System.out.println(d);
                    if (d < me3.radius * 3 && me3.maker != ID && (gameMode > 0 ? badTeam.contains(me3.maker) : true)) {
                        hurt(2);
                        world.vspeed -= 1;
                        xspeed += (me3.xspeed / 64);
                        lastHit = me3.maker;
                        me3.alive = false;
                        killMessage = "~ was shredded by `'s shotgun.";
                    }
                }
                if (e instanceof IceShardEntity) {
                    IceShardEntity me3 = (IceShardEntity) e;
                    if (checkCollision(me3.X, me3.Y) && me3.maker != ID
                            && (gameMode > 0 ? badTeam.contains(me3.maker) : true)) {
                        hurt(15);
                        world.vspeed -= 5;
                        xspeed += 7 - random.nextInt(14);
                        lastHit = me3.maker;
                        me3.alive = false;
                        killMessage = "~ was hit by `'s icey attack!";
                    }
                }
                if (e instanceof SnowEntity) {
                    SnowEntity me3 = (SnowEntity) e;
                    if (checkCollision(me3.X, me3.Y) && me3.maker != ID
                            && (gameMode > 0 ? badTeam.contains(me3.maker) : true)) {
                        hurt(8);
                        world.vspeed -= 3;
                        xspeed += 3 - random.nextInt(6);
                        lastHit = me3.maker;
                        me3.alive = false;
                        killMessage = "~ will need to be thawed out after fighting `!";
                    }
                }
                if (e instanceof SpoutEntity) {
                    SpoutEntity me3 = (SpoutEntity) e;
                    if (checkCollision(me3.X, me3.Y)) {
                        if (me3.maker != ID && (gameMode > 0 ? badTeam.contains(me3.maker) : true)) {
                            hurt(5);
                            lastHit = me3.maker;
                        }
                        world.vspeed -= 5;
                        killMessage = "~ was kicked out of `'s swimming pool.";
                    }
                }
                if (e instanceof BallLightningEntity) {
                    BallLightningEntity me3 = (BallLightningEntity) e;
                    if (checkCollision(e.X, e.Y)) {
                        if (me3.maker != ID && (gameMode > 0 ? badTeam.contains(me3.maker) : true)) {
                            hurt(10);
                            lastHit = me3.maker;
                            world.vspeed -= random.nextInt(22);
                            xspeed += 18 - random.nextInt(36);
                            killMessage = "~ was shockingly killed by `!";
                        }
                    }
                }
                if (e instanceof WallofFireEntity) {
                    WallofFireEntity me3 = (WallofFireEntity) e;
                    checkCollision(me3.X, me3.Y);// Just to move the hitbox so when it is passed, it works
                    // pointDis(me3.X, me3.Y, world.x, world.y)<me3.height
                    if (me3.checkCollision(playerHitbox) && me3.maker != ID
                            && (gameMode > 0 ? badTeam.contains(me3.maker) : true)) {
                        hurt(35);
                        me3.alive = false;
                        lastHit = me3.maker;
                        world.vspeed -= 8;
                        xspeed += 9 - random.nextInt(18);
                        world.status |= World.ST_FLAMING;
                        killMessage = "~ smelled `'s armpits, and then died.";
                    }
                }
            }

            if (world.keys[KeyEvent.VK_CONTROL] && !world.dead) {
                double direction = APPLET.pointDir(150, 150, world.mouseX, world.mouseY);
                double distance = APPLET.pointDis(150, 150, world.mouseX, world.mouseY) / 8;
                world.incX += APPLET.lengthdir_x(distance, direction);
                world.incY -= APPLET.lengthdir_y(distance, direction);
            }
            if (world.dead) {
                world.status = 0;
                world.y = -50;
                world.x = -50;
                // this.chatActive = false;
                HP = MAXHP;
                this.lungs = this.maxlungs;
                world.move = 0;
                world.vspeed = 0;
                this.xspeed = 0;

            }

            if (HP <= 0) {
                world.viewdX = world.viewX;
                world.viewdY = world.viewY;
                HP = MAXHP;
                this.lungs = this.maxlungs;
                world.y = -50;
                world.x = -50;
                if (killingSpree >= 148.413d) {
                    CTD.postRSSfeed(username + " had a streak going",
                            ((int) Math.log(killingSpree)) + " kills in a row!");// Anti-cheating - use logs
                }
                killingSpree = 0;
                world.dead = true;
                // this.chatActive = false;
                ByteBuffer die = ByteBuffer.allocate(5).putInt(lastHit);
                try {
                    out.addMesssage(die, Server.DEATH);
                } catch (IOException ex) {
                    // ex.printStackTrace();
                }
                if (lastHit == ID) {
                    XP -= 25;
                    this.sendMessage(username + " has committed suicide.", 0xFF0436);
                } else {
                    this.sendMessage(killMessage.replaceAll("~", username).replaceAll("`", getKiller(lastHit)),
                            0x04FFF8);// username + " has been defeated by "+getKiller(lastHit)
                }
                try {
                    this.sendMovement();
                } catch (Exception ex) {
                    // Logger.getLogger(APPLET.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (energico < maxeng) {
                energico += engrecharge;
            } else {
                energico = maxeng;
            }
            if (world.keys[KeyEvent.VK_E]) {
                world.incX += 10;
            }
            if (world.keys[KeyEvent.VK_Q]) {
                world.incX -= 10;
            }
            if (world.keys[KeyEvent.VK_Z]) {
                world.incX = 0;
                world.incY = 0;
            }
            if (!world.isSolid(world.x + (int) xspeed, world.y)) {
                world.x += xspeed;
            }
            if (world.vspeed >= 0) {
                if ("Earth Stance".equals(passiveList[spellBook].getName()) && world.vspeed < 0) {
                    world.vspeed *= knockbackDecay;
                }
                xspeed *= .75 * knockbackDecay;
                if (Math.abs(xspeed) < .001 && xspeed != 0) {
                    xspeed = 0;
                    sendMovement();
                }
            }
            // prevMove = world.move;
            world.onUpdate();

            if (((((Math.signum(prevVspeed) != Math.signum(world.vspeed)) || ((prevMove) != (world.move)))
                    || counting++ > 200))) {
                counting = 0;
                try {
                    sendMovement();
                    prevMove = world.move;
                    if (sendRequest && sendcount++ >= 30) {
                        sendcount = 0;
                        // System.out.println("REQUEST START");
                        ByteBuffer bb = ByteBuffer.allocate(24);
                        out.addMesssage(bb.putInt(1), Server.MAP);
                        sendRequest = false;
                    }
                } catch (Exception ex) {
                    Logger.getLogger(APPLET.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
            }

            prevVspeed = world.vspeed;
            // if (busy) continue;
            Xp = (int) world.x;
            Yp = (int) world.y;

            if (world.keys[KeyEvent.VK_SPACE]) {
            }

            repaint();
            World.setTime();
        }
    }

    public String getKiller(int i) {
        for (Player p : world.playerList) {
            if (p.ID == i) {
                return p.username;
            }
        }
        return "No One";
    }

    double knockbackDecay = 1;

    public String addHost() {
        try {
            URL whatismyip = new URL("https://checkip.amazonaws.com/");
            BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));

            String ip = in.readLine(); // you get the IP as a String
            // System.out.println(ip);
            int an = JOptionPane.showConfirmDialog(connect, "Host the server through the internet?", "Server Type?",
                    JOptionPane.YES_NO_OPTION);
            if (an != 0) {
                ip = InetAddress.getLocalHost().getHostAddress();
            }
            String yes = JOptionPane.showInputDialog("Server Name?");
            if (yes != null) {
                serverName = yes.replaceAll("[^A-Za-z0-9\\s]", "").replaceAll(" ", "");

                CTD.addServer(serverName.length() > 0 ? serverName : "SERVER", ip);
                return ip;
            } else {
                serverName = "SERVER";
                CTD.addServer(serverName.length() > 0 ? serverName : "SERVER", ip);
                // ip = InetAddress.getLocalHost().getHostAddress();
            }
            return "NO";
        } catch (IOException | HeadlessException ex) {
            // Logger.getLogger(APPLET.class.getName()).log(Level.SEVERE, null, ex);
            return "localhost";
        }
    }

    String names[];// = new String[yay.size()/3];
    String counts[];// = new String[yay.size()/3];
    Thread pinger;

    public void getHosts() {
        ArrayList<String> yay = null;
        while (yay == null) {
            // System.err.println("LOOKING");
            yay = CTD.getServers();
        }
        // System.out.println(yay.size());
        hosts = new String[yay.size() / 3];
        names = new String[yay.size() / 3];
        counts = new String[yay.size() / 3];
        for (int i = 0; i < yay.size(); i++) {
            if (i % 3 == 0) {
                names[i / 3] = yay.get(i);
            }
            if (i % 3 == 1) {
                hosts[i / 3] = yay.get(i);
            }
            if (i % 3 == 2) {
                counts[i / 3] = "Players:   " + yay.get(i);
            }
        }
        Row[] newRows = new Row[names.length];
        for (int i = 0; i < newRows.length; i++) {
            newRows[i] = new Row(names[i], "PINGING", counts[i]);
        }
        menu.setModel(new JComboBox<>(newRows).getModel());
        // menu.setModel(new JComboBox<>(names).getModel());
        if (pinger != null) {
            pinger.interrupt();
        }
        Runnable waffles;
        waffles = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < counts.length; i++) {
                    String st = "DOWN";
                    try {
                        Scanner n = new Scanner(
                                Runtime.getRuntime().exec("ping " + hosts[i] + " -n 1").getInputStream());
                        while (n.hasNext()) {
                            st += n.nextLine();
                        }
                        // System.out.println(st);
                        Pattern p = Pattern.compile("Average = (.*?)ms");
                        Matcher m = p.matcher(st);
                        if (m.find()) {
                            st = "PING:   " + m.group(1);
                        }
                    } catch (IOException ex) {
                    }
                    ((Row) menu.getModel().getElementAt(i)).val = st;
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        return;
                    }
                }
            }
        };
        pinger = new Thread(waffles);
        pinger.start();
    }

    @Override
    public void update(Graphics GameGraphics) {
        if (doubleBuffer == null) {
            doubleBuffer = createImage(900, 900);
        }
        Graphics DoubleBufferGraphics = doubleBuffer.getGraphics();
        DoubleBufferGraphics.setColor(this.getBackground());
        DoubleBufferGraphics.fillRect(0, 0, 900, 900);
        DoubleBufferGraphics.setColor(getForeground());
        paint(DoubleBufferGraphics);
        paint(GameGraphics);
        GameGraphics.drawImage(doubleBuffer, 0, 0, getWidth(), getHeight(), this);
    }

    @Override
    public void paint(Graphics g) {
        if (notDone) {
            super.paint(g);
            return;
        }
        if (screenBuffer == null) {
            screenBuffer = new BufferedImage(300, 300, BufferedImage.TYPE_INT_ARGB);
            graphicsBuffer = screenBuffer.createGraphics();
        }
        if (bigscreenBuffer == null) {
            bigscreenBuffer = new BufferedImage(900, 900, BufferedImage.TYPE_INT_ARGB);
            biggraphicsBuffer = bigscreenBuffer.createGraphics();
            biggraphicsBuffer.setFont(chatFont);
        }
        setSize(owner.getSize());

        setVisible(owner.isVisible());
        // Graphics2D pancakes = (Graphics2D)g;
        // double scale = owner.getHeight()/300;
        // pancakes.scale(scale, scale);
        graphicsBuffer.setColor(Color.black);
        if (world != null) {

            // world.drawPlayers(graphicsBuffer);
            if (matchOver > 0) {
                if (!spellselection.isVisible()) {
                    matchOver--;
                }
                HP = MAXHP;
                world.dead = false;
                if (matchOver == 0) {
                    world.y = 0;
                    world.x = (goodTeam ? world.wIdTh / 2 : 0) + random.nextInt(world.wIdTh / 2);
                    world.dead = false;
                    passiveList[spellBook].onSpawn(this);
                    spellselection.setVisible(false);
                }
                biggraphicsBuffer.drawImage(bimage, 0, 0, 900, 900, null);
                biggraphicsBuffer.setColor(Color.white);
                biggraphicsBuffer.fillRect(399, 199, 100, 50);
                biggraphicsBuffer.setColor(Color.black);
                biggraphicsBuffer.drawRect(400, 200, 100, 50);
                biggraphicsBuffer.drawRect(399, 199, 100, 50);
                biggraphicsBuffer.drawString("LOADOUTS", 400, 230);
                biggraphicsBuffer.drawString("Starting match in " + (1 + (matchOver / 40)) + "...", 360, 300);
                biggraphicsBuffer.drawString("Combatants:", 400, 326);
                for (int i = 0; i < world.playerList.size(); i++) {
                    Player p = world.playerList.get(i);
                    biggraphicsBuffer.setColor(gameMode > 0 ? (p.myTeam ? Color.GREEN : Color.red) : Color.red);
                    biggraphicsBuffer.drawString(p.username, 424, 336 + 16 + (i * 16));
                }
                if (chatActive) {
                    biggraphicsBuffer.setColor(Color.gray);
                    biggraphicsBuffer.fillRect(32, 810, biggraphicsBuffer.getFontMetrics().stringWidth(chatMessage),
                            20);
                    biggraphicsBuffer.setColor(Color.white);
                    biggraphicsBuffer.drawString(chatMessage, 32, 830);
                }
                drawChat();
            } else {
                world.onDraw(graphicsBuffer);
                world.drawEntities(graphicsBuffer);
                // if (timeToHeal % 10 == 0)

                // calculateLoS();
                // graphicsBuffer.setColor(dark);
                // graphicsBuffer.fill(hideFromMe);
                // graphicsBuffer.setPaint(null);
                graphicsBuffer.setColor(Color.black);
                graphicsBuffer.drawRect((int) world.x - world.viewX - 16, (int) world.y - world.viewY - 64, 32, 4);
                if (world.keys[KeyEvent.VK_S]) {
                    graphicsBuffer.drawRect((int) world.x - world.viewX - 16, (int) world.y - world.viewY - 61, 32, 4);
                }
                if (lungs < maxlungs) {
                    graphicsBuffer.drawRect((int) world.x - world.viewX - 16, (int) world.y - world.viewY - 66, 32, 4);
                }
                graphicsBuffer.drawRect(0, 0, 4, 300);
                graphicsBuffer.setColor(Color.green);
                graphicsBuffer.drawRect((int) world.x - world.viewX - 15, (int) world.y - world.viewY - 63,
                        (int) (30d * ((double) HP / (double) MAXHP)), 2);
                if (lungs < maxlungs) {
                    graphicsBuffer.setColor(Color.white);
                    graphicsBuffer.drawRect((int) world.x - world.viewX - 15, (int) world.y - world.viewY - 67,
                            (int) (30d * ((double) lungs / (double) maxlungs)), 2);
                }
                if (world.keys[KeyEvent.VK_S]) {
                    graphicsBuffer.setColor(Color.RED);
                    graphicsBuffer.drawRect((int) world.x - world.viewX - 15, (int) world.y - world.viewY - 60,
                            (int) (30d * ((double) dig / (double) 100)), 2);
                }
                if (dpyeng > energico) {
                    dpyeng -= ((dpyeng - energico) / 10);
                } else {
                    dpyeng = energico;
                }
                graphicsBuffer.setColor(Color.orange);
                graphicsBuffer.fillRect(1, 1, 2, (int) (300d * ((double) dpyeng / (double) maxeng)));
                graphicsBuffer.setColor(Color.red);
                graphicsBuffer.drawRect(1, 1, 2, (int) (300d * ((double) dpyeng / (double) maxeng)));
                for (int i = 0; i < 5; i++) {
                    graphicsBuffer.drawImage(spellList[spellBook][i].getImage().getImage(), 4 + i * 34, 0, 32, 16,
                            this);
                    if (this.leftClick == i) {
                        graphicsBuffer.setColor(Color.orange);
                        graphicsBuffer.drawRect(4 + i * 34, 0, 32, 16);
                        graphicsBuffer.drawString("L", 4 + i * 34, 10);
                    } else {
                        if (this.rightClick == i) {
                            graphicsBuffer.setColor(Color.red);
                            graphicsBuffer.drawRect(4 + i * 34, 0, 32, 16);
                            graphicsBuffer.drawString("R", 4 + i * 34, 10);
                        } else {
                            if (this.midClick == i) {
                                graphicsBuffer.setColor(Color.YELLOW);
                                graphicsBuffer.drawRect(4 + i * 34, 0, 32, 16);
                                graphicsBuffer.drawString("M", 4 + i * 34, 10);
                            } else {
                                if (inputer.setTo == i) {
                                    graphicsBuffer.setColor(Color.GREEN);
                                    graphicsBuffer.drawRect(4 + i * 34, 0, 32, 16);
                                } else {
                                    graphicsBuffer.setColor(Color.white);
                                    graphicsBuffer.drawRect(4 + i * 34, 0, 32, 16);

                                }
                            }
                        }
                    }
                }
                graphicsBuffer.setColor(Color.BLUE);
                graphicsBuffer.fillRect(1, 1, 2, (int) (300d * ((double) energico / (double) maxeng)));

                graphicsBuffer.setColor(purple);
                graphicsBuffer.drawRect(1, 1, 2, (int) (300d * ((double) energico / (double) maxeng)));

                // graphicsBuffer.drawImage(this.sightSeeing, 0, 0, null);
                biggraphicsBuffer.drawImage(screenBuffer,
                        (bigscreenBuffer.getWidth() - bigscreenBuffer.getHeight()) / 2, 0, bigscreenBuffer.getHeight(),
                        bigscreenBuffer.getHeight(), this);
                world.drawPlayers(biggraphicsBuffer);

                for (Entity e : world.entityList) {
                    e.drawOverlay(biggraphicsBuffer, world.viewX, world.viewY);
                }
                Composite c = biggraphicsBuffer.getComposite();
                biggraphicsBuffer.setComposite(Additive.additive);
                for (Entity e : world.entityList) {
                    e.drawAdditive(biggraphicsBuffer, world.viewX, world.viewY);
                }
                biggraphicsBuffer.setComposite(c);
                if (chatActive) {
                    biggraphicsBuffer.setColor(Color.gray);
                    biggraphicsBuffer.fillRect(32, 810, biggraphicsBuffer.getFontMetrics().stringWidth(chatMessage),
                            20);
                    biggraphicsBuffer.setColor(Color.white);
                    biggraphicsBuffer.drawString(chatMessage, 32, 830);
                }
                drawChat();
                if (world.keys[KeyEvent.VK_SPACE]) {
                    biggraphicsBuffer.setColor(Color.black);
                    biggraphicsBuffer.fillRect(128, 128, 644, 644);
                    biggraphicsBuffer.setColor(Color.yellow);
                    biggraphicsBuffer.drawString(username, 256, 256);
                    biggraphicsBuffer.drawString("" + score, 512, 256);
                    for (int i = 0; i < world.playerList.size(); i++) {
                        Player p = world.playerList.get(i);
                        biggraphicsBuffer.setColor(gameMode > 0 ? (p.myTeam ? Color.GREEN : Color.red) : Color.red);
                        biggraphicsBuffer.drawString(p.username, 256, 256 + 16 + (i * 16));
                        biggraphicsBuffer.drawString("" + p.score, 512, 256 + 16 + (i * 16));
                    }
                }

                if (world.keys[KeyEvent.VK_ALT]) {
                    biggraphicsBuffer.setColor(Color.red);
                    this.checkCollision(0, 0);
                    AffineTransform prevTrans = biggraphicsBuffer.getTransform();
                    biggraphicsBuffer.scale(3, 3);
                    biggraphicsBuffer.translate(-world.viewX, -world.viewY);
                    biggraphicsBuffer.draw(playerHitbox);
                    biggraphicsBuffer.setTransform(prevTrans);

                }
                if (world.dead) {
                    biggraphicsBuffer.setColor(deadbg);
                    biggraphicsBuffer.fillRect(0, 0, 900, 900);
                    biggraphicsBuffer.setColor(Color.BLACK);
                    if (gameMode == Server.THEHIDDEN && !goodTeam) {
                        if (lastHit == ID) {
                            biggraphicsBuffer.drawString("You killed yourself... That's The Hidden's Job!", 128, 128);
                        } else {
                            biggraphicsBuffer.drawString("You were defeated by The Hidden!", 128, 128);
                        }
                    } else {
                        biggraphicsBuffer.drawString("You were defeated, press space to get back in on the action!",
                                128, 128);
                        biggraphicsBuffer.drawString("In the meantime, use S and W to switch loadouts.", 128, 144);
                        biggraphicsBuffer.drawString("Forced respawn in " + (1 + ((400 - forcedRespawn) / 40)) + "...",
                                128, 160);
                    }
                    if (chatActive) {
                        biggraphicsBuffer.setColor(Color.gray);
                        biggraphicsBuffer.fillRect(32, 810, biggraphicsBuffer.getFontMetrics().stringWidth(chatMessage),
                                20);
                        biggraphicsBuffer.setColor(Color.white);
                        biggraphicsBuffer.drawString(chatMessage, 32, 830);
                    }
                    drawChat();
                    for (int yyy = 0; yyy < 5; yyy++) {
                        if (yyy == spellBook) {
                            biggraphicsBuffer.setColor(Color.BLUE);
                        } else {
                            biggraphicsBuffer.setColor(Color.BLACK);
                        }
                        for (int xxx = 0; xxx < spellList[yyy].length; xxx++) {
                            biggraphicsBuffer.drawString(spellList[yyy][xxx].getName(), 128 + (xxx * 128),
                                    300 + (yyy * 64));
                        }
                    }
                    if (world.keys[KeyEvent.VK_W]) {
                        if (spellBook > 0) {
                            spellBook--;
                        }
                        world.keys[KeyEvent.VK_W] = false;
                    }
                    if (world.keys[KeyEvent.VK_S]) {
                        if (spellBook < 4) {
                            spellBook++;
                        }
                        world.keys[KeyEvent.VK_S] = false;
                    }
                    if ((!(gameMode == Server.THEHIDDEN && !goodTeam)) || lastHit == ID) {
                        if (world.keys[KeyEvent.VK_SPACE] || forcedRespawn++ > 400) {
                            forcedRespawn = 0;
                            world.y = 0;
                            world.x = (goodTeam ? world.wIdTh / 2 : 0) + random.nextInt(world.wIdTh / 2);
                            world.dead = false;
                            passiveList[spellBook].onSpawn(this);
                            HP = MAXHP;
                            lastHit = ID;
                        }
                    }
                }
            }
        }
        g.drawImage(bigscreenBuffer, (getWidth() - getHeight()) / 2, 0, getHeight(), getHeight(), null);
    }

    Font chatFont = new Font("Arial", Font.BOLD, 18);
    Font nameFont = new Font("Arial", Font.BOLD, 12);

    public void sendMessage(String s) {
        sendMessage(s, 0x3333FF);
    }

    public void sendMessage(String s, int color) {
        ByteBuffer bb = ByteBuffer.allocate(s.length() * 4 + 4);
        bb.putInt(color);
        Server.putString(bb, s);
        try {
            out.addMesssage(bb, Server.MESSAGE);
        } catch (IOException ex) {
            // ex.printStackTrace();
        }
    }

    public static double lengthdir_x(double R, double T) {
        return (R * (Math.cos(T * Math.PI / 180)));
    }

    public static double lengthdir_y(double R, double T) {
        return (-R * (Math.sin(T * Math.PI / 180)));
    }

    public static double pointDir(double x1, double y1, double x2, double y2) {
        return Math.toDegrees(Math.atan2(y2 - y1, x2 - x1));
    }

    public static double pointDis(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    public void readWorld() throws Exception {
        ByteBuffer toRead;
        busy = true;
        world.status = 0;
        toRead = Server.readByteBuffer(input);// ByteBuffer.wrap(buf);
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
            ArrayList<Integer> TeamTemp = (ArrayList<Integer>) badTeam.clone();
            badTeam = (ArrayList<Integer>) myTeam.clone();
            myTeam = TeamTemp;
            goodTeam = false;
        }
        for (Player p : world.playerList) {
            p.myTeam = false;
            if (myTeam.contains(p.ID) && gameMode > 0) {
                p.myTeam = true;
            }
        }
        HP = MAXHP;
        passiveList[spellBook].onSpawn(this);
        for (Player p : world.playerList) {
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
        ByteBuffer chunks[] = new ByteBuffer[world.wIdTh / 100];
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

    boolean expand = false;

    public void sendMovement() {
        if (ID == -1) {
            return;
        }
        try {
            // out.write(Server.MOVE);
            ByteBuffer toSend = ByteBuffer.allocate(4 * 4);
            toSend.putShort((short) world.x);
            toSend.putShort((short) world.y);
            toSend.putShort((short) world.move);
            toSend.putShort((short) world.vspeed);
            toSend.putShort((short) world.leftArmAngle);
            toSend.putShort((short) world.rightArmAngle);
            toSend.putShort(world.status);
            toSend.putShort(HP);
            // Server.writeByteBuffer(toSend, out);
            out.addMesssage(toSend, Server.MOVE);

        } catch (Exception e) {
            // e.printStackTrace();
        }
    }

    public void readEntityList(ByteBuffer toRead) {
        boolean done;
        world.entityList.clear();
        done = false;
        try {
            while (!done) {
                if (!toRead.hasRemaining())
                    break;
                String className = Server.getString(toRead);
                Class[] args1 = new Class[2];
                args1[0] = ByteBuffer.class;
                args1[1] = World.class;
                try {
                    Class.forName(className).getMethod("reconstruct", args1).invoke(null, toRead, world);
                    world.entityList.get(world.entityList.size() - 1).setID(toRead.getInt());
                } catch (ClassNotFoundException | NoSuchMethodException | SecurityException ex) {
                    Logger.getLogger(APPLET.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (Exception e) {

        }
    }

    int shockdrain = 0;

    public int hurt(double pain) {
        if (passiveList[spellBook].getName().equals("Lightning Shield")) {
            if (random.nextInt(15 - (shockdrain * 2)) == 0) {
                pain *= .25;
                if (pain < 1) {
                    pain = 1;
                }
                energico -= pain * 50;
                world.entityList.add(new ShockEffectEntity((int) world.x, (int) world.y, 6 + (int) pain));
            }
        }
        HP -= pain;
        return (int) pain;
    }

    public static boolean portAvailable(int port) {
        if (port < 0 || port > 65535) {
            throw new IllegalArgumentException("Invalid start port: " + port);
        }

        ServerSocket ss = null;
        try {
            ss = new ServerSocket(port);
            ss.setReuseAddress(true);
            return true;
        } catch (IOException e) {
        } finally {

            if (ss != null) {
                try {
                    ss.close();
                } catch (IOException e) {
                    /* should not be thrown */
                }
            }
        }

        return false;
    }

    String hostIP = "";
    String serverName = "";
    ServerGUI sgui;

    public void serverOutput() {
        // sgui = new ServerGUI();
        // sgui.setVisible(true);
    }

    static class ImagePanel extends JPanel {
        private Image image;

        public ImagePanel(Image image) {
            this.image = image;

        }

        @Override
        protected void paintComponent(Graphics g) {
            // super.paintComponent(g);

            g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        // super.paintComponent(g);

        g.drawImage(bimage, 0, 0, getWidth(), getHeight(), null);
    }

    private static class Row {

        private String id = "", val = "", extra = "";

        public Row(String id, String val, String extra) {
            this.id = id;
            this.val = val;
            this.extra = extra;
        }

        public String getId() {
            return id;
        }

        public String getVal() {
            return val;
        }

        public String getExtra() {
            return extra;
        }
    }

    private static class RowCellRenderer extends JTable implements ListCellRenderer {

        public RowCellRenderer() {
            setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        }

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
                boolean cellHasFocus) {
            setModel(new RowTableModel((Row) value));
            this.getColumnModel().getColumn(0).setWidth(100);
            if (isSelected) {
                getSelectionModel().setSelectionInterval(0, 0);
            }
            return this;
        }
    }

    private static class RowTableModel extends AbstractTableModel {

        private Row row;

        public RowTableModel(Row row) {
            this.row = row;
        }

        int colCount = 3;

        @Override
        public int getRowCount() {
            return 1;
        }

        @Override
        public int getColumnCount() {
            return colCount;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            if (row == null) {
                return "";
            }
            switch (columnIndex) {
                case 0:
                    return row.getId();
                case 1:
                    return row.getVal();
                case 2:
                    return row.getExtra();
            }
            return null;
        }
    }

    RealClip airCast = ResourceLoader.loadSound("https://west-it.webs.com/sounds/airCast.wav", "aircast.wav");
    RealClip fireCast = ResourceLoader.loadSound("https://west-it.webs.com/sounds/fireCast.wav", "firecast.wav");

    public void drawChat() {
        for (int i = 0; i < chat.length; i++) {
            biggraphicsBuffer.setColor(backgroundChat);
            biggraphicsBuffer.fillRect(32, 826 - (16 * ((chat.length - i) + 1)),
                    biggraphicsBuffer.getFontMetrics().stringWidth(chat[i]), 16);
            biggraphicsBuffer.setColor(chatcolor[i]);
            biggraphicsBuffer.drawString(chat[i], 32, 840 - (16 * ((chat.length - i) + 1)));
        }
    }

    static long authCode = -1;

    static protected long getAuth() {
        if (authCode == -1) {
            try {
                long s1111I11I11 = APPLET.class.getFields().length;
                long s1I1111II11 = Server.class.getFields().length;
                long s11I1111I1I = World.class.getFields().length;
                long sI1I1I11I1I = PlayerOnline.class.getFields().length;
                authCode = (((((sI1I1I11I1I * sI1I1I11I1I) - sI1I1I11I1I) / s1I1111II11) + s1111I11I11) * s11I1111I1I)
                        / s1I1111II11;
                // authCode = 1;
            } catch (Exception ex) {
                Logger.getLogger(APPLET.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return authCode;
    }
}
