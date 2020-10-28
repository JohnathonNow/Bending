/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johnwesthoff.bending.logic;

import com.johnwesthoff.bending.Constants;
import com.johnwesthoff.bending.util.network.ResourceLoader;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * @author John
 */
public class Player {
    public int x, y, ID, move, vspeed;
    public boolean myTeam = false, done = false;
    public Rectangle playerHitbox;
    public String username = "Player";
    public int score = 0;
    public short status = 0, HP = 0;
    public Image[] bodyParts;// Body, head, ua, la, ul, ll
    public Thread loader;
    public boolean sameTeam;
    public byte[] partss;
    public int[] colorss, colorss2;

    public Player(int X, int Y, final byte[] parts, final int[] colors, final int[] colors2) {
        x = X;
        y = Y;
        playerHitbox = new Rectangle(x, y, Constants.PLAYER_WIDTH, Constants.PLAYER_HEIGHT);
        this.partss = parts;
        this.colorss = colors;
        this.colorss2 = colors2;
        Runnable getStuff = () -> {
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
        };
        loader = new Thread(getStuff);
        loader.start();
    }

    public int left = 1;
    public double leftArmAngle = Constants.RIGHT_ANGLE, rightArmAngle = Constants.RIGHT_ANGLE;

    public void onDraw(Graphics g, int viewX, int viewY) {
        if ((status & Constants.ST_INVISIBLE) != 0) {
            return;
        }
        int offs = x % Constants.WALK_CYCLE;
        if (!done) {
            // x+=move;
            g.drawArc(((x - 2) - viewX) * Constants.MULTIPLIER, ((y - 10) - viewY) * Constants.MULTIPLIER, 4, 4, 0, 360);
            g.drawLine(((x) - viewX) * Constants.MULTIPLIER, ((y - 6) - viewY) * Constants.MULTIPLIER,
                    ((x) - viewX) * Constants.MULTIPLIER, ((y - 3) - viewY) * Constants.MULTIPLIER);
            g.drawLine(((x - 2) - viewX) * Constants.MULTIPLIER, ((y - 4) - viewY) * Constants.MULTIPLIER,
                    ((x + 2) - viewX) * Constants.MULTIPLIER, ((y - 4) - viewY) * Constants.MULTIPLIER);
            g.drawLine(((x) - viewX) * Constants.MULTIPLIER, ((y - 3) - viewY) * Constants.MULTIPLIER,
                    ((x + offs - 2) - viewX) * Constants.MULTIPLIER, ((y) - viewY) * Constants.MULTIPLIER);
            g.drawLine(((x) - viewX) * Constants.MULTIPLIER, ((y - 3) - viewY) * Constants.MULTIPLIER,
                    ((x + 2 - offs) - viewX) * Constants.MULTIPLIER, ((y) - viewY) * Constants.MULTIPLIER);
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
            g2.drawImage(bodyParts[0], (x - viewX) * Constants.MULTIPLIER * left + (left < 0 ? -18 : 0),
                    (y - yUp - 6 - viewY) * Constants.MULTIPLIER, null);
            g2.drawImage(bodyParts[1],
                    (int) (x + 2 - ((bodyParts[1].getWidth(null) - 23) / 5) - viewX) * Constants.MULTIPLIER * left
                            + (left < 0 ? -(6 + (bodyParts[1].getWidth(null) - 23)) : 0),
                    ((y - yUp - 16 - ((bodyParts[1].getHeight(null) - 31)) / 3) - viewY) * Constants.MULTIPLIER, null);

            double ffs = Math.toRadians(((4 - offs) * 6));
            AffineTransform previousAT = g2.getTransform();
            g2.translate((x - 3 - viewX) * Constants.MULTIPLIER * left, ((y - yUp - 6) - viewY) * Constants.MULTIPLIER);
            g2.rotate(Math.toRadians(leftArmAngle - Constants.RIGHT_ANGLE), 4 * (left + 1), 2);
            g2.drawImage(bodyParts[2], 0, 0, null);
            g2.setTransform(previousAT);

            g2.translate((((x - 3 - viewX) + lengthdir_x(6 * left, leftArmAngle * left))) * left * Constants.MULTIPLIER,
                    (((y - yUp - 6) - this.lengthdir_y(6 * left, leftArmAngle * left)) - viewY) * Constants.MULTIPLIER);
            g2.rotate(Math.toRadians(leftArmAngle - Constants.RIGHT_ANGLE), 4 * (left + 1), 2);
            g2.drawImage(bodyParts[3], 0, 0, null);
            g2.setTransform(previousAT);

            g2.translate((x + 9 - viewX) * Constants.MULTIPLIER * left, ((y - yUp - 6) - viewY) * Constants.MULTIPLIER);
            g2.rotate(Math.toRadians(rightArmAngle - Constants.RIGHT_ANGLE), 8 - left * 4, 4);
            g2.drawImage(bodyParts[2], 0, 0, null);
            g2.setTransform(previousAT);

            g2.translate((((x + 9 - viewX) + this.lengthdir_x(6 * left, rightArmAngle * left)) * Constants.MULTIPLIER) * left,
                    (((y - yUp - 6) - this.lengthdir_y(6 * left, rightArmAngle * left)) - viewY) * Constants.MULTIPLIER);
            g2.rotate(Math.toRadians(rightArmAngle - Constants.RIGHT_ANGLE), 8 - left * 4, 4);
            g2.drawImage(bodyParts[3], 0, 0, null);
            g2.setTransform(previousAT);

            g2.drawImage(bodyParts[4], (x + 1 - viewX) * Constants.MULTIPLIER * left,
                    ((y - yUp + 7) - viewY) * Constants.MULTIPLIER, null);
            g2.drawImage(bodyParts[4], (x + 5 - viewX) * Constants.MULTIPLIER * left,
                    ((y - yUp + 7) - viewY) * Constants.MULTIPLIER, null);

            g2.translate((x + 5 - viewX) * Constants.MULTIPLIER * left, ((y + 13) - viewY - yUp) * Constants.MULTIPLIER);
            g2.rotate(ffs);
            g2.drawImage(bodyParts[5], 0, 0, null);
            g2.setTransform(previousAT);

            g2.translate((x + 1 - viewX) * Constants.MULTIPLIER * left, ((y + 13) - viewY - yUp) * Constants.MULTIPLIER);
            g2.rotate(-ffs);
            g2.drawImage(bodyParts[5], 0, 0, null);
            g2.setTransform(previousAT);
            g2.scale(left, 1);
        }
        g.setColor(myTeam ? Color.GREEN : Color.MAGENTA);
        g.drawString(username, ((x - (username.length())) - viewX) * Constants.MULTIPLIER, (y - 40 - viewY) * Constants.MULTIPLIER);
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
        playerHitbox.setLocation(x - playerHitbox.width / 2, y - (Constants.HEAD + 10));
        return (playerHitbox.contains(px, py));
    }
}
