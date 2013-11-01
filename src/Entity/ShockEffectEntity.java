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

/**
 *
 * @author John
 */
public class ShockEffectEntity extends Entity{
    public int radius = 16;
    public int life = 3;
    public ShockEffectEntity(int x, int y, int radius)
    {
        X = x;
        Y = y;
        this.radius = radius;
        
    }
    @Override
    public void onDraw(Graphics G, int viewX, int viewY) {
        if (X>viewX&&X<viewX+300&&Y>viewY&&Y<viewY+300)
        {
            G.setColor(Color.yellow);
            int x = X+radius-r.nextInt(radius*2)-viewX;
            int y =  Y+radius-r.nextInt(radius*2)-viewY;
            G.drawLine(x,y, x+radius-r.nextInt(radius*2), y+radius-r.nextInt(radius*2));
            G.drawLine(x+radius-r.nextInt(radius*2), y+radius-r.nextInt(radius*2), X+radius-r.nextInt(radius*2)-viewX, Y+radius-r.nextInt(radius*2)-viewY);
            
            G.setColor(Color.white);
            G.drawLine(X+radius-r.nextInt(radius*2)-viewX, Y+radius-r.nextInt(radius*2)-viewY, X+radius-r.nextInt(radius*2)-viewX, Y+radius-r.nextInt(radius*2)-viewY);
        }
    }

    @Override
    public void onUpdate(World apples) {
       if (life--<0)
       {
           alive = false;
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
       // throw new UnsupportedOperationException("Not supported yet.");
    }
    

}
