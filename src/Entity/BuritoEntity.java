package Entity;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


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
public class BuritoEntity extends Entity{
//    public int maker = 0;
    public int radius = 16;
    public int gravity = 1;
    public BuritoEntity(int x, int y, int hspeed, int vspeed, int ma)
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
            // g.setComposite(new Additive());
            G.setColor(Color.ORANGE);
            for (int i = 0; i < 4; i ++)
            {
                G.fillArc((int)X-viewX+(i*2)-4, (int)Y-viewY, 4, 4, 0, 360);
            }
            G.setColor(Color.RED);
            G.fillArc(4+(int)X-viewX, (int)Y-viewY, 3, 3, 0, 360);
        }
    }
    int next = 0;
    @Override
    public void onUpdate(World apples) {
       if (!apples.inBounds((int)X, (int)Y)||apples.checkCollision((int)X, (int)Y))
       {
           
           alive = false;
           //apples.explode(X, Y, 32, 8, 16);
       }
       apples.ground.FillCircleW((int)X, (int)Y, (int)Math.sqrt(xspeed*xspeed+yspeed*yspeed), World.LAVA);
       if (next++>4)
       {
            next = 0;
            yspeed+=gravity;
       }
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
    private boolean collided(World w)
    {
        double xx = X;
        double yy = Y;
        for (int i = 0; i < 16; i++)
        {
            if (w.inBounds((int)xx,(int)yy)&&w.ground.cellData[(int)xx][(int)yy]==World.WATER)
            {
                X = (int)xx;
                Y = (int)yy;
                return true;
            }
            xx+=xspeed/16d;
            yy+=yspeed/16d;
        }
        return false;
    }
    public static void reconstruct(ByteBuffer in, World world) {
        try {
            world.entityList.add(new BuritoEntity(in.getInt(),in.getInt(),in.getInt(),in.getInt(),in.getInt()));
        } catch (Exception ex) {
            Logger.getLogger(BuritoEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
