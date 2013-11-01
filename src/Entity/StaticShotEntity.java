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
public class StaticShotEntity extends Entity{
    public int maker = 0;
    public int radius = 0;
    public StaticShotEntity(int x, int y, int hspeed, int vspeed, int ma)
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
            G.setColor(radius==0?Color.blue:Color.red);
            G.drawArc((X-3)-viewX, (Y-3)-viewY, 6, 6, 0, 360);
            G.drawLine((X-2)-viewX, Y-viewY, (X+2)-viewX, Y-viewY);
        }
    }

    @Override
    public void onUpdate(World apples) {
       if (apples.checkCollision(X, Y))
       {
           xspeed = 0;
           yspeed = 0;
           radius = 96;
           //apples.explode(X, Y, 32, 8, 16);
       }
       if (!apples.inBounds(X, Y))
       {
           alive = false;
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
            out.putInt(yspeed);
            out.putInt(maker);
        } catch (Exception ex) {
            Logger.getLogger(ExplosionEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static void reconstruct(ByteBuffer in, World world) {
        try {
            world.entityList.add(new StaticShotEntity(in.getInt(),in.getInt(),in.getInt(),in.getInt(),in.getInt()));
        } catch (Exception ex) {
            Logger.getLogger(StaticShotEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

        @Override
public void onServerUpdate(Server lol)
{
    for (Player p:lol.playerList)
       {
           if (APPLET.pointDis(X, Y, p.x, p.y)<radius&&maker!=p.ID)
           {
                alive = false;
                lol.sendMessage(Server.DESTROY, ByteBuffer.allocate(30).putInt(MYID));
                lol.sendMessage(Server.CHARGE, ByteBuffer.allocate(40).putInt(X).putInt(Y).putInt(radius).putInt(200).putInt(0));//maker
                return;
           }
       }
}
}
