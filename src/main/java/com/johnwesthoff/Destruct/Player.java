/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johnwesthoff.Destruct;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 *
 * @author John
 */
public class Player {
    public int x, y, ID, move, vspeed;
    public boolean myTeam = false;
    public Rectangle playerHitbox;
    public String username = "Player";
    public int score = 0;
    short status = 0;
    public Image[] bodyParts;// Body, head, ua, la, ul, ll
    Thread loader;
    boolean done = false;
    short HP = 0;
    final byte[] partss;
    final int colorss[];
    final int colorss2[];

    public Player(int X, int Y, final byte[] parts, final int colors[], final int colors2[]) {
        x = X;
        y = Y;
        playerHitbox = new Rectangle(x, y, 20, 40);
        this.partss = parts;
        this.colorss = colors;
        this.colorss2 = colors2;
        Runnable getStuff = new Runnable() {

            @Override
            public void run() {
                bodyParts = new Image[partss.length];
                try {
                    for (int i = 0; i < partss.length; i++) {
                        bodyParts[i] = ResourceLoader.loadImageNoHash(
                                "https://west-it.webs.com/bodyParts/p" + (i + 1) + "_" + parts[i] + ".png",
                                "p" + (i + 1) + "_" + parts[i] + ".png");
                        bodyParts[i] = World.changeColor((BufferedImage) bodyParts[i], Color.white,
                                new Color(colorss[i]));
                        bodyParts[i] = World.changeColor((BufferedImage) bodyParts[i], Color.lightGray,
                                new Color(colorss2[i]));
                        bodyParts[i] = World.changeColor((BufferedImage) bodyParts[i], new Color(0xBEBEBE),
                                new Color(colorss2[i]).darker());
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                // System.err.println(partss[1]);
                done = true;
            }
        };
        loader = new Thread(getStuff);
        loader.start();
    }

    int left = 1;
    double leftArmAngle = 90, rightArmAngle = 90;

    public void onDraw(Graphics g, int viewX, int viewY) {
        if ((status & World.ST_INVISIBLE) != 0) {
            return;
        }
        int offs = x % 8;
        if (!done) {
            // x+=move;
            g.drawArc(((x - 2) - viewX) * 3, ((y - 10) - viewY) * 3, 4, 4, 0, 360);
            g.drawLine(((x) - viewX) * 3, ((y - 6) - viewY) * 3, ((x) - viewX) * 3, ((y - 3) - viewY) * 3);
            g.drawLine(((x - 2) - viewX) * 3, ((y - 4) - viewY) * 3, ((x + 2) - viewX) * 3, ((y - 4) - viewY) * 3);
            g.drawLine(((x) - viewX) * 3, ((y - 3) - viewY) * 3, ((x + offs - 2) - viewX) * 3, ((y) - viewY) * 3);
            g.drawLine(((x) - viewX) * 3, ((y - 3) - viewY) * 3, ((x + 2 - offs) - viewX) * 3, ((y) - viewY) * 3);
        } else {
            int yUp = 20;

            if (Integer.signum(move) == -1) {
                left = -1;
            }
            if (Integer.signum(move) == 1) {
                left = 1;
            }
            Graphics2D g2 = (Graphics2D) g;
            g2.scale(left, 1);
            g2.drawImage(bodyParts[0], (x - viewX) * 3 * left + (left < 0 ? -18 : 0), (y - yUp - 6 - viewY) * 3, null);
            g2.drawImage(bodyParts[1],
                    (int) (x + 2 - ((bodyParts[1].getWidth(null) - 23) / 5) - viewX) * 3 * left
                            + (left < 0 ? -(6 + (bodyParts[1].getWidth(null) - 23)) : 0),
                    ((y - yUp - 16 - ((bodyParts[1].getHeight(null) - 31)) / 3) - viewY) * 3, null);

            double ffs = Math.toRadians(((4 - offs) * 6));
            AffineTransform previousAT = g2.getTransform();
            g2.translate((x - 3 - viewX) * 3 * left, ((y - yUp - 6) - viewY) * 3);
            g2.rotate(Math.toRadians(leftArmAngle - 90), 4 * (left + 1), 2);
            g2.drawImage(bodyParts[2], 0, 0, null);
            g2.setTransform(previousAT);

            g2.translate((((x - 3 - viewX) + lengthdir_x(6 * left, leftArmAngle * left))) * left * 3,
                    (((y - yUp - 6) - this.lengthdir_y(6 * left, leftArmAngle * left)) - viewY) * 3);
            g2.rotate(Math.toRadians(leftArmAngle - 90), 4 * (left + 1), 2);
            g2.drawImage(bodyParts[3], 0, 0, null);
            g2.setTransform(previousAT);

            g2.translate((x + 9 - viewX) * 3 * left, ((y - yUp - 6) - viewY) * 3);
            g2.rotate(Math.toRadians(rightArmAngle - 90), 8 - left * 4, 4);
            g2.drawImage(bodyParts[2], 0, 0, null);
            g2.setTransform(previousAT);

            g2.translate((((x + 9 - viewX) + this.lengthdir_x(6 * left, rightArmAngle * left)) * 3) * left,
                    (((y - yUp - 6) - this.lengthdir_y(6 * left, rightArmAngle * left)) - viewY) * 3);
            g2.rotate(Math.toRadians(rightArmAngle - 90), 8 - left * 4, 4);
            g2.drawImage(bodyParts[3], 0, 0, null);
            g2.setTransform(previousAT);

            g2.drawImage(bodyParts[4], (x + 1 - viewX) * 3 * left, ((y - yUp + 7) - viewY) * 3, null);
            g2.drawImage(bodyParts[4], (x + 5 - viewX) * 3 * left, ((y - yUp + 7) - viewY) * 3, null);

            g2.translate((x + 5 - viewX) * 3 * left, ((y + 13) - viewY - yUp) * 3);
            g2.rotate(ffs);
            g2.drawImage(bodyParts[5], 0, 0, null);
            g2.setTransform(previousAT);

            g2.translate((x + 1 - viewX) * 3 * left, ((y + 13) - viewY - yUp) * 3);
            g2.rotate(-ffs);
            g2.drawImage(bodyParts[5], 0, 0, null);
            g2.setTransform(previousAT);
            g2.scale(left, 1);
        }
        g.setColor(myTeam ? Color.GREEN : Color.MAGENTA);
        g.drawString(username, ((x - (username.length())) - viewX) * 3, (y - 40 - viewY) * 3);
    }

    public double lengthdir_x(double R, double T) {
        return (R * Math.cos(T * Math.PI / 180));
    }

    public double lengthdir_y(double R, double T) {
        return (-R * Math.sin(T * Math.PI / 180));
    }

    public double pointDir(double x1, double y1, double x2, double y2) {
        return Math.toDegrees(Math.atan2(y2 - y1, x2 - x1));
    }

    public double pointDis(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    public boolean checkCollision(int px, int py) {
        playerHitbox.setLocation(x - playerHitbox.width / 2, y - (World.head + 10));
        return (playerHitbox.contains(px, py));
    }
}
