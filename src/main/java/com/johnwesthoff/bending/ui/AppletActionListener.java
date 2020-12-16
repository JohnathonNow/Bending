/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johnwesthoff.bending.ui;

import static com.johnwesthoff.bending.Client.currentlyLoggedIn;

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
import javax.swing.JOptionPane;

import com.johnwesthoff.bending.Client;
import com.johnwesthoff.bending.Server;
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

    Client app;

    public AppletActionListener(final Client app) {
        this.app = app;

        // using factory to inject dependency
        gameService = GameServiceFactory.create();
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        final String command = e.getActionCommand();
        if (command.equals(app.connect.getText())) {
            app.notDone = false;
            // app.setResizable(true);
            app.username = Client.jtb.getText();
            app.serverIP = JOptionPane.showInputDialog("Server IP?");
            app.init();
            if (app.start()) {
                app.spellselection.setVisible(false);
                app.spellselection.choochootrain.setVisible(false);
                Client.immaKeepTabsOnYou.setSelectedIndex(0);
                if (!app.failed) {
                    app.removeAll();
                    app.owner.setBackground(Color.black);
                }
            }
        }
        if (command.equals(app.hosting.getText())) {
            if (Client.portAvailable(app.port)) {
                app.hostingPlace = Server.main2(new String[] { "" + (app.hostIP = app.addHost()), "" });
                app.hosting.setText("Started!");
                Client.container.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
                try {
                    app.ST.add(app.trayIcon);
                } catch (final AWTException ex) {
                    // Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
                app.serverOutput();
                System.err.printf("Server %s started\nwith address %s" + app.hostIP + "\nand port " + app.port,
                        gameService.getServerName(), gameService.getHostIp(), gameService.getPort());
                app.getHosts();
            } else {
                app.hosting.setText("Server Unstartable");
            }
        }
        if (command.equals(app.refresh.getText())) {
            app.getHosts();
        }
        if (command.equals(app.ChooseSpells.getText())) {
            app.spellselection.XP.setText("XP: " + Client.XP);
            app.spellselection.USER.setText("USER: " + Client.jtb.getText());
            Client.immaKeepTabsOnYou.setSelectedIndex(1);
            app.spellselection.setVisible(true);
        }
        if (command.equals(app.chooseclothing.getText())) {
            app.cc.setVisible(true);
            app.cc.loadClothing();
            Client.immaKeepTabsOnYou.setSelectedIndex(4);
            // app.add(app.cc.getPanel());
        }
        if (command.equals("Exit")) {
            gameService.tryToRemoveServer(gameService.getHostIp());
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
                app.out.addMessage(LeaveEvent.getPacket(null));
                Thread.sleep(1000);
            } catch (IOException | InterruptedException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                app.destroy();
                Client.gameAlive = false;
                if (app.connection != null) {
                    app.input.close();
                    app.out.close();
                    app.connection.close();
                    app.communication.interrupt();
                }
                app.mainProcess.interrupt();
                if (app.hostingPlace != null) {
                    app.hostingPlace.kill();
                    app.hostingPlace = null;
                }

                Client.container.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                Client.container.dispose();
                Client.container.removeAll();
                // this.expander.interrupt();
                app.ST.remove(app.trayIcon);
                app.removeAll();

                final WindowEvent windowClosing = new WindowEvent(Client.container, WindowEvent.WINDOW_CLOSING);
                Client.container.dispatchEvent(windowClosing);
                Client.container = null;
                Thread.sleep(100);
                Client.main(new String[] {});
                currentlyLoggedIn = false;

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
            if (!currentlyLoggedIn) {
                if (currentlyLoggedIn = gameService.login(Client.jtb.getText(), "PASSWORD IGNORED")) {
                    if (app.JRB.isSelected()) {
                        app.userpassinfo.setProperty("username", Client.jtb.getText());
                        app.userpassinfo.setProperty("password", "PASSWORD IGNORED");
                        app.userpassinfo.setProperty("remember", "yes");
                    } else {
                        app.userpassinfo.setProperty("username", "");
                        app.userpassinfo.setProperty("password", "");
                        app.userpassinfo.setProperty("remember", "");
                    }
                    try {
                        app.userpassinfo.store(new FileOutputStream(new File(ResourceLoader.dir + "properties.xyz")),
                                "");
                    } catch (final Exception ex) {
                        Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    // verify.setEnabled(false);
                    Client.jtb.setEditable(false);
                    app.jtp.setEditable(false);
                    app.verify.setText("Log Out");
                    app.verify.setForeground(Color.red);

                    // @TODO : Open / close principle
                    Client.XP = gameService.getPlayerExperience(Client.jtb.getText(), "PASS IGNORED");

                    app.cc.loadClothing();
                    app.spellselection.loadSpells();
                    app.chooseclothing.setEnabled(true);
                    app.ChooseSpells.setEnabled(true);
                    app.connect.setEnabled(true);

                    gameService.getUnlocks(Client.jtb.getText(), "I NOW DO NOTHING");
                }
            } else {
                app.verify.setText("Log In");
                Client.jtb.setEditable(true);
                app.jtp.setEditable(true);
                app.chooseclothing.setEnabled(false);
                app.ChooseSpells.setEnabled(false);
                app.connect.setEnabled(false);
                currentlyLoggedIn = false;
                app.verify.setForeground(Color.black);
            }
            app.verify.setActionCommand(app.verify.getText());
        }
    }
}
