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
public class IceShardEntity extends Entity{
    public int maker = 0;
    public int radius = 16;
    public int gravity = 0;
    public IceShardEntity(int x, int y, int hspeed, int vspeed, int ma)
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
            G.setColor(Color.CYAN);
            G.drawLine((X-xspeed)-viewX, (Y-yspeed)-viewY, X-viewX, Y-viewY);
            
        }
    }

    @Override
    public void onUpdate(World apples) {
       if (collided(apples))
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
            out.putInt(X);
            out.putInt(Y);
            out.putInt(xspeed);
            out.putInt(yspeed);
            out.putInt(maker);
        } catch (Exception ex) {
            Logger.getLogger(ExplosionEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static void reconstruct(ByteBuffer in, World world) {
        try {
            world.entityList.add(new IceShardEntity(in.getInt(),in.getInt(),in.getInt(),in.getInt(),in.getInt()));
        } catch (Exception ex) {
            Logger.getLogger(IceShardEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    int time = 0;
    @Override
public void onServerUpdate(Server lol)
{
    if (lol.earth.checkCollision(X, Y))
       {
           radius=16;
           lol.earth.ground.FillCircleW(X, Y, 54, World.ICE);
           lol.sendMessage(Server.FILL, ByteBuffer.allocate(40).putInt(X).putInt(Y).putInt(54).put(World.ICE));
           alive = false;
       }
    if (time++>1)
    {
        time = 0;
         lol.earth.ground.freeze(previousX-(xspeed*4), previousY-(yspeed*4), 54);
         lol.sendMessage(Server.FREEZE, ByteBuffer.allocate(40).putInt(previousX-(xspeed*4)).putInt(previousY-(yspeed*4)).putInt(54));
    }
}
    
    private boolean collided(World w)
    {
        double direction = APPLET.pointDir(previousX, previousY, X, Y);
        int speed = (int)APPLET.pointDis(previousX, previousY, X, Y);
        for (int i = 0; i <= speed; i++)
        {
            if (w.checkCollision(X+(int)APPLET.lengthdir_x(i,direction),Y+(int)APPLET.lengthdir_y(i,direction)))
            {
                X = X+(int)APPLET.lengthdir_x(i,direction);
                Y = Y+(int)APPLET.lengthdir_y(i,direction);
                return true;
            }
        }
        return false;
    }
}
