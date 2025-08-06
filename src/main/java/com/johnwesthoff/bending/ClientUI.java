package com.johnwesthoff.bending;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.MenuItem;
import java.awt.Polygon;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.table.AbstractTableModel;

import com.johnwesthoff.bending.app.game.GameServiceFactory;
import com.johnwesthoff.bending.entity.Entity;
import com.johnwesthoff.bending.logic.ClientInputListener;
import com.johnwesthoff.bending.logic.Player;
import com.johnwesthoff.bending.logic.PlayerOnline;
import com.johnwesthoff.bending.logic.World;
import com.johnwesthoff.bending.spells.Spell;
import com.johnwesthoff.bending.ui.AppletActionListener;
import com.johnwesthoff.bending.ui.BadVersion;
import com.johnwesthoff.bending.ui.ClothingChooser1;
import com.johnwesthoff.bending.ui.Register;
import com.johnwesthoff.bending.ui.ServerGUI;
import com.johnwesthoff.bending.ui.SpellList1;
import com.johnwesthoff.bending.ui.Verify;
import com.johnwesthoff.bending.util.graphics.Renderer;
import com.johnwesthoff.bending.util.graphics.TextureAtlas;
import com.johnwesthoff.bending.util.network.ResourceLoader;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import javax.swing.JPanel;
import java.io.IOException;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.system.MemoryUtil.NULL;

public class ClientUI extends JPanel implements Runnable {
    private static final long serialVersionUID = 1L;
    public transient TextureAtlas textureAtlas;
    private final Renderer renderer = new Renderer();
    private long window;
    public ClothingChooser1 cc = new ClothingChooser1(this);
    public Color chatcolor[] = new Color[] { Color.PINK, Color.PINK, Color.PINK, Color.PINK, Color.PINK, Color.PINK,
            Color.PINK, Color.PINK, Color.PINK, Color.PINK, Color.PINK };
    public JButton connect, hosting, refresh, register, verify, ChooseSpells, chooseclothing, mapMaker;
    public JCheckBox JRB;
    public JComboBox<Object> menu;
    public JFrame owner;
    public JLabel jUs = new JLabel("Username:"), jPa = new JLabel("Password:");
    public JPasswordField jtp = new JPasswordField();
    public static BufferedImage bimage;
    public static double runningSpeed = 1d, swimmingSpeed = 1d;
    public static JFrame container;
    public static JTabbedPane immaKeepTabsOnYou;
    public static JTextField jtb = new JTextField();
    public static AppletActionListener actioner;
    public SystemTray ST;
    public TrayIcon trayIcon;
    public Register form = new Register();
    public Verify exactly = new Verify();
    public SpellList1 spellselection;
    public ClientInputListener inputer;
    public static byte[] Clothing = new byte[] { 1, 1, 1, 1, 1, 1 };
    public static int[] Colors = new int[] { Color.red.getRGB(), Color.orange.getRGB(), Color.red.getRGB(),
            Color.orange.getRGB(), Color.black.getRGB(), Color.orange.getRGB() };
    public static int[] Colors2 = new int[] { Color.red.getRGB(), Color.orange.getRGB(), Color.red.getRGB(),
            Color.orange.getRGB(), Color.black.getRGB(), Color.orange.getRGB() };
    public Color Clear = new Color(0, 0, 0, 0);

    public ClientUI() {
        super();
        new File(ResourceLoader.dir).mkdirs();
        new File(ResourceLoader.dir + "images").mkdirs();
        new File(ResourceLoader.dir + "sounds").mkdirs();
        try {
            bimage = ResourceLoader.loadImage("AgedPaper.png");
            textureAtlas = new TextureAtlas();
            // Thread.sleep(100);
        } catch (final Exception ex) {
            // ex.printStackTrace();
        }
    }

    // RadialGradientPaint cantSee = new RadialGradientPaint(150f,150f,150f,new
    // float[]{0f,1f},new Color[]{new Color(0,0,0,0),new Color(0,0,0,255)});
    Polygon lineOfSight = new Polygon();
    public BadVersion bv;

    public static void launch(String name) {
        Session sess = Session.getInstance();
        System.out.println("Loading 1.2");
        Spell.init();
        final Client client = new Client();
        final ClientUI clientui = new ClientUI();
        sess.gameAlive = true;
        clientui.cc = new ClothingChooser1(clientui);
        sess.clientui = clientui;
        sess.client = client;
        sess.net = new ClientNetworking();
        immaKeepTabsOnYou = new JTabbedPane();
        actioner = new AppletActionListener(clientui);
        clientui.inputer = new ClientInputListener(sess);

        // using factory to inject dependency
        sess.gameService = GameServiceFactory.create();
        // @TODO : define here player instance

        clientui.setSize(960, 540);
        clientui.setPreferredSize(clientui.getSize());

        // JPanel e = new JPanel();
        // e.setSize(300,300);
        container = new JFrame();
        clientui.owner = container;
        // container.setUndecorated(true);
        clientui.form.setVisible(false);
        container.setSize(clientui.getSize());
        container.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        clientui.cc.setVisible(false);
        sess.spellList = new Spell[10][5];// {{Spell.spells.get(0),Spell.spells.get(1),Spell.spells.get(2),Spell.spells.get(3),Spell.spells.get(5)},{Spell.spells.get(0),Spell.spells.get(1),Spell.spells.get(2),Spell.spells.get(3),Spell.spells.get(5)},{Spell.spells.get(0),Spell.spells.get(1),Spell.spells.get(2),Spell.spells.get(3),Spell.spells.get(5)},{Spell.spells.get(0),Spell.spells.get(1),Spell.spells.get(2),Spell.spells.get(3),Spell.spells.get(5)},{Spell.spells.get(0),Spell.spells.get(1),Spell.spells.get(2),Spell.spells.get(3),Spell.spells.get(5)}});
        sess.spellList[0] = (new Spell[] { Spell.lookup("Airbending"), Spell.lookup("Earthbending"),
                Spell.lookup("EarthbendingShard"), Spell.lookup("WaterbendingShard"), Spell.lookup("Firebending") });
        sess.spellList[1] = (new Spell[] { Spell.lookup("AirbendingJump"), Spell.lookup("Firebending"),
                Spell.lookup("AirbendingGust"), Spell.lookup("EarthbendingSpike"), Spell.lookup("EarthbendingShard") });
        sess.spellList[2] = (new Spell[] { Spell.lookup("AirbendingJump"), Spell.lookup("Waterbending"),
                Spell.lookup("WaterbendingShard"), Spell.lookup("Firebending"), Spell.lookup("AirbendingAir") });
        sess.spellList[3] = (new Spell[] { Spell.lookup("SpellRandom"), Spell.lookup("SpellRandom"),
                Spell.lookup("SpellRandom"), Spell.lookup("SpellRandom"), Spell.lookup("SpellRandom") });
        sess.spellList[4] = (new Spell[] { Spell.lookup("SpellRandomMatch"), Spell.lookup("SpellRandomMatch"),
                Spell.lookup("SpellRandomMatch"), Spell.lookup("SpellRandomMatch"), Spell.lookup("SpellRandomMatch") });
        sess.spellList[5] = (new Spell[] { Spell.lookup("AirbendingGust"), Spell.lookup("WaterbendingFreeze"),
                Spell.lookup("Firebending_Thrower"), Spell.lookup("Firebending_Wall"),
                Spell.lookup("EarthbendingShard") });
        Spell shield = Spell.lookup("EarthbendingShield");
        sess.passiveList = (new Spell[] { shield, shield, shield, shield, shield, shield });
        // container.add(me);
        clientui.JRB = new JCheckBox();

        sess.userpassinfo = new Properties();
        if (new File(ResourceLoader.dir + "properties.xyz").exists()) {
            try {
                sess.userpassinfo.load(new FileInputStream(new File(ResourceLoader.dir + "properties.xyz")));
            } catch (final Exception ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (sess.userpassinfo.isEmpty()) {
            sess.userpassinfo.setProperty("username", name);
            sess.userpassinfo.setProperty("password", "");
            sess.userpassinfo.setProperty("remember", "");
            try {
                sess.userpassinfo.store(new FileOutputStream(new File(ResourceLoader.dir + "properties.xyz")), "");
            } catch (final Exception ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            clientui.jtb.setText(sess.userpassinfo.getProperty("username", name));
            clientui.jtp.setText(sess.userpassinfo.getProperty("password", ""));
        }
        if (!name.isEmpty()) { 
            clientui.jtb.setText(name);
        }
        clientui.setBackground(Color.white);
        clientui.setLayout(null);
        clientui.menu = new JComboBox<Object>() {
            private static final long serialVersionUID = -5781421606071388365L;

            @Override
            public Dimension getSize() {
                final Dimension dim = super.getSize();
                dim.width = 256;
                return dim;
            }
        };
        clientui.menu.setRenderer(new RowCellRenderer());
        clientui.connect = new JButton("Connect");
        clientui.hosting = new JButton("Host");
        clientui.refresh = new JButton("Refresh");
        clientui.register = new JButton("Register");
        clientui.verify = new JButton("Log In");
        clientui.ChooseSpells = new JButton("Spellbooks");
        clientui.chooseclothing = new JButton("Appearance");
        clientui.mapMaker = new JButton("Map Editor");
        container.setMaximumSize(clientui.getSize());
        clientui.addMouseListener(clientui.inputer);
        clientui.addMouseMotionListener(clientui.inputer);
        clientui.addMouseWheelListener(clientui.inputer);
        container.addKeyListener(clientui.inputer);
        container.addMouseListener(clientui.inputer);
        container.setResizable(true);
        clientui.requestFocus();
        clientui.add(clientui.jUs);
        clientui.jUs.setLocation(16, 16);
        clientui.jUs.setSize(80, 16);
        clientui.add(clientui.jtb);
        clientui.jtb.setLocation(96, 16);
        clientui.add(clientui.menu);
        clientui.menu.setLocation(-24, -64);
        clientui.menu.setSize(0, 0);
        clientui.menu.setVisible(false);
        clientui.add(clientui.connect);
        clientui.connect.setLocation(80, 48);
        clientui.connect.setSize(140, 16);
        clientui.add(clientui.hosting);
        clientui.hosting.setLocation(80, 80 + 6 + 32);
        clientui.hosting.setSize(140, 16);
        clientui.add(clientui.ChooseSpells);
        clientui.ChooseSpells.setLocation(80, 80 + 6 + 32 + 32);
        clientui.ChooseSpells.setSize(140, 16);
        clientui.add(clientui.chooseclothing);
        clientui.chooseclothing.setLocation(80, 80 + 6 + 32 + 32 + 32);
        clientui.chooseclothing.setSize(140, 16);

        clientui.add(clientui.mapMaker);
        clientui.mapMaker.setLocation(80, 80 + 6 + 32 + 32 + 32 + 32);
        clientui.mapMaker.setSize(140, 16);
        /// me.JRB.setText("Remember me?");
        clientui.bv = new BadVersion();
        clientui.JRB.setActionCommand("RM");
        clientui.spellselection = new SpellList1(clientui);
        clientui.spellselection.setVisible(false);
        clientui.ChooseSpells.addActionListener(actioner);
        clientui.ChooseSpells.setActionCommand(clientui.ChooseSpells.getText());
        clientui.chooseclothing.addActionListener(actioner);
        clientui.chooseclothing.setActionCommand(clientui.chooseclothing.getText());
        clientui.mapMaker.addActionListener(actioner);
        clientui.mapMaker.setActionCommand(clientui.mapMaker.getText());
        clientui.jtb.setSize(300 - 128, 16);
        clientui.jtb.setPreferredSize(clientui.jtb.getSize());
        // me.jtb.setLocation(16, 16);dfsdfsdf
        clientui.jtb.setVisible(true);
        clientui.jtb.requestFocus();
        clientui.jtb.addKeyListener(clientui.inputer);
        clientui.jtp.setSize(300 - 128 - 16, 16);
        clientui.jtp.setPreferredSize(clientui.jtb.getSize());
        // me.jtp.setLocation(16, 16);
        clientui.jtp.setVisible(true);
        clientui.jtp.requestFocus();
        clientui.jtp.addKeyListener(clientui.inputer);
        clientui.connect.setActionCommand(clientui.connect.getText());
        clientui.connect.addActionListener(actioner);
        clientui.hosting.setActionCommand(clientui.hosting.getText());
        clientui.hosting.addActionListener(actioner);
        clientui.refresh.setActionCommand(clientui.refresh.getText());
        clientui.refresh.addActionListener(actioner);
        clientui.register.setActionCommand(clientui.register.getText());
        clientui.register.addActionListener(actioner);
        clientui.verify.setActionCommand(clientui.verify.getText());
        clientui.verify.addActionListener(actioner);
        clientui.setFocusable(true);
        clientui.addKeyListener(clientui.inputer);
        clientui.setVisible(true);
        UIManager.getDefaults().put("TabbedPane.contentBorderInsets", new Insets(0, 0, 0, 0));
        UIManager.getDefaults().put("TabbedPane.tabsOverlapBorder", true);
        immaKeepTabsOnYou.setUI(new BasicTabbedPaneUI() {
            @Override
            protected int calculateTabAreaHeight(final int tab_placement, final int run_count,
                    final int max_tab_height) {
                return 0;
            }
        });
        immaKeepTabsOnYou.addKeyListener(clientui.inputer);
        immaKeepTabsOnYou.addMouseListener(clientui.inputer);
        immaKeepTabsOnYou.addMouseMotionListener(clientui.inputer);
        immaKeepTabsOnYou.addTab("THEGAME", clientui);
        immaKeepTabsOnYou.addTab("SPELLSELECT", clientui.spellselection);
        immaKeepTabsOnYou.addTab("SPELLLIST", clientui.spellselection.choochootrain);
        immaKeepTabsOnYou.addTab("PASSIVELIST", clientui.spellselection.choochootrain2);
        immaKeepTabsOnYou.addTab("ClothingChooser", clientui.cc);
        immaKeepTabsOnYou.addTab("BADVERSION", clientui.bv);
        immaKeepTabsOnYou.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("LEFT"), "none");
        immaKeepTabsOnYou.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("RIGHT"), "none");
        immaKeepTabsOnYou.setLocation(-1, -1);
        immaKeepTabsOnYou.setBackground(Color.black);
        immaKeepTabsOnYou.setBorder(BorderFactory.createEmptyBorder());
        container.setContentPane(immaKeepTabsOnYou);
        clientui.repaint();
        sess.mainProcess = new Thread(clientui);
        sess.mainProcess.start();
        clientui.chooseclothing.setEnabled(false);
        clientui.ChooseSpells.setEnabled(false);
        clientui.connect.setEnabled(false);
        clientui.ST = SystemTray.getSystemTray();
        try {
            // container.getContentPane().setB

            final PopupMenu pop = new PopupMenu();
            clientui.trayIcon = new TrayIcon(ResourceLoader.loadImage("GrassTexture.png"));
            clientui.trayIcon.setToolTip("DestructibleTerrain");
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
            clientui.trayIcon.setPopupMenu(pop);
            clientui.trayIcon.setActionCommand("Tray");
            clientui.trayIcon.addActionListener(actioner);
            final Frame frame = new Frame("");
            frame.setUndecorated(true);
            frame.setResizable(false);
            frame.setVisible(false);
            clientui.trayIcon.addMouseListener(new MouseListener() {

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
            container.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        } catch (final Exception ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

        sess.XP = sess.gameService.getPlayerExperience(clientui.jtb.getText(), "PASS IGNORED");

        clientui.cc.loadClothing();
        clientui.spellselection.loadSpells();
        clientui.chooseclothing.setEnabled(true);
        clientui.ChooseSpells.setEnabled(true);
        clientui.connect.setEnabled(true);

        sess.gameService.getUnlocks(clientui.jtb.getText(), "I NOW DO NOTHING");
        container.setVisible(true);
    }

    // @Override
    public void init() {
        Session sess = Session.getInstance();
        try {
            sess.loggedOn = true;
            sess.temp = "C:";// (System.getenv("TEMP"));
        } catch (final Exception ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addChat(final String message, final Color color) {
        Session sess = Session.getInstance();
        for (int i = 0; i < sess.chat.length - 1; i++) {
            sess.chat[i] = sess.chat[i + 1];
            chatcolor[i] = chatcolor[i + 1];
        }
        sess.chat[sess.chat.length - 1] = message;
        chatcolor[sess.chat.length - 1] = color;
    }

    public void initGL() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        window = glfwCreateWindow(Constants.WIDTH_EXT, Constants.HEIGHT_EXT, "Bending", NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        glfwShowWindow(window);

        GL.createCapabilities();

        renderer.init(textureAtlas);
    }

    public void destroy() {
        renderer.destroy();
        glfwDestroyWindow(window);
        glfwTerminate();
        Session.getInstance().isAlive = false;
        Session.getInstance().gameAlive = false;
    }

    public boolean refreshShadows = false;
    public long swagTime = 0;

    @Override
    public void run() {
        Session sess = Session.getInstance();
        initGL();
        while (!glfwWindowShouldClose(window)) {
            sess.client.tick();
            renderer.draw(sess.world, textureAtlas);
            glfwSwapBuffers(window);
            glfwPollEvents();
        }
        destroy();
    }


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
            return this.getComponent(0);
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
