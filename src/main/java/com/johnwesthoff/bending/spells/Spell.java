/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johnwesthoff.bending.spells;

import static com.johnwesthoff.bending.util.network.ResourceLoader.loadIcon;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import com.johnwesthoff.bending.Client;
import com.johnwesthoff.bending.logic.World;
import com.johnwesthoff.bending.spells.air.AirAffinity;
import com.johnwesthoff.bending.spells.air.AirRun;
import com.johnwesthoff.bending.spells.air.Airbending;
import com.johnwesthoff.bending.spells.air.AirbendingAir;
import com.johnwesthoff.bending.spells.air.AirbendingGust;
import com.johnwesthoff.bending.spells.air.AirbendingJump;
import com.johnwesthoff.bending.spells.air.AirbendingTornado;
import com.johnwesthoff.bending.spells.dark.DarkAura;
import com.johnwesthoff.bending.spells.dark.DarkSoulBall;
import com.johnwesthoff.bending.spells.dark.DarkSummonBall;
import com.johnwesthoff.bending.spells.dark.DarkTeleport;
import com.johnwesthoff.bending.spells.dark.Darkness;
import com.johnwesthoff.bending.spells.earth.Earthbending;
import com.johnwesthoff.bending.spells.earth.EarthbendingSand;
import com.johnwesthoff.bending.spells.earth.EarthbendingShard;
import com.johnwesthoff.bending.spells.earth.EarthbendingShield;
import com.johnwesthoff.bending.spells.earth.EarthbendingSpike;
import com.johnwesthoff.bending.spells.earth.EarthbendingStance;
import com.johnwesthoff.bending.spells.earth.EarthbendingWallOfSand;
import com.johnwesthoff.bending.spells.fire.Burito;
import com.johnwesthoff.bending.spells.fire.FireMaster;
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
import com.johnwesthoff.bending.spells.water.BreathUnderWater;
import com.johnwesthoff.bending.spells.water.WaterSpout;
import com.johnwesthoff.bending.spells.water.WaterStorm;
import com.johnwesthoff.bending.spells.water.WaterTreading;
import com.johnwesthoff.bending.spells.water.Waterbending;
import com.johnwesthoff.bending.spells.water.WaterbendingFreeze;
import com.johnwesthoff.bending.spells.water.WaterbendingShard;
import com.johnwesthoff.bending.util.network.OrderedOutputStream;

/*
 * Energy cost table:
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
    public int ID;
    public float X, Y, mx, my, subID = 0, maker = 0;
    public boolean locked = false;
    public int unlockXP = 0;
    public static ArrayList<Spell> spells = new ArrayList<>(), passives = new ArrayList<>();
    public static ArrayList<String> spellnames = new ArrayList<>(), passivenames = new ArrayList<>();
    public static ArrayList<String> spelltips = new ArrayList<>(), passivetips = new ArrayList<>();
    public static ArrayList<ImageIcon> spellimages = new ArrayList<>(), passiveimages = new ArrayList<>();
    public static ImageIcon lockedImage;
    public static Spell noSpell = new NOSPELL();
    protected ImageIcon icon;

    public static void init() {
        spells.clear();
        spellnames.clear();
        spelltips.clear();
        spellimages.clear();
        passives.clear();
        passivenames.clear();
        passivetips.clear();
        passiveimages.clear();
        // System.out.println("YAY1");
        spells.add(Spell.getAirSpell(0));
        spells.add(Spell.getAirSpell(1));
        spells.add(Spell.getAirSpell(2));
        spells.add(Spell.getAirSpell(4));
        spells.add(Spell.getAirSpell(5));
        // spells.add(Spell.getAirSpell(3));
        // System.out.println("YAY2");
        spells.add(Spell.getEarthSpell(0));
        spells.add(Spell.getEarthSpell(1));
        spells.add(Spell.getEarthSpell(2));
        spells.add(Spell.getEarthSpell(4));
        spells.add(Spell.getEarthSpell(5));
        // spells.add(Spell.getEarthSpell(3));
        // System.out.println("YAY3");
        spells.add(Spell.getWaterSpell(0));
        spells.add(Spell.getWaterSpell(1));
        spells.add(Spell.getWaterSpell(2));
        spells.add(Spell.getWaterSpell(3));
        spells.add(Spell.getWaterSpell(4));
        // spells.add(Spell.getWaterSpell(4));
        // System.out.println("YAY4");
        spells.add(Spell.getFireSpell(0));
        spells.add(Spell.getFireSpell(1));
        spells.add(Spell.getFireSpell(2));
        spells.add(Spell.getFireSpell(4));
        spells.add(Spell.getFireSpell(5));
        // spells.add(Spell.getFireSpell(3));

        spells.add(Spell.getLightningSpell(0));
        spells.add(Spell.getLightningSpell(1));
        spells.add(Spell.getLightningSpell(2));
        spells.add(Spell.getLightningSpell(3));
        spells.add(Spell.getLightningSpell(5));

        spells.add(Spell.getDarkSpell(0));
        spells.add(Spell.getDarkSpell(1));
        spells.add(Spell.getDarkSpell(2));
        spells.add(Spell.getDarkSpell(3));
        spells.add(Spell.getDarkSpell(4));

        spells.add(new Burito());
        spells.add(noSpell);
        spells.add(noSpell);
        spells.add(noSpell);
        spells.add(noSpell);
        spells.add(noSpell);
        spells.add(noSpell);
        spells.add(noSpell);
        spells.add(noSpell);
        spells.add(noSpell);
        spells.add(noSpell);
        spells.add(noSpell);
        spells.add(noSpell);
        spells.add(noSpell);
        spells.add(noSpell);
        spells.add(noSpell);
        spells.add(noSpell);
        spells.add(new FireMaster());

        passives.add(Spell.noSpell);
        passives.add(Spell.getAirSpell(3));
        passives.add(Spell.getAirSpell(6));
        passives.add(Spell.getEarthSpell(3));
        passives.add(Spell.getEarthSpell(6));
        passives.add(Spell.getWaterSpell(5));
        passives.add(Spell.getWaterSpell(6));
        passives.add(Spell.getFireSpell(3));
        passives.add(Spell.getFireSpell(6));
        passives.add(Spell.getLightningSpell(6));
        passives.add(Spell.getLightningSpell(7));
        lockedImage = (loadIcon("https://west-it.webs.com/spells/lockedSpell.png"));
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
        // System.out.println("YAY");
    }

    public void onSpawn(Client me) {

    }

    public void unlock() {
        if (Client.XP >= unlockXP) {
            locked = false;
        }
        if (getName().equals("Burito")) {
            locked = !(Client.jtb.getText().equals("Joey") && Client.currentlyLoggedIn);
        }
    }

    public int getID() {
        return ID;
    }

    public void getMessage(OrderedOutputStream out) {
        ByteBuffer bb = ByteBuffer.allocate(24);
        bb.putInt((int) subID).putInt((int) X).putInt((int) Y).putInt((int) mx).putInt((int) my);
        try {
            out.addMesssage(bb, ID);
        } catch (IOException ex) {
            // ex.printStackTrace();
        }
    }

    /**
     * Executes the command for the spell as a result of a player action
     * 
     * @param app the client performing this action and having its state modified as
     *            a result
     *
     */
    public abstract void getAction(Client app);

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
     *
     */
    public abstract void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid,
            ByteBuffer buf);

    public abstract int getCost();

    public abstract String getName();

    public abstract void getPassiveAction(Client app);

    public String getTip() {
        return "<html>A basic air spell<br>Low Energy Cost<br>Travels in a straight line<br>Deals low damage</html>";
    }

    public final ImageIcon getImage() {
        return icon;
    }

    public static Spell getAirSpell(int i) {
        switch (i) {
            default:
            case 0:
                return new Airbending();
            case 1:
                return new AirbendingJump();
            case 2:
                return new AirbendingTornado();
            case 3:
                return new AirRun();
            case 4:
                return new AirbendingGust();
            case 5:
                return new AirbendingAir();
            case 6:
                return new AirAffinity();
        }
    }

    public static Spell getWaterSpell(int i) {
        switch (i) {
            default:
            case 0:
                return new Waterbending();
            case 1:
                return new WaterbendingFreeze();
            case 2:
                return new WaterSpout();
            case 3:
                return new WaterbendingShard();
            case 4:
                return new WaterStorm();
            case 5:
                return new BreathUnderWater();
            case 6:
                return new WaterTreading();
        }
    }

    static public Spell getEarthSpell(int i) {
        switch (i) {
            default:
            case 0:
                return new Earthbending();
            case 1:
                return new EarthbendingSpike();
            case 2:
                return new EarthbendingShard();
            case 3:
                return new EarthbendingShield();
            case 4:
                return new EarthbendingSand();
            case 5:
                return new EarthbendingWallOfSand();
            case 6:
                return new EarthbendingStance();
        }
    }

    static public Spell getFireSpell(int i) {
        switch (i) {
            default:
            case 0:
                return new Firebending();
            case 1:
                return new Firebending_Lava();
            case 2:
                return new FirebendingJump();
            case 4:
                return new Firebending_Wall();
            case 5:
                return new Firebending_Thrower();
            case 3:
                return new FirebendingCharge();
            case 6:
                return new FireProof();
        }
    }

    static public Spell getLightningSpell(int i) {
        switch (i) {
            default:
            case 0:
                return new Lightning();
            case 1:
                return new LightningStorm();
            case 2:
                return new LightningBall();
            case 3:
                return new LightningMine();
            case 5:
                return new LightningRod();
            case 6:
                return new LightningShield();
            case 7:
                return new LightningOvercharge();
        }
    }

    static public Spell getDarkSpell(int i) {
        switch (i) {
            default:
            case 0:
                return new Darkness();
            case 1:
                return new DarkSoulBall();
            case 2:
                return new DarkAura();
            case 3:
                return new DarkSummonBall();
            case 4:
                return new DarkTeleport();
            case 6:
                return new LightningShield();
        }
    }

    
}