package Entity;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import destruct.Server;
import destruct.World;
import java.awt.Graphics;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author John
 */
public class SpoutSourceEntity extends Entity{
    public int life = 0;
//    public int maker = 0;
    public SpoutSourceEntity(int x, int y, int l, int m)
    {
        X = x;
        Y = y;
        life = l;
        maker = m;
    }
    @Override
    public void onDraw(Graphics G, int viewX, int viewY) {
    }

    @Override
    public void onUpdate(World apples) {
      apples.entityList.add(new SpoutEntity((int)X,(int)Y,0,-10,maker));
      if (life--<0)
      {
          alive = false;
      }
    }
    @Override
public void onServerUpdate(Server lol)
{
    /*if (!lol.earth.inBounds(X, Y)||lol.earth.checkCollision(X, Y))
       {
           lol.earth.ground.FillCircleW(X, Y, radius, World.STONE);
           lol.sendMessage(Server.FILL, ByteBuffer.allocate(40).putInt(X).putInt(Y).putInt(radius).put(World.STONE));
       }*/
}
    @Override
    public void cerealize(ByteBuffer out) {
        try {
            Server.putString(out,  this.getClass().getName());
            out.putInt((int)X);
            out.putInt((int)Y);
            out.putInt(life);
        } catch (Exception ex) {
            Logger.getLogger(ExplosionEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static void reconstruct(ByteBuffer in, World world) {
        try {
            world.entityList.add(new SpoutSourceEntity(in.getInt(),in.getInt(),in.getInt(),in.getInt()));
        } catch (Exception ex) {
            Logger.getLogger(SpoutEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
