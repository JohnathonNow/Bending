package Entity;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import destruct.Player;
import destruct.Server;
import destruct.World;
import java.awt.Color;
import java.awt.Graphics;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author John
 */
public class MissileEntity extends Entity{
    public int maker = 0;
    public int radius = 16;
    public MissileEntity(int x, int y, int hspeed, int vspeed, int ma)
    {
        X = x;
        Y = y;
        xspeed = hspeed;
        yspeed = vspeed;
        maker = ma;
    }
    @Override
    public void onDraw(Graphics G, int viewX, int viewY) {
        if (X>viewX&&X<viewX+300&&Y>viewY&&Y<viewY+300)
        {
            G.setColor(Color.white);
            
            int deg = r.nextInt(360);
            G.fillArc((int)(X-1)-viewX, (int)(Y-1)-viewY, 2, 2, deg, 60);
            deg = r.nextInt(360);
            G.fillArc((int)(X-2)-viewX, (int)(Y-2)-viewY, 4, 4, deg, 60);
            deg = r.nextInt(360);
            G.fillArc((int)(X-3)-viewX, (int)(Y-3)-viewY, 6, 6, deg, 60);
            deg = r.nextInt(360);
            G.fillArc((int)(X-4)-viewX, (int)(Y-4)-viewY, 8, 8, deg, 60);
        }
    }

    @Override
    public void onUpdate(World apples) {
       if (!apples.inBounds(X, Y)||apples.checkCollision(X, Y))
       {
           alive = false;
           //apples.explode(X, Y, 32, 8, 16);
       }
       for (Player p:apples.playerList)
       {
           if (maker!=p.ID&&p.checkCollision((int)X, (int)Y))
           {
               alive = false;
           }
       }
       /*if (yspeed<12)
       {
           yspeed++;
       }*/
    }

    @Override
    public void cerealize(ByteBuffer out) {
        try {
            Server.putString(out,  this.getClass().getName());
            out.putInt((int)X);
            out.putInt((int)Y);
            out.putInt((int)xspeed);
            out.putInt((int)yspeed);
            out.putInt(maker);
        } catch (Exception ex) {
            Logger.getLogger(ExplosionEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static void reconstruct(ByteBuffer in, World world) {
        try {
            world.entityList.add(new MissileEntity(in.getInt(),in.getInt(),in.getInt(),in.getInt(),in.getInt()));
        } catch (Exception ex) {
            Logger.getLogger(MissileEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
