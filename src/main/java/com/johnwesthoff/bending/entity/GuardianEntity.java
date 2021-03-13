package com.johnwesthoff.bending.entity;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import com.johnwesthoff.bending.Constants;
import com.johnwesthoff.bending.Server;
import com.johnwesthoff.bending.logic.PlayerOnline;
import com.johnwesthoff.bending.logic.World;
import com.johnwesthoff.bending.networking.handlers.AiEvent;
import com.johnwesthoff.bending.networking.handlers.DestroyEvent;
import com.johnwesthoff.bending.networking.handlers.MessageEvent;
import com.johnwesthoff.bending.util.network.ResourceLoader;


public class GuardianEntity extends EnemyEntity {
    static BufferedImage sprite;

    public GuardianEntity(int x, int y, int hspeed, int vspeed, int ma) {
        super(x, y, hspeed, vspeed, ma);
        HP = 5000;
        sprite = (ResourceLoader.loadImage("guardian.png"));
    }

    @Override
    public void drawOverlay(Graphics G, int viewX, int viewY) {
        G.setColor(Color.black);
        G.drawRect(64, 64, 900 - 128, 4);
        G.setColor(Color.red);
        G.fillRect(64, 64, (int) ((900f) * (HP / 5000f)) - 128, 4);
        if (X > viewX && X < viewX + Constants.WIDTH_INT && Y > viewY && Y < viewY + Constants.HEIGHT_INT) {
            // G.setColor(Color.RED);
            // G.fillArc(((int)X-30)-viewX, ((int)Y-30)-viewY, 60, 60, 0, 360);
            // G.setColor(Color.black);
            // G.drawArc(((int)X-30)-viewX, ((int)Y-30)-viewY, 60, 60, 0, (360*HP)/500);
            G.drawImage(sprite, (int) ((drawX - viewX) * 3f) - 32, (int) ((drawY - viewY) * 3f) - 100, null);
        }
        // System.out.println("HI!");
    }

    @Override
    public void onUpdate(World apples) {
        if (!apples.isSolid(X, Y - 40)) {
            Y -= 40;
            if (apples.inBounds(X + move, Y + yspeed)) {
                float toMove = move, XXX1 = X + 3, YYY1 = Y - 4, XXX2 = X - 3, YYY2 = Y - 4;
                while (true) {
                    YYY1 += 1;
                    if (!apples.inBounds(XXX1, YYY1)) {
                        break;
                    }
                    if (apples.isSolid(XXX1, YYY1)) {
                        break;
                    }
                }
                while (true) {
                    YYY2 += 1;
                    if (!apples.inBounds(XXX2, YYY2)) {
                        break;
                    }
                    if (apples.isSolid(XXX2, YYY2)) {
                        break;
                    }
                }
                X += !apples.isSolid(X + toMove, Y + yspeed) ? move : 0;
            }
            for (int i = 0; i < 40; i++) {
                if (Y > 0 && apples.isSolid(X, Y + 1)) {
                    break;
                }
                Y += 1;
            }
        }
        if (!apples.isSolid(X, Y + 4)) {
            yspeed = Math.min(4, yspeed + 1);
        } else {
            yspeed = Math.min(0, yspeed);
        }
        if (!apples.isSolid(X, Y + yspeed)) {
            Y += yspeed;
        } else {
            yspeed = 0;
        }
        drawX += (X - drawX) / 2;
        drawY += (Y - drawY) / 2;
    }

    @Override
    public void onServerUpdate(Server handle) {
        if (HP <= 0) {
            handle.sendMessage(DestroyEvent.getPacket(this));
            this.setAlive(false);
            if (Server.gameMode == Server.DEFENDER) {
                PlayerOnline P = handle.getPlayer(lastHit);
                if (P != null) {
                    if (handle.team1.contains(P.ID)) {
                        P.score = -10000;
                    } else {
                        P.score = 10000;
                    }
                    String yes = P.username + " has killed the earthbender's guardian!";
                    handle.sendMessage(MessageEvent.getPacket(0xFF00FF, yes));
                    handle.expander.interrupt();
                } else {
                    for (int p : handle.team1) {
                        (handle.getPlayer(p)).score = -10000;
                    }
                    String yes = "The earthbenders have failed to protect their guardian!";
                    handle.sendMessage(MessageEvent.getPacket(0xFF00FF, yes));
                    handle.expander.interrupt();
                }
            }

        }
        if (!handle.earth.isType((int) X, (int) Y, Constants.AIR)) {
            if (air-- < 0) {
                HP -= 2;
            }
        } else {
            air = 100;
        }
        if (handle.earth.isType((int) X, (int) Y, Constants.LAVA)) {
            HP -= 2;
        }
        if (timer++ > 90) {
            // System.out.println(X);
            handle.sendMessage(AiEvent.getPacket(this));
            timer = 0;
        }
    }

    /**
     * Reconstruct the guardian entity
     * 
     * @param in
     * @param world World in which the entity should be reconstructed
     */
    public static void reconstruct(ByteBuffer in, World world) {
        // System.out.println("IM BACK!");
        world.entityList.add(new GuardianEntity(in.getInt(), in.getInt(), in.getInt(), in.getInt(), in.getInt())
                .addStuff(in.getInt(), in.getInt()));

    }
}
