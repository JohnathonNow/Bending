/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ClothingChooser.java
 *
 * Created on Nov 24, 2012, 10:38:32 PM
 */
package com.johnwesthoff.bending.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JColorChooser;

import com.johnwesthoff.bending.Client;
import com.johnwesthoff.bending.Session;
import com.johnwesthoff.bending.app.avatar.AvatarService;
import com.johnwesthoff.bending.app.avatar.AvatarServiceFactory;
import com.johnwesthoff.bending.logic.World;
import com.johnwesthoff.bending.util.network.ResourceLoader;

/**
 *
 * @author Family
 */
public class ClothingChooser1 extends javax.swing.JPanel implements Runnable {

    private final AvatarService avatarService;

    private static final long serialVersionUID = 125466973567201631L;
    /** Creates new form ClothingChooser */
    Color[] colors = new Color[] { Color.white, Color.white, Color.white, Color.white, Color.white, Color.white };
    Color[] colors2 = new Color[] { Color.white, Color.white, Color.white, Color.white, Color.white, Color.white };
    byte[] cloths = new byte[] { 1, 1, 1, 1, 1, 1 };
    Image[] stuff = new Image[6];
    boolean done = false;
    Session app;
    Image background;

    public ClothingChooser1(Session session) {
        initComponents();
        Thread me = new Thread(this);
        me.start();
        this.app = session;
        avatarService = AvatarServiceFactory.create();
        // g = canvas.getGraphics();
        try {
            background = ResourceLoader.loadImage("Capture.PNG");
        } catch (Exception ex) {
            // ex.printStackTrace();
        }
    }

    @Override
    public void paintComponent(Graphics G) {
        G.drawImage(Client.bimage, 0, 0, getWidth(), getHeight(), null);
        G.drawImage(stuff[4], 9 + 100, 60 + 12 + 200, this);
        G.drawImage(stuff[4], 24 + 100, 60 + 12 + 200, this);

        G.drawImage(stuff[5], 24 + 100, 80 + 12 + 200, this);

        G.drawImage(stuff[5], 9 + 100, 80 + 12 + 200, this);

        G.drawImage(stuff[0], 6 + 100, 24 + 12 + 200, this);
        G.drawImage(stuff[1], 12 - ((stuff[1].getWidth(this) - 23) / 2) + 100,
                -(stuff[1].getHeight(this) - 31) + 12 + 200, this);

        G.drawImage(stuff[2], 0 + 100, 25 + 12 + 200, this);

        G.drawImage(stuff[3], 0 + 100, 45 + 12 + 200, this);

        G.drawImage(stuff[2], 30 + 100, 25 + 12 + 200, this);

        G.drawImage(stuff[3], 30 + 100, 45 + 12 + 200, this);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    int waffles = 4;
    int waffles2 = 5;

    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {
        buttonHead = new javax.swing.JButton();
        // canvas = new java.awt.Canvas();
        buttonHeadcolor = new javax.swing.JButton();
        buttonBodycolor = new javax.swing.JButton();
        buttonScolor = new javax.swing.JButton();
        buttonFcolor = new javax.swing.JButton();
        buttonTcolor = new javax.swing.JButton();
        buttonShincolor = new javax.swing.JButton();
        buttonHeadcolor2 = new javax.swing.JButton();
        buttonBodycolor2 = new javax.swing.JButton();
        buttonScolor2 = new javax.swing.JButton();
        buttonFcolor2 = new javax.swing.JButton();
        buttonTcolor2 = new javax.swing.JButton();
        buttonShincolor2 = new javax.swing.JButton();
        buttonBody = new javax.swing.JButton();
        buttonShoulder = new javax.swing.JButton();
        ForeArm = new javax.swing.JButton();
        ThighButton = new javax.swing.JButton();
        buttonShin = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();

        // jPanel1.setBackground(new java.awt.Color(0, 255, 204));
        // jPanel1.setForeground(new java.awt.Color(51, 255, 255));

        buttonHead.setText("Head");
        buttonHead.addActionListener(event -> {
            cloths[1]++;
            if (cloths[1] == 4 && !Client.unlocks.get(0, 1)) {
                cloths[1]++;
            }
            if (cloths[1] > waffles2) {
                cloths[1] = 1;
            }
            getImages();
        });        

        buttonHeadcolor.setText("Head Color");
        buttonHeadcolor.addActionListener(event -> {
            colors[1] = JColorChooser.showDialog(this, "Choose Head Color", colors[1]);
            if (colors[1] == null)
                colors[1] = Color.BLACK;
            getImages();
        });

        buttonBodycolor.setText("Body Color");
        buttonBodycolor.addActionListener(event -> {
            colors[0] = JColorChooser.showDialog(this, "Choose Body Color", colors[0]);
            if (colors[0] == null)
                colors[0] = Color.BLACK;
            getImages();
        });

        buttonScolor.setText("Shoulder Color");
        buttonScolor.addActionListener(event -> {
            colors[2] = JColorChooser.showDialog(this, "Choose Shoulder Color", colors[2]);
            if (colors[2] == null)
                colors[2] = Color.BLACK;
            getImages();
        });

        buttonFcolor.setText("Fore Arm Color");
        buttonFcolor.addActionListener(event -> {
            colors[3] = JColorChooser.showDialog(this, "Choose Arm Color", colors[3]);
            if (colors[3] == null)
                colors[3] = Color.BLACK;
            getImages();
        });

        buttonTcolor.setText("Thigh Color");
        buttonTcolor.addActionListener(event -> {
            colors[4] = JColorChooser.showDialog(this, "Choose Leg Color", colors[4]);
            if (colors[4] == null)
                colors[4] = Color.BLACK;
            getImages();
        });

        buttonShincolor.setText("Shin Color");
        buttonShincolor.addActionListener(event -> {
            colors[5] = JColorChooser.showDialog(this, "Choose Foot Color", colors[5]);
            if (colors[5] == null)
                colors[5] = Color.BLACK;
            getImages();
        });

        buttonHeadcolor2.setText("Head Color 2");
        buttonHeadcolor2.addActionListener(event -> COLOR(1, event));

        buttonBodycolor2.setText("Body Color 2");
        buttonBodycolor2.addActionListener(event -> COLOR(0, event));

        buttonScolor2.setText("Shoulder Color 2");
        buttonScolor2.addActionListener(event -> COLOR(2, event));

        buttonFcolor2.setText("Fore Arm Color 2");
        buttonFcolor2.addActionListener(event -> COLOR(3, event));

        buttonTcolor2.setText("Thigh Color 2");
        buttonTcolor2.addActionListener(event -> COLOR(4, event));

        buttonShincolor2.setText("Shin Color 2");
        buttonShincolor2.addActionListener(event -> COLOR(5, event));

        buttonBody.setText("Body");
        buttonBody.addActionListener(event -> {
            cloths[0]++;
            if (cloths[0] > waffles) {
                cloths[0] = 1;
            }
            getImages();
        });

        buttonShoulder.setText("Shoulder");
        buttonShoulder.addActionListener(event -> {
            cloths[2]++;
            if (cloths[2] > waffles) {
                cloths[2] = 1;
            }
            getImages();
        });

        ForeArm.setText("ForeArm");
        ForeArm.addActionListener(event -> {
            cloths[3]++;
            if (cloths[3] > waffles) {
                cloths[3] = 1;
            }
            getImages();
        });

        ThighButton.setText("Thigh");
        ThighButton.addActionListener(event -> {
            cloths[4]++;
            if (cloths[4] > waffles) {
                cloths[4] = 1;
            }
            getImages();
        });

        buttonShin.setText("Shin");
        buttonShin.addActionListener(event -> {
            cloths[5]++;
            if (cloths[5] > waffles) {
                cloths[5] = 1;
            }
            getImages();
        });

        jButton1.setText("Save");
        jButton1.addActionListener(event -> {
            if (Client.currentlyLoggedIn) {
                String post = "";
                for (int i = 0; i < cloths.length; i++) {
                    post += cloths[i];
                    post += ",";
                }
                for (int i = 0; i < colors.length; i++) {
                    post += colors[i].getRGB();
                    post += ",";
                }
                for (int i = 0; i < colors2.length; i++) {
                    post += colors2[i].getRGB();
                    post += i == colors2.length - 1 ? "" : ",";
                }
                // System.out.println(post);
                avatarService.changesAppearance(post, Client.jtb.getText());
            }
        });

        jButton2.setText("Load");
        jButton2.addActionListener(event -> {
            if (Client.currentlyLoggedIn) {
                loadClothing();
            }
        });

        jButton3.setText("Finish");
        jButton3.addActionListener(event -> {
            Client.Clothing = cloths;
            for (int i = 0; i < colors.length; i++) {
                Client.Colors[i] = colors[i].getRGB();
                Client.Colors2[i] = colors2[i].getRGB();
            }
            Client.immaKeepTabsOnYou.setSelectedIndex(0);
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(this);
        setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout
                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup().addGap(160, 160, 160)
                                // .addComponent(canvas, javax.swing.GroupLayout.PREFERRED_SIZE, 160,
                                // javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(65, 65, 65)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(buttonHead).addComponent(buttonBody).addComponent(buttonShoulder)
                                        .addComponent(ForeArm).addComponent(ThighButton).addComponent(buttonShin))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(buttonHeadcolor).addComponent(buttonBodycolor)
                                        .addComponent(buttonScolor).addComponent(buttonFcolor)
                                        .addComponent(buttonTcolor).addComponent(buttonShincolor))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(buttonHeadcolor2).addComponent(buttonBodycolor2)
                                        .addComponent(buttonScolor2).addComponent(buttonFcolor2)
                                        .addComponent(buttonTcolor2).addComponent(buttonShincolor2)))
                        .addGroup(jPanel1Layout.createSequentialGroup().addGap(103, 103, 103).addComponent(jButton1)
                                .addGap(32, 32, 32).addComponent(jButton3).addGap(18, 18, 18).addComponent(jButton2))
                        .addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(jLabel1)))
                        .addContainerGap(17, Short.MAX_VALUE)));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout
                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup().addGap(28, 28, 28).addComponent(jLabel1)
                                .addGap(36, 36, 36)
                                .addGroup(jPanel1Layout.createSequentialGroup().addGap(93, 96, 96)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel1Layout
                                                .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(buttonHead).addComponent(buttonHeadcolor2))
                                        .addComponent(buttonHeadcolor))

                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(buttonBodycolor).addComponent(buttonBodycolor2)
                                        .addComponent(buttonBody))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(buttonScolor).addComponent(buttonScolor2)
                                        .addComponent(buttonShoulder))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(buttonFcolor).addComponent(buttonFcolor2).addComponent(ForeArm))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(buttonTcolor).addComponent(buttonTcolor2)
                                        .addComponent(ThighButton))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(buttonShincolor).addComponent(buttonShincolor2)
                                        .addComponent(buttonShin))))
                        // .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 71,
                        // Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jButton1).addComponent(jButton2).addComponent(jButton3))
                        .addGap(38, 38, 38)));

        // javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        // setLayout(layout);
        // layout.setHorizontalGroup(
        // layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        // .addGroup(layout.createSequentialGroup()
        // .addContainerGap()
        // .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE,
        // javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        // );
        // layout.setVerticalGroup(
        // layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        // .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE,
        // javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        // );

    }// </editor-fold>

    private void COLOR(int i, java.awt.event.ActionEvent evt) {

        colors2[i] = JColorChooser.showDialog(this, "Choose Secondary Color", colors[i]);
        if (colors2[i] == null)
            colors2[i] = Color.BLACK;
        getImages();
    }

    /**
     * Loads the clothing of the client
     */
    public void loadClothing() {
        avatarService.getAppearance(Client.jtb.getText(), app.jtp.getText());

        for (int i = 0; i < colors.length; i++) {
            colors[i] = new Color(Client.Colors[i]);
            colors2[i] = new Color(Client.Colors2[i]);
        }
        cloths = Client.Clothing;
        getImages();
    }

    /**
     * @param args the command line arguments
     */
    Thread getTem;

    /**
     * Loads the images
     */
    public void getImages() {
        Runnable getStuff =
            /* Originally declared as a new Runnable instance with a single method; To resolve sonic-lint warning java:S1604
               converted into a single lambda function without any change to the method body. */
            (() -> {
                try {
                    done = false;
                    for (int i = 0; i < cloths.length; i++) {
                        // System.out.println("YES");
                        stuff[i] = ResourceLoader.loadImageNoHash(
                                "p" + (i + 1) + "_" + cloths[i] + ".png",
                                "p" + (i + 1) + "_" + cloths[i] + ".png");
                        stuff[i] = World.changeColor(
                                World.changeColor(World.changeColor((BufferedImage) stuff[i], Color.white, colors[i]),
                                        Color.LIGHT_GRAY, colors2[i]),
                                new Color(0xBEBEBE), colors2[i].darker());
                    }
                    done = true;
                    painting();
                } catch (Exception ex) {

                    ex.printStackTrace();
                }
            });
        if (getTem != null) {
            getTem.interrupt();
        }
        getTem = new Thread(getStuff);
        getTem.start();
    }

    // Variables declaration - do not modify
    private javax.swing.JButton ForeArm;
    private javax.swing.JButton ThighButton;
    private javax.swing.JButton buttonBody;
    private javax.swing.JButton buttonBodycolor;
    private javax.swing.JButton buttonFcolor;
    private javax.swing.JButton buttonHead;
    private javax.swing.JButton buttonHeadcolor;
    private javax.swing.JButton buttonScolor;
    private javax.swing.JButton buttonShin;
    private javax.swing.JButton buttonShincolor;
    private javax.swing.JButton buttonShoulder;
    private javax.swing.JButton buttonTcolor;
    private javax.swing.JButton buttonBodycolor2;
    private javax.swing.JButton buttonFcolor2;
    private javax.swing.JButton buttonHeadcolor2;
    private javax.swing.JButton buttonScolor2;
    private javax.swing.JButton buttonShincolor2;
    private javax.swing.JButton buttonTcolor2;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration
    Graphics g;

    public void painting() {
        // G.drawImage(background,0,0,null);
        repaint();

    }

    @Override
    public void run() {
        while (true) {
            while ((!this.isVisible()) || !done) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ex) {
                    // ex.printStackTrace();
                }
            }
            painting();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                // ex.printStackTrace();
            }
        }
    }
}
