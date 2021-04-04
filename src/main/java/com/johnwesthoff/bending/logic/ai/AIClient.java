package com.johnwesthoff.bending.logic.ai;

import java.awt.Color;
import java.awt.Graphics;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.johnwesthoff.bending.Client;
import com.johnwesthoff.bending.ClientNetworking;
import com.johnwesthoff.bending.ClientUI;
import com.johnwesthoff.bending.Constants;
import com.johnwesthoff.bending.Server;
import com.johnwesthoff.bending.Session;
import com.johnwesthoff.bending.app.game.GameServiceFactory;
import com.johnwesthoff.bending.logic.World;
import com.johnwesthoff.bending.spells.Spell;

public class AIClient extends ClientUI implements Runnable {
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
    private static final long serialVersionUID = 1L;
    private Mover mover;
    private SpellCaster spellCaster;

    public AIClient() {
        mover = new IMover();
        spellCaster = new ISpellCaster();
    }

    public static void launch() {
        Session sess = Session.getInstance();
        Spell.init();
        final Client client = new Client();
        sess.clientui = new AIClient();
        sess.gameAlive = true;
        sess.client = client;
        sess.net = new ClientNetworking();
        // using factory to inject dependency
        sess.gameService = GameServiceFactory.create();

        sess.spellList = new Spell[10][5];// {{Spell.spells.get(0),Spell.spells.get(1),Spell.spells.get(2),Spell.spells.get(3),Spell.spells.get(5)},{Spell.spells.get(0),Spell.spells.get(1),Spell.spells.get(2),Spell.spells.get(3),Spell.spells.get(5)},{Spell.spells.get(0),Spell.spells.get(1),Spell.spells.get(2),Spell.spells.get(3),Spell.spells.get(5)},{Spell.spells.get(0),Spell.spells.get(1),Spell.spells.get(2),Spell.spells.get(3),Spell.spells.get(5)},{Spell.spells.get(0),Spell.spells.get(1),Spell.spells.get(2),Spell.spells.get(3),Spell.spells.get(5)}});
        sess.spellList[0] = (new Spell[] { Spell.lookup("Airbending"), Spell.lookup("Earthbending"),
                Spell.lookup("EarthbendingShard"), Spell.lookup("WaterbendingShard"), Spell.lookup("Firebending") });
        sess.spellList[1] = (new Spell[] { Spell.lookup("AirbendingJump"), Spell.lookup("Firebending"),
                Spell.lookup("AirbendingGust"), Spell.lookup("EarthbendingSpike"), Spell.lookup("EarthbendingShard") });
        sess.spellList[2] = (new Spell[] { Spell.lookup("AirbendingJump"), Spell.lookup("Waterbending"),
                Spell.lookup("WaterbendingShard"), Spell.lookup("Firebending"), Spell.lookup("AirbendingAir") });
        sess.spellList[3] = (new Spell[] { Spell.lookup("SpellRandom"), Spell.lookup("SpellRandom"),
                Spell.lookup("SpellRandom"), Spell.lookup("SpellRandom"), Spell.lookup("SpellRandom") });
        sess.spellList[4] = (new Spell[] { Spell.lookup("SpellRandomMatch"), Spell.lookup("SpellRandomMatch"),
                Spell.lookup("SpellRandomMatch"), Spell.lookup("SpellRandomMatch"), Spell.lookup("SpellRandomMatch") });
        sess.spellList[5] = (new Spell[] { Spell.lookup("AirbendingGust"), Spell.lookup("WaterbendingFreeze"),
                Spell.lookup("Firebending_Thrower"), Spell.lookup("Firebending_Wall"),
                Spell.lookup("EarthbendingShard") });
        Spell shield = Spell.lookup("EarthbendingShield");
        sess.passiveList = (new Spell[] { shield, shield, shield, shield, shield, shield });
        sess.notDone = false;
        // app.setResizable(true);
        sess.username = "Bot John";
        sess.mainProcess = new Thread(sess.clientui);
        sess.mainProcess.start();
        // app.serverIP = JOptionPane.showInputDialog("Server IP?");
        sess.net.startAi();
        sess.net.sendMessage("it is a bus!");
    }

    public void init() {
        // Do nothing
    }

    public void addChat(final String message, final Color color) {
        // Do nothing
    }

    public void destroy() {
        Session.getInstance().isAlive = false;
        Session.getInstance().gameAlive = false;
    }

    @Override
    public void run() {
        Session sess = Session.getInstance();
        int counting = 150;
        Thread.currentThread().setPriority(Thread.NORM_PRIORITY);
        sess.lastTime = System.nanoTime();
        double delta = 0;
        while (sess.gameAlive) {
            try {
                // Thread.yield();
            } catch (final Exception e2) {
                e2.printStackTrace();
            }
            final long now = System.nanoTime();

            delta += (now - sess.lastTime) / (1000000000 / Constants.FPS);
            sess.lastTime = now;
            boolean willSendMovement = false;
            if (!sess.started) {
                delta = 0;
                try {
                    Thread.sleep(10);
                } catch (final InterruptedException e1) {
                    e1.printStackTrace();
                }
                continue;
            }
            if (sess.matchOver > 0) {
                try {
                    Thread.sleep(10);
                } catch (final InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
            while (delta >= 1) {
                sess.ticks++;
                delta -= 1;
                mover.move(sess);
                spellCaster.cast(sess);
                willSendMovement |= sess.client.tick();
                try {
                    sess.world.ground.handleWater();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (sess.world.dead) {
                if ((!(sess.gameMode == Server.THEHIDDEN && !sess.goodTeam)) || sess.lastHit == sess.ID) {
                    sess.forcedRespawn = 0;
                    sess.world.y = 0;
                    sess.world.x = (sess.goodTeam ? sess.world.wIdTh / 2 : 0)
                            + sess.random.nextInt(sess.world.wIdTh / 2);
                    sess.world.dead = false;
                    sess.passiveList[sess.spellBook].onSpawn(sess);
                    sess.HP = sess.MAXHP;
                    sess.net.sendMessage(
                            "My bones! How could you do this, " + sess.world.getPlayerName(sess.lastHit) + "?");
                    sess.lastHit = sess.ID;
                }
            }
            sess.world.viewX = (int) Math.min(
                    Math.max((sess.world.x - (Constants.WIDTH_INT + 1) / 2) + sess.world.incX, 0),
                    Math.max(0, sess.world.wIdTh - Constants.WIDTH_INT - 1));

            sess.world.viewY = (int) Math.min(Math.max((sess.world.y - Constants.HEIGHT_INT / 2) + sess.world.incY, 0),
                    Math.max(0, sess.world.hEigHt - Constants.HEIGHT_INT));
            if (willSendMovement) {
                try {
                    sess.net.sendMovement();
                } catch (final Exception ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            World.setTime();
        }

    }

    @Override
    public void update(final Graphics GameGraphics) {
        // no draw
    }

    @Override
    public void paint(final Graphics g) {
        // no paint
    }

}
