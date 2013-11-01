package Entity;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import static Entity.Entity.r;
import destruct.APPLET;
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
public class RodEntity extends Entity{
    public int maker = 0;
    public int radius = 16;
    public RodEntity(int x, int y, int hspeed, int ma)
    {
        X = x;
        Y = y;
        xspeed = hspeed;
        maker = ma;
    }
    @Override
    public void onDraw(Graphics G, int viewX, int viewY) {
        if (X>viewX&&X<viewX+300&&Y>viewY&&Y<viewY+300)
        {
            G.setColor(Color.gray);
            G.fillRect(X-2-viewX, Y-32-viewY, 4, 32);
            G.setColor(Color.darkGray);
            G.fillRect(X-16-viewX, Y-4-viewY, 32, 4);
            G.setColor(Color.lightGray);
            G.fillArc(X-4-viewX, Y-36-viewY-4, 8, 8, 0, 360);
            G.setColor(Color.black);
            G.drawRect(X-2-viewX, Y-32-viewY, 4, 32);
            G.drawRect(X-16-viewX, Y-4-viewY, 32, 4);
            G.drawArc(X-4-viewX, Y-36-viewY-4, 8, 8, 0, 360);
            G.setColor(Color.YELLOW);
            int dir = r.nextInt(360), dis = 8+r.nextInt(16);
            int xx = X + (int)APPLET.lengthdir_x(dis,dir)-viewX, yy = Y +  (int)APPLET.lengthdir_y(dis, dir) - 34-viewY;
            dir = r.nextInt(360); dis = 8+r.nextInt(16);
            int xxx = xx + (int)APPLET.lengthdir_x(dis,dir), yyy = yy +  (int)APPLET.lengthdir_y(dis, dir);
            G.drawLine(X-viewX, Y-34-viewY, xx, yy);
            G.drawLine(xx, yy, xxx, yyy);
        }
    }
    public int life = 300;
    @Override
    public void onUpdate(World apples) {
       if (!apples.inBounds(X, Y)||life--<0)
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
            }
       }
       if (apples.x>X-48&&apples.x<X+48)
       {
          if (apples.y>Y-48&&apples.y<Y+48)
          {
              apples.status |= World.ST_SHOCKED;
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
            out.putInt(X);
            out.putInt(Y);
            out.putInt(xspeed);
            out.putInt(maker);
        } catch (Exception ex) {
            Logger.getLogger(ExplosionEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static void reconstruct(ByteBuffer in, World world) {
        try {
            world.entityList.add(new RodEntity(in.getInt(),in.getInt(),in.getInt(),in.getInt()));
        } catch (Exception ex) {
            Logger.getLogger(RodEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
