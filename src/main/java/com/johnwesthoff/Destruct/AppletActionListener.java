/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johnwesthoff.Destruct;

import static com.johnwesthoff.Destruct.APPLET.currentlyLoggedIn;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author John
 */
public class AppletActionListener implements ActionListener {
    ConnectToDatabase INSTANCE = ConnectToDatabase.INSTANCE();
    APPLET pointer;

    public AppletActionListener(APPLET pointer) {
        this.pointer = pointer;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals(pointer.connect.getText())) {
            if ((!pointer.loggedOn) && pointer.menu.getItemCount() > 0) {
                if (!currentlyLoggedIn) {
                    pointer.loggedOn = false;
                    // pointer.repaint();
                    return;
                }
                pointer.notDone = false;
                // owner.setResizable(true);
                pointer.username = APPLET.jtb.getText();
                pointer.serverIP = (String) pointer.hosts[pointer.menu.getSelectedIndex()];
                if ("enterip".equals(pointer.serverIP)) {
                    pointer.serverIP = JOptionPane.showInputDialog("Server IP?");
                }
                pointer.init();
                if (pointer.start()) {
                    pointer.spellselection.setVisible(false);
                    pointer.spellselection.choochootrain.setVisible(false);
                    APPLET.immaKeepTabsOnYou.setSelectedIndex(0);
                    if (!pointer.failed) {
                        pointer.removeAll();
                        pointer.owner.setBackground(Color.black);
                    }
                } else {
                    pointer.loggedOn = false;
                    pointer.notDone = true;
                    pointer.repaint();
                }
            }
        }
        if (command.equals(pointer.hosting.getText())) {
            if (APPLET.portAvailable(pointer.port)) {
                pointer.hostingPlace = Server.main2(new String[] { "" + (pointer.hostIP = pointer.addHost()), "" });
                pointer.hosting.setText("Started!");
                APPLET.container.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
                try {
                    pointer.ST.add(pointer.trayIcon);
                } catch (AWTException ex) {
                    // Logger.getLogger(APPLET.class.getName()).log(Level.SEVERE, null, ex);
                }
                pointer.serverOutput();
                System.err.println("Server " + pointer.serverName + " started\nwith address " + pointer.hostIP
                        + "\nand port " + pointer.port);
                pointer.getHosts();
            } else {
                pointer.hosting.setText("Server Unstartable");
            }
        }
        if (command.equals(pointer.refresh.getText())) {
            pointer.getHosts();
        }
        if (command.equals(pointer.ChooseSpells.getText())) {
            pointer.spellselection.XP.setText("XP: " + APPLET.XP);
            pointer.spellselection.USER.setText("USER: " + APPLET.jtb.getText());
            APPLET.immaKeepTabsOnYou.setSelectedIndex(1);
            pointer.spellselection.setVisible(true);
        }
        if (command.equals(pointer.chooseclothing.getText())) {
            pointer.cc.setVisible(true);
            pointer.cc.loadClothing();
            APPLET.immaKeepTabsOnYou.setSelectedIndex(4);
            // pointer.add(pointer.cc.getPanel());
        }
        if (command.equals("Exit")) {
            if (!"".equals(pointer.hostIP)) {
                INSTANCE.removeServer(pointer.hostIP);
            }
            pointer.ST.remove(pointer.trayIcon);
            System.exit(0);
        }
        if (command.equals("Hide")) {
            pointer.trayIcon.displayMessage(null, "Game Hidden...", TrayIcon.MessageType.INFO);
            pointer.owner.setVisible(false);
            if (pointer.sgui != null) {
                pointer.sgui.setVisible(false);
            }
        }
        if (command.equals("Show")) {
            pointer.owner.setVisible(true);
            if (pointer.sgui != null) {
                pointer.sgui.setVisible(true);
            }
        }
        if (command.equals("Restart")) {

            try {
                ByteBuffer die = ByteBuffer.allocate(5);
                pointer.out.addMesssage(die, Server.LOGOUT);
                Thread.sleep(1000);
            } catch (IOException | InterruptedException ex) {
                Logger.getLogger(APPLET.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                pointer.destroy();
                APPLET.gameAlive = false;
                if (pointer.connection != null) {
                    pointer.input.close();
                    pointer.out.close();
                    pointer.connection.close();
                    pointer.communication.interrupt();
                }
                pointer.mainProcess.interrupt();
                if (pointer.hostingPlace != null) {
                    pointer.hostingPlace.kill();
                    pointer.hostingPlace = null;
                }

                APPLET.container.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                APPLET.container.dispose();
                APPLET.container.removeAll();
                // this.expander.interrupt();
                pointer.ST.remove(pointer.trayIcon);
                pointer.removeAll();
                if (APPLET.master == null) {
                    WindowEvent windowClosing = new WindowEvent(APPLET.container, WindowEvent.WINDOW_CLOSING);
                    APPLET.container.dispatchEvent(windowClosing);
                    APPLET.container = null;
                    Thread.sleep(100);
                    APPLET.main(new String[] {});
                    currentlyLoggedIn = false;
                } else {
                    APPLET.master.stuff();
                }
                // System.exit(1);
            } catch (IOException | InterruptedException ex) {
                Logger.getLogger(APPLET.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (command.equals(pointer.register.getText())) {
            pointer.form.setVisible(true);
        }
        if (command.equals(pointer.mapMaker.getText())) {
            MapMaker.main(new String[] { "" });
        }
        if (command.equals(pointer.verify.getText())) {
            // exactly.setVisible(true);
            if (!currentlyLoggedIn) {
                if (currentlyLoggedIn = pointer.CTD.logIn(APPLET.jtb.getText(), pointer.jtp.getText())) {
                    if (pointer.JRB.isSelected()) {
                        pointer.userpassinfo.setProperty("username", APPLET.jtb.getText());
                        pointer.userpassinfo.setProperty("password", pointer.jtp.getText());
                        pointer.userpassinfo.setProperty("remember", "yes");
                    } else {
                        pointer.userpassinfo.setProperty("username", "");
                        pointer.userpassinfo.setProperty("password", "");
                        pointer.userpassinfo.setProperty("remember", "");
                    }
                    try {
                        pointer.userpassinfo
                                .store(new FileOutputStream(new File(ResourceLoader.dir + "properties.xyz")), "");
                    } catch (Exception ex) {
                        Logger.getLogger(APPLET.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    // verify.setEnabled(false);
                    APPLET.jtb.setEditable(false);
                    pointer.jtp.setEditable(false);
                    pointer.verify.setText("Log Out");
                    pointer.verify.setForeground(Color.red);
                    APPLET.XP = pointer.CTD.getXP(APPLET.jtb.getText(), pointer.jtp.getText());
                    pointer.cc.loadClothing();
                    pointer.spellselection.loadSpells();
                    pointer.chooseclothing.setEnabled(true);
                    pointer.ChooseSpells.setEnabled(true);
                    pointer.connect.setEnabled(true);
                    INSTANCE.getUnlocks(APPLET.jtb.getText(), pointer.jtp.getText());
                }
            } else {
                pointer.verify.setText("Log In");
                APPLET.jtb.setEditable(true);
                pointer.jtp.setEditable(true);
                pointer.chooseclothing.setEnabled(false);
                pointer.ChooseSpells.setEnabled(false);
                pointer.connect.setEnabled(false);
                currentlyLoggedIn = false;
                pointer.verify.setForeground(Color.black);
            }
            pointer.verify.setActionCommand(pointer.verify.getText());
        }
    }
}
