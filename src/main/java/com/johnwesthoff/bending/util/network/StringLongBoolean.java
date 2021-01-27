/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johnwesthoff.bending.util.network;

import java.util.StringTokenizer;

import com.johnwesthoff.bending.Session;
import com.johnwesthoff.bending.app.game.GameService;
import com.johnwesthoff.bending.app.game.GameServiceFactory;

/**
 *
 * @author John
 */
public final class StringLongBoolean {
    private final GameService gameService;

    private long[] mylong;
    private StringTokenizer ST;

    public StringLongBoolean(String yes) {
        construct(yes);
        gameService = GameServiceFactory.create();
    }

    public void construct(String yes) {
        ST = new StringTokenizer(yes, ",");
        mylong = new long[ST.countTokens()];
        String y;
        int i = 0;
        while (ST.hasMoreElements()) {
            y = ST.nextToken(",");
            mylong[i] = Long.parseLong(y);
            i++;
        }
    }

    public boolean get(int drawer, long index) {
        if (index > 63) {
            index = 0;
        }
        if (drawer >= mylong.length) {
            long temp[] = mylong;
            mylong = new long[drawer + 1];
            System.arraycopy(temp, 0, mylong, 0, temp.length);
        }
        return ((mylong[drawer] >> index) & 1) != 0;
    }

    public void set(int drawer, long index, boolean val) {
        if (get(drawer, index) != val) {
            if (val) {
                mylong[drawer] |= 1l << index;
            } else {
                mylong[drawer] &= ~(1l << index);
            }
            gameService.postUnlocks(Session.getInstance().clientui.jtb.getText());
        }
    }

    @Override
    public String toString() {
        String ret = "";
        for (long l : mylong) {
            ret += l;
            ret += ",";
        }
        return ret.substring(0, ret.length() - 1);
    }
}
