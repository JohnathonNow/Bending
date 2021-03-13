package com.johnwesthoff.bending.entity;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import com.johnwesthoff.bending.Constants;
import com.johnwesthoff.bending.logic.World;

import java.awt.*;
import java.nio.ByteBuffer;


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
                if (creator.ground.cellData[x + i1][y + i2] == Constants.AIR) {
                    creator.ground.cellData[x + i1][y + i2] = Constants.WATER;
                }
                if (creator.ground.cellData[x + i1][y + i2] == Constants.GROUND) {
                    if (creator.random.nextBoolean())
                        creator.ground.cellData[x + i1][y + i2] = Constants.WATER;
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
                if (creator.ground.cellData[x + i1][y + i2] == Constants.AIR) {
                    creator.ground.cellData[x + i1][y + i2] = Type;
                }
                if (creator.ground.cellData[x + i1][y + i2] == Constants.GROUND) {
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