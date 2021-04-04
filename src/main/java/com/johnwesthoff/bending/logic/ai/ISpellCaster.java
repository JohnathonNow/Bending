package com.johnwesthoff.bending.logic.ai;

import java.util.Random;

import com.johnwesthoff.bending.Constants;
import com.johnwesthoff.bending.Session;

public class ISpellCaster implements SpellCaster {
    Random r = new Random();
    int timer0 = 0;
    int timer1 = 0;
    int timer2 = 0;

    @Override
    public void cast(Session sess) {
        sess.world.mouseX = r.nextInt(Constants.WIDTH_INT);
        sess.world.mouseY = r.nextInt(Constants.HEIGHT_INT);
        sess.world.pressX = sess.world.mouseX;
        sess.world.pressY = sess.world.mouseY;
        if (timer0-- <= 0) {
            sess.spellList[0][4].getEffectiveSpell(0).cast(sess, 0);
            timer0 = (int)Constants.FPS / 4 + r.nextInt((int)Constants.FPS * 4);
        }
        if (timer1-- <= 0) {
            sess.spellList[0][1].getEffectiveSpell(0).cast(sess, 0);
            timer1 = (int)Constants.FPS / 4 + r.nextInt((int)Constants.FPS * 4);
        }
        if (timer2-- <= 0) {
            sess.spellList[0][0].getEffectiveSpell(0).cast(sess, 0);
            timer2 = (int)Constants.FPS / 4 + r.nextInt((int)Constants.FPS * 4);
        }
    }
}
