/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import destruct.Server;
import destruct.World;
import java.awt.Color;
import java.awt.Graphics;
import java.nio.ByteBuffer;


/**
 *
 * @author John
 */
public class RainEntity extends Entity{


    int life = 250;
    int maker;
    public RainEntity(int x, int y, int ID)
    {
        X = x;
        Y = y;
        maker = ID;
    }
    @Override
    public void onDraw(Graphics G, int viewX, int viewY) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        G.setColor(Color.GRAY);
            for (int i = 0; i < 14; i++)
            {
                G.fillArc((int)X+20-r.nextInt(80)-viewX, (int)Y+10-r.nextInt(20)-viewY-10, 20+r.nextInt(40), 5+r.nextInt(15), 0, 360);
            }
    }

    @Override
    public void onUpdate(World apples) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void cerealize(ByteBuffer out) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    @Override
    public void onServerUpdate(Server lol)
    {

        if (r.nextInt(12)==5)
        {
            int x = (int)X+40-r.nextInt(80), y = (int)Y-15;
            int Iw = Server.getID();
            //lol.earth.entityList.add((new WaterBallEntity(x,y,0,0,maker).setID(Iw)));
            //lol.sendMessage(Server.WATERBENDING,ByteBuffer.allocate(28).putInt(0).putInt(x).putInt(y).putInt(0).putInt(5).putInt(-1).putInt(Iw));
            lol.earth.ground.puddle(x, (int)Y, 10);
            lol.sendMessage(Server.PUDDLE, ByteBuffer.allocate(40).putInt(x).putInt((int)Y).putInt(10));
        }
        if (life--<0)
           {
               //lol.earth.ground.FillCircleW(X, Y, radius, World.STONE);
               alive = false;
               lol.sendMessage(Server.DESTROY, ByteBuffer.allocate(40).putInt(MYID));
           }
    }
}
