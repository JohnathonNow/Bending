/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johnwesthoff.bending.ui;

import static com.johnwesthoff.bending.Main.currentlyLoggedIn;

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

import com.johnwesthoff.bending.Main;
import com.johnwesthoff.bending.Server;
import com.johnwesthoff.bending.app.game.GameService;
import com.johnwesthoff.bending.app.game.GameServiceFactory;
import com.johnwesthoff.bending.util.network.ResourceLoader;

/**
 *
 * @author John
 */
public class AppletActionListener implements ActionListener {

    private final GameService gameService;

    Main app;

    public AppletActionListener(final Main app) {
        this.app = app;

        // using factory to inject dependency
        gameService = GameServiceFactory.create();
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        final String command = e.getActionCommand();
        if (command.equals(app.connect.getText())) {
            if ((!app.loggedOn) && app.menu.getItemCount() > 0) {
                if (!currentlyLoggedIn) {
                    app.loggedOn = false;
                    // app.repaint();
                    return;
                }
                app.notDone = false;
                // app.setResizable(true);
                app.username = Main.jtb.getText();
                app.serverIP = (String) app.hosts[app.menu.getSelectedIndex()];
                if ("enterip".equals(app.serverIP)) {
                    app.serverIP = JOptionPane.showInputDialog("Server IP?");
                }
                app.init();
                if (app.start()) {
                    app.spellselection.setVisible(false);
                    app.spellselection.choochootrain.setVisible(false);
                    Main.immaKeepTabsOnYou.setSelectedIndex(0);
                    if (!app.failed) {
                        app.removeAll();
                        app.owner.setBackground(Color.black);
                    }
                } else {
                    app.loggedOn = false;
                    app.notDone = true;
                    app.repaint();
                }
            }
        }
        if (command.equals(app.hosting.getText())) {
            if (Main.portAvailable(app.port)) {
                app.hostingPlace = Server.main2(new String[] { "" + (app.hostIP = app.addHost()), "" });
                app.hosting.setText("Started!");
                Main.container.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
                try {
                    app.ST.add(app.trayIcon);
                } catch (final AWTException ex) {
                    // Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
                app.serverOutput();
                System.err.printf(
                        "Server %s started\nwith address %s" + app.hostIP + "\nand port " + app.port,
                        gameService.getServerName(),
                        gameService.getHostIp(),
                        gameService.getPort()
                );
                app.getHosts();
            } else {
                app.hosting.setText("Server Unstartable");
            }
        }
        if (command.equals(app.refresh.getText())) {
            app.getHosts();
        }
        if (command.equals(app.ChooseSpells.getText())) {
            app.spellselection.XP.setText("XP: " + Main.XP);
            app.spellselection.USER.setText("USER: " + Main.jtb.getText());
            Main.immaKeepTabsOnYou.setSelectedIndex(1);
            app.spellselection.setVisible(true);
        }
        if (command.equals(app.chooseclothing.getText())) {
            app.cc.setVisible(true);
            app.cc.loadClothing();
            Main.immaKeepTabsOnYou.setSelectedIndex(4);
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
                final ByteBuffer die = ByteBuffer.allocate(5);
                app.out.addMesssage(die, Server.LOGOUT);
                Thread.sleep(1000);
            } catch (IOException | InterruptedException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                app.destroy();
                Main.gameAlive = false;
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

                Main.container.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                Main.container.dispose();
                Main.container.removeAll();
                // this.expander.interrupt();
                app.ST.remove(app.trayIcon);
                app.removeAll();

                final WindowEvent windowClosing = new WindowEvent(Main.container, WindowEvent.WINDOW_CLOSING);
                Main.container.dispatchEvent(windowClosing);
                Main.container = null;
                Thread.sleep(100);
                Main.main(new String[] {});
                currentlyLoggedIn = false;

            } catch (IOException | InterruptedException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
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
                if (currentlyLoggedIn = gameService.login(Main.jtb.getText(), app.jtp.getText())) {
                    if (app.JRB.isSelected()) {
                        app.userpassinfo.setProperty("username", Main.jtb.getText());
                        app.userpassinfo.setProperty("password", app.jtp.getText());
                        app.userpassinfo.setProperty("remember", "yes");
                    } else {
                        app.userpassinfo.setProperty("username", "");
                        app.userpassinfo.setProperty("password", "");
                        app.userpassinfo.setProperty("remember", "");
                    }
                    try {
                        app.userpassinfo
                                .store(new FileOutputStream(new File(ResourceLoader.dir + "properties.xyz")), "");
                    } catch (final Exception ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    // verify.setEnabled(false);
                    Main.jtb.setEditable(false);
                    app.jtp.setEditable(false);
                    app.verify.setText("Log Out");
                    app.verify.setForeground(Color.red);

                    // @TODO : Open / close principle
                    Main.XP = gameService.getPlayerExperience(Main.jtb.getText(), app.jtp.getText());

                    app.cc.loadClothing();
                    app.spellselection.loadSpells();
                    app.chooseclothing.setEnabled(true);
                    app.ChooseSpells.setEnabled(true);
                    app.connect.setEnabled(true);

                    gameService.getUnlocks(Main.jtb.getText(), app.jtp.getText());
                }
            } else {
                app.verify.setText("Log In");
                Main.jtb.setEditable(true);
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
