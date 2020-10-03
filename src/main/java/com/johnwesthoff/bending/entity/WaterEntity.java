package com.johnwesthoff.bending.entity;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.Graphics;
import java.nio.ByteBuffer;

import com.johnwesthoff.bending.logic.World;

/**
 *
 * @author John
 */
public class WaterEntity extends Entity {

    public WaterEntity(int x, int y, World creator) {
        X = x;
        Y = y;
        xspeed = 0;
        yspeed = 0;
        for (int i1 = -10; i1 <= 10; i1++) {
            for (int i2 = -10; i2 <= 10; i2++) {
                if (!creator.inBounds(x + i1, y + i2))
                    continue;
                if (creator.ground.cellData[x + i1][y + i2] == World.AIR) {
                    creator.ground.cellData[x + i1][y + i2] = World.WATER;
                }
                if (creator.ground.cellData[x + i1][y + i2] == World.GROUND) {
                    if (creator.random.nextBoolean())
                        creator.ground.cellData[x + i1][y + i2] = World.WATER;
                }
            }
        }
        alive = false;
    }

    public WaterEntity(int x, int y, World creator, byte Type) {
        X = x;
        Y = y;
        xspeed = 0;
        yspeed = 0;
        for (int i1 = -10; i1 <= 10; i1++) {
            for (int i2 = -10; i2 <= 10; i2++) {
                if (!creator.inBounds(x + i1, y + i2))
                    continue;
                if (creator.ground.cellData[x + i1][y + i2] == World.AIR) {
                    creator.ground.cellData[x + i1][y + i2] = Type;
                }
                if (creator.ground.cellData[x + i1][y + i2] == World.GROUND) {
                    if (creator.random.nextBoolean())
                        creator.ground.cellData[x + i1][y + i2] = Type;
                }
            }
        }
        alive = false;
    }

    @Override
    public void onDraw(Graphics G, int viewX, int viewY) {

    }

    @Override
    public void onUpdate(World apples) {
    }

    @Override
    public void cerealize(ByteBuffer out) {

    }

}