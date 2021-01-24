package com.johnwesthoff.bending;

import static com.johnwesthoff.bending.util.math.Ops.lengthdir_x;
import static com.johnwesthoff.bending.util.math.Ops.lengthdir_y;
import static com.johnwesthoff.bending.util.math.Ops.pointDir;
import static com.johnwesthoff.bending.util.math.Ops.pointDis;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.johnwesthoff.bending.entity.Entity;
import com.johnwesthoff.bending.logic.Player;
import com.johnwesthoff.bending.logic.World;
import com.johnwesthoff.bending.networking.handlers.DeathEvent;
import com.johnwesthoff.bending.networking.handlers.MapEvent;
import com.johnwesthoff.bending.spells.Spell;
import java.awt.event.KeyEvent;

public class Client {
    private Session session = Session.getInstance();
    private int counting = 0;
    private int sendcount = 0;

    public boolean tick() {
        World world = session.getWorld();
        boolean willSendMovement = false;
        turnSpecific();

        if (Math.abs(session.getXspeed()) < .001 && session.getXspeed() != 0) {
            session.setXspeed(0);
            willSendMovement = true;
        }

        
        for (final Entity e : session.getWorld().entityList) {
            e.checkAndHandleCollision(this);
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

        handleDeath();
        session.getWorld().onUpdate();
        Player localPlayer = session.getLocalPlayer();
        localPlayer.x = session.getWorld().x;
        localPlayer.y = session.getWorld().y;
        localPlayer.status = session.getWorld().status;
        localPlayer.leftArmAngle = session.getWorld().leftArmAngle;
        localPlayer.rightArmAngle = session.getWorld().rightArmAngle;
        if (Math.signum(session.getWorld().move) == -1) {
            localPlayer.left = -1;
        }
        if (Math.signum(session.getWorld().move) == 1) {
            localPlayer.left = 1;
        }
        if (((Math.signum(session.getPrevVspeed()) != Math.signum(session.getWorld().vspeed)) || ((session.getPrevMove()) != (world.move)))
                || counting++ > Constants.NETWORK_UPDATE_POSITION_RATE) {
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

    private void turnSpecific() {
        isMyTurn = (gameMode != Server.TURNBASED) || (whoseTurn == ID);

        if (isMyTurn) {
            handleStatusEffects();
            if (timeToHeal++ > 30 && HP < MAXHP) {
                timeToHeal = 0;
                HP++;
            }
            if (world.inBounds(world.x, world.y) && energico > 0
                    && world.isType((int) world.x, (int) world.y, Constants.JUICE)) {
                if (HP < MAXHP) {
                    energico -= 40;
                    HP++;
                }
            }
            handlePassiveEffect();
            if (HP > MAXHP) {
                HP = MAXHP;
            }
            
            
            if (world.x > world.wIdTh) {
                world.x = world.wIdTh;
            }
            if (world.checkCollision(world.x, world.y - World.head) || world.isLiquid(world.x, world.y - World.head)) {
                if (lungs-- < 0) {
                    HP--;
                    killMessage = "~ suffocated after fighting `...";
                }
            } else {
                lungs = maxlungs;
            }
            if (energico < 0) {
                energico = 0;
            }
            if (world.isType((int) world.x, (int) world.y, Constants.LAVA)) {
                world.status |= Constants.ST_FLAMING;
                killMessage = "~ burned to a crisp after fighting `!";
            }
            if ((world.status & Constants.ST_FLAMING) != 0) {
                HP -= random.nextInt(2);
                if ((passiveList[spellBook].getName().equals("Fireproof"))) {
                    energico += (inputer.doublecast * 3);
                }
                if (random.nextInt(10) == 0) {
                    world.status &= ~Constants.ST_FLAMING;// Stop being on fire
                }
            }
            if ((world.status & Constants.ST_SHOCKED) != 0) {
                energico -= 25;
                if (random.nextInt(10) == 0) {
                    world.status &= ~Constants.ST_SHOCKED;// Stop being on fire
                }
            }
            /*
             * if (world.isIce((int) world.x, (int) world.y + 6)) { xspeed += world.move;
             * 
             * // Removed for now because this makes people angry xspeed *= 1.4; if (xspeed
             * > 15) { xspeed = 15; } if (xspeed < -15) { xspeed = -15; } }
             */

            // @TODO : be carefull of SRP && OCP
            dig = world.getIncrementedDig(dig, Spell.lookup("AirbendingAir"), this);

            if (energico < maxeng) {
                energico += engrecharge;
            } else {
                energico = maxeng;
            }

            if (!world.isSolid(world.x + (int) xspeed, world.y)) {
                world.x += xspeed;
            }

            for (final Player p : world.playerList) {
                if ((p.status & Constants.ST_DRAIN) != 0) {
                    if (Math.abs(p.x - world.x) < Constants.AURA_RADIUS / 2) {
                        if (Math.abs(p.y - world.y) < Constants.AURA_RADIUS / 2) {
                            lastHit = p.ID;
                            killMessage = "~'s soul was corrupted by `'s Aura of Darkness.";
                            HP--;// Lose health from aura
                        }
                    }
                }
            }
        }
    }

    private void handlePassiveEffect() {
        if (!"Air Run".equals(passiveList[spellBook].getName())) {
            runningSpeed = 1;
        }
        if ((!"Air Affinity".equals(passiveList[spellBook].getName()))
                && (!"Earth Stance".equals(passiveList[spellBook].getName()))) {
            world.floatiness = 0;
            maxlungs = 100;
        }
        if (!"Water Treader".equals(passiveList[spellBook].getName())) {
            swimmingSpeed = 1;
        }
        if (!"Earth Shield".equals(passiveList[spellBook].getName())) {
            MAXHP = 100;
        }
        if (!"Overcharged".equals(passiveList[spellBook].getName())) {
            maxeng = 1000;
        }
        if (!"Earth Stance".equals(passiveList[spellBook].getName())) {
            knockbackDecay = 1;
        }
        if (world.vspeed >= 0) {
            if ("Earth Stance".equals(passiveList[spellBook].getName()) && world.vspeed < 0) {
                world.vspeed *= knockbackDecay;
            }
            xspeed *= .75 * knockbackDecay;

        }
        passiveList[spellBook].getPassiveAction(this);
    }

    private boolean handleStatusEffects() {
        boolean willSendMovement = false;
        if (removeAura > 0) {
            removeAura--;
            world.status |= Constants.ST_DRAIN;
            if (removeAura == 0) {
                world.status &= ~Constants.ST_DRAIN;
                willSendMovement = true;
            }
        }
        if (turnVisible > 0) {
            turnVisible--;
            world.status |= Constants.ST_INVISIBLE;
            if (turnVisible == 0) {
                world.status &= ~Constants.ST_INVISIBLE;
                willSendMovement = true;
            }
        }
        if (gameMode == Server.THEHIDDEN) {
            if (goodTeam) {
                world.status |= Constants.ST_INVISIBLE;
                spellBook = 5;// Force use of TheHidden book
            } else {
                if (!badTeam.isEmpty()) {
                    lastHit = badTeam.get(0);
                }
            }
        } else {
            if (spellBook >= 5) {
                spellBook = 0;
            }
        }
        return willSendMovement;
    }

    private boolean handleDeath() {
        if (HP <= 0) {
            world.viewdX = world.viewX;
            world.viewdY = world.viewY;
            HP = MAXHP;
            this.lungs = this.maxlungs;
            world.y = -50;
            world.x = -50;
            Spell.randomSpell.setSpells();
            if (killingSpree >= 148.413d) {
                // Anti-cheating - use logs
                gameService.feedRss(String.format("%s had a streak going", username),
                        String.format("%o kills in a row!", (int) Math.log(killingSpree)));
            }
            killingSpree = 0;
            world.dead = true;
            // this.chatActive = false;
            try {
                out.addMessage(DeathEvent.getPacket(lastHit, ID));
            } catch (final IOException ex) {
                // ex.printStackTrace();
            }
            if (lastHit == ID) {
                XP -= 25;
                this.sendMessage(username + " has committed suicide.", 0xFF0436);
            } else {
                this.sendMessage(killMessage.replaceAll("~", username).replaceAll("`", getKiller(lastHit)), 0x04FFF8);// username
                                                                                                                      // +
                                                                                                                      // "
                                                                                                                      // has
                                                                                                                      // been
                                                                                                                      // defeated
                                                                                                                      // by
                                                                                                                      // "+getKiller(lastHit)
            }
        return true;
        }
        return false;
    }

}
