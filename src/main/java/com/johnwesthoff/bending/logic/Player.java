/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johnwesthoff.bending.logic;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import com.johnwesthoff.bending.Constants;
import com.johnwesthoff.bending.Session;
import com.johnwesthoff.bending.util.network.ResourceLoader;

/**
 * @author John
 */
public class Player {
    public int ID;
    public double x, y, move, vspeed, showx, showy;
    public boolean myTeam = false, done = false;
    public Rectangle playerHitbox;
    public String username = "Player";
    public int score = 0;
    public int floatiness = 0;
    public short status = 0, HP = 0;
    public Image[] bodyParts;// Body, head, ua, la, ul, ll
    public Thread loader;
    public boolean sameTeam;
    public byte[] partss;
    public int[] colorss, colorss2;

    public Player(int X, int Y, final byte[] parts, final int[] colors, final int[] colors2) {
        x = X;
        y = Y;
        showx = x;
        showy = y;
        playerHitbox = new Rectangle((int)x, (int)y, Constants.PLAYER_WIDTH, Constants.PLAYER_HEIGHT);
        this.partss = parts;
        this.colorss = colors;
        this.colorss2 = colors2;
        Runnable getStuff = () -> {
            bodyParts = new Image[partss.length];
            try {
                for (int i = 0; i < partss.length; i++) {
                    bodyParts[i] = ResourceLoader.loadImageNoHash(
                            "p" + (i + 1) + "_" + parts[i] + ".png",
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

    public void truePos() {
        /*
        if (x > showx) {
            showx += Constants.POSITION_CORRECTION;
            if (x < showx) {
                showx = x;
            }
        }
        if (x < showx) {
            showx -= Constants.POSITION_CORRECTION;
            if (x > showx) {
                showx = x;
            }
        }
        if (y > showy) {
            showy += Constants.POSITION_CORRECTION;
            if (y < showy) {
                showy = y;
            }
        }
        if (y < showy) {
            showy -= Constants.POSITION_CORRECTION;
            if (y > showy) {
                showy = y;
            }
        }
        */
        showx = x;
        showy = y;
    }

    public int left = 1;
    public double leftArmAngle = Constants.RIGHT_ANGLE, rightArmAngle = Constants.RIGHT_ANGLE;

    public void onDraw(Graphics g, int viewX, int viewY) {
        if ((status & Constants.ST_INVISIBLE) != 0) {
            return;
        }
        int offs = (int)showx % Constants.WALK_CYCLE;
        truePos();
        if (!done) {
            // showx+=move;
            g.drawArc((((int)showx - 2) - viewX) * Constants.MULTIPLIER, (((int)showy - 10) - viewY) * Constants.MULTIPLIER, 4, 4, 0, 360);
            g.drawLine((((int)showx) - viewX) * Constants.MULTIPLIER, (((int)showy - 6) - viewY) * Constants.MULTIPLIER,
                    (((int)showx) - viewX) * Constants.MULTIPLIER, (((int)showy - 3) - viewY) * Constants.MULTIPLIER);
            g.drawLine((((int)showx - 2) - viewX) * Constants.MULTIPLIER, ((int)(showy - 4) - viewY) * Constants.MULTIPLIER,
                    (((int)showx + 2) - viewX) * Constants.MULTIPLIER, (((int)showy - 4) - viewY) * Constants.MULTIPLIER);
            g.drawLine((((int)showx) - viewX) * Constants.MULTIPLIER, (((int)showy - 3) - viewY) * Constants.MULTIPLIER,
                    (((int)showx + offs - 2) - viewX) * Constants.MULTIPLIER, (((int)showy) - viewY) * Constants.MULTIPLIER);
            g.drawLine((((int)showx) - viewX) * Constants.MULTIPLIER, (((int)showy - 3) - viewY) * Constants.MULTIPLIER,
                    (((int)showx + 2 - offs) - viewX) * Constants.MULTIPLIER, (((int)showy) - viewY) * Constants.MULTIPLIER);
        } else {
            int yUp = 20;

            if (Integer.signum((int)move) == -1) {
                left = -1;
            }
            if (Integer.signum((int)move) == 1) {
                left = 1;
            }
            Graphics2D g2 = (Graphics2D) g;
            g2.scale(left, 1);
            g2.drawImage(bodyParts[0], ((int)showx - viewX) * Constants.MULTIPLIER * left + (left < 0 ? -18 : 0),
                    ((int)showy - yUp - 6 - viewY) * Constants.MULTIPLIER, null);
            g2.drawImage(bodyParts[1],
                    (int) ((int)showx + 2 - ((bodyParts[1].getWidth(null) - 23) / 5) - viewX) * Constants.MULTIPLIER * left
                            + (left < 0 ? -(6 + (bodyParts[1].getWidth(null) - 23)) : 0),
                    (((int)showy - yUp - 16 - ((bodyParts[1].getHeight(null) - 31)) / 3) - viewY) * Constants.MULTIPLIER, null);

            double ffs = Math.toRadians(((4 - offs) * 6));
            AffineTransform previousAT = g2.getTransform();
            g2.translate((showx - 3 - viewX) * Constants.MULTIPLIER * left, ((showy - yUp - 6) - viewY) * Constants.MULTIPLIER);
            g2.rotate(Math.toRadians(leftArmAngle - Constants.RIGHT_ANGLE), 4 * (left + 1), 2);
            g2.drawImage(bodyParts[2], 0, 0, null);
            g2.setTransform(previousAT);

            g2.translate((((showx - 3 - viewX) + lengthdir_x(6 * left, leftArmAngle * left))) * left * Constants.MULTIPLIER,
                    (((showy - yUp - 6) - this.lengthdir_y(6 * left, leftArmAngle * left)) - viewY) * Constants.MULTIPLIER);
            g2.rotate(Math.toRadians(leftArmAngle - Constants.RIGHT_ANGLE), 4 * (left + 1), 2);
            g2.drawImage(bodyParts[3], 0, 0, null);
            g2.setTransform(previousAT);

            g2.translate((showx + 9 - viewX) * Constants.MULTIPLIER * left, ((showy - yUp - 6) - viewY) * Constants.MULTIPLIER);
            g2.rotate(Math.toRadians(rightArmAngle - Constants.RIGHT_ANGLE), 8 - left * 4, 4);
            g2.drawImage(bodyParts[2], 0, 0, null);
            g2.setTransform(previousAT);

            g2.translate((((showx + 9 - viewX) + this.lengthdir_x(6 * left, rightArmAngle * left)) * Constants.MULTIPLIER) * left,
                    (((showy - yUp - 6) - this.lengthdir_y(6 * left, rightArmAngle * left)) - viewY) * Constants.MULTIPLIER);
            g2.rotate(Math.toRadians(rightArmAngle - Constants.RIGHT_ANGLE), 8 - left * 4, 4);
            g2.drawImage(bodyParts[3], 0, 0, null);
            g2.setTransform(previousAT);

            g2.drawImage(bodyParts[4], ((int)showx + 1 - viewX) * Constants.MULTIPLIER * left,
                    (((int)showy - yUp + 7) - viewY) * Constants.MULTIPLIER, null);
            g2.drawImage(bodyParts[4], ((int)showx + 5 - viewX) * Constants.MULTIPLIER * left,
                    (((int)showy - yUp + 7) - viewY) * Constants.MULTIPLIER, null);

            g2.translate((showx + 5 - viewX) * Constants.MULTIPLIER * left, ((showy + 13) - viewY - yUp) * Constants.MULTIPLIER);
            g2.rotate(ffs);
            g2.drawImage(bodyParts[5], 0, 0, null);
            g2.setTransform(previousAT);

            g2.translate((showx + 1 - viewX) * Constants.MULTIPLIER * left, ((showy + 13) - viewY - yUp) * Constants.MULTIPLIER);
            g2.rotate(-ffs);
            g2.drawImage(bodyParts[5], 0, 0, null);
            g2.setTransform(previousAT);
            g2.scale(left, 1);
        }
        g.setColor(myTeam ? Color.GREEN : Color.MAGENTA);
        g.drawString(username, (((int)showx - (username.length())) - viewX) * Constants.MULTIPLIER, ((int)showy - 40 - viewY) * Constants.MULTIPLIER);
        if (((status & Constants.ST_FLAMING)) != 0) {
            World.drawFire(g, ((int)x + 4 - viewX) * Constants.WIDTH_SCALE, ((int)y - viewY) * Constants.HEIGHT_SCALE);
        }
        if (((status & Constants.ST_DRAIN)) != 0) {
            g.setColor(Color.BLACK);
            g.drawArc(((int)x - viewX - (Constants.AURA_RADIUS / 2)) * Constants.WIDTH_SCALE,
                    ((int)y - viewY - (Constants.AURA_RADIUS)) * Constants.WIDTH_SCALE, Constants.AURA_RADIUS * Constants.HEIGHT_SCALE,
                    Constants.AURA_RADIUS * Constants.HEIGHT_SCALE, Session.getInstance().random.nextInt(360), Session.getInstance().random.nextInt(90));
        }
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
        playerHitbox.setLocation((int)x - playerHitbox.width / 2, (int)y - (Constants.HEAD + 10));
        return (playerHitbox.contains(px, py));
    }
}
