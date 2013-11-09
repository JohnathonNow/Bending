package Entity;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import destruct.APPLET;
import destruct.Server;
import destruct.World;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author John
 */
public class PumpkinEntity extends Entity{
    public int maker = 0;
    public int radius = 16;
    public static Color brown = new Color(0xCC6600);
    public PumpkinEntity(int x, int y)
    {
        X = x;
        Y = y;      
        
    }
    boolean near = false;
    @Override
    public void onDraw(Graphics G, int viewX, int viewY) {
        if (X>viewX&&X<viewX+300&&Y>viewY&&Y<viewY+300)
        {
            G.setColor(Color.ORANGE);
                G.fillArc((int)(X-16)-viewX, (int)(Y-10)-viewY, 32, 20, 0, 360);
            G.setColor(Color.BLACK);
                G.drawArc((int)(X-16)-viewX, (int)(Y-10)-viewY, 32, 20, 0, 360);
                G.drawArc((int)(X-13)-viewX, (int)(Y-10)-viewY, 26, 20, 0, 360);
                G.drawArc((int)(X-10)-viewX, (int)(Y-10)-viewY, 20, 20, 0, 360);
                G.drawArc((int)(X-6)-viewX, (int)(Y-10)-viewY, 12, 20, 0, 360);
                G.drawArc((int)(X-3)-viewX, (int)(Y-12)-viewY, 6, 4, 0, 360);
                G.drawRect((int)(X-2)-viewX, (int)(Y-14)-viewY, 4, 6);
                G.drawArc(((int)X-2)-viewX, (int)(Y-16)-viewY, 4, 3, 0, 360);
            G.setColor(brown);
                G.fillArc((int)(X-3)-viewX, (int)(Y-11)-viewY, 7, 3, 0, 360);
                G.fillRect((int)(X-1)-viewX, (int)(Y-13)-viewY, 3, 3);
                G.fillArc((int)(X-2)-viewX, (int)(Y-16)-viewY, 4, 3, 0, 360);
                //G.drawArc((X-3)-viewX, (Y-10)-viewY, 6, 20, 0, 360);
               if (near)
               {
                   G.setColor(Color.BLACK);
                   G.drawString("PRESS C TO UNLOCK", (int)X-(G.getFontMetrics().stringWidth("PRESS C TO UNLOCK")/2)-viewX, (int)Y-32-viewY);
               }
        }
    }
    boolean down = false;
    public PumpkinEntity floor(World world)
    {
        int tooMuch = 0;
        while (!world.isSolid(X, Y+1))
        {
            Y++;
            if (tooMuch++>1024)
            {
                down = true;
                return this;
            }
        }
        down = true;
        return this;
    }
    public int life = 256;
    @Override
    public void onUpdate(World apples) {
       if (!apples.inBounds(X, Y))
       {
           alive = false;
           //apples.explode(X, Y, 32, 8, 16);
       }
       if (!apples.isSolid(X, Y-10))
       {
           Y-=10;
       }
       for (int i = 1; i < 10; i++)
       {
            if (!apples.isSolid(X, Y+i))
            {
                Y+=i;
                //life-=i/2;
            }
       }
       near = false;
       if (Math.abs(apples.x-X)<64)
       {
        if (Math.abs(apples.y-Y)<64)
        {
            near = true;
            if (apples.keys[KeyEvent.VK_C])
            {
                alive = false;
                APPLET.unlocks.set(0, 1, true);
                APPLET.CTD.postRSSfeed(APPLET.username + " found a rare item!","A Viking Helmet!");
            }
        }
       }
       /*if (yspeed<12)
       {
           yspeed++;
       }*/
    }
    @Override
    public void onServerUpdate(Server apples) {
        if (life<0)
        {
            alive = false;
            apples.sendMessage(Server.DESTROY, ByteBuffer.allocate(30).putInt(MYID));
        }
    }
    @Override
    public void cerealize(ByteBuffer out) {
        try {
            Server.putString(out,  this.getClass().getName());
            out.putInt((int)X);
            out.putInt((int)Y);
        } catch (Exception ex) {
            Logger.getLogger(ExplosionEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static void reconstruct(ByteBuffer in, World world) {
        try {
            world.entityList.add(new PumpkinEntity(in.getInt(),in.getInt()));
        } catch (Exception ex) {
            Logger.getLogger(PumpkinEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}