/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johnwesthoff.bending.spells;

import static com.johnwesthoff.bending.util.network.ResourceLoader.loadIcon;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;

import com.johnwesthoff.bending.Constants;
import com.johnwesthoff.bending.Session;
import com.johnwesthoff.bending.logic.World;
import com.johnwesthoff.bending.networking.handlers.SpellEvent;
import com.johnwesthoff.bending.spells.air.AirAffinity;
import com.johnwesthoff.bending.spells.air.AirRun;
import com.johnwesthoff.bending.spells.air.Airbending;
import com.johnwesthoff.bending.spells.air.AirbendingAir;
import com.johnwesthoff.bending.spells.air.AirbendingGust;
import com.johnwesthoff.bending.spells.air.AirbendingJump;
import com.johnwesthoff.bending.spells.air.AirbendingTornado;
import com.johnwesthoff.bending.spells.chemistry.Chemistry;
import com.johnwesthoff.bending.spells.chemistry.ChemistryCauldron;
import com.johnwesthoff.bending.spells.chemistry.ChemistryEnergy;
import com.johnwesthoff.bending.spells.chemistry.ChemistryInvisibility;
import com.johnwesthoff.bending.spells.chemistry.ChemistryPassive;
import com.johnwesthoff.bending.spells.chemistry.ChemistryPoison;
import com.johnwesthoff.bending.spells.earth.Earthbending;
import com.johnwesthoff.bending.spells.earth.EarthbendingSand;
import com.johnwesthoff.bending.spells.earth.EarthbendingShard;
import com.johnwesthoff.bending.spells.earth.EarthbendingShield;
import com.johnwesthoff.bending.spells.earth.EarthbendingSpike;
import com.johnwesthoff.bending.spells.earth.EarthbendingStance;
import com.johnwesthoff.bending.spells.earth.EarthbendingWallOfSand;
import com.johnwesthoff.bending.spells.fire.FireProof;
import com.johnwesthoff.bending.spells.fire.Firebending;
import com.johnwesthoff.bending.spells.fire.FirebendingCharge;
import com.johnwesthoff.bending.spells.fire.FirebendingJump;
import com.johnwesthoff.bending.spells.fire.Firebending_Lava;
import com.johnwesthoff.bending.spells.fire.Firebending_Thrower;
import com.johnwesthoff.bending.spells.fire.Firebending_Wall;
import com.johnwesthoff.bending.spells.lightning.Lightning;
import com.johnwesthoff.bending.spells.lightning.LightningBall;
import com.johnwesthoff.bending.spells.lightning.LightningMine;
import com.johnwesthoff.bending.spells.lightning.LightningOvercharge;
import com.johnwesthoff.bending.spells.lightning.LightningRod;
import com.johnwesthoff.bending.spells.lightning.LightningShield;
import com.johnwesthoff.bending.spells.lightning.LightningStorm;
import com.johnwesthoff.bending.spells.misc.SpellRandom;
import com.johnwesthoff.bending.spells.misc.SpellRandomMatch;
import com.johnwesthoff.bending.spells.water.BreathUnderWater;
import com.johnwesthoff.bending.spells.water.WaterSpout;
import com.johnwesthoff.bending.spells.water.WaterStorm;
import com.johnwesthoff.bending.spells.water.WaterTreading;
import com.johnwesthoff.bending.spells.water.Waterbending;
import com.johnwesthoff.bending.spells.water.WaterbendingFreeze;
import com.johnwesthoff.bending.spells.water.WaterbendingShard;
import com.johnwesthoff.bending.util.network.OrderedOutputStream;

/*
 * Cooldown reference table:
 * <100 ~ Very Low
 * >100 ~ Low
 * >200 ~ Moderate-Low
 * >300 ~ Moderate
 * >400 ~ Moderate-High
 * >500 ~ High
 * >600 ~ Very High
 * >750 ~ Insanely High
 * =1000 ~ Total
 */
public abstract class Spell {
    public int ID; // class of spell
    public float X, Y, mx, my;
    public int subID = 0; // individual spell id
    public int maker = 0;
    public boolean locked = false;
    public int unlockXP = 0;
    private int timer = 0;
    public static ArrayList<Spell> spells = new ArrayList<>(), passives = new ArrayList<>();
    public static ArrayList<String> spellnames = new ArrayList<>(), passivenames = new ArrayList<>();
    public static ArrayList<String> spelltips = new ArrayList<>(), passivetips = new ArrayList<>();
    public static ArrayList<ImageIcon> spellimages = new ArrayList<>(), passiveimages = new ArrayList<>();
    public static ImageIcon lockedImage;
    public static HashMap<String, Spell> spellLookup = new HashMap<>();
    public static Spell noSpell = new NOSPELL();
    public static SpellRandom randomSpell = new SpellRandom();
    public static SpellRandomMatch randomSpellMatch = new SpellRandomMatch();
    public static boolean inited = false;
    protected ImageIcon icon;

    /**
     * Registers all available spells
     */
    public static void registerSpells() {
        registerSpell(new Airbending());
        registerSpell(new AirbendingGust());
        registerSpell(new AirbendingJump());
        registerSpell(new AirbendingTornado());
        registerSpell(new AirbendingAir());
        registerSpell(new Earthbending());
        registerSpell(new EarthbendingSand());
        registerSpell(new EarthbendingShard());
        registerSpell(new EarthbendingSpike());
        registerSpell(new EarthbendingWallOfSand());
        registerSpell(new Waterbending());
        registerSpell(new WaterbendingFreeze());
        registerSpell(new WaterbendingShard());
        registerSpell(new WaterSpout());
        registerSpell(new WaterStorm());
        registerSpell(new Firebending());
        registerSpell(new FirebendingJump());
        registerSpell(new Firebending_Lava());
        registerSpell(new Firebending_Thrower());
        registerSpell(new Firebending_Wall());
        registerSpell(new Lightning());
        registerSpell(new LightningBall());
        registerSpell(new LightningMine());
        registerSpell(new LightningRod());
        registerSpell(new LightningStorm());
        registerSpell(new Chemistry());
        registerSpell(new ChemistryPoison());
        registerSpell(new ChemistryEnergy());
        registerSpell(new ChemistryInvisibility());
        registerSpell(new ChemistryCauldron());
        registerSpell(randomSpell);
        registerSpell(randomSpellMatch);
    }

    /**
     * Registers all available (passive) spells
     */
    private static void registerPassives() {
        registerPassive(new AirRun());
        registerPassive(new AirAffinity());
        registerPassive(new EarthbendingStance());
        registerPassive(new EarthbendingShield());
        registerPassive(new BreathUnderWater());
        registerPassive(new WaterTreading());
        registerPassive(new FirebendingCharge());
        registerPassive(new FireProof());
        registerPassive(new LightningOvercharge());
        registerPassive(new LightningShield());
        //registerPassive(new ChemistryPassive());

    }

    public static void init() {
        if (inited)
            return;
        inited = true;
        spells.clear();
        spellnames.clear();
        spelltips.clear();
        spellimages.clear();
        passives.clear();
        passivenames.clear();
        passivetips.clear();
        passiveimages.clear();
        registerSpells();
        registerPassives();
        lockedImage = (loadIcon("lockedSpell.png"));
        // System.out.println("YAY5");
        for (int i = 0; i < spells.size(); i++) {
            spellnames.add(spells.get(i).getName());
            spellimages.add(spells.get(i).getImage());
            spelltips.add(spells.get(i).getTip());
        }
        for (int i = 0; i < passives.size(); i++) {
            passivenames.add(passives.get(i).getName());
            passiveimages.add(passives.get(i).getImage());
            passivetips.add(passives.get(i).getTip());
        }
        randomSpell.setSpells();
        randomSpellMatch.setSpells();
    }

    /**
     * Registers a new spell
     * 
     * @param spell spell to initialize
     */
    private static void registerSpell(Spell spell) {
        spell.subID = spells.size();
        spells.add(spell);
        spellLookup.put(spell.getClass().getSimpleName(), spell);
    }

    /**
     * Registers a new (passive) spell
     * 
     * @param spell spell to initialize
     */
    private static void registerPassive(Spell spell) {
        spell.subID = passives.size();
        passives.add(spell);
        spellLookup.put(spell.getClass().getSimpleName(), spell);
    }

    /**
     * Gets the spell
     * 
     * @param i
     * @return Current spell
     */
    public static Spell getSpell(int i) {
        return spells.get(i);
    }

    public static Spell lookup(String i) {
        return spellLookup.get(i);
    }

    public static Spell getPassive(int i) {
        return passives.get(i);
    }

    public void onSpawn(Session me) {

    }

    public void unlock() {
        Session sess = Session.getInstance();
        if (sess.XP >= unlockXP) {
            locked = false;
        }
    }

    /**
     * Gets the class of the spell
     * 
     * @return Class of the spell
     */
    public int getID() {
        return ID;
    }

    public void getMessage(OrderedOutputStream out) {
        try {
            out.addMessage(SpellEvent.getPacket(subID, (int) X, (int) Y, (int) mx, (int) my));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Gets the spell that results from this spell being cast from a given slot This
     * is useful for SpellRandom, which calls a different spell than itself
     * 
     * @param slot the slot this spell is being called from
     * @return the spell to be used when the spell is in a given slot
     */
    public Spell getEffectiveSpell(int slot) {
        return this;
    }

    /**
     * Executes the command for the spell as a result of a player action
     *
     * @param app the client performing this action and having its state modified as
     *            a result
     */
    public abstract void getAction(Session app);

    /**
     * Executes the command for the spell as a result of a network event
     *
     * @param world the game state object being modified as a result of this spell
     * @param px    the x coordinate of the casting player
     * @param py    the y coordinate of the casting player
     * @param mx    the x coordinate of the mouse of the casting player
     * @param my    the y coordinate of the mouse of the casting player
     * @param pid   the id of the casting player
     * @param eid   the id of the first entity created by casting this spell
     */
    public abstract void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid,
            ByteBuffer buf);

    public abstract String getName();

    public abstract void getPassiveAction(Session app);

    public abstract int getCoolDown();

    public final boolean isCooledDown(Session app, int index) {
        return app.mana_flow >= this.getEffectiveSpell(index).timer;
    }

    public final void startCoolDown(Session app, int index) {
        this.timer = app.mana_flow + this.getEffectiveSpell(index).getCoolDown() * Constants.MANA_REGEN_RATE;
    }

    public void cast(Session sess, int index) {
        if (this.isCooledDown(sess, index) && sess.isMyTurn) {
            if ((sess.passiveList[sess.spellBook].getName().equals("Fire Charge"))
                    && (this.getEffectiveSpell(index) instanceof Firebending)) {
                if (sess.random.nextInt(5 - sess.clientui.inputer.doublecast) == 0) {
                    this.getAction(sess);
                }
            }
            this.getAction(sess);
            this.startCoolDown(sess, index);
        }
    }

    public String getTip() {
        return "<html>A basic air spell<br>Low Cooldown<br>Travels in a straight line<br>Deals low damage</html>";
    }

    public final ImageIcon getImage() {
        return icon;
    }

}