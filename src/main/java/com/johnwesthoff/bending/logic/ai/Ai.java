package com.johnwesthoff.bending.logic.ai;

import java.awt.event.KeyEvent;
import java.util.Random;

import com.johnwesthoff.bending.Constants;
import com.johnwesthoff.bending.Session;
import com.johnwesthoff.bending.logic.Player;
import com.johnwesthoff.bending.logic.World;
import com.johnwesthoff.bending.util.math.Ops;

public class Ai implements Mover, SpellCaster {
    /*
     * Bot plan of action:
     * 
     * Have one of several moods
     * 
     * 1. Exploring - wanders around the map, preferring air travel and jumps. Fires
     * at enemies it sees, maybe switching to an offensive or defensive mood
     * accordingly.
     * 
     * 2. Offensive - jumps back and forth, attempts to either shoot at enemy or
     * clear a path to the enemy.
     * 
     * 3. Defensive - jumps back and forth, attempts to build walls between enemy
     * and self.
     * 
     * 4. Tunneler - digs a tunnel to a random location and attempts to build a
     * fort.
     * 
     */
    enum Strategy {
        Chase, Strafe, Retreat, Explore,
    }

    enum Move {
        Left, Right, Still
    }

    int exploreTimer = 0;
    int fightTimer = 0;
    boolean holdJump = false;
    Move move = Move.Still;
    Random r = new Random();
    int spellTimers[] = { 0, 0, 0, 0, 0 };
    boolean tryCasting[] = { false, false, false, false, false };
    boolean tryBlocking = false;
    int enemyLastSeen = 0;
    int playerTarget = -1;
    Strategy strategy;

    private void decide(Session sess) {
        for (int i = 0; i < tryCasting.length; i++) {
            tryCasting[i] = false;
        }
        if (enemyLastSeen > Constants.FPS * 10) {
            playerTarget = -1;
        }
        Player target = sess.world.getPlayer(playerTarget);
        if (target == null || Ops.pointDis(sess.world.x, sess.world.y, target.x, target.y) > 600) {
            int nearest = -1;
            double distance = 99999;
            for (Player p : sess.world.playerList) {
                double d = Ops.pointDis(sess.world.x, sess.world.y, p.x, p.y);
                if (d < distance && !p.sameTeam) {
                    distance = d;
                    nearest = p.ID;
                }
            }
            if (distance < 300) {
                enemyLastSeen = sess.ticks;
                playerTarget = nearest;
            }
            target = sess.world.getPlayer(playerTarget);
        }
        if (target == null) {
            strategy = Strategy.Explore;
        } else {
            if (target.sameTeam) {
                playerTarget = -1; //New target since we are attacking a teammate
            }
            if (sess.HP >= sess.MAXHP / 2) {
                if (fightTimer-- <= 0) {
                    fightTimer = (int) Constants.FPS / 3 + r.nextInt((int) Constants.FPS * 4);
                    holdJump = r.nextInt(2) == 0;
                    if (r.nextBoolean() || Ops.pointDis(sess.world.x, sess.world.y, target.x, target.y) < 70) {
                        strategy = Strategy.Strafe;
                    } else {
                        strategy = Strategy.Chase;
                    }
                }
            } else {
                if (fightTimer-- <= 0) {
                    fightTimer = (int) Constants.FPS / 3 + r.nextInt((int) Constants.FPS * 4);
                    holdJump = r.nextInt(2) == 0;
                    if (r.nextBoolean()) {
                        strategy = Strategy.Retreat;
                        tryBlocking = r.nextBoolean();
                    } else {
                        strategy = Strategy.Explore;
                    }
                }
            }
        }
        switch (strategy) {
        case Chase:
            move = target.x < sess.world.x ? Move.Left : Move.Right;
            tryCasting[0] = true;
            tryCasting[1] = true;
            tryCasting[2] = true;
            tryCasting[3] = true;
            tryCasting[4] = true;
            holdJump = r.nextInt(2) == 0;
            sess.world.mouseX = (int) target.x - sess.world.viewX;
            sess.world.mouseY = (int) target.y - sess.world.viewY + 100 - r.nextInt(200);
            break;
        case Explore:
            if (exploreTimer-- <= 0) {
                // Time to choose a new pattern
                holdJump = r.nextInt(2) == 0;
                int d = r.nextInt(5);
                if (d == 0) {
                    move = Move.Still;
                } else if (d <= 2) {
                    move = Move.Left;
                } else {
                    move = Move.Right;
                }
                exploreTimer = (int) Constants.FPS / 3 + r.nextInt((int) Constants.FPS * 4);
                sess.world.mouseX = r.nextInt(Constants.WIDTH_INT);
                sess.world.mouseY = r.nextInt(Constants.HEIGHT_INT / 2);
                tryCasting[2] = true;
            }
            break;
        case Retreat:
            sess.world.mouseY = r.nextInt(Constants.HEIGHT_INT);
            move = target.x > sess.world.x ? Move.Left : Move.Right;
            if (tryBlocking) {
                tryCasting[1] = tryBlocking;
                sess.world.mouseX = (int) target.x - sess.world.viewX;
            } else {
                tryCasting[2] = !tryBlocking;
                sess.world.mouseX = move == Move.Left ? 0 : Constants.WIDTH_INT;
            }
            break;
        case Strafe:
            tryCasting[0] = true;
            tryCasting[1] = true;
            tryCasting[2] = true;
            tryCasting[3] = true;
            tryCasting[4] = true;
            if (exploreTimer-- <= 0) {
                // Time to choose a new pattern
                holdJump = true;
                int d = r.nextInt(5);
                if (d == 0) {
                    move = Move.Still;
                } else if (d <= 2) {
                    move = Move.Left;
                } else {
                    move = Move.Right;
                }
                exploreTimer = (int) Constants.FPS / 2 + r.nextInt((int) Constants.FPS);
                sess.world.mouseX = (int) target.x - sess.world.viewX;
                sess.world.mouseY = (int) target.y - sess.world.viewY + 100 - r.nextInt(200);
            }
            break;
        default:
            break;
        }

        sess.world.pressX = sess.world.mouseX;
        sess.world.pressY = sess.world.mouseY;
    }

    @Override
    public void move(Session sess) {
        decide(sess);
        if (holdJump) {
            sess.world.jump = (float) (Constants.JUMP_COEFFICIENT);
        } else {
            sess.world.jump = 0;
        }
        sess.world.keys[KeyEvent.VK_S] = (sess.world.checkCollision(sess.world.x, sess.world.y - World.head)
                || sess.world.isLiquid(sess.world.x, sess.world.y - World.head));
        switch (move) {
        case Left:
            sess.world.move = -2;
            break;
        case Right:
            sess.world.move = 2;
            break;
        case Still:
            sess.world.move = 0;
            break;
        }
    }

    @Override
    public void cast(Session sess) {
        for (int i = 0; i < spellTimers.length; i++) {
            if (spellTimers[i]-- <= 0 && tryCasting[i]) {
                sess.spellList[0][i].getEffectiveSpell(0).cast(sess, 0);
                spellTimers[i] = (int) Constants.FPS / 4 + r.nextInt((int) Constants.FPS * 4);
            }
        }
    }

    public Ai() {
        strategy = Strategy.Explore;
    }
}
