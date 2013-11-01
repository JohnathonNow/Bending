package Entity;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import destruct.Player;
import destruct.PlayerOnline;
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
public class EnemyEntity extends Entity{
    public int HP = 500;
    public int move = 0;
    public int target = 0;
    public int timer = 0;
    public int id = 0;
    public EnemyEntity(int x, int y, int hspeed, int vspeed, int hp)
    {
        X = x;
        Y = y;
        xspeed = hspeed;
        yspeed = vspeed;
        move = hspeed;
        HP = hp;
    }
    @Override
    public void onDraw(Graphics G, int viewX, int viewY) {
        if (X>viewX&&X<viewX+300&&Y>viewY&&Y<viewY+300)
        {
            G.setColor(Color.RED);
            G.fillArc((X-30)-viewX, (Y-30)-viewY, 60, 60, 0, 360);
        }
     //   System.out.println("HI!");
    }
    @Override
    public void onUpdate(World apples) {
        int min = 9999;
        double dis;
            for (Player p:apples.playerList)
            {
                dis = pointDis(X,Y,p.x,p.y);
                if (dis<min)
                {
                    min = (int)dis;
                    target = p.ID;
                    if (p.x>X){ move = 2;}
                    else{ move = -2;}
                }
            }
            if (!apples.serverWorld)
            {
                dis = pointDis(X,Y,apples.x,apples.y);
                if (dis<min)
                {
                    target = -1;
                    if (apples.x>X){ move = 2;}
                    else{ move = -2;}
                }
            }
        if (!apples.isSolid(X,Y-40))
            {
            Y-=40;
            if (apples.inBounds(X+move,Y+yspeed))
            {
                float slope;
                int toMove = move, XXX1 = X+3, YYY1 = Y-4, XXX2 = X-3, YYY2 = Y-4;
                while (true)
                {
                    YYY1+=1;
                    if (!apples.inBounds(XXX1,YYY1))
                    {
                        break;
                    }
                    if (apples.isSolid(XXX1,YYY1))
                    {
                        break;
                    }
                }
                while (true)
                {
                    YYY2+=1;
                    if (!apples.inBounds(XXX2,YYY2))
                    {
                        break;
                    }
                    if (apples.isSolid(XXX2,YYY2))
                    {
                        break;
                    }
                }
                X+=!apples.isSolid(X+toMove,Y+yspeed)?move:0;
            }
            for (int i = 0; i < 40; i++)
            {
                if (Y>0&&apples.isSolid(X,Y+1))
                {
                    break;
                }
                Y+=1;
            }
            }
            if (!apples.isSolid(X,Y+4))
            {
                yspeed=Math.min(4,yspeed+1);
            }
            else
            {
                yspeed = Math.min(0,yspeed);
            }
            if (!apples.isSolid(X,Y+yspeed))
            {
                Y+=yspeed;
            }
            else
            {
                yspeed=0;
            }
    }
    @Override
    public void onServerUpdate(Server handle) {
        int min = 9999;
            for (PlayerOnline p:handle.playerList)
            {
                double dis = pointDis(X,Y,p.x,p.y);
                if (dis<min)
                {
                    min = (int)dis;
                    target = p.ID;
                    if (p.x>X){ move = 2;}
                    else{ move = -2;}
                }
            }

        if (timer++>90)
        {
            //System.out.println(X);
            handle.sendMessage(Server.AI,ByteBuffer.allocate(28).putInt(X).putInt(Y).putInt(move).putInt(yspeed).putInt(HP).putInt(MYID).putInt(target));
            timer = 0;
        }
    }
    @Override
    public void cerealize(ByteBuffer out) {
        try {
            Server.putString(out,  this.getClass().getName());
            out.putInt(X);
            out.putInt(Y);
            out.putInt(move);
            out.putInt(yspeed);
            out.putInt(HP);
            out.putInt(target);
            out.putInt(id);
        } catch (Exception ex) {
            Logger.getLogger(ExplosionEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public EnemyEntity addStuff(int i,int it)
    {
        target = i;
        id = it;
        return this;
    }
    public static void reconstruct(ByteBuffer in, World world) {
            //System.out.println("IM BACK!");
            world.entityList.add(new EnemyEntity(in.getInt(),in.getInt(),in.getInt(),in.getInt(),in.getInt()).addStuff(in.getInt(),in.getInt()));
        
    }

}
