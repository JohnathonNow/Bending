package com.johnwesthoff.bending;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Insets;
import java.awt.MenuItem;
import java.awt.Polygon;
import java.awt.PopupMenu;
import java.awt.Rectangle;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.table.AbstractTableModel;

import com.johnwesthoff.bending.app.game.GameService;
import com.johnwesthoff.bending.app.game.GameServiceFactory;
import com.johnwesthoff.bending.entity.Entity;
import com.johnwesthoff.bending.entity.ShockEffectEntity;
import com.johnwesthoff.bending.logic.ClientInputListener;
import com.johnwesthoff.bending.logic.Player;
import com.johnwesthoff.bending.logic.PlayerOnline;
import com.johnwesthoff.bending.logic.World;
import com.johnwesthoff.bending.networking.NetworkManager;
import com.johnwesthoff.bending.networking.handlers.DeathEvent;
import com.johnwesthoff.bending.networking.handlers.LoginEvent;
import com.johnwesthoff.bending.networking.handlers.MapEvent;
import com.johnwesthoff.bending.networking.handlers.MessageEvent;
import com.johnwesthoff.bending.networking.handlers.MoveEvent;
import com.johnwesthoff.bending.spells.Spell;
import com.johnwesthoff.bending.ui.AppletActionListener;
import com.johnwesthoff.bending.ui.ClothingChooser1;
import com.johnwesthoff.bending.ui.Register;
import com.johnwesthoff.bending.ui.ServerGUI;
import com.johnwesthoff.bending.ui.SpellList1;
import com.johnwesthoff.bending.ui.Verify;
import com.johnwesthoff.bending.util.audio.RealClip;
import com.johnwesthoff.bending.util.network.NetworkMessage;
import com.johnwesthoff.bending.util.network.OrderedOutputStream;
import com.johnwesthoff.bending.util.network.ResourceLoader;
import com.johnwesthoff.bending.util.network.StringLongBoolean;

/**
 *
 * @author Family
 */
public class Client extends JPanel implements Runnable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public boolean goodTeam = false;
    public String chat[] = { "", "", "", "", "", "", "", "", "", "" };
    public Color chatcolor[] = new Color[] { Color.PINK, Color.PINK, Color.PINK, Color.PINK, Color.PINK, Color.PINK,
            Color.PINK, Color.PINK, Color.PINK, Color.PINK, Color.PINK };
    public static StringLongBoolean unlocks = new StringLongBoolean("0");
    public int score = 0;
    public int mapRotation = 0;
    public boolean chatActive = false;
    public String chatMessage = "";
    public int gameMode = 1;
    public int fireTime = 0;
    public int ticks = 0;
    public int whoseTurn = -1;
    public Color purple = new Color(0xA024C2), backgroundChat = new Color(0, 0, 0, 200),
            deadbg = new Color(255, 255, 255, 127), dark = new Color(0, 0, 0, 128);
    public short matchOver = 0, forcedRespawn = 0;
    public static AppletActionListener actioner;
    public static ClientInputListener inputer;
    public ArrayList<Integer> myTeam = new ArrayList<>(), badTeam = new ArrayList<>();
    public static boolean currentlyLoggedIn = false;
    public double maxeng, dpyeng, energico = maxeng = dpyeng = 1000;
    public int port = 25565;
    public Properties userpassinfo;
    public ClothingChooser1 cc = new ClothingChooser1(this);
    public double engrecharge = 4;
    public Random random = new Random();
    public String serverIP = "LocalHost";
    public Thread mainProcess;
    public boolean notDone = true;
    public boolean ignored = true;
    public Image doubleBuffer;
    public BufferedImage Grass, Sky, Sand, Stone, screenBuffer, Bark, Ice, LavaLand, Crystal, ether, bigscreenBuffer;
    public static BufferedImage bimage;
    public Graphics2D graphicsBuffer, biggraphicsBuffer;
    public URL base;// = getDocumentBase();
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
    public JFrame owner;
    public short MAXHP, HP = MAXHP = 100;
    public static boolean gameAlive = true;
    public int maxlungs, lungs = maxlungs = 100;
    public static double runningSpeed = 1d, swimmingSpeed = 1d;
    public static Client thisone;
    public static int XP = 0;
    public double prevVspeed = 0;
    public static boolean shortJump = false;
    public String killMessage = "~ was defeated by `.";
    public int timeToHeal = 0;
    public JComboBox<Object> menu;
    public JButton connect, hosting, refresh, register, verify, ChooseSpells, chooseclothing, mapMaker;
    public JCheckBox JRB;
    public String[] hosts = new String[1];
    public static JTextField jtb = new JTextField();
    public JPasswordField jtp = new JPasswordField();
    public JLabel jUs = new JLabel("Username:"), jPa = new JLabel("Password:");
    public Register form = new Register();
    public Verify exactly = new Verify();
    public SystemTray ST;
    public TrayIcon trayIcon;
    public Spell[][] spellList;
    public Spell[] passiveList;
    public int leftClick = 0, rightClick = 1, midClick = 2;
    public double xspeed = 0;
    public SpellList1 spellselection;
    public static JFrame container;
    public static JTabbedPane immaKeepTabsOnYou;
    public double prevMove;
    public short turnVisible = -1, removeAura = -1;

    /**
     * GameService instance
     */
    public GameService gameService;

    public Client() {
        super();
        new File(ResourceLoader.dir).mkdirs();
        new File(ResourceLoader.dir + "images").mkdirs();
        new File(ResourceLoader.dir + "sounds").mkdirs();
        try {
            bimage = ResourceLoader.loadImage("AgedPaper.png");
            Thread.sleep(100);
        } catch (final Exception ex) {
            // ex.printStackTrace();
        }
    }

    // RadialGradientPaint cantSee = new RadialGradientPaint(150f,150f,150f,new
    // float[]{0f,1f},new Color[]{new Color(0,0,0,0),new Color(0,0,0,255)});
    Polygon lineOfSight = new Polygon();

    public static void main(final String args[]) {
        System.out.println("Loading BENDING v 2.020.09.17" + System.getProperty("os.name") + File.separator);

        gameAlive = true;
        Spell.init();
        final Client main = new Client();
        immaKeepTabsOnYou = new JTabbedPane();
        actioner = new AppletActionListener(main);
        inputer = new ClientInputListener(main);
        thisone = main;

        // using factory to inject dependency
        main.gameService = GameServiceFactory.create();
        // @TODO : define here player instance

        main.setSize(960, 540);
        main.setPreferredSize(main.getSize());

        // JPanel e = new JPanel();
        // e.setSize(300,300);
        container = new JFrame() {
            private static final long serialVersionUID = 1255782830759040881L;

            @Override
            public void paintComponents(final Graphics g) {
                // super.paintComponent(g);

                g.drawImage(Client.bimage, 0, 0, getWidth(), getHeight(), null);
                super.paintComponents(g);
            }
        };
        main.owner = container;
        // container.setUndecorated(true);
        main.form.setVisible(false);
        container.setSize(main.getSize());
        container.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        main.cc.setVisible(false);
        main.spellList = new Spell[10][5];// {{Spell.spells.get(0),Spell.spells.get(1),Spell.spells.get(2),Spell.spells.get(3),Spell.spells.get(5)},{Spell.spells.get(0),Spell.spells.get(1),Spell.spells.get(2),Spell.spells.get(3),Spell.spells.get(5)},{Spell.spells.get(0),Spell.spells.get(1),Spell.spells.get(2),Spell.spells.get(3),Spell.spells.get(5)},{Spell.spells.get(0),Spell.spells.get(1),Spell.spells.get(2),Spell.spells.get(3),Spell.spells.get(5)},{Spell.spells.get(0),Spell.spells.get(1),Spell.spells.get(2),Spell.spells.get(3),Spell.spells.get(5)}});
        main.spellList[0] = (new Spell[] { Spell.spells.get(0), Spell.spells.get(0), Spell.spells.get(0),
                Spell.spells.get(0), Spell.spells.get(0) });
        main.spellList[1] = (new Spell[] { Spell.spells.get(0), Spell.spells.get(0), Spell.spells.get(0),
                Spell.spells.get(0), Spell.spells.get(0) });
        main.spellList[2] = (new Spell[] { Spell.spells.get(0), Spell.spells.get(0), Spell.spells.get(0),
                Spell.spells.get(0), Spell.spells.get(0) });
        main.spellList[3] = (new Spell[] { Spell.spells.get(0), Spell.spells.get(0), Spell.spells.get(0),
                Spell.spells.get(0), Spell.spells.get(0) });
        main.spellList[4] = (new Spell[] { Spell.spells.get(0), Spell.spells.get(0), Spell.spells.get(0),
                Spell.spells.get(0), Spell.spells.get(0) });

        main.spellList[5] = (new Spell[] { Spell.spells.get(1), Spell.spells.get(11), Spell.spells.get(18),
                Spell.spells.get(19), Spell.spells.get(7) });
        // container.add(me);
        main.passiveList = (new Spell[] { Spell.noSpell, Spell.noSpell, Spell.noSpell, Spell.noSpell, Spell.noSpell,
                Spell.noSpell });
        // container.add(me);
        main.JRB = new JCheckBox() {
            private static final long serialVersionUID = -3327024393489960573L;

            @Override
            public void paintComponent(final Graphics g) {
                super.paintComponent(g);
                g.drawImage(bimage, getX(), getY(), getWidth(), getHeight(), null);
            }
        };

        main.userpassinfo = new Properties();
        if (new File(ResourceLoader.dir + "properties.xyz").exists()) {
            try {
                main.userpassinfo.load(new FileInputStream(new File(ResourceLoader.dir + "properties.xyz")));
            } catch (final Exception ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (main.userpassinfo.isEmpty()) {
            main.userpassinfo.setProperty("username", "");
            main.userpassinfo.setProperty("password", "");
            main.userpassinfo.setProperty("remember", "");
            try {
                main.userpassinfo.store(new FileOutputStream(new File(ResourceLoader.dir + "properties.xyz")), "");
            } catch (final Exception ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            if (!main.userpassinfo.getProperty("remember", "").equals("")) {
                Client.jtb.setText(main.userpassinfo.getProperty("username", ""));
                main.jtp.setText(main.userpassinfo.getProperty("password", ""));
                main.JRB.setSelected(true);
            }
        }
        main.setBackground(Color.white);
        main.setLayout(null);
        main.menu = new JComboBox<Object>() {
            private static final long serialVersionUID = -5781421606071388365L;

            @Override
            public Dimension getSize() {
                final Dimension dim = super.getSize();
                dim.width = 256;
                return dim;
            }
        };
        main.menu.setRenderer(new RowCellRenderer());
        main.connect = new JButton("Connect");
        main.hosting = new JButton("Host");
        main.refresh = new JButton("Refresh");
        main.register = new JButton("Register");
        main.verify = new JButton("Log In");
        main.ChooseSpells = new JButton("Spellbooks");
        main.chooseclothing = new JButton("Appearance");
        main.mapMaker = new JButton("Map Editor");
        container.setMaximumSize(main.getSize());
        main.addMouseListener(inputer);
        main.addMouseMotionListener(inputer);
        container.addKeyListener(inputer);
        container.addMouseListener(inputer);
        container.setResizable(true);
        if (!(args.length > 0 && args[0].equals("Client"))) {
            container.setVisible(true);
        }
        main.requestFocus();
        main.add(main.jUs);
        main.jUs.setLocation(16, 16);
        main.jUs.setSize(64, 16);
        main.add(Client.jtb);
        Client.jtb.setLocation(96, 16);
        main.add(main.menu);
        main.menu.setLocation(-24, -64);
        main.menu.setSize(0, 0);
        main.menu.setVisible(false);
        main.add(main.connect);
        main.connect.setLocation(80, 80 + 6);
        main.connect.setSize(140, 16);
        main.add(main.hosting);
        main.hosting.setLocation(80, 80 + 6 + 32);
        main.hosting.setSize(140, 16);
        main.add(main.ChooseSpells);
        main.ChooseSpells.setLocation(80, 80 + 6 + 32 + 32);
        main.ChooseSpells.setSize(140, 16);
        main.add(main.chooseclothing);
        main.chooseclothing.setLocation(80, 80 + 6 + 32 + 32 + 32);
        main.chooseclothing.setSize(140, 16);

        main.add(main.mapMaker);
        main.mapMaker.setLocation(80, 80 + 6 + 32 + 32 + 32 + 32);
        main.mapMaker.setSize(140, 16);
        /// me.JRB.setText("Remember me?");

        main.JRB.setActionCommand("RM");
        main.spellselection = new SpellList1(main);
        main.spellselection.setVisible(false);
        main.ChooseSpells.addActionListener(actioner);
        main.ChooseSpells.setActionCommand(main.ChooseSpells.getText());
        main.chooseclothing.addActionListener(actioner);
        main.chooseclothing.setActionCommand(main.chooseclothing.getText());
        main.mapMaker.addActionListener(actioner);
        main.mapMaker.setActionCommand(main.mapMaker.getText());
        Client.jtb.setSize(300 - 128, 16);
        Client.jtb.setPreferredSize(Client.jtb.getSize());
        // me.jtb.setLocation(16, 16);dfsdfsdf
        Client.jtb.setVisible(true);
        Client.jtb.requestFocus();
        Client.jtb.addKeyListener(inputer);
        main.jtp.setSize(300 - 128, 16);
        main.jtp.setPreferredSize(Client.jtb.getSize());
        // me.jtp.setLocation(16, 16);
        main.jtp.setVisible(true);
        main.jtp.requestFocus();
        main.jtp.addKeyListener(inputer);
        main.connect.setActionCommand(main.connect.getText());
        main.connect.addActionListener(actioner);
        main.hosting.setActionCommand(main.hosting.getText());
        main.hosting.addActionListener(actioner);
        main.refresh.setActionCommand(main.refresh.getText());
        main.refresh.addActionListener(actioner);
        main.register.setActionCommand(main.register.getText());
        main.register.addActionListener(actioner);
        main.verify.setActionCommand(main.verify.getText());
        main.verify.addActionListener(actioner);
        main.setFocusable(true);
        main.addKeyListener(inputer);
        main.setVisible(true);
        UIManager.getDefaults().put("TabbedPane.contentBorderInsets", new Insets(0, 0, 0, 0));
        UIManager.getDefaults().put("TabbedPane.tabsOverlapBorder", true);
        immaKeepTabsOnYou.setUI(new BasicTabbedPaneUI() {
            @Override
            protected int calculateTabAreaHeight(final int tab_placement, final int run_count,
                    final int max_tab_height) {
                return 0;
            }
        });
        immaKeepTabsOnYou.addKeyListener(inputer);
        immaKeepTabsOnYou.addMouseListener(inputer);
        immaKeepTabsOnYou.addMouseMotionListener(inputer);
        immaKeepTabsOnYou.addTab("THEGAME", main);
        immaKeepTabsOnYou.addTab("SPELLSELECT", main.spellselection);
        immaKeepTabsOnYou.addTab("SPELLLIST", main.spellselection.choochootrain);
        immaKeepTabsOnYou.addTab("PASSIVELIST", main.spellselection.choochootrain2);
        immaKeepTabsOnYou.addTab("ClothingChooser", main.cc);
        immaKeepTabsOnYou.setLocation(-1, -1);
        immaKeepTabsOnYou.setBackground(Color.black);
        immaKeepTabsOnYou.setBorder(BorderFactory.createEmptyBorder());
        container.setContentPane(immaKeepTabsOnYou);
        main.repaint();
        main.mainProcess = new Thread(main);
        main.mainProcess.start();
        main.getHosts();
        main.chooseclothing.setEnabled(false);
        main.ChooseSpells.setEnabled(false);
        main.connect.setEnabled(false);
        main.ST = SystemTray.getSystemTray();
        try {
            // container.getContentPane().setB

            final PopupMenu pop = new PopupMenu();
            main.trayIcon = new TrayIcon(ResourceLoader.loadImage("GrassTexture.png"));
            main.trayIcon.setToolTip("DestructibleTerrain");
            final MenuItem exitItem = new MenuItem("Exit");
            final MenuItem hideItem = new MenuItem("Hide");
            final MenuItem showItem = new MenuItem("Show");
            final MenuItem restartItem = new MenuItem("Restart");
            pop.add(showItem);
            pop.add(hideItem);
            pop.add(exitItem);
            pop.add(restartItem);
            hideItem.addActionListener(actioner);
            exitItem.addActionListener(actioner);
            showItem.addActionListener(actioner);
            restartItem.addActionListener(actioner);
            main.trayIcon.setPopupMenu(pop);
            main.trayIcon.setActionCommand("Tray");
            main.trayIcon.addActionListener(actioner);
            final Frame frame = new Frame("");
            frame.setUndecorated(true);
            frame.setResizable(false);
            frame.setVisible(false);
            main.trayIcon.addMouseListener(new MouseListener() {

                @Override
                public void mouseClicked(final MouseEvent e) {
                    // throw new UnsupportedOperationException("Not supported yet.");
                }

                @Override
                public void mousePressed(final MouseEvent e) {
                    // throw new UnsupportedOperationException("Not supported yet.");
                }

                @Override
                public void mouseReleased(final MouseEvent e) {
                    // throw new UnsupportedOperationException("Not supported yet.");
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        frame.setVisible(true);
                        frame.add(pop);
                        pop.show(frame, e.getXOnScreen(), e.getYOnScreen());
                        frame.setVisible(false);
                    }
                }

                @Override
                public void mouseEntered(final MouseEvent e) {
                    // throw new UnsupportedOperationException("Not supported yet.");
                }

                @Override
                public void mouseExited(final MouseEvent e) {
                    // throw new UnsupportedOperationException("Not supported yet.");
                }
            });
            {

            }
            if (args.length > 0 && args[0].equals("Client")) {
                container.setVisible(false);
            } else {
                // me.ST.add(me.trayIcon);
            }
            container.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        } catch (final Exception ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

        Client.XP = main.gameService.getPlayerExperience(Client.jtb.getText(), "PASS IGNORED");

        main.cc.loadClothing();
        main.spellselection.loadSpells();
        main.chooseclothing.setEnabled(true);
        main.ChooseSpells.setEnabled(true);
        main.connect.setEnabled(true);

        main.gameService.getUnlocks(Client.jtb.getText(), "I NOW DO NOTHING");

    }

    // @Override
    public void init() {
        try {
            loggedOn = true;
            temp = "C:";// (System.getenv("TEMP"));
            Grass = ResourceLoader.loadImage("GrassTexture.jpg");
            Sky = ResourceLoader.loadImage("SkyTexture.jpg");
            Sand = ResourceLoader.loadImage("SandTexture.jpg");
            Stone = ResourceLoader.loadImage("StoneTexture.jpg");
            Bark = ResourceLoader.loadImage("BarkTexture.jpg");
            Ice = ResourceLoader.loadImage("iceTexture.png");
            Crystal = ResourceLoader.loadImage("crystalTexture.png");
            LavaLand = ResourceLoader.loadImage("lavalandTexture.png");
            ether = ResourceLoader.loadImage("ether.png");
            // me.getGraphics().clearRect(0, 0, me.getWidth(), me.getHeight());
            // container.requestFocus();
            // me.transferFocus();
            // me.requestFocus();
            // me.validate();
        } catch (final Exception ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int ID = 0;
    public boolean started = false;
    // @Override
    public short dig = 0;
    public boolean loggedIn = false;
    public boolean failed = false;
    public Thread communication;
    public static byte[] Clothing = new byte[] { 1, 1, 1, 1, 1, 1 };
    public static int[] Colors = new int[] { Color.red.getRGB(), Color.orange.getRGB(), Color.red.getRGB(),
            Color.orange.getRGB(), Color.black.getRGB(), Color.orange.getRGB() };
    public static int[] Colors2 = new int[] { Color.red.getRGB(), Color.orange.getRGB(), Color.red.getRGB(),
            Color.orange.getRGB(), Color.black.getRGB(), Color.orange.getRGB() };

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
            Player p = new Player(0, 0, Clothing, Colors, Colors2);
            p.username = username;
            out.addMessage(LoginEvent.getPacket(p));
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

    public Rectangle playerHitbox;

    public boolean checkCollision(final float px, final float py) {
        playerHitbox.setLocation((int) world.x - playerHitbox.width / 2, (int) world.y - (World.head + 10));
        return (playerHitbox.contains(px, py));
    }

    public void addChat(final String message, final Color color) {
        for (int i = 0; i < chat.length - 1; i++) {
            chat[i] = chat[i + 1];
            chatcolor[i] = chatcolor[i + 1];
        }
        chat[chat.length - 1] = message;
        chatcolor[chat.length - 1] = color;
    }

    public boolean busy = false;
    public boolean isMyTurn = true;
    public int lastHit = -1;
    public byte sendcount = 0;
    public ArrayList<int[]> stuff = new ArrayList<>();
    public boolean isAlive = true;
    public Area hideFromMe = new Area(), box = new Area(new Rectangle(0, 0, 300, 300));
    public Thread expander;

    // @Override
    public void destroy() {
        isAlive = false;
        gameAlive = false;
    }

    public boolean sendRequest = true;
    public int Xp = 0, Yp = 0;
    public int pc = 0;
    public Color Clear = new Color(0, 0, 0, 0);

    public void calculateLoS() {
        lineOfSight.reset();
        int Xx;
        int Yy;
        int d;
        int dr;
        int dx;
        int dy;
        boolean yes;
        final int resolution = 18;

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
            final Area swag = new Area(lineOfSight);
            hideFromMe.add(box);
            hideFromMe.subtract(swag);
        }

    }

    public boolean refreshShadows = false;
    public long swagTime = 0;

    @Override
    public void run() {
        int counting = 150;
        Thread.currentThread().setPriority(Thread.NORM_PRIORITY);
        lastTime = System.nanoTime();
        double delta = 0;
        while (gameAlive) {
            try {
                // Thread.yield();
            } catch (final Exception e2) {
                e2.printStackTrace();
            }
            final long now = System.nanoTime();

            delta += (now - lastTime) / (1000000000 / Constants.FPS);
            owner.setTitle(" Packet Count: " + pc + " FPS: " + (1000000000 / (now - lastTime)));
            lastTime = now;

            if (!owner.isVisible()) {
                if (!SystemTray.isSupported()) {
                    gameService.tryToRemoveServer(hostIP);
                    System.exit(0);
                }

                // System.out.println(started);
            }
            if (!started) {
                delta = 0;
                try {
                    Thread.sleep(10);
                } catch (final InterruptedException e1) {
                    e1.printStackTrace();
                }
                continue;
            }
            if (matchOver > 0) {
                try {
                    Thread.sleep(10);
                } catch (final InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
            while (delta >= 1) {
                ticks++;
                delta -= 1;
                isMyTurn = (gameMode != Server.TURNBASED) || (whoseTurn == ID);
                if (isMyTurn) {
                    if (removeAura > 0) {
                        removeAura--;
                        world.status |= Constants.ST_DRAIN;
                        if (removeAura == 0) {
                            world.status &= ~Constants.ST_DRAIN;
                            sendMovement();
                        }
                    }
                    if (turnVisible > 0) {
                        turnVisible--;
                        world.status |= Constants.ST_INVISIBLE;
                        if (turnVisible == 0) {
                            world.status &= ~Constants.ST_INVISIBLE;
                            sendMovement();
                        }
                    }
                    if (gameMode == Server.THEHIDDEN) {
                        if (goodTeam) {
                            world.status |= Constants.ST_INVISIBLE;
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
                    if (world.checkCollision(world.x, world.y - World.head)
                            || world.isLiquid(world.x, world.y - World.head)) {
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
                    if (world.isType((int) world.x, (int) world.y, Constants.LAVA)) {
                        world.status |= Constants.ST_FLAMING;
                        killMessage = "~ burned to a crisp after fighting `!";
                    }
                    if ((world.status & Constants.ST_FLAMING) != 0) {
                        HP -= random.nextInt(2);
                        if ((passiveList[spellBook].getName().equals("Fireproof"))) {
                            energico += (inputer.doublecast * 3);
                        }
                        if (random.nextInt(10) == 0) {
                            world.status &= ~Constants.ST_FLAMING;// Stop being on fire
                        }
                    }
                    if ((world.status & Constants.ST_SHOCKED) != 0) {
                        energico -= 25;
                        if (random.nextInt(10) == 0) {
                            world.status &= ~Constants.ST_SHOCKED;// Stop being on fire
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

                    // @TODO : be carefull of SRP && OCP
                    dig = world.getIncrementedDig(dig, Spell.getSpell(4), this);
                    
                    if (energico < maxeng) {
                        energico += engrecharge;
                    } else {
                        energico = maxeng;
                    }
                    

                    if (!world.isSolid(world.x + (int) xspeed, world.y)) {
                        world.x += xspeed;
                    }

                    if (world.vspeed >= 0) {
                        if ("Earth Stance".equals(passiveList[spellBook].getName()) && world.vspeed < 0) {
                            world.vspeed *= knockbackDecay;
                        }
                        xspeed *= .75 * knockbackDecay;
                        
                    }
                }

                if (Math.abs(xspeed) < .001 && xspeed != 0) {
                    xspeed = 0;
                    sendMovement();
                }

                for (final Player p : world.playerList) {
                    if ((p.status & Constants.ST_DRAIN) != 0) {
                        if (Math.abs(p.x - world.x) < Constants.AURA_RADIUS / 2) {
                            if (Math.abs(p.y - world.y) < Constants.AURA_RADIUS / 2) {
                                lastHit = p.ID;
                                killMessage = "~'s soul was corrupted by `'s Aura of Darkness.";
                                HP--;// Lose health from aura
                            }
                        }
                    }
                }
                for (final Entity e : world.entityList) {
                    e.checkAndHandleCollision(this);
                }
                world.determineInc();
                if (world.keys[KeyEvent.VK_CONTROL] && !world.dead) {
                    final double direction = Client.pointDir(150, 150, world.mouseX, world.mouseY);
                    final double distance = Client.pointDis(150, 150, world.mouseX, world.mouseY) / 8;
                    world.incX += Client.lengthdir_x(distance, direction);
                    world.incY -= Client.lengthdir_y(distance, direction);
                    world.cameraMoved = true;
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
                    Spell.randomSpell.setSpells();
                    if (killingSpree >= 148.413d) {
                        // Anti-cheating - use logs
                        gameService.feedRss(String.format("%s had a streak going", username),
                                String.format("%o kills in a row!", (int) Math.log(killingSpree)));
                    }
                    killingSpree = 0;
                    world.dead = true;
                    // this.chatActive = false;
                    try {
                        out.addMessage(DeathEvent.getPacket(lastHit, ID));
                    } catch (final IOException ex) {
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
                    } catch (final Exception ex) {
                        // Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                

                
                // prevMove = world.move;
                world.onUpdate();

                if (((((Math.signum(prevVspeed) != Math.signum(world.vspeed)) || ((prevMove) != (world.move)))
                        || counting++ > Constants.NETWORK_UPDATE_POSITION_RATE))) {
                    counting = 0;
                    try {
                        sendMovement();
                        prevMove = world.move;
                    } catch (final Exception ex) {
                        Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                if (sendRequest && sendcount++ >= Constants.NETWORK_UPDATE_MAP_RATE) {
                    sendcount = 0;
                    try {
                        out.addMessage(MapEvent.getPacketClient());
                    } catch (final Exception ex) {
                        Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    sendRequest = false;
                }

                prevVspeed = world.vspeed;
                // if (busy) continue;
                Xp = (int) world.x;
                Yp = (int) world.y;

                if (world.keys[KeyEvent.VK_SPACE]) {
                }

            }
            draw();
            if ((now - swagTime) >= (1000000000 / Constants.FPS / 2)) {
                repaint();
                swagTime = now;
            }
            World.setTime();
        }
    }

    private String getKiller(final int i) {
        for (final Player p : world.playerList) {
            if (p.ID == i) {
                return p.username;
            }
        }
        return "No One";
    }

    public double knockbackDecay = 1;

    public String addHost() {
        try {
            final URL whatismyip = new URL("https://checkip.amazonaws.com/");
            final BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));

            String ip = in.readLine(); // you get the IP as a String
            // System.out.println(ip);
            final int an = JOptionPane.showConfirmDialog(connect, "Host the server through the internet?",
                    "Server Type?", JOptionPane.YES_NO_OPTION);
            if (an != 0) {
                ip = InetAddress.getLocalHost().getHostAddress();
            }
            final String yes = JOptionPane.showInputDialog("Server Name?");
            if (yes != null) {
                String serverName = yes.replaceAll("[^A-Za-z0-9\\s]", "").replaceAll(" ", "");
                gameService.tryToCreateServer(serverName, ip);
                return ip;
            } else {
                gameService.tryToCreateServer(GameService.DEFAULT_SERVER_NAME, ip);
                // ip = InetAddress.getLocalHost().getHostAddress();
            }
            return "NO";
        } catch (IOException | HeadlessException ex) {
            // Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            return "localhost";
        }
    }

    public String names[];// = new String[yay.size()/3];
    public String counts[];// = new String[yay.size()/3];
    public Thread pinger;

    public void getHosts() {
        ArrayList<String> yay = null;
        while (yay == null) {
            // System.err.println("LOOKING");
            yay = gameService.retrieveServers();
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
        final Row[] newRows = new Row[names.length];
        for (int i = 0; i < newRows.length; i++) {
            newRows[i] = new Row(names[i], "PINGING", counts[i]);
        }
        menu.setModel(new JComboBox<Object>(newRows).getModel());
        // menu.setModel(new JComboBox<>(names).getModel());
        if (pinger != null) {
            pinger.interrupt();
        }
    }

    @Override
    public void update(final Graphics GameGraphics) {
        if (doubleBuffer == null) {
            doubleBuffer = createImage(Constants.WIDTH_EXT, Constants.HEIGHT_EXT);
        }
        final Graphics DoubleBufferGraphics = doubleBuffer.getGraphics();
        DoubleBufferGraphics.setColor(this.getBackground());
        DoubleBufferGraphics.fillRect(0, 0, Constants.WIDTH_EXT, Constants.HEIGHT_EXT);
        DoubleBufferGraphics.setColor(getForeground());
        paint(DoubleBufferGraphics);
        // paint(GameGraphics);
        GameGraphics.drawImage(doubleBuffer, 0, 0, getWidth(), getHeight(), this);
    }

    @Override
    public void paint(final Graphics g) {
        if (notDone) {
            super.paint(g);
            return;
        }
        g.drawImage(bigscreenBuffer, 0, 0, getWidth(), getHeight(), null);
    }

    public void draw() {
        if (notDone) {
            return;
        }
        if (screenBuffer == null) {
            screenBuffer = new BufferedImage(Constants.WIDTH_INT, Constants.HEIGHT_INT, BufferedImage.TYPE_INT_ARGB);
            graphicsBuffer = screenBuffer.createGraphics();
        }
        if (bigscreenBuffer == null) {
            bigscreenBuffer = new BufferedImage(Constants.WIDTH_EXT, Constants.HEIGHT_EXT, BufferedImage.TYPE_INT_ARGB);
            biggraphicsBuffer = bigscreenBuffer.createGraphics();
            biggraphicsBuffer.setFont(chatFont);
        }
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
                    Spell.randomSpellMatch.setSpells();
                }
                biggraphicsBuffer.drawImage(bimage, 0, 0, bigscreenBuffer.getWidth(), bigscreenBuffer.getHeight(),
                        null);
                biggraphicsBuffer.setColor(Color.white);
                biggraphicsBuffer.fillRect(399, 199, 100, 50);
                biggraphicsBuffer.setColor(Color.black);
                biggraphicsBuffer.drawRect(400, 200, 100, 50);
                biggraphicsBuffer.drawRect(399, 199, 100, 50);
                biggraphicsBuffer.drawString("LOADOUTS", 400, 230);
                biggraphicsBuffer.drawString("Starting match in " + (1 + (matchOver / 40)) + "...", 360, 300);
                biggraphicsBuffer.drawString("Combatants:", 400, 326);
                for (int i = 0; i < world.playerList.size(); i++) {
                    final Player p = world.playerList.get(i);
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
                graphicsBuffer.fillRect(1, 1, 2,
                        (int) ((double) Constants.HEIGHT_INT * ((double) dpyeng / (double) maxeng)));
                graphicsBuffer.setColor(Color.red);
                graphicsBuffer.drawRect(1, 1, 2,
                        (int) ((double) Constants.HEIGHT_INT * ((double) dpyeng / (double) maxeng)));
                for (int i = 0; i < 5; i++) {
                    graphicsBuffer.drawImage(spellList[spellBook][i].getEffectiveSpell(i).getImage().getImage(), 4 + i * 34, 0, 32, 16,
                            this);
                    if (this.leftClick == i) {
                        graphicsBuffer.setColor(Color.orange);
                        graphicsBuffer.drawRect(4 + i * 34, 0, 32, 16);
                        graphicsBuffer.drawString("L", 4 + i * 34, 10);
                    } else if (this.rightClick == i) {
                        graphicsBuffer.setColor(Color.red);
                        graphicsBuffer.drawRect(4 + i * 34, 0, 32, 16);
                        graphicsBuffer.drawString("R", 4 + i * 34, 10);
                    } else if (this.midClick == i) {
                        graphicsBuffer.setColor(Color.YELLOW);
                        graphicsBuffer.drawRect(4 + i * 34, 0, 32, 16);
                        graphicsBuffer.drawString("M", 4 + i * 34, 10);
                    } else if (inputer.setTo == i) {
                        graphicsBuffer.setColor(Color.GREEN);
                        graphicsBuffer.drawRect(4 + i * 34, 0, 32, 16);
                    } else {
                        graphicsBuffer.setColor(Color.white);
                        graphicsBuffer.drawRect(4 + i * 34, 0, 32, 16);
                    }
                    if (!spellList[spellBook][i].isEnergyEfficient(this, i)) {
                        graphicsBuffer.setColor(Color.MAGENTA);
                        graphicsBuffer.drawRect(5 + i * 34, 1, 30, 14);
                    }
                    if (!spellList[spellBook][i].isCooledDown(this, i)) {
                        graphicsBuffer.setColor(Color.CYAN);
                        graphicsBuffer.drawRect(6 + i * 34, 2, 28, 12);
                    }
                }
                graphicsBuffer.setColor(Color.BLUE);
                graphicsBuffer.fillRect(1, 1, 2,
                        (int) ((double) Constants.HEIGHT_INT * ((double) energico / (double) maxeng)));

                graphicsBuffer.setColor(purple);
                graphicsBuffer.drawRect(1, 1, 2,
                        (int) ((double) Constants.HEIGHT_INT * ((double) energico / (double) maxeng)));
                if (world.keys[KeyEvent.VK_ALT]) {
                    graphicsBuffer.setColor(Color.red);
                    this.checkCollision(0, 0);
                    final AffineTransform prevTrans = graphicsBuffer.getTransform();
                    graphicsBuffer.translate(-world.viewX, -world.viewY);
                    graphicsBuffer.draw(playerHitbox);
                    graphicsBuffer.setTransform(prevTrans);

                }
                // graphicsBuffer.drawImage(this.sightSeeing, 0, 0, null);
                biggraphicsBuffer.drawImage(screenBuffer, 0, 0, bigscreenBuffer.getWidth(), bigscreenBuffer.getHeight(),
                        this);
                world.drawPlayers(biggraphicsBuffer);

                for (final Entity e : world.entityList) {
                    e.drawOverlay(biggraphicsBuffer, world.viewX, world.viewY);
                }
                // The below lines are commented out until we get a faster way to do this
                // Composite c = biggraphicsBuffer.getComposite();
                // biggraphicsBuffer.setComposite(Additive.additive);
                for (final Entity e : world.entityList) {
                    e.drawAdditive(biggraphicsBuffer, world.viewX, world.viewY);
                }
                // biggraphicsBuffer.setComposite(c);
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
                        final Player p = world.playerList.get(i);
                        biggraphicsBuffer.setColor(gameMode > 0 ? (p.myTeam ? Color.GREEN : Color.red) : Color.red);
                        biggraphicsBuffer.drawString(p.username, 256, 256 + 16 + (i * 16));
                        biggraphicsBuffer.drawString("" + p.score, 512, 256 + 16 + (i * 16));
                    }
                }
                /*
                 * if (world.keys[KeyEvent.VK_ALT]) { biggraphicsBuffer.setColor(Color.red);
                 * this.checkCollision(0, 0); AffineTransform prevTrans =
                 * biggraphicsBuffer.getTransform(); biggraphicsBuffer.scale(3, 3);
                 * biggraphicsBuffer.translate(-world.viewX, -world.viewY);
                 * biggraphicsBuffer.draw(playerHitbox);
                 * biggraphicsBuffer.setTransform(prevTrans);
                 * 
                 * }
                 */
                if (world.dead) {
                    biggraphicsBuffer.setColor(deadbg);
                    biggraphicsBuffer.fillRect(0, 0, Constants.WIDTH_EXT, Constants.HEIGHT_EXT);
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
                        biggraphicsBuffer.drawString("Forced respawn in " + (1 + ((400 - forcedRespawn) / 40)) + (!isMyTurn?" seconds after your turn starts":" seconds..."),
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
                            biggraphicsBuffer.drawString(spellList[yyy][xxx].getEffectiveSpell(xxx).getName(), 128 + (xxx * 128),
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
                    if (isMyTurn) {
                        forcedRespawn++;
                    }
                    if ((!(gameMode == Server.THEHIDDEN && !goodTeam)) || lastHit == ID) {
                        if (world.keys[KeyEvent.VK_SPACE] || forcedRespawn > 400) {
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
    }

    public Font chatFont = new Font("Arial", Font.BOLD, 18);
    public Font nameFont = new Font("Arial", Font.BOLD, 12);

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

    public static double lengthdir_x(final double R, final double T) {
        return (R * (Math.cos(T * Math.PI / 180)));
    }

    public static double lengthdir_y(final double R, final double T) {
        return (-R * (Math.sin(T * Math.PI / 180)));
    }

    public static double pointDir(final double x1, final double y1, final double x2, final double y2) {
        return Math.toDegrees(Math.atan2(y2 - y1, x2 - x1));
    }

    public static double pointDis(final double x1, final double y1, final double x2, final double y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
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
                    world.rightArmAngle, world.status, HP, world.ID));

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

    public int shockdrain = 0;

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

    public static boolean portAvailable(final int port) {
        if (port < 0 || port > 65535) {
            throw new IllegalArgumentException("Invalid start port: " + port);
        }

        ServerSocket ss = null;
        try {
            ss = new ServerSocket(port);
            ss.setReuseAddress(true);
            return true;
        } catch (final IOException e) {
        } finally {

            if (ss != null) {
                try {
                    ss.close();
                } catch (final IOException e) {
                    /* should not be thrown */
                }
            }
        }

        return false;
    }

    public String hostIP = "";
    public ServerGUI sgui;

    public void serverOutput() {
        // sgui = new ServerGUI();
        // sgui.setVisible(true);
    }

    static class ImagePanel extends JPanel {
        private static final long serialVersionUID = 7707876428305555174L;
        private final Image image;

        public ImagePanel(final Image image) {
            this.image = image;

        }

        @Override
        protected void paintComponent(final Graphics g) {
            // super.paintComponent(g);

            g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
        }
    }

    @Override
    protected void paintComponent(final Graphics g) {
        // super.paintComponent(g);

        g.drawImage(bimage, 0, 0, getWidth(), getHeight(), null);
    }

    private static class Row {

        private String id = "", val = "", extra = "";

        public Row(final String id, final String val, final String extra) {
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

    private static class RowCellRenderer extends JTable implements ListCellRenderer<Object> {
        private static final long serialVersionUID = -4593849054661400146L;

        public RowCellRenderer() {
            setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        }

        @Override
        public Component getListCellRendererComponent(final JList<? extends Object> list, final Object value,
                final int index, final boolean isSelected, final boolean cellHasFocus) {
            setModel(new RowTableModel((Row) value));
            this.getColumnModel().getColumn(0).setWidth(100);
            if (isSelected) {
                getSelectionModel().setSelectionInterval(0, 0);
            }
            return this;
        }
    }

    private static class RowTableModel extends AbstractTableModel {
        private static final long serialVersionUID = -1141890084123415074L;
        private final Row row;

        public RowTableModel(final Row row) {
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
        public Object getValueAt(final int rowIndex, final int columnIndex) {
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

    public RealClip airCast = ResourceLoader.loadSound("aircast.wav");
    public RealClip fireCast = ResourceLoader.loadSound("firecast.wav");

    public void drawChat() {
        for (int i = 0; i < chat.length; i++) {
            biggraphicsBuffer.setColor(backgroundChat);
            biggraphicsBuffer.fillRect(32, 826 - (16 * ((chat.length - i) + 1)),
                    biggraphicsBuffer.getFontMetrics().stringWidth(chat[i]), 16);
            biggraphicsBuffer.setColor(chatcolor[i]);
            biggraphicsBuffer.drawString(chat[i], 32, 840 - (16 * ((chat.length - i) + 1)));
        }
    }

    public static long authCode = -1;

    static public long getAuth() {
        if (authCode == -1) {
            try {
                final long s1111I11I11 = Client.class.getFields().length;
                final long s1I1111II11 = Server.class.getFields().length;
                final long s11I1111I1I = World.class.getFields().length;
                final long sI1I1I11I1I = PlayerOnline.class.getFields().length;
                authCode = (((((sI1I1I11I1I * sI1I1I11I1I) - sI1I1I11I1I) / s1I1111II11) + s1111I11I11) * s11I1111I1I)
                        / s1I1111II11;
                // authCode = 1;
            } catch (final Exception ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return authCode;
    }

}
