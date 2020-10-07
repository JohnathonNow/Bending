/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johnwesthoff.bending.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.johnwesthoff.bending.Constants;
import com.johnwesthoff.bending.logic.World;
import com.johnwesthoff.bending.util.network.ResourceLoader;

public class MapMaker extends javax.swing.JFrame implements Runnable, MouseMotionListener, ComponentListener {
    private static final long serialVersionUID = 3296372568977541697L;
    /**
     * Creates new form MapMaker
     */
    int www = 900, hhh = 900;
    public static final byte AIR = 0, GROUND = 1, WATER = 2, OIL = 3, LAVA = 4, SAND = 5, STONE = 6, TREE = 7, ICE = 8,
            CRYSTAL = 9, ETHER = 10;
    byte writeData[][] = new byte[www][hhh];
    public TexturePaint skyPaint, grassPaint, sandPaint, stonePaint, barkPaint, icePaint, nightPaint, crystalPaint,
            etherPaint;

    TexturePaint doodles[] = new TexturePaint[] { skyPaint, grassPaint, null, null, null, sandPaint, stonePaint,
            barkPaint, icePaint, crystalPaint, etherPaint };
    BufferedImage Grass, Sky, Sand, Stone, screenBuffer, Bark, Ice, LavaLand, Crystal, bigscreenBuffer, Ether;
    int kind = 0;
    public final byte liquidList[] = { WATER, OIL, LAVA, SAND };
    public final int liquidStats[][] = new int[liquidList.length][6];
    public final byte solidList[] = { SAND, GROUND, STONE, TREE, ICE };
    public final byte aList[] = new byte[127];
    String dir = System.getenv("APPDATA") + File.separator + "Bending" + File.separator;
    public final ArrayList<BrushStroke> bufferedStrokes;
    Color[] colors = new Color[] { Color.BLACK, Color.GREEN, Color.BLUE, Color.LIGHT_GRAY, Color.RED, Color.YELLOW,
            Color.GRAY, Color.ORANGE, Color.cyan, Color.cyan };

    public MapMaker() {
        bufferedStrokes = new ArrayList<>();

        File yay = new File(dir + "maps" + File.separator);
        System.err.println(yay.mkdirs());
        try {
            initComponents();
            Arrays.sort(liquidList);
            aList[WATER] = (byte) Arrays.binarySearch(liquidList, WATER);
            aList[LAVA] = (byte) Arrays.binarySearch(liquidList, LAVA);
            aList[SAND] = (byte) Arrays.binarySearch(liquidList, SAND);
            aList[OIL] = (byte) Arrays.binarySearch(liquidList, OIL);
            liquidStats[aList[WATER]][0] = 5;
            liquidStats[aList[WATER]][1] = 9;
            liquidStats[aList[WATER]][2] = Color.blue.getRGB();
            liquidStats[aList[WATER]][3] = 30;

            liquidStats[aList[LAVA]][0] = 2;
            liquidStats[aList[LAVA]][1] = 3;
            liquidStats[aList[LAVA]][2] = Color.red.getRGB();
            liquidStats[aList[LAVA]][3] = 60;

            liquidStats[aList[OIL]][0] = 5;
            liquidStats[aList[OIL]][1] = 6;
            liquidStats[aList[OIL]][2] = Color.lightGray.getRGB();
            liquidStats[aList[OIL]][3] = 10;

            liquidStats[aList[SAND]][0] = 1;
            liquidStats[aList[SAND]][1] = 1;
            liquidStats[aList[SAND]][2] = Color.red.getRGB();
            liquidStats[aList[SAND]][3] = 50;
            EditingPane.addMouseMotionListener(this);
            jScrollPane1.addMouseMotionListener(this);
            Grass = ResourceLoader.loadImage("GrassTexture.jpg");
            Sky = ResourceLoader.loadImage("SkyTexture.jpg");
            Sand = ResourceLoader.loadImage("SandTexture.jpg");
            Stone = ResourceLoader.loadImage("StoneTexture.jpg");
            Bark = ResourceLoader.loadImage("BarkTexture.jpg");
            Ice = ResourceLoader.loadImage("iceTexture.png");
            LavaLand = ResourceLoader.loadImage("lavalandTexture.png");
            skyPaint = new TexturePaint(Sky, new Rectangle(200, 200));
            Crystal = ResourceLoader.loadImage("crystalTexture.png");
            Ether = ResourceLoader.loadImage("ether.png");
            grassPaint = new TexturePaint(Grass, new Rectangle(256, 256));
            sandPaint = new TexturePaint(Sand, new Rectangle(256, 256));
            stonePaint = new TexturePaint(Stone, new Rectangle(256, 256));
            barkPaint = new TexturePaint(Bark, new Rectangle(256, 256));
            icePaint = new TexturePaint(Ice, new Rectangle(256, 256));
            crystalPaint = new TexturePaint(Crystal, new Rectangle(256, 256));
            etherPaint = new TexturePaint(Ether, new Rectangle(100, 100));
            doodles = new TexturePaint[] { skyPaint, grassPaint, null, null, null, sandPaint, stonePaint, barkPaint,
                    icePaint, crystalPaint, etherPaint };
            EditingPane.setSize(www, hhh);
            bg.setPaint(skyPaint);
            bg.fillRect(0, 0, www, hhh);

            Thread me = new Thread(this);
            me.start();
        } catch (Exception ex) {
            Logger.getLogger(MapMaker.class.getName()).log(Level.SEVERE, null, ex);
        }
        jComboBox1.addActionListener(new ActionListenerImpl());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        thickness = new javax.swing.JSlider();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        buffering = new javax.swing.JProgressBar();
        jButton11 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox<Object>();
        jScrollPane2 = new javax.swing.JScrollPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        EditingPane = new JPanelImpl();
        jButton12 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jButton1.setText("Grass");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Stone");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Ice");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Water");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("Lava");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        thickness.setMajorTickSpacing(25);
        thickness.setMaximum(300);
        thickness.setOrientation(javax.swing.JSlider.VERTICAL);
        thickness.setPaintLabels(true);
        thickness.setPaintTicks(true);
        thickness.setValue(100);
        thickness.setInverted(true);

        jButton6.setText("Sand");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setText("Bark");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setText("Sky");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton9.setText("Save Map");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton10.setText("Load Map");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        buffering.setBackground(new java.awt.Color(0, 51, 255));
        buffering.setForeground(new java.awt.Color(255, 255, 0));
        buffering.setValue(50);

        jButton11.setText("Crystal");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<Object>(
                new String[] { "900x900", "400x400", "1200x1200", "2000x900", "900x2000", "2000x2000" }));

        jScrollPane2.setPreferredSize(new java.awt.Dimension(900, 900));

        jScrollPane1.setWheelScrollingEnabled(false);

        EditingPane.setMaximumSize(new java.awt.Dimension(8000, 8000));
        EditingPane.setMinimumSize(new java.awt.Dimension(300, 300));
        EditingPane.setPreferredSize(new java.awt.Dimension(2000, 2000));
        EditingPane.setRequestFocusEnabled(false);

        javax.swing.GroupLayout EditingPaneLayout = new javax.swing.GroupLayout(EditingPane);
        EditingPane.setLayout(EditingPaneLayout);
        EditingPaneLayout.setHorizontalGroup(EditingPaneLayout
                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 2000, Short.MAX_VALUE));
        EditingPaneLayout.setVerticalGroup(EditingPaneLayout
                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 2000, Short.MAX_VALUE));

        jScrollPane1.setViewportView(EditingPane);
        EditingPane.getAccessibleContext().setAccessibleParent(jScrollPane2);

        jScrollPane2.setViewportView(jScrollPane1);

        jButton12.setText("Load Image");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jButton13.setText("Ether");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jMenuBar1.setBackground(new java.awt.Color(250, 250, 250));
        jMenuBar1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jMenu1.setText("File");

        jMenuItem1.setAccelerator(
                javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem1.setText("Save");
        jMenuItem1.setBorderPainted(true);
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem2.setAccelerator(
                javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem2.setText("Load");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);
        jMenu1.add(jSeparator1);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");

        jMenuItem3.setText("Clear");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem3);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 945, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout
                                .createSequentialGroup().addGap(99, 99, 99)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jButton9, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jButton10, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jButton12, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                Short.MAX_VALUE)))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 61,
                                                Short.MAX_VALUE)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(buffering, javax.swing.GroupLayout.Alignment.TRAILING,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout
                                                        .createSequentialGroup()
                                                        .addComponent(thickness, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                69, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(
                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addGroup(layout
                                                                .createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.LEADING,
                                                                        false)
                                                                .addComponent(jButton13,
                                                                        javax.swing.GroupLayout.Alignment.TRAILING,
                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                        Short.MAX_VALUE)
                                                                .addComponent(jButton2,
                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                        Short.MAX_VALUE)
                                                                .addComponent(jButton3,
                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                        Short.MAX_VALUE)
                                                                .addComponent(jButton4,
                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                        Short.MAX_VALUE)
                                                                .addComponent(jButton5,
                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                        Short.MAX_VALUE)
                                                                .addComponent(jButton6,
                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                        Short.MAX_VALUE)
                                                                .addComponent(jButton7,
                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                        Short.MAX_VALUE)
                                                                .addComponent(jButton11,
                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                        Short.MAX_VALUE)
                                                                .addComponent(jButton1,
                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                        Short.MAX_VALUE)
                                                                .addComponent(jButton8,
                                                                        javax.swing.GroupLayout.Alignment.TRAILING,
                                                                        javax.swing.GroupLayout.DEFAULT_SIZE, 81,
                                                                        Short.MAX_VALUE))))))
                        .addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout
                .createSequentialGroup().addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup().addComponent(jButton8)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jButton1)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jButton2)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jButton3)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jButton4)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jButton5)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jButton6)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jButton7)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jButton11))
                                        .addComponent(thickness, javax.swing.GroupLayout.PREFERRED_SIZE, 281,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 61,
                                        Short.MAX_VALUE)
                                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(41, 41, 41).addComponent(jButton9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(buffering, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap()));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton1ActionPerformed
        kind = GROUND;
    }// GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton2ActionPerformed
        kind = STONE;
    }// GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton3ActionPerformed
        kind = ICE;
    }// GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton4ActionPerformed
        kind = WATER;
    }// GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton5ActionPerformed
        kind = LAVA;
    }// GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton6ActionPerformed
        kind = SAND;
    }// GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton7ActionPerformed
        kind = TREE;
    }// GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton8ActionPerformed
        kind = AIR;
    }// GEN-LAST:event_jButton8ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton9ActionPerformed
        save();
    }// GEN-LAST:event_jButton9ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton10ActionPerformed
        load();
    }// GEN-LAST:event_jButton10ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItem1ActionPerformed
        save();
    }// GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItem2ActionPerformed
        load();
    }// GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItem3ActionPerformed

        writeData = new byte[900][900];
        bg.setPaint(skyPaint);
        bg.fillRect(0, 0, 900, 900);
        EditingPane.repaint();
    }// GEN-LAST:event_jMenuItem3ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton11ActionPerformed
        kind = CRYSTAL;
    }// GEN-LAST:event_jButton11ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton12ActionPerformed

        loadPicture();
    }// GEN-LAST:event_jButton12ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton13ActionPerformed
        kind = ETHER;
    }// GEN-LAST:event_jButton13ActionPerformed

    /**
     * @param args the command line arguments
     */
    BufferedImage bi = new BufferedImage(www, hhh, BufferedImage.TYPE_INT_RGB);
    Graphics2D bg = bi.createGraphics();

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        // <editor-fold defaultstate="collapsed" desc=" Look and feel setting code
        // (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the default
         * look and feel. For details see
         * https://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MapMaker.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        // </editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MapMaker().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel EditingPane;
    private javax.swing.JProgressBar buffering;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox<Object> jComboBox1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JSlider thickness;

    // End of variables declaration//GEN-END:variables
    public void save() {
        DataOutputStream fos;
        try {
            if (!bufferedStrokes.isEmpty()) {
                JOptionPane.showMessageDialog(rootPane, "Please wait for the file to buffer!");
                return;
            }
            File lll = new File("maps");
            if (!lll.exists()) {
                lll.mkdir();
            }
            String name = JOptionPane.showInputDialog("Save as?");
            File file = new File(dir + "maps" + File.separator + name + ".ter");

            fos = new DataOutputStream(new FileOutputStream(file));
            fos.writeInt(www);
            fos.writeInt(hhh);
            for (int i = 0; i < writeData.length; i++) {
                fos.write(writeData[i]);
            }
            fos.flush();
            fos.close();
        } catch (HeadlessException | IOException ex) {
            Logger.getLogger(MapMaker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void load() {
        DataInputStream fos;
        try {

            String name = JOptionPane.showInputDialog("Load what?");
            File file = new File(dir + "maps" + File.separator + name + ".ter");
            if (!file.exists()) {
                JOptionPane.showMessageDialog(rootPane, "File Not Found!");
                return;
            }
            fos = new DataInputStream(new FileInputStream(file));
            www = fos.readInt();
            hhh = fos.readInt();
            writeData = new byte[www][hhh];
            bi = new BufferedImage(www, hhh, BufferedImage.TYPE_INT_RGB);
            bg = bi.createGraphics();
            EditingPane.setSize(www, hhh);
            bg.setPaint(skyPaint);
            bg.fillRect(0, 0, www, hhh);
            for (int i = 0; i < writeData.length; i++) {
                byte in[] = new byte[hhh];
                fos.read(in);
                writeData[i] = in;
            }
            fos.close();
            drawTerrain();
        } catch (Exception ex) {
            Logger.getLogger(MapMaker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    byte toSet = 0;

    /**
     * Loads the picture
     */
    public void loadPicture() {
        try {

            String name;
            JFileChooser e = new JFileChooser();
            e.showOpenDialog(null);
            File file = e.getSelectedFile();
            if (!file.exists()) {
                JOptionPane.showMessageDialog(rootPane, "File Not Found!");
                return;
            }
            name = file.getAbsolutePath();
            BufferedImage loaded = ResourceLoader.loadImageFromPC(name);
            www = loaded.getWidth();
            hhh = loaded.getHeight();
            if (hhh % 100 != 0 || www % 100 != 0 || hhh < 400 || www < 400) {
                JOptionPane.showMessageDialog(rootPane,
                        "Maps must be at least 400x400 and all dimensions must be divisible by 100.");
                return;
            }
            writeData = new byte[www][hhh];
            bi = new BufferedImage(www, hhh, BufferedImage.TYPE_INT_RGB);
            bg = bi.createGraphics();
            EditingPane.setSize(www, hhh);
            bg.setPaint(skyPaint);
            bg.fillRect(0, 0, www, hhh);
            int color, red, blue, green;
            for (int cx = 0; cx < www; cx++) {
                for (int cy = 0; cy < hhh; cy++) {
                    color = loaded.getRGB(cx, cy);
                    red = getRed(color);
                    blue = getBlue(color);
                    green = getGreen(color);
                    if (red > 200 && blue > 200 && green > 200) {
                        writeData[cx][cy] = Constants.AIR;
                        continue;
                    }
                    if (red > 100 && blue > 200 && green < 20) {
                        writeData[cx][cy] = Constants.CRYSTAL;
                        continue;
                    }
                    if (red < 50 && blue > 100 && green > 40) {
                        writeData[cx][cy] = Constants.ETHER;
                        continue;
                    }
                    if (red < 100 && blue < 100 && green > 200) {
                        writeData[cx][cy] = Constants.GROUND;
                        continue;
                    }
                    if (red < 100 && blue > 200 && green > 200) {
                        writeData[cx][cy] = Constants.ICE;
                        continue;
                    }
                    if (red > 200 && blue < 20 && green < 20) {
                        writeData[cx][cy] = Constants.LAVA;
                        continue;
                    }
                    if (red < 20 && blue < 20 && green < 20) {
                        writeData[cx][cy] = Constants.OIL;
                        continue;
                    }
                    if (red > 200 && blue < 20 && green > 200) {
                        writeData[cx][cy] = Constants.SAND;
                        continue;
                    }
                    if (red > 100 && blue > 100 && green > 100) {
                        writeData[cx][cy] =Constants.STONE;
                        continue;
                    }
                    if (red > 100 && blue < 20 && green < 256) {
                        writeData[cx][cy] = Constants.TREE;
                        continue;
                    }
                    if (red < 20 && blue > 200 && green < 20) {
                        writeData[cx][cy] = Constants.WATER;
                        continue;
                    }
                }
            }
            drawTerrain();
        } catch (Exception ex) {
            Logger.getLogger(MapMaker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns the red value of an ARGB
     * @param argb ARGB value
     * @return red value
     */
    public static int getRed(int argb) {
        return 0xFF & (argb >> 16);
    }

    /**
     * Gets the blue value of an ARGB
     * @param argb ARGB value
     * @return blue value
     */
    public static int getBlue(int argb) {
        return 0xFF & argb;
    }

    /**
     * Gets the green value of an ARGB
     * @param argb ARGB value
     * @return green value
     */
    public static int getGreen(int argb) {
        return 0xFF & (argb >> 8);
    }

    double max = 1;
    double count = 0;

    public void buffer() throws InterruptedException {
        buffering.setValue(0);
        buffering.setForeground(Color.red);
        max = bufferedStrokes.size();
        count = 0;
        while (!bufferedStrokes.isEmpty()) {
            BrushStroke bs = bufferedStrokes.get(0);
            FillLine(bs.X1, bs.Y1, bs.X2, bs.Y2, bs.THICKNESS, (byte) bs.MATERIAL);
            Thread.sleep(1);
            count++;
            bufferedStrokes.remove(0);
            buffering.setValue((int) ((count / max) * 100));
        }
        buffering.setValue(100);
        buffering.setForeground(Color.green);
    }

    long timeNoSee = 0;
    int x, y;

    @Override
    public void mouseDragged(MouseEvent e) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change
        // body of generated methods, choose Tools | Templates.
        if (e.getSource().equals(EditingPane)) {
            long hereIAm = System.currentTimeMillis();
            bg.setStroke(new BasicStroke(thickness.getValue(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            if (doodles[kind] == null) {
                bg.setColor(colors[kind]);
            } else {
                bg.setPaint(doodles[kind]);
            }
            if (hereIAm - timeNoSee < 100) {
                bg.drawLine(x, y, e.getX(), e.getY());
                // FillLine(x,y,e.getX(),e.getY(),thickness.getValue(),(byte)kind);
                synchronized (bufferedStrokes) {
                    bufferedStrokes.add(new BrushStroke(x, y, e.getX(), e.getY(), thickness.getValue(), kind));
                    buffering.setValue(0);
                    max = bufferedStrokes.size();
                    count = 0;
                    buffering.setForeground(Color.red);
                }
                // bg.fillArc(x-2, y-2, 4,4, 0, 360);
                // bg.fillArc(e.getX()-2, e.getY()-2, 4,4, 0, 360);
                EditingPane.getGraphics().drawImage(bi, 0, 0, rootPane);
            }
            x = e.getX();
            y = e.getY();
            timeNoSee = hereIAm;
        }
        if (e.getSource().equals(jScrollPane1)) {
            EditingPane.getGraphics().drawImage(bi, 0, 0, rootPane);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change
        // body of generated methods, choose Tools | Templates.
    }

    /**
     * Fills a circle
     * @param X X coordinate
     * @param Y Y coordinate
     * @param R Radius
     * @param T Type for the filling
     */
    public void FillCircle(int X, int Y, int R, byte T) {
        // long time = System.nanoTime();
        for (int i1 = Math.max(X - (R + 1), 0); i1 < Math.min(X + (R + 1), www); i1++) {
            for (int i2 = Math.max(Y - (R + 1), 0); i2 < Math.min(Y + (R + 1), hhh); i2++) {
                if (Math.round(Math.sqrt(Math.pow(i1 - X, 2) + Math.pow(i2 - Y, 2))) < (R / 2) + .1) {
                    writeData[i1][i2] = T;
                }
            }
        }
    }

    /**
     * Fills the line
     * @param X1 Starting point (x coordinate)
     * @param Y1 Starting point (y coordinate)
     * @param X2 Ending point (x coordinate)
     * @param Y2 Ending point (y coordinate)
     * @param R Radius
     * @param T
     */
    public void FillLine(int X1, int Y1, int X2, int Y2, int R, byte T) {
        double dir = Math.atan2(X2 - X1, Y2 - Y1);
        double dis = Math.sqrt((X2 - X1) * (X2 - X1) + (Y2 - Y1) * (Y2 - Y1));
        double x = X1, y = Y1;
        for (int i = 0; i <= dis; i++) {
            x = X1 + i * Math.cos(dir);
            y = Y1 + i * Math.sin(dir);
            FillCircle((int) x, (int) y, R, T);
        }
    }

    /**
     * Starts and runs the thread
     */
    public void run() {
        while (true) {
            try {
                Thread.sleep(250);
                if ((!bufferedStrokes.isEmpty()) && System.currentTimeMillis() - timeNoSee > 100) {
                    buffer();
                } else {
                    Thread.sleep(150);
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(MapMaker.class.getName()).log(Level.SEVERE, null, ex);
            }
            // EditingPane.getGraphics().drawImage(bi, 0, 0, rootPane);
        }
    }

    /**
     * Draws the terrain
     * @throws Exception
     */
    public synchronized void drawTerrain() throws Exception {
        // G2.drawImage(Iter, -3, -3, null);
        bg.setPaint(skyPaint);//
        bg.fillRect(0, 0, www, hhh);
        for (int i = 0; i <= liquidList.length; i++) {
            if (SAND == aList[i])
                continue;
            for (int X = 0; X < www; X++) {
                for (int Y = 0; Y < hhh; Y++) {
                    if (writeData[X][Y] == liquidList[aList[i]]) {
                        bi.setRGB(X, Y, liquidStats[aList[i]][2]);
                    }
                }
            }
            EditingPane.repaint();
        }
        for (int X = 0; X < www; X++) {
            for (int Y = 0; Y < hhh; Y++) {
                switch (writeData[X][Y]) {
                    default:
                        break;
                    case GROUND:
                        bi.setRGB(Math.min(X, www), Math.min(Y, hhh), Grass.getRGB(X % 256, Y % 256));
                        break;
                    case SAND:
                        bi.setRGB(Math.min(X, www), Math.min(Y, hhh), Sand.getRGB(X % 256, Y % 256));
                        break;
                    case STONE:
                        bi.setRGB(Math.min(X, www), Math.min(Y, hhh), Stone.getRGB(X % 256, Y % 256));
                        break;
                    case TREE:
                        bi.setRGB(Math.min(X, www), Math.min(Y, hhh), Bark.getRGB(X % 256, Y % 256));
                        break;
                    case ICE:
                        bi.setRGB(Math.min(X, www), Math.min(Y, hhh), Ice.getRGB(X % 256, Y % 256));
                        break;
                    case CRYSTAL:
                        bi.setRGB(Math.min(X, www), Math.min(Y, hhh), Crystal.getRGB(X % 256, Y % 256));
                        break;
                }
            }
        }

    }

    @Override
    public void componentResized(ComponentEvent e) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change
        // body of generated methods, choose Tools | Templates.
    }

    @Override
    public void componentMoved(ComponentEvent e) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change
        // body of generated methods, choose Tools | Templates.
    }

    @Override
    public void componentShown(ComponentEvent e) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change
        // body of generated methods, choose Tools | Templates.
    }

    @Override
    public void componentHidden(ComponentEvent e) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change
        // body of generated methods, choose Tools | Templates.
    }

    private class JPanelImpl extends JPanel {
        private static final long serialVersionUID = -9090043087012784870L;


        private JPanelImpl() {
            // throw new UnsupportedOperationException("Not supported yet."); //To change
            // body of generated methods, choose Tools | Templates.

            super();
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            g.drawImage(bi, 0, 0, rootPane);
        }
    }

    public class BrushStroke {
        public int X1, Y1, X2, Y2, THICKNESS, MATERIAL;

        public BrushStroke(int X1, int Y1, int X2, int Y2, int THICKNESS, int MATERIAL) {
            this.X1 = X1;
            this.Y1 = Y1;
            this.X2 = X2;
            this.Y2 = Y2;
            this.THICKNESS = THICKNESS;
            this.MATERIAL = MATERIAL;
        }
    }

    private class ActionListenerImpl implements ActionListener {

        public ActionListenerImpl() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // 900x900, 300x300, 1200x1200, 2000x900, 900x2000
            switch (jComboBox1.getSelectedIndex()) {
                case 0:
                    www = 900;
                    hhh = 900;
                    break;
                case 1:
                    www = 400;
                    hhh = 400;
                    break;
                case 2:
                    www = 1200;
                    hhh = 1200;
                    break;
                case 3:
                    www = 2000;
                    hhh = 900;
                    break;
                case 4:
                    www = 900;
                    hhh = 2000;
                    break;
                case 5:
                    www = 2000;
                    hhh = 2000;
                    break;
            }
            writeData = new byte[www][hhh];
            EditingPane.setSize(www, hhh);
            bi = new BufferedImage(www, hhh, BufferedImage.TYPE_INT_RGB);
            bg = bi.createGraphics();

            try {
                drawTerrain();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
