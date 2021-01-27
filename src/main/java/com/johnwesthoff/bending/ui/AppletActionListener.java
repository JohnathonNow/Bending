/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johnwesthoff.bending.ui;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;

import com.johnwesthoff.bending.Client;
import com.johnwesthoff.bending.ClientUI;
import com.johnwesthoff.bending.Server;
import com.johnwesthoff.bending.Session;
import com.johnwesthoff.bending.app.game.GameService;
import com.johnwesthoff.bending.app.game.GameServiceFactory;
import com.johnwesthoff.bending.networking.handlers.LeaveEvent;
import com.johnwesthoff.bending.util.network.ResourceLoader;

/**
 *
 * @author John
 */
public class AppletActionListener implements ActionListener {

    private final GameService gameService;

    ClientUI app;
    Session sess;

    public AppletActionListener(final ClientUI app) {
        this.app = app;
        sess = Session.getInstance();

        // using factory to inject dependency
        gameService = GameServiceFactory.create();
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        final String command = e.getActionCommand();
        if (command.equals(app.connect.getText())) {
            sess.notDone = false;
            // app.setResizable(true);
            sess.username = app.jtb.getText();
            //app.serverIP = JOptionPane.showInputDialog("Server IP?");
            app.init();
            if (sess.net.start()) {
                app.spellselection.setVisible(false);
                app.spellselection.choochootrain.setVisible(false);
                app.immaKeepTabsOnYou.setSelectedIndex(0);
                if (!sess.failed) {
                    app.removeAll();
                    app.owner.setBackground(Color.black);
                }
            }
        }
        if (command.equals(app.hosting.getText())) {
            sess.hostingPlace = Server.main2(new String[] { "" + ("0.0.0.0"), "" });
            gameService.tryToCreateServer(GameService.DEFAULT_SERVER_NAME, "0.0.0.0");
            app.hosting.setText("Started!");
            app.container.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            try {
                app.ST.add(app.trayIcon);
            } catch (final AWTException ex) {
                // Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
            app.serverOutput();
            System.err.printf("Server %s started\nwith address %s" + "\nand port " + sess.port,
                    gameService.getServerName(), gameService.getHostIp(), gameService.getPort());
            //app.getHosts();
        }
        if (command.equals(app.ChooseSpells.getText())) {
            app.spellselection.XP.setText("XP: " + sess.XP);
            app.spellselection.USER.setText("USER: " + app.jtb.getText());
            app.immaKeepTabsOnYou.setSelectedIndex(1);
            app.spellselection.setVisible(true);
        }
        if (command.equals(app.chooseclothing.getText())) {
            app.cc.setVisible(true);
            app.cc.loadClothing();
            app.immaKeepTabsOnYou.setSelectedIndex(4);
            // app.add(app.cc.getPanel());
        }
        if (command.equals("Exit")) {
            //gameService.tryToRemoveServer(gameService.getHostIp());
            app.ST.remove(app.trayIcon);
            System.exit(0);
        }
        if (command.equals("Hide")) {
            app.trayIcon.displayMessage(null, "Game Hidden...", TrayIcon.MessageType.INFO);
            app.owner.setVisible(false);
            if (app.sgui != null) {
                app.sgui.setVisible(false);
            }
        }
        if (command.equals("Show")) {
            app.owner.setVisible(true);
            if (app.sgui != null) {
                app.sgui.setVisible(true);
            }
        }
        if (command.equals("Restart")) {

            try {
                sess.out.addMessage(LeaveEvent.getPacket(null));
                Thread.sleep(1000);
            } catch (IOException | InterruptedException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                app.destroy();
                sess.gameAlive = false;
                if (sess.connection != null) {
                    sess.input.close();
                    sess.out.close();
                    sess.connection.close();
                    sess.communication.interrupt();
                }
                sess.mainProcess.interrupt();
                if (sess.hostingPlace != null) {
                    sess.hostingPlace.kill();
                    sess.hostingPlace = null;
                }

                app.container.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                app.container.dispose();
                app.container.removeAll();
                // this.expander.interrupt();
                app.ST.remove(app.trayIcon);
                app.removeAll();

                final WindowEvent windowClosing = new WindowEvent(app.container, WindowEvent.WINDOW_CLOSING);
                app.container.dispatchEvent(windowClosing);
                app.container = null;
                Thread.sleep(100);
                ClientUI.main(new String[] {});
                sess.currentlyLoggedIn = false;

            } catch (IOException | InterruptedException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (command.equals(app.register.getText())) {
            app.form.setVisible(true);
        }
        if (command.equals(app.mapMaker.getText())) {
            MapMaker.main(new String[] { "" });
        }
        if (command.equals(app.verify.getText())) {
            // exactly.setVisible(true);
            if (!sess.currentlyLoggedIn) {
                if (sess.currentlyLoggedIn = gameService.login(app.jtb.getText(), "PASSWORD IGNORED")) {
                    if (app.JRB.isSelected()) {
                        sess.userpassinfo.setProperty("username", app.jtb.getText());
                        sess.userpassinfo.setProperty("password", "PASSWORD IGNORED");
                        sess.userpassinfo.setProperty("remember", "yes");
                    } else {
                        sess.userpassinfo.setProperty("username", "");
                        sess.userpassinfo.setProperty("password", "");
                        sess.userpassinfo.setProperty("remember", "");
                    }
                    try {
                        sess.userpassinfo.store(new FileOutputStream(new File(ResourceLoader.dir + "properties.xyz")),
                                "");
                    } catch (final Exception ex) {
                        Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    // verify.setEnabled(false);
                    app.jtb.setEditable(false);
                    app.jtp.setEditable(false);
                    app.verify.setText("Log Out");
                    app.verify.setForeground(Color.red);

                    // @TODO : Open / close principle
                    sess.XP = gameService.getPlayerExperience(app.jtb.getText(), "PASS IGNORED");

                    app.cc.loadClothing();
                    app.spellselection.loadSpells();
                    app.chooseclothing.setEnabled(true);
                    app.ChooseSpells.setEnabled(true);
                    app.connect.setEnabled(true);

                    gameService.getUnlocks(app.jtb.getText(), "I NOW DO NOTHING");
                }
            } else {
                app.verify.setText("Log In");
                app.jtb.setEditable(true);
                app.jtp.setEditable(true);
                app.chooseclothing.setEnabled(false);
                app.ChooseSpells.setEnabled(false);
                app.connect.setEnabled(false);
                sess.currentlyLoggedIn = false;
                app.verify.setForeground(Color.black);
            }
            app.verify.setActionCommand(app.verify.getText());
        }
    }
}
