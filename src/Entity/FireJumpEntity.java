package Entity;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import destruct.Server;
import destruct.World;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.nio.ByteBuffer;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author John
 */
public class FireJumpEntity extends Entity{
//    public int maker = 0;
    public int radius = 16;
    public int gravity = 0;
    public FireJumpEntity(int x, int y, int hspeed, int vspeed, int ma)
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
            Graphics2D g = (Graphics2D)G;
            Composite c = g.getComposite();
            // g.setComposite(new Additive());
            g.setColor(new Color(255,r.nextInt(255),0,r.nextInt(255)));
                g.fillArc((int)(X-8)-viewX, (int)(Y-8)-viewY, 16,16, 0, 360); 
            for (int i = 0; i < 6; i++)
            {
                int e1 = 6-r.nextInt(12), e2 = 6-r.nextInt(12);
                g.setColor(new Color(255,r.nextInt(255),0,r.nextInt(255)));
                g.fillArc((int)(X+e1)-viewX, (int)(Y+e2)-viewY, e1, e2, 0, 360); 
            }
            g.setComposite(c);
        }
    }
    int next = 0;
    @Override
    public void onUpdate(World apples) {
       if (!apples.inBounds(X, Y)||apples.checkCollision(X, Y))
       {
           
           alive = false;
           //apples.explode(X, Y, 32, 8, 16);
       }
       if (next++>10)
       {
            alive = false;
       }
//       X = apples.playerList.get(maker).x;
//       Y = apples.playerList.get(maker).y;
       /*if (yspeed<12)
       {
           yspeed++;
       }*/
    }
    @Override
public void onServerUpdate(Server lol)
{
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
            world.entityList.add(new FireJumpEntity(in.getInt(),in.getInt(),in.getInt(),in.getInt(),in.getInt()));
        } catch (Exception ex) {
            Logger.getLogger(FireJumpEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
