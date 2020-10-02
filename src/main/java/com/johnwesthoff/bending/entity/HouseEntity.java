package com.johnwesthoff.bending.entity;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.johnwesthoff.bending.Server;
import com.johnwesthoff.bending.logic.World;

/**
 *
 * @author John
 */
public class HouseEntity extends Entity {
    Polygon P = new Polygon();
    public int houseW = 20, houseH = 20, houseX = 450 - houseW, houseY = 150 - houseH;
    Boolean chimney = false;
    Color wall = Color.DARK_GRAY, roof = Color.RED, door = Color.RED, window = Color.CYAN;
    boolean done = false;

    public HouseEntity setLook(Color roof, Color wall, Color door, Color window, boolean chimney) {
        this.roof = roof;
        this.wall = wall;
        this.door = door;
        this.chimney = chimney;
        this.window = window;
        setPoints();
        return this;
    }

    public HouseEntity(int x, int y, int w, int h) {
        X = x;
        Y = y;
        houseW = w;
        houseH = h;
        houseX = (int) X - houseW;
        houseY = (int) Y - houseH;
        xspeed = 0;
        yspeed = 0;
        setPoints();

    }

    public final void setPoints() {
        P.reset();
        P.addPoint(houseX, houseY);
        P.addPoint(houseX + (houseW / 2), houseY - (houseH / 2));
        /*
         * if (chimney) { P.addPoint((int)(houseX+(houseW/1.5)),
         * (int)(houseY-(houseH/2.2))); P.addPoint((int)(houseX+(houseW/1.5)),
         * (int)(houseY-(houseH/2))); P.addPoint((int)(houseX+(houseW/1.3)),
         * (int)(houseY-(houseH/2))); }
         */
        P.addPoint(houseX + houseW, houseY);
        P.addPoint(houseX + houseW, houseY + houseH);
        P.addPoint(houseX, houseY + houseH);
    }

    @Override
    public void onDraw(Graphics G, int viewX, int viewY) {
        if (X > viewX - houseW && X < viewX + houseW + 300 && Y > viewY - (houseH) && Y < viewY + houseH + 300) {
            if (chimney) {
                G.setColor(Color.black);
                G.fillRect((int) (houseX + (houseW / 1.5)) - viewX, (int) (houseY - (houseH / 2.2)) - viewY,
                        (int) (houseW * .2), houseH / 2);
            }
            G.setColor(roof);
            P.translate(-viewX, -viewY);
            G.fillPolygon(P);
            P.translate(viewX, viewY);
            G.setColor(Color.black);
            // G.fillRect((int)(houseX+(houseW/1.5))-viewX,
            // (int)(houseY-(houseH/2.2))-viewY,(int)(houseW*.2),houseH/2);
            G.drawRect(houseX - viewX, houseY - viewY, houseW, houseH);
            G.drawLine(houseX - viewX, houseY - viewY, houseX + (houseW / 2) - viewX, houseY - (viewY + (houseH / 2)));
            G.drawLine(houseX + houseW - viewX, houseY - viewY, houseX + (houseW / 2) - viewX,
                    houseY - (viewY + (houseH / 2)));
            G.setColor(wall);
            G.fillRect(houseX + 1 - viewX, houseY + 1 - viewY, houseW - 1, houseH - 1);
            G.setColor(door);
            G.fillRect(houseX + (houseW / 6) - viewX, houseY + houseH / 2 - viewY, (houseW / 9), houseH / 2);
            G.setColor(window);
            G.fillRect((int) (houseX + (houseW / 1.5) - viewX), houseY + houseH / 6 - viewY, (houseW / 4), houseH / 3);
        }
    }

    @Override
    public void onUpdate(World apples) {
        apples.ground.fillPolygon(P, (byte) 1);
    }

    @Override
    public void cerealize(ByteBuffer out) {
        try {
            Server.putString(out, this.getClass().getName());
            out.putInt(houseX + houseW);
            out.putInt(houseY + houseH);
            out.putInt(houseW);
            out.putInt(houseH);
            out.putInt(roof.getRGB());
            out.putInt(wall.getRGB());
            out.putInt(door.getRGB());
            out.putInt(window.getRGB());
            out.putInt(chimney == true ? 1 : 0);
        } catch (Exception ex) {
            Logger.getLogger(ExplosionEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void reconstruct(ByteBuffer in, World world) {
        try {
            HouseEntity toAdd = new HouseEntity(in.getInt(), in.getInt(), in.getInt(), in.getInt()).setLook(
                    new Color(in.getInt()), new Color(in.getInt()), new Color(in.getInt()), new Color(in.getInt()),
                    in.getInt() == 1 ? true : false);
            world.entityList.add(toAdd);
            toAdd.setPoints();
        } catch (Exception ex) {
            Logger.getLogger(HouseEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}