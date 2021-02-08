package com.johnwesthoff.bending;

import static com.johnwesthoff.bending.util.math.Ops.lengthdir_x;
import static com.johnwesthoff.bending.util.math.Ops.lengthdir_y;
import static com.johnwesthoff.bending.util.math.Ops.pointDir;
import static com.johnwesthoff.bending.util.math.Ops.pointDis;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.johnwesthoff.bending.entity.Entity;
import com.johnwesthoff.bending.entity.ShockEffectEntity;
import com.johnwesthoff.bending.logic.Player;
import com.johnwesthoff.bending.logic.World;
import com.johnwesthoff.bending.networking.handlers.DeathEvent;
import com.johnwesthoff.bending.networking.handlers.MapEvent;
import com.johnwesthoff.bending.spells.Spell;

public class Client {
    private Session session = Session.getInstance();
    private int counting = 0;
    private int sendcount = 0;

    public boolean tick() {
        session.mana_drain = 0;
        World world = session.getWorld();
        boolean willSendMovement = turnSpecific();

        if (Math.abs(session.getXspeed()) < .001 && session.getXspeed() != 0) {
            session.setXspeed(0);
            willSendMovement = true;
        }

        for (final Entity e : session.getWorld().entityList) {
            e.checkAndHandleCollision(session);
        }
        session.getWorld().determineInc();
        handleCamera();
        if (session.getWorld().dead) {
            session.getWorld().status = 0;
            session.getWorld().y = -50;
            session.getWorld().x = -50;
            session.setHP(session.getMAXHP());
            session.setLungs(session.getMaxlungs());
            session.getWorld().move = 0;
            session.getWorld().vspeed = 0;
            session.setXspeed(0);
        }

        willSendMovement |= handleDeath();
        session.getWorld().onUpdate();
        Player localPlayer = session.getLocalPlayer();
        int oldLeft = localPlayer.left;
        if (Math.signum(session.getWorld().move) == -1) {
            localPlayer.left = -1;
        }
        if (Math.signum(session.getWorld().move) == 1) {
            localPlayer.left = 1;
        }
        if (oldLeft != localPlayer.left) {
            double t = world.leftArmAngle;
            world.leftArmAngle = world.rightArmAngle;
            world.rightArmAngle = t;
        }
        localPlayer.x = session.getWorld().x;
        localPlayer.y = session.getWorld().y;
        localPlayer.status = session.getWorld().status;
        localPlayer.leftArmAngle = session.getWorld().leftArmAngle;
        localPlayer.rightArmAngle = session.getWorld().rightArmAngle;

        if (((Math.signum(session.getPrevVspeed()) != Math.signum(session.getWorld().vspeed))
                || ((session.getPrevMove()) != (world.move))) || counting++ > Constants.NETWORK_UPDATE_POSITION_RATE) {
            counting = 0;
            willSendMovement = true;
            session.setPrevMove(world.move);
        }
        if (session.isSendRequest() && sendcount++ >= Constants.NETWORK_UPDATE_MAP_RATE) {
            sendcount = 0;
            try {
                session.getOut().addMessage(MapEvent.getPacketClient());
            } catch (final Exception ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
            session.setSendRequest(false);
        }
        session.setPrevVspeed(world.vspeed);
        session.setXp((int) world.x);
        session.setYp((int) world.y);
        return willSendMovement;
    }

    private void handleCamera() {
        if (session.getWorld().keys[KeyEvent.VK_CONTROL] && !session.getWorld().dead) {
            final double direction = pointDir(150, 150, session.getWorld().mouseX, session.getWorld().mouseY);
            final double distance = pointDis(150, 150, session.getWorld().mouseX, session.getWorld().mouseY) / 8;
            session.getWorld().incX += lengthdir_x(distance, direction);
            session.getWorld().incY -= lengthdir_y(distance, direction);
            session.getWorld().cameraMoved = true;
        }
    }

    private boolean turnSpecific() {
        boolean wsm = false;
        World world = session.getWorld();
        if ((session.getGameMode() == Server.TURNBASED) && (session.getWhoseTurn() != session.getID())) {
            session.world.move = 0;
            return wsm;
        }
        wsm |= handleStatusEffects();
        handlePassiveEffect();
        handleHealing();
        if (world.x > world.wIdTh) {
            world.x = world.wIdTh;
        }

        // @TODO : be carefull of SRP && OCP
        session.setDig(world.getIncrementedDig(session.getDig(), Spell.lookup("AirbendingAir"), this));

        if (!world.isSolid(world.x + (int) session.getXspeed(), world.y)) {
            world.x += session.getXspeed();
        }

        for (final Player p : world.playerList) {
            if ((p.status & Constants.ST_DRAIN) != 0) {
                if (Math.abs(p.x - world.x) < Constants.AURA_RADIUS / 2) {
                    if (Math.abs(p.y - world.y) < Constants.AURA_RADIUS / 2) {
                        session.setLastHit(p.ID);
                        session.setKillMessage("~'s soul was corrupted by `'s Aura of Darkness.");
                        session.setHP((short) (session.getHP() - 1));// Lose health from aura
                    }
                }
            }
        }
        return wsm;
    }

    private void handleHealing() {
        if (session.timeToHeal++ > 30 && session.HP < session.MAXHP) {
            session.timeToHeal = 0;
            session.HP++;
        }
        if (session.world.inBounds(session.world.x, session.world.y) && session.energico > 0
                && session.world.isType((int) session.world.x, (int) session.world.y, Constants.JUICE)) {
            if (session.HP < session.MAXHP) {
                session.mana_drain += Constants.MANA_REGEN_RATE * 3 / 4;
                session.HP++;
            }
        }
        if (session.HP > session.MAXHP) {
            session.HP = session.MAXHP;
        }
        if (session.world.checkCollision(session.world.x, session.world.y - World.head)
                || session.world.isLiquid(session.world.x, session.world.y - World.head)) {
            if (session.lungs-- < 0) {
                session.HP--;
                session.killMessage = "~ suffocated after fighting `...";
            }
        } else if (session.world.isGas(session.world.x, session.world.y - World.head)) {
            session.lungs -= Constants.GAS_BREATH_TIME;
            if (session.lungs < 0) {
                session.HP -= Constants.GAS_BREATH_DAMAGE;
                session.killMessage = "~ suffocated after fighting `...";
            }
        } else {
            session.lungs = session.maxlungs;
        }
        session.mana_flow += Constants.MANA_REGEN_RATE - Math.min(session.mana_drain, Constants.MANA_REGEN_RATE);
    }

    private void handlePassiveEffect() {
        if (!"Air Run".equals(session.passiveList[session.spellBook].getName())) {
            session.runningSpeed = 1;
        }
        if ((!"Air Affinity".equals(session.passiveList[session.spellBook].getName()))
                && (!"Earth Stance".equals(session.passiveList[session.spellBook].getName()))) {
            session.world.floatiness = 0;
            session.maxlungs = 100;
        }
        if (!"Water Treader".equals(session.passiveList[session.spellBook].getName())) {
            session.swimmingSpeed = 1;
        }
        if (!"Earth Shield".equals(session.passiveList[session.spellBook].getName())) {
            session.MAXHP = 100;
        }
        if (!"Overcharged".equals(session.passiveList[session.spellBook].getName())) {
            session.maxeng = 1000;
        }
        if (!"Earth Stance".equals(session.passiveList[session.spellBook].getName())) {
            session.knockbackDecay = 1;
        }
        if (session.world.vspeed >= 0) {
            if ("Earth Stance".equals(session.passiveList[session.spellBook].getName()) && session.world.vspeed < 0) {
                session.world.vspeed *= session.knockbackDecay;
            }
            session.xspeed *= .75 * session.knockbackDecay;

        }
        session.passiveList[session.spellBook].getPassiveAction(session);
    }

    private boolean handleStatusEffects() {
        boolean willSendMovement = false;
        if (session.removeAura > 0) {
            session.removeAura--;
            session.world.status |= Constants.ST_DRAIN;
            if (session.removeAura == 0) {
                session.world.status &= ~Constants.ST_DRAIN;
                willSendMovement = true;
            }
        }
        if (session.turnVisible > 0) {
            session.turnVisible--;
            session.world.status |= Constants.ST_INVISIBLE;
            if (session.turnVisible == 0) {
                session.world.status &= ~Constants.ST_INVISIBLE;
                willSendMovement = true;
            }
        }
        if (session.gameMode == Server.THEHIDDEN) {
            if (session.goodTeam) {
                session.world.status |= Constants.ST_INVISIBLE;
                session.spellBook = 5;// Force use of TheHidden book
            } else {
                if (!session.badTeam.isEmpty()) {
                    session.lastHit = session.badTeam.get(0);
                }
            }
        } else {
            if (session.spellBook >= 5) {
                session.spellBook = 0;
            }
        }
        if (session.world.isType((int) session.world.x, (int) session.world.y, Constants.LAVA)) {
            session.world.status |= Constants.ST_FLAMING;
            session.killMessage = "~ burned to a crisp after fighting `!";
        }
        if ((session.world.status & Constants.ST_FLAMING) != 0) {
            session.HP -= session.random.nextInt(2);
            if ((session.passiveList[session.spellBook].getName().equals("Fireproof"))) {
                session.energico += (session.clientui.inputer.doublecast * 3);
            }
            if (session.random.nextInt(10) == 0) {
                session.world.status &= ~Constants.ST_FLAMING;// Stop being on fire
            }
        }
        if ((session.world.status & Constants.ST_SHOCKED) != 0) {
            session.mana_drain += Constants.MANA_REGEN_RATE;
            if (session.random.nextInt(10) == 0) {
                session.world.status &= ~Constants.ST_SHOCKED;// Stop being shocked
            }
        }
        return willSendMovement;
    }

    private boolean handleDeath() {
        World world = session.getWorld();
        if (session.getHP() <= 0) {
            world.viewdX = world.viewX;
            world.viewdY = world.viewY;
            session.setHP(session.getMAXHP());
            session.setLungs(session.getMaxlungs());
            world.y = -50;
            world.x = -50;
            Spell.randomSpell.setSpells();
            world.dead = true;
            // this.chatActive = false;
            try {
                session.getOut().addMessage(DeathEvent.getPacket(session.getLastHit(), session.getID()));
            } catch (final IOException ex) {
                // ex.printStackTrace();
            }
            if (session.getLastHit() == session.getID()) {
                session.net.sendMessage(session.getUsername() + " has committed suicide.", 0xFF0436);
            } else {
                session.net.sendMessage(session.killMessage.replace("~", session.getUsername()).replace("`",
                        getKiller(session.getLastHit())), 0x04FFF8);
            }
            return true;
        }
        return false;
    }

    public String getKiller(final int i) {
        for (final Player p : Session.getInstance().world.playerList) {
            if (p.ID == i) {
                return p.username;
            }
        }
        return "No One";
    }

    public Session getSession() {
        return Session.getInstance();
    }

    public boolean checkCollision(final float px, final float py) {
        session.localPlayer.playerHitbox.setLocation((int) session.world.x - session.localPlayer.playerHitbox.width / 2,
                (int) session.world.y - (World.head + 10));
        return (session.localPlayer.playerHitbox.contains(px, py));
    }

    public int hurt(double pain) {
        if (session.passiveList[session.spellBook].getName().equals("Lightning Shield")) {
            if (session.random.nextInt(15 - (session.shockdrain * 2)) == 0) {
                pain *= .25;
                if (pain < 1) {
                    pain = 1;
                }
                session.energico -= pain * 50;
                session.world.entityList
                        .add(new ShockEffectEntity((int) session.world.x, (int) session.world.y, 6 + (int) pain));
            }
        }
        session.HP -= pain;
        return (int) pain;
    }
}