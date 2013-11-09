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
import java.awt.Polygon;
import java.nio.ByteBuffer;

/**
 *
 * @author John
 */
public class EnergyEntity extends Entity{
    public int maker = 0;
    public int radius = 16;
    public int gravity = 0;
    int decay = 0;
    int a1, a2, a3;
    int s1, s2, s3;
    int life = 255;
    int xstart, ystart;
     Polygon P = new Polygon();
    public EnergyEntity(int x, int y, int hspeed, int vspeed, int ma)
    {
        X = x;
        Y = y;
        xspeed = hspeed*3;
        yspeed = vspeed*3;
        xstart = x; ystart = y;
        maker = ma;
        P.addPoint(x, y);
    }
    @Override
    public void onDraw(Graphics G, int viewX, int viewY) {
            
            G.setColor(new Color(255,255,0,life));
            P.translate(-viewX, -viewY);
            G.drawPolygon(P);
            P.translate(viewX, viewY);
            
            G.setColor(Color.DARK_GRAY);
            for (int i = 0; i < 14; i++)
            {
                G.fillArc(xstart+10-r.nextInt(20)-viewX-10, ystart+10-r.nextInt(20)-viewY-10, 5+r.nextInt(15), 5+r.nextInt(15), 0, 360);
            }
    }

    @Override
    public void onUpdate(World apples) {
        P.addPoint((int)X+5-r.nextInt(10), (int)Y+5-r.nextInt(10));
       if (apples.checkCollision(X, Y))
       {
               decay = 5;
               xspeed = 0;
               yspeed = 0;
       }

       if ((life-=decay) <= 0)
       {
           alive = false;
       }
   //    yspeed+=gravity;
       
       /*if (yspeed<12)
       {
           yspeed++;
       }*/
    }
    long time = System.currentTimeMillis();
    @Override
public void onServerUpdate(Server lol)
{
    
   // System.out.println(System.currentTimeMillis()-time);
    time = System.currentTimeMillis();
    if (!lol.earth.inBounds(X, Y)||lol.earth.checkCollision(X, Y))
       {
           radius*=3;
           alive = false;
           //lol.earth.ground.FillCircleW(X, Y, radius, World.STONE);
           lol.sendMessage(Server.CHARGE, ByteBuffer.allocate(40).putInt((int)X).putInt((int)Y).putInt(radius).putInt(200).putInt(maker));
       }
    for (Player p:lol.playerList)
       {
           if (APPLET.pointDis(X, Y-World.head, p.x, p.y)<40&&maker!=p.ID)
           {
               alive = false;
               lol.sendMessage(Server.DESTROY, ByteBuffer.allocate(30).putInt(MYID));
                lol.sendMessage(Server.CHARGE, ByteBuffer.allocate(40).putInt((int)X).putInt((int)Y).putInt(radius).putInt(200).putInt(maker));
           }
       }
}

    @Override
    public void cerealize(ByteBuffer out) {
       // throw new UnsupportedOperationException("Not supported yet.");
    }
    

}
