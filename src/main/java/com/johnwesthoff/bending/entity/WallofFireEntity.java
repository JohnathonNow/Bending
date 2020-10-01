package com.johnwesthoff.bending.entity;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import com.johnwesthoff.bending.Client;
import com.johnwesthoff.bending.Constants;
import com.johnwesthoff.bending.Server;
import com.johnwesthoff.bending.logic.World;

import java.awt.*;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author John
 */
public class WallofFireEntity extends Entity {
    // public int maker = 0;
    public int height = 30;
    public int width = 2;
    public int gravity = 0;
    public Rectangle hitBox;
    int life = 0;

    public WallofFireEntity(int x, int y, int hspeed, int vspeed, int ma) {
        X = x;
        Y = y;
        xspeed = hspeed;
        yspeed = vspeed;
        maker = ma;
        hitBox = new Rectangle(0, 0, width, height);
    }

    public boolean checkCollision(Rectangle r) {
        hitBox.setLocation((int) X - 1, (int) Y);
        return hitBox.intersects(r);
    }

    @Override
    public void onDraw(Graphics G, int viewX, int viewY) {
        if (X > viewX && X < viewX + Constants.WIDTH_INT && Y > viewY && Y < viewY + Constants.HEIGHT_INT) {
            Graphics2D g = (Graphics2D) G;
            Composite c = g.getComposite();
            // g.setComposite(new Additive());
            g.setColor(new Color(255, r.nextInt(255), 0, r.nextInt(255)));
            g.fillArc(((int) X - 6) - viewX, (int) (Y - 6) - viewY, 12, 12, 0, 360);
            for (int y = 0; y < 30; y += 3) {
                for (int i = 0; i < 4; i++) {
                    int e1 = 6 - r.nextInt(12), e2 = 6 - r.nextInt(12);
                    g.setColor(new Color(255, r.nextInt(255), 0, r.nextInt(255)));
                    g.fillArc((int) (X + e1) - viewX, (int) (Y + e2 + y) - viewY, e1, e2, 0, 360);
                }
            }
            g.setComposite(c);
        }
    }

    int next = 0;

    @Override
    public void onUpdate(World apples) {
        if (!apples.inBounds(X, Y) || apples.checkCollision(X, Y) || life++ > 7) {

            alive = false;
            // apples.explode(X, Y, 32, 8, 16);
        }
        if (next++ > 2) {
            next = 0;
            yspeed += gravity;
        }
        /*
         * if (yspeed<12) { yspeed++; }
         */
    }

    @Override
    public void onServerUpdate(Server lol) {
    }

    @Override
    public void checkAndHandleCollision(Client client) {

        client.checkCollision(X, Y);// Just to move the hitbox so when it is passed, it works
        // pointDis(me3.X, me3.Y, world.x, world.y)<me3.height
        if (checkCollision(client.playerHitbox) && maker != client.ID
                && (client.gameMode <= 0 || client.badTeam.contains(maker))) {
            client.hurt(35);
            alive = false;
            client.lastHit = maker;
            client.world.vspeed -= 8;
            client.xspeed += 9 - client.random.nextInt(18);
            client.world.status |= World.ST_FLAMING;
            client.killMessage = "~ smelled `'s armpits, and then died.";
        }
    }

    @Override
    public void cerealize(ByteBuffer out) {
        try {
            Server.putString(out, this.getClass().getName());
            out.putInt((int) X);
            out.putInt((int) Y);
            out.putInt((int) xspeed);
            out.putInt((int) yspeed);
            out.putInt(maker);
        } catch (Exception ex) {
            Logger.getLogger(ExplosionEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void reconstruct(ByteBuffer in, World world) {
        try {
            world.entityList.add(new WallofFireEntity(in.getInt(), in.getInt(), in.getInt(), in.getInt(), in.getInt()));
        } catch (Exception ex) {
            Logger.getLogger(WallofFireEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
