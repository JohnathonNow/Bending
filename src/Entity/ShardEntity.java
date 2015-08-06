package Entity;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import destruct.APPLET;
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
public class ShardEntity extends Entity{
//    public int maker = 0;
    public int radius = 16;
    public int gravity = 0;
    public ShardEntity(int x, int y, int hspeed, int vspeed, int ma)
    {
        X = x;
        Y = y;
        xspeed = hspeed/2;
        yspeed = vspeed/2;
        maker = ma;
    }
    @Override
    public void onDraw(Graphics G, int viewX, int viewY) {
        if (X>viewX&&X<viewX+300&&Y>viewY&&Y<viewY+300)
        {
            G.setColor(Color.DARK_GRAY);
            G.drawLine((int)(X-xspeed)-viewX, (int)(Y-yspeed)-viewY, (int)X-viewX, (int)Y-viewY);
            
        }
    }

    @Override
    public void onUpdate(World apples) {
       if ((apples.isSolid(X, Y)))
       {
          // apples.ground.FillCircleW(X, Y, radius, World.STONE);
           alive = false;
           //apples.explode(X, Y, 32, 8, 16);
       }
       for (Player p:apples.playerList)
       {
           if (apples.pointDis(X, Y, p.x, p.y)<radius&&maker!=p.ID)
           {
               alive = false;
           }
       }
       yspeed+=gravity;
       
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
            world.entityList.add(new ShardEntity(in.getInt(),in.getInt(),in.getInt(),in.getInt(),in.getInt()));
        } catch (Exception ex) {
            Logger.getLogger(ShardEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @Override
public void onServerUpdate(Server lol)
{
    if (collided(lol.earth))
       {
           radius*=3;
           lol.earth.ground.ClearCircle((int)X, (int)Y, radius);
           lol.sendMessage(Server.DIG, ByteBuffer.allocate(40).putInt((int)X).putInt((int)Y).putInt(radius));
           alive = false;
       }
}
private boolean collided(World w)
    {
        double direction = APPLET.pointDir(previousX, previousY, X, Y);
        int speed = (int)APPLET.pointDis(previousX, previousY, X, Y);
        for (int i = 0; i < speed; i++)
        {
            if (w.isSolid(X+(int)APPLET.lengthdir_x(i,direction),Y+(int)APPLET.lengthdir_y(i,direction)))
            {
                X = X+(int)APPLET.lengthdir_x(i,direction);
                Y = Y+(int)APPLET.lengthdir_y(i,direction);
                return true;
            }
        }
        return false;
    }
}
