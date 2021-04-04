package com.johnwesthoff.bending.logic.ai;

import java.awt.event.KeyEvent;
import java.util.Random;

import com.johnwesthoff.bending.Constants;
import com.johnwesthoff.bending.Session;
import com.johnwesthoff.bending.logic.World;

public class IMover implements Mover {
    int timer = 0;
    boolean holdJump = false;
    int dir = 0;
    Random r = new Random();

    @Override
    public void move(Session sess) {
        if (timer-- <= 0) {
            // Time to choose a new pattern
            holdJump = r.nextInt(2) == 0;
            dir = r.nextInt(5);
            timer = (int) Constants.FPS / 3 + r.nextInt((int) Constants.FPS * 4);
        }
        if (holdJump) {
            sess.world.jump = (float) (Constants.JUMP_COEFFICIENT);
        } else {
            sess.world.jump = 0;
        }
        sess.world.keys[KeyEvent.VK_S] = (sess.world.checkCollision(sess.world.x, sess.world.y - World.head)
                || sess.world.isLiquid(sess.world.x, sess.world.y - World.head));
        if (dir == 0) {
            sess.world.move = 0;
        } else if (dir == 1 || dir == 2) {
            sess.world.move = 2;
        } else if (dir == 3 || dir == 4) {
            sess.world.move = -2;
        }
    }
}
