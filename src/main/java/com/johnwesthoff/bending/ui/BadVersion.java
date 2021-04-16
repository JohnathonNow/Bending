/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johnwesthoff.bending.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.awt.Cursor;
import java.awt.Desktop;


import javax.swing.JLabel;
import javax.swing.event.MouseInputListener;

import com.johnwesthoff.bending.Constants;
import com.johnwesthoff.bending.Session;

/**
 *
 * @author John
 */
public class BadVersion extends javax.swing.JPanel {
    private JLabel label;

    public BadVersion() {
        initComponents();

    }

    @Override
    protected void paintComponent(Graphics g) {
        // super.paintComponent(g);
        g.drawImage(Session.getInstance().clientui.bimage, 0, 0, getWidth(), getHeight(), null);
    }

    public void go() {
        String s = String.format("This server uses version %d, but you're using version %d - click here!",
                Session.getInstance().serverVersion, Constants.VERSION);
        label.setText(s);
    }

    private void initComponents() {
        label = new JLabel();
        this.setLayout(new BorderLayout());
        this.add(label, BorderLayout.CENTER);
        label.setForeground(Color.BLUE.darker());
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        label.addMouseListener(new MouseInputListener() {

            @Override
            public void mouseClicked(MouseEvent arg0) {
                try {
                    Desktop.getDesktop().browse(new URI("https://github.com/JohnathonNow/Bending/releases"));
                     
                } catch (Exception e) {
                    // don't care
                }

            }

            @Override
            public void mouseEntered(MouseEvent arg0) {
                // don't care
            }

            @Override
            public void mouseExited(MouseEvent arg0) {
                // don't care
            }

            @Override
            public void mousePressed(MouseEvent arg0) {
                // don't care
            }

            @Override
            public void mouseReleased(MouseEvent arg0) {
                // don't care
            }

            @Override
            public void mouseDragged(MouseEvent arg0) {
                // don't care
            }

            @Override
            public void mouseMoved(MouseEvent arg0) {
                // don't care
            }
        });
    }
}
