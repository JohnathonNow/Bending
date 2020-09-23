/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johnwesthoff.bending.spells;

import static com.johnwesthoff.bending.util.network.ResourceLoader.loadIcon;

import java.awt.Color;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;

import com.johnwesthoff.bending.Client;
import com.johnwesthoff.bending.Server;
import com.johnwesthoff.bending.entity.EffectEntity;
import com.johnwesthoff.bending.entity.GustEntity;
import com.johnwesthoff.bending.entity.MissileEntity;
import com.johnwesthoff.bending.entity.TornadoEntity;
import com.johnwesthoff.bending.logic.World;
import com.johnwesthoff.bending.util.network.OrderedOutputStream;
import com.johnwesthoff.bending.util.network.ResourceLoader;

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

    public static class Airbending extends Spell {
        public Airbending() {
            ID = Server.AIRBENDING;
            try {
                icon = (loadIcon("https://west-it.webs.com/spells/airSpell.png"));
            } catch (Exception ex) {
                Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void getAction(Client app) {
            // throw new UnsupportedOperationException("Not supported yet.");
            X = app.world.x;
            Y = app.world.y - World.head;
            mx = app.world.viewX;
            my = app.world.mouseY - app.world.viewY;
            double direction = 360 - Client.pointDir(app.world.x - app.world.viewX,
                    app.world.y - World.head - app.world.viewY, app.world.mouseX, app.world.mouseY);
            // direction+=180;
            mx = ((int) (Client.lengthdir_x(8, direction)));
            my = ((int) (Client.lengthdir_y(8, direction)));
            maker = ID;
            getMessage(app.out);
        }

        @Override
        public int getCost() {
            return 100;
        }

        @Override
        public String getName() {
            return "AirBall";
        }

        @Override
        public void getPassiveAction(Client app) {
            // throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
            world.entityList.add(new MissileEntity(px, py, mx, my, pid).setID(eid));
        }
    }

    public static class AirbendingJump extends Spell {
        public AirbendingJump() {
            ID = Server.AIRBENDING;
            subID = 1;
            try {
                icon = (loadIcon("https://west-it.webs.com/spells/airJump.png"));
            } catch (Exception ex) {
                Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void getAction(Client app) {
            // throw new UnsupportedOperationException("Not supported yet.");
            X = app.world.x;
            Y = app.world.y;
            mx = (app.world.mouseX - app.world.viewX);
            my = (app.world.mouseY - app.world.viewY);
            app.xspeed = Math.min(Math.max(((app.world.x - app.world.viewX) - app.world.mouseX), -16), 16);
            app.world.vspeed = Math.min(Math.max(((app.world.y - app.world.viewY) - app.world.mouseY), -16), 16);
            my = (int) app.world.vspeed;
            app.world.keepMoving = false;
            mx = (int) app.xspeed;
            my = -my / 7;
            mx = -mx / 7;
            getMessage(app.out);
        }

        @Override
        public int getCost() {
            return 200;
        }

        @Override
        public String getName() {
            return "AirJump";
        }

        @Override
        public String getTip() {
            return "<html>An agile air spell<br>Low-Moderate Energy Cost<br>Fly in a chosen direction</html>";
        }

        @Override
        public void getPassiveAction(Client app) {
            // throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
            world.entityList.add(new EffectEntity(px, py, mx, my, world.random.nextInt(40), Color.WHITE).setID(eid));
        }

    }

    public static class AirbendingTornado extends Spell {
        public AirbendingTornado() {
            ID = Server.AIRBENDING;
            subID = 2;
            try {
                icon = (loadIcon("https://west-it.webs.com/spells/airTornado.png"));
            } catch (Exception ex) {
                Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void getAction(Client app) {
            // throw new UnsupportedOperationException("Not supported yet.");
            X = app.world.mouseX + app.world.viewX;
            Y = app.world.mouseY + app.world.viewY;
            mx = -Math.min(Math.max(((app.world.x - app.world.viewX) - app.world.mouseX) / 2, -2), 2);
            my = 0;
            getMessage(app.out);
        }

        @Override
        public int getCost() {
            return 500;
        }

        @Override
        public String getName() {
            return "Tornado";
        }

        @Override
        public String getTip() {
            return "<html>An intermediate air spell<br>High Energy Cost<br>Summons a tornado</html>";
        }

        @Override
        public void getPassiveAction(Client app) {
            // throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
            world.entityList.add(new TornadoEntity(px, py, mx, pid).setID(eid));
        }
    }

    public static class AirbendingGust extends Airbending {
        public AirbendingGust() {
            ID = Server.AIRBENDING;
            subID = 4;
            try {
                icon = (loadIcon("https://west-it.webs.com/spells/airGust.png"));
            } catch (Exception ex) {
                Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void getAction(Client app) {
            ID = Server.AIRBENDING;
            subID = 4;
            // throw new UnsupportedOperationException("Not supported yet.");
            mx = app.world.mouseX + app.world.viewX;
            my = app.world.mouseY + app.world.viewY;
            X = app.world.pressX + app.world.viewX;
            Y = app.world.pressY + app.world.viewY;
            if (app.world.isSolid(X, Y) || app.pointDis(app.world.x, app.world.y, X, Y) > 300) {
                app.energico += this.getCost();
                return;
            }
            double direction = Client.pointDir(mx, my, X, Y);
            // direction+=180;
            mx = -((int) (Client.lengthdir_x(12, direction)));
            my = ((int) (Client.lengthdir_y(12, direction)));
            maker = ID;
            getMessage(app.out);
        }

        @Override
        public int getCost() {
            return 200;
        }

        @Override
        public String getName() {
            return "AirGust";
        }

        @Override
        public String getTip() {
            return "<html>An advanced air spell<br>Low-Moderate Energy Cost<br>Summons a gust</html>";
        }

        @Override
        public void getPassiveAction(Client app) {
            // throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
            world.entityList.add(new GustEntity(px, py, mx, my, pid).setID(eid));
        }
    }

    public static class AirbendingAir extends Airbending {
        public AirbendingAir() {
            ID = Server.AIRBENDING;
            subID = 5;
            try {
                icon = new ImageIcon(
                        ResourceLoader.loadImage("https://west-it.webs.com/spells/airAir.png", "airAir.png"));
            } catch (Exception ex) {
                Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void getAction(Client app) {
            // throw new UnsupportedOperationException("Not supported yet.");
            mx = app.world.mouseX + app.world.viewX;
            my = app.world.mouseY + app.world.viewY;
            X = app.world.x;
            Y = app.world.y;
            maker = ID;
            getMessage(app.out);
        }

        @Override
        public int getCost() {
            return 75;
        }

        @Override
        public String getName() {
            return "Air Clear";
        }

        @Override
        public String getTip() {
            return "<html>A useful air spell<br>Very Low Energy Cost<br>Digs you out of tight spots</html>";
        }

        @Override
        public void getPassiveAction(Client app) {
            // throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
            world.ground.ClearCircle(px, py, 48);
        }
    }

    public static class AirRun extends Airbending {
        boolean recharge = false;

        public AirRun() {
            ID = Server.AIRBENDING;
            subID = 3;

            try {
                icon = (loadIcon("https://west-it.webs.com/spells/airrun.png"));
            } catch (Exception ex) {
                Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void getAction(Client app) {
        }

        int number = 0;

        @Override
        public void onSpawn(Client me) {
            number = 0;
            for (Spell e : me.spellList[me.spellBook]) {
                if (e instanceof Airbending) {
                    number++;
                }
            }
        }

        @Override
        public void getPassiveAction(Client app) {
            app.runningSpeed = 1 + (0.19 * (double) (number));
        }

        @Override
        public int getCost() {
            return 0;
        }

        @Override
        public String getName() {
            return "Air Run";
        }

        @Override
        public String getTip() {
            return "<html>A passive air spell<br>Move faster; Jump higher</html>";
        }

        @Override
        public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    public static class AirAffinity extends Airbending {
        boolean recharge = false;

        public AirAffinity() {
            ID = Server.AIRBENDING;
            subID = 7;

            try {
                icon = (loadIcon("https://west-it.webs.com/spells/airfloat.png"));
            } catch (Exception ex) {
                Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void getAction(Client app) {
        }

        int number = 0;

        @Override
        public void onSpawn(Client me) {
            number = 0;
            for (Spell e : me.spellList[me.spellBook]) {
                if (e instanceof Airbending) {
                    number++;
                }
            }
        }

        @Override
        public void getPassiveAction(Client app) {
            app.maxlungs = 150 + (75 * number);
            app.world.floatiness = 2;
        }

        @Override
        public int getCost() {
            return 0;
        }

        @Override
        public String getName() {
            return "Air Affinity";
        }

        @Override
        public String getTip() {
            return "<html>A passive air spell<br>Fall more slowly<br>Have more lung capacity</html>";
        }

        @Override
        public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    public static class Earthbending extends Spell {
        public Earthbending() {
            ID = Server.EARTHBENDING;
            subID = 0;
            try {
                icon = (loadIcon("https://west-it.webs.com/spells/earthRock.png"));
            } catch (Exception ex) {
                Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void getAction(Client app) {
            // throw new UnsupportedOperationException("Not supported yet.");
            X = app.world.x;
            Y = app.world.y - World.head;
            mx = app.world.viewX;
            my = app.world.mouseY - app.world.viewY;
            double direction = 360 - Client.pointDir(app.world.x - app.world.viewX, app.world.y - app.world.viewY,
                    app.world.mouseX, app.world.mouseY);
            // direction+=180;
            if (app.world.isSolid(app.world.x, app.world.y - World.head + 1)) {
                app.energico += 50;
            }
            mx = ((int) (Client.lengthdir_x(12, direction)));
            my = ((int) (Client.lengthdir_y(12, direction)));
            maker = ID;
            getMessage(app.out);
        }

        @Override
        public int getCost() {
            return 150;
        }

        @Override
        public String getName() {
            return "EarthBall";
        }

        @Override
        public String getTip() {
            return "<html>A basic earth spell<br>Low Energy Cost<br>Highly affected by gravity<br>Deals moderate damage</html>";
        }

        @Override
        public void getPassiveAction(Client app) {
            // throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    public static class EarthbendingSpike extends Earthbending {
        public EarthbendingSpike() {
            ID = Server.EARTHBENDING;
            subID = 1;
            try {
                icon = (loadIcon("https://west-it.webs.com/spells/earth.png"));
            } catch (Exception ex) {
                Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void getAction(Client app) {
            // throw new UnsupportedOperationException("Not supported yet.");
            ID = Server.EARTHBENDING;
            subID = 1;
            mx = app.world.mouseX + app.world.viewX;
            my = app.world.mouseY + app.world.viewY;
            X = app.world.pressX + app.world.viewX;
            Y = app.world.pressY + app.world.viewY;

            double direction = Client.pointDir(mx, my, X, Y);
            if ((mx == X) && (my == Y)) {
                direction = 90;
            }
            // direction+=180;

            mx = X - ((int) (Client.lengthdir_x(72, direction)));
            my = Y + ((int) (Client.lengthdir_y(72, direction)));
            maker = ID;
            if (!app.world.isSolid(X, Y) || app.pointDis(app.world.x, app.world.y, X, Y) > 300
                    || !app.world.inBounds(mx, my) || !app.world.inBounds(mx - 56, my)
                    || !app.world.inBounds(mx + 56, my)) {
                app.energico += this.getCost();
                return;
            }
            if (app.world.isSolid(app.world.x, app.world.y + 4)) {
                app.energico += 150;
            }
            getMessage(app.out);
        }

        @Override
        public int getCost() {
            return 450;
        }

        @Override
        public String getName() {
            return "EarthSpike";
        }

        @Override
        public String getTip() {
            return "<html>An intermediate earth spell<br>Moderate-High Energy Cost<br>Summon a spike from the ground</html>";
        }

        @Override
        public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    public static class EarthbendingShard extends Earthbending {
        public EarthbendingShard() {
            ID = Server.EARTHBENDING;
            subID = 2;
            try {
                icon = (loadIcon("https://west-it.webs.com/spells/earthShard.png"));
            } catch (Exception ex) {
                Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void getAction(Client app) {
            X = app.world.x;
            Y = app.world.y - World.head;
            mx = app.world.viewX;
            my = app.world.mouseY - app.world.viewY;
            double direction = 360 - Client.pointDir(app.world.x - app.world.viewX,
                    app.world.y - World.head - app.world.viewY, app.world.mouseX, app.world.mouseY);
            // direction+=180;
            if (app.world.isSolid(app.world.x, app.world.y + 4)) {
                app.energico += 50;
            }
            mx = ((int) (Client.lengthdir_x(32, direction)));
            my = ((int) (Client.lengthdir_y(32, direction)));
            maker = ID;
            getMessage(app.out);
        }

        @Override
        public int getCost() {
            return 200;
        }

        @Override
        public String getName() {
            return "EarthShard";
        }

        @Override
        public String getTip() {
            return "<html>A basic earth spell<br>Low-Moderate Energy Cost<br>Travels quickly in a straight line<br>Deals moderate damage</html>";
        }

        @Override
        public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    public static class EarthbendingSand extends Earthbending {
        public EarthbendingSand() {
            ID = Server.EARTHBENDING;
            subID = 4;
            try {
                icon = (loadIcon("https://west-it.webs.com/spells/earthSand.png"));
            } catch (Exception ex) {
                Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void getAction(Client app) {
            // throw new UnsupportedOperationException("Not supported yet.");
            X = app.world.x;
            Y = app.world.y - World.head;
            mx = app.world.viewX;
            my = app.world.mouseY - app.world.viewY;
            double direction = 360 - Client.pointDir(app.world.x - app.world.viewX,
                    app.world.y - World.head - app.world.viewY, app.world.mouseX, app.world.mouseY);
            // direction+=180;
            if (app.world.isSolid(app.world.x, app.world.y + 4)) {
                app.energico += 50;
            }
            mx = ((int) (Client.lengthdir_x(18, direction)));
            my = ((int) (Client.lengthdir_y(18, direction)));
            maker = ID;
            getMessage(app.out);
        }

        @Override
        public int getCost() {
            return 325;
        }

        @Override
        public String getName() {
            return "Sand Shotgun";
        }

        @Override
        public String getTip() {
            return "<html>An advanced earth spell<br>Moderate Energy Cost<br>Highly affected by gravity<br>Deals High damage</html>";
        }

        @Override
        public void getPassiveAction(Client app) {
            // throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    public static class EarthbendingWallOfSand extends Earthbending {
        public EarthbendingWallOfSand() {
            ID = Server.EARTHBENDING;
            subID = 5;
            try {
                icon = (loadIcon("https://west-it.webs.com/spells/earthwos.png"));
            } catch (Exception ex) {
                Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void getAction(Client app) {
            // throw new UnsupportedOperationException("Not supported yet.");
            X = app.world.pressX + app.world.viewX;
            Y = app.world.pressY + app.world.viewY;
            mx = 12;
            my = 12;
            if (app.world.isSolid(app.world.x, app.world.y + 4)) {
                app.energico += 100;
            }
            if (app.world.inBounds((int) app.world.x, (int) (app.world.y - World.head + 4))
                    && app.world.ground.cellData[(int) app.world.x][(int) app.world.y + 4] == World.SAND) {
                app.energico += 200;
            }
            maker = ID;
            getMessage(app.out);
        }

        @Override
        public int getCost() {
            return 425;
        }

        @Override
        public String getName() {
            return "Wall of Sand";
        }

        @Override
        public String getTip() {
            return "<html>A basic earth spell<br>Moderate-High Energy Cost<br>Creates sand where you need it</html>";
        }

        @Override
        public void getPassiveAction(Client app) {
            // throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    public static class EarthbendingShield extends Earthbending {
        public EarthbendingShield() {
            ID = Server.EARTHBENDING;
            subID = 3;
            try {
                icon = (loadIcon("https://west-it.webs.com/spells/earthshield.png"));
            } catch (Exception ex) {
                Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        int number = 0;

        @Override
        public void onSpawn(Client me) {
            number = 0;
            for (Spell e : me.spellList[me.spellBook]) {
                if (e instanceof Earthbending) {
                    number++;
                }
            }
            getPassiveAction(me);
        }

        @Override
        public void getAction(Client app) {
            if (app.energico > 0 && app.HP < app.MAXHP) {
                app.energico -= 150;
                app.HP += 5;
            }
        }

        @Override
        public void getPassiveAction(Client app) {
            /*
             * if (app.energico>0) { app.energico--; }
             */
            app.MAXHP = (short) (110 + (10 * number));

        }

        @Override
        public int getCost() {
            return 0;
        }

        @Override
        public String getName() {
            return "Earth Shield";
        }

        @Override
        public String getTip() {
            return "<html>A passive earth spell<br>Increase maximum health</html>";
        }

        @Override
        public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    public static class EarthbendingStance extends Earthbending {
        public EarthbendingStance() {
            ID = Server.EARTHBENDING;
            subID = 7;
            try {
                icon = (loadIcon("https://west-it.webs.com/spells/earthstance.png"));
            } catch (Exception ex) {
                Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        int number = 0;

        @Override
        public void onSpawn(Client me) {
            number = 0;
            for (Spell e : me.spellList[me.spellBook]) {
                if (e instanceof Earthbending) {
                    number++;
                }
            }
            getPassiveAction(me);
        }

        @Override
        public void getPassiveAction(Client app) {
            app.knockbackDecay = 0.8 - ((double) number / 10d);
            app.world.floatiness = -7 * (number + 1);
        }

        @Override
        public String getName() {
            return "Earth Stance";
        }

        @Override
        public String getTip() {
            return "<html>A passive earth spell<br>Decrease knockback</html>";
        }

        @Override
        public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    public static class Waterbending extends Spell {
        public Waterbending() {
            ID = Server.WATERBENDING;
            subID = 0;
            try {
                icon = (loadIcon("https://west-it.webs.com/spells/waterSpell.png"));
            } catch (Exception ex) {
                Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void getAction(Client app) {
            // throw new UnsupportedOperationException("Not supported yet.");
            X = app.world.x;
            Y = app.world.y - World.head;
            if (app.world.isLiquid(app.world.x, app.world.y)) {
                app.energico += 125;
            }
            mx = app.world.viewX;
            my = app.world.mouseY - app.world.viewY;
            double direction = 360 - Client.pointDir(app.world.x - app.world.viewX,
                    app.world.y - World.head - app.world.viewY, app.world.mouseX, app.world.mouseY);
            // direction+=180;
            mx = ((int) (Client.lengthdir_x(8, direction)));
            my = ((int) (Client.lengthdir_y(8, direction)));
            maker = ID;
            getMessage(app.out);
        }

        @Override
        public int getCost() {
            return 150;
        }

        @Override
        public String getName() {
            return "WaterBall";
        }

        @Override
        public String getTip() {
            return "<html>An essential water spell<br>Low Energy Cost<br>Highly affected by gravity<br>Creates a pool of water</html>";
        }

        @Override
        public void getPassiveAction(Client app) {
            // throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    public static class WaterbendingFreeze extends Waterbending {
        public WaterbendingFreeze() {
            ID = Server.WATERBENDING;
            subID = 1;
            try {
                icon = (loadIcon("https://west-it.webs.com/spells/freezeSpell.png"));
            } catch (Exception ex) {
                Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void getAction(Client app) {
            // throw new UnsupportedOperationException("Not supported yet.");
            X = app.world.x;
            Y = app.world.y - World.head;
            if (app.world.isLiquid(app.world.x, app.world.y)) {
                app.energico += 100;
            }
            mx = app.world.viewX;
            my = app.world.mouseY - app.world.viewY;
            double direction = 360 - Client.pointDir(app.world.x - app.world.viewX,
                    app.world.y - World.head - app.world.viewY, app.world.mouseX, app.world.mouseY);
            // direction+=180;
            mx = ((int) (Client.lengthdir_x(12, direction)));
            my = ((int) (Client.lengthdir_y(12, direction)));
            maker = ID;
            getMessage(app.out);
        }

        @Override
        public int getCost() {
            return 250;
        }

        @Override
        public String getName() {
            return "Freeze";
        }

        @Override
        public String getTip() {
            return "<html>A basic water spell<br>Low-Moderate Energy Cost<br>Highly affected by gravity<br>Freezes nearby water</html>";
        }

        @Override
        public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    public static class WaterSpout extends Waterbending {
        public WaterSpout() {
            ID = Server.WATERBENDING;
            subID = 2;
            try {
                icon = (loadIcon("https://west-it.webs.com/spells/waterspout.png"));
            } catch (Exception ex) {
                Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void getAction(Client app) {
            // throw new UnsupportedOperationException("Not supported yet.");
            mx = app.world.mouseX + app.world.viewX;
            my = app.world.mouseY + app.world.viewY;
            X = mx;
            Y = my;
            if (app.world.ground.cellData[(int) X][(int) Y] != World.WATER) {
                app.energico += this.getCost();
                return;
            }
            if (app.world.isLiquid(app.world.x, app.world.y)) {
                app.energico += 150;
            }
            maker = ID;
            getMessage(app.out);
        }

        @Override
        public int getCost() {
            return 450;
        }

        @Override
        public String getName() {
            return "WaterSpout";
        }

        @Override
        public String getTip() {
            return "<html>An intermediate water spell<br>Moderate-High Energy Cost<br>Summon a waterspout from a water source</html>";
        }

        @Override
        public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    public static class WaterbendingShard extends Waterbending {
        public WaterbendingShard() {
            ID = Server.WATERBENDING;
            subID = 4;
            try {
                icon = (loadIcon("https://west-it.webs.com/spells/watershard.png"));
            } catch (Exception ex) {
                Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void getAction(Client app) {
            // throw new UnsupportedOperationException("Not supported yet.");
            X = app.world.x;
            Y = app.world.y - World.head;
            if (app.world.isLiquid(app.world.x, app.world.y)) {
                app.energico += 50;
            }
            mx = app.world.viewX;
            my = app.world.mouseY - app.world.viewY;
            double direction = 360 - Client.pointDir(app.world.x - app.world.viewX,
                    app.world.y - World.head - app.world.viewY, app.world.mouseX, app.world.mouseY);
            // direction+=180;
            mx = ((int) (Client.lengthdir_x(9, direction)));
            my = ((int) (Client.lengthdir_y(9, direction)));
            maker = ID;
            getMessage(app.out);
        }

        @Override
        public int getCost() {
            return 250;
        }

        @Override
        public String getName() {
            return "Ice Shard";
        }

        @Override
        public String getTip() {
            return "<html>An intermediate water spell<br>Low-Moderate Energy Cost<br>Not affected by gravity<br>Deals light damage<br>Creates an ice block</html>";
        }

        @Override
        public void getPassiveAction(Client app) {
            // throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    public static class WaterStorm extends Waterbending {
        public WaterStorm() {
            ID = Server.WATERBENDING;
            subID = 6;
            icon = new ImageIcon(
                    ResourceLoader.loadImage("https://west-it.webs.com/spells/waterStorm.png", "waterStorm.png"));
        }

        @Override
        public void getAction(Client app) {
            // throw new UnsupportedOperationException("Not supported yet.");
            mx = app.world.mouseX + app.world.viewX;
            my = app.world.mouseY + app.world.viewY;
            X = mx;
            Y = my;
            if (app.world.isLiquid(app.world.x, app.world.y)) {
                app.energico += 150;
            }
            maker = ID;
            getMessage(app.out);
        }

        @Override
        public int getCost() {
            return 656;
        }

        @Override
        public String getName() {
            return "Storm";
        }

        @Override
        public String getTip() {
            return "<html>An intermediate water spell<br>Very High Energy Cost<br>Summon a storm cloud</html>";
        }

        @Override
        public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    public static class BreathUnderWater extends Waterbending {
        public BreathUnderWater() {
            ID = Server.WATERBENDING;
            subID = 3;
            try {
                icon = (loadIcon("https://west-it.webs.com/spells/BreathUnderWater.png"));
            } catch (Exception ex) {
                Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void getAction(Client app) {
        }

        int number = 0;

        @Override
        public void onSpawn(Client me) {
            number = 0;
            for (Spell e : me.spellList[me.spellBook]) {
                if (e instanceof Waterbending) {
                    number++;
                }
            }
        }

        @Override
        public void getPassiveAction(Client app) {
            if (app.world.inBounds(app.world.x, app.world.y) && app.energico > 0
                    && app.world.isType((int) app.world.x, (int) app.world.y, World.WATER)) {
                if (app.HP < app.MAXHP) {
                    app.energico -= 15 + (5 - number) * 5;
                    app.HP++;
                }
                if (number >= 0) {
                    app.lungs = app.maxlungs;
                }
            }
        }

        @Override
        public int getCost() {
            return 0;
        }

        @Override
        public String getName() {
            return "Water Heal";
        }

        @Override
        public String getTip() {
            return "<html>A passive water spell<br>High Energy Cost<br>Heal when in water</html>";
        }

        @Override
        public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

    }

    public static class WaterTreading extends Waterbending {
        public WaterTreading() {
            ID = Server.WATERBENDING;
            subID = 23;
            try {
                icon = (loadIcon("https://west-it.webs.com/spells/waterswim.png"));
            } catch (Exception ex) {
                Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void getAction(Client app) {
        }

        int number = 0;

        @Override
        public void onSpawn(Client me) {
            number = 0;
            for (Spell e : me.spellList[me.spellBook]) {
                if (e instanceof Waterbending) {
                    number++;
                }
            }
        }

        @Override
        public void getPassiveAction(Client app) {
            Client.swimmingSpeed = 1 + (0.23 * (double) (number));
            if (app.world.inBounds(app.world.x, app.world.y)
                    && app.world.ground.cellData[(int) app.world.x][(int) app.world.y] == World.WATER) {
                app.lungs = app.maxlungs;
            }
        }

        @Override
        public int getCost() {
            return 0;
        }

        @Override
        public String getName() {
            return "Water Treader";
        }

        @Override
        public String getTip() {
            return "<html>A passive water spell<br>Move faster through fluids</html>";
        }

        @Override
        public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    public static class Firebending extends Spell {
        public Firebending() {
            ID = Server.FIREBENDING;
            subID = 0;
            try {
                icon = (loadIcon("https://west-it.webs.com/spells/fireball.png"));
            } catch (Exception ex) {
                Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void getAction(Client app) {
            // throw new UnsupportedOperationException("Not supported yet.");
            X = app.world.x;
            Y = app.world.y - World.head;
            if (app.world.isLiquid(X, Y)) {
                app.energico += 50;
            }
            mx = app.world.viewX;
            my = app.world.mouseY - app.world.viewY;
            double direction = 360 - Client.pointDir(app.world.x - app.world.viewX,
                    app.world.y - World.head - app.world.viewY, app.world.mouseX, app.world.mouseY);
            // direction+=180;
            mx = ((int) (Client.lengthdir_x(12, direction)));
            my = ((int) (Client.lengthdir_y(12, direction)));
            maker = ID;
            getMessage(app.out);
        }

        @Override
        public int getCost() {
            return 250;
        }

        @Override
        public String getName() {
            return "Fireball";
        }

        @Override
        public String getTip() {
            return "<html>A basic fire spell<br>Low-Moderate Energy Cost<br>Shoot a fire ball<br>Lightly effected by gravity</html>";
        }

        @Override
        public void getPassiveAction(Client app) {
            // throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    public static class Firebending_Lava extends Firebending {
        public Firebending_Lava() {
            ID = Server.FIREBENDING;
            subID = 1;
            try {
                icon = (loadIcon("https://west-it.webs.com/spells/lava.png"));
            } catch (Exception ex) {
                Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void getAction(Client app) {
            // throw new UnsupportedOperationException("Not supported yet.");
            X = app.world.x;
            Y = app.world.y - World.head;
            if (app.world.isLiquid(X, Y)) {
                app.energico += 50;
            }
            mx = app.world.viewX;
            my = app.world.mouseY - app.world.viewY;
            double direction = 360 - Client.pointDir(app.world.x - app.world.viewX,
                    app.world.y - World.head - app.world.viewY, app.world.mouseX, app.world.mouseY);
            // direction+=180;
            mx = ((int) (Client.lengthdir_x(8, direction)));
            my = ((int) (Client.lengthdir_y(8, direction)));
            maker = ID;
            getMessage(app.out);
        }

        @Override
        public int getCost() {
            return 450;
        }

        @Override
        public String getName() {
            return "Lavaball";
        }

        @Override
        public String getTip() {
            return "<html>An intermediate fire spell<br>Moderate-High Energy Cost<br>Shoot a lava ball<br>Lightly effected by gravity<br>Creates a pool of lava</html>";
        }

        @Override
        public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    public static class Firebending_Thrower extends Firebending {
        public Firebending_Thrower() {
            ID = Server.FIREBENDING;
            subID = 5;
            try {
                icon = (loadIcon("https://west-it.webs.com/spells/flamer.png"));
            } catch (Exception ex) {
                Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void getAction(Client app) {
            // throw new UnsupportedOperationException("Not supported yet.");
            X = app.world.x;
            Y = app.world.y - World.head;
            mx = app.world.viewX;
            my = app.world.mouseY - app.world.viewY;
            double direction = 360 - Client.pointDir(app.world.x - app.world.viewX,
                    app.world.y - World.head - app.world.viewY, app.world.mouseX, app.world.mouseY);
            // direction+=180;
            mx = ((int) (Client.lengthdir_x(4, direction)));
            my = ((int) (Client.lengthdir_y(4, direction)));
            maker = ID;
            getMessage(app.out);
        }

        @Override
        public int getCost() {
            return 250;
        }

        @Override
        public String getName() {
            return "Flame Thrower";
        }

        @Override
        public String getTip() {
            return "<html>A basic fire spell<br>Low-Moderate Energy Cost<br>Set your foes on fire!<br>Lightly effected by gravity</html>";
        }

        @Override
        public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    public static class Firebending_Wall extends Firebending {
        public Firebending_Wall() {
            ID = Server.FIREBENDING;
            subID = 4;
            try {
                icon = (loadIcon("https://west-it.webs.com/spells/fireWall.png"));
            } catch (Exception ex) {
                Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void getAction(Client app) {
            // throw new UnsupportedOperationException("Not supported yet.");
            X = app.world.x;
            Y = app.world.y - World.head;
            mx = app.world.viewX;
            my = app.world.mouseY - app.world.viewY;
            maker = ID;
            getMessage(app.out);
            app.world.leftArmAngle = 225;
            app.world.rightArmAngle = 315;
        }

        @Override
        public int getCost() {
            return 750;
        }

        @Override
        public String getName() {
            return "Fire Wall";
        }

        @Override
        public String getTip() {
            return "<html>An advanced fire spell<br>Very Very High Energy Cost<br>Projects two separating columns of fire<br>Not effected by gravity</html>";
        }

        @Override
        public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    public static class FirebendingJump extends Spell {
        public FirebendingJump() {
            ID = Server.FIREBENDING;
            subID = 2;
            try {
                icon = (loadIcon("https://west-it.webs.com/spells/firejump.png"));
            } catch (Exception ex) {
                Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void getAction(Client app) {
            // throw new UnsupportedOperationException("Not supported yet.");
            X = app.world.x;
            Y = app.world.y - World.head;
            mx = (app.world.mouseX - app.world.viewX);
            my = (app.world.mouseY - app.world.viewY);
            app.xspeed = -Math.min(Math.max(((app.world.x - app.world.viewX) - app.world.mouseX), -16), 16);
            app.world.vspeed = -Math
                    .min(Math.max(((app.world.y - World.head - app.world.viewY) - app.world.mouseY), -16), 16);
            my = (int) app.world.vspeed;
            app.world.keepMoving = false;
            mx = (int) app.xspeed;
            // my=-my;mx=-mx;
            getMessage(app.out);
        }

        @Override
        public int getCost() {
            return 400;
        }

        @Override
        public String getName() {
            return "FireJump";
        }

        @Override
        public String getTip() {
            return "<html>An agile fire spell<br>Moderate-High Energy Cost<br>Fly in a chosen direction<br>Burn those in your path</html>";
        }

        @Override
        public void getPassiveAction(Client app) {
            // throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    public static class FirebendingCharge extends Firebending {
        int charge = 0;

        public FirebendingCharge() {
            ID = Server.FIREBENDING;
            subID = 3;
            try {
                icon = (loadIcon("https://west-it.webs.com/spells/firecharge.png"));
            } catch (Exception ex) {
                Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void getAction(Client app) {
            // throw new UnsupportedOperationException("Not supported yet.");
        }

        int number = 0;

        @Override
        public void onSpawn(Client me) {
            number = 0;
            for (Spell e : me.spellList[me.spellBook]) {
                if (e instanceof Firebending) {
                    number++;
                }
            }
            Client.inputer.doublecast = number;
        }

        @Override
        public void getPassiveAction(Client app) {

            // throw new UnsupportedOperationException("Not supported yet.");
            /*
             * if (app.energico<app.maxeng) { if (charge++>1) { app.HP--; charge = 0; }
             * app.energico+=app.engrecharge*3; }
             */
        }

        @Override
        public int getCost() {
            return 0;
        }

        @Override
        public String getName() {
            return "Fire Charge";
        }

        @Override
        public String getTip() {
            return "<html>A passive fire spell<br>Have a chance at casting twice as many fire spells</html>";
        }

        @Override
        public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    public static class FireProof extends Firebending {
        int charge = 0;

        public FireProof() {
            ID = Server.FIREBENDING;
            subID = 7;
            try {
                icon = (loadIcon("https://west-it.webs.com/spells/fireproof.png"));
            } catch (Exception ex) {
                Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void getAction(Client app) {
            // throw new UnsupportedOperationException("Not supported yet.");
        }

        int number = 0;

        @Override
        public void onSpawn(Client me) {
            number = 0;
            for (Spell e : me.spellList[me.spellBook]) {
                if (e instanceof Firebending) {
                    number++;
                }
            }
            Client.inputer.doublecast = number;
        }

        @Override
        public String getName() {
            return "Fireproof";
        }

        @Override
        public String getTip() {
            return "<html>A passive fire spell<br>Fire damage recharges your batteries</html>";
        }

        @Override
        public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    public static class Lightning extends Spell {
        public Lightning() {
            ID = Server.LIGHTNING;
            subID = 0;
            locked = true;
            unlockXP = 250;
            try {
                icon = new ImageIcon(ResourceLoader.loadImage("https://west-it.webs.com/spells/lightningstrike.png",
                        "lightningstrike.png"));
            } catch (Exception ex) {
                Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void getAction(Client app) {
            // throw new UnsupportedOperationException("Not supported yet.");
            ID = Server.LIGHTNING;
            subID = 0;
            X = app.world.x;
            Y = app.world.y - World.head;
            mx = app.world.viewX;
            my = app.world.mouseY - app.world.viewY;
            double direction = 360 - Client.pointDir(app.world.x - app.world.viewX,
                    app.world.y - World.head - app.world.viewY, app.world.mouseX, app.world.mouseY);
            // direction+=180;
            mx = ((int) (Client.lengthdir_x(8, direction)));
            my = ((int) (Client.lengthdir_y(8, direction)));
            maker = ID;
            app.HP -= 1;
            getMessage(app.out);
        }

        @Override
        public int getCost() {
            return 0;
        }

        @Override
        public String getName() {
            return "Lightning Strike";
        }

        @Override
        public void getPassiveAction(Client app) {
            // throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public String getTip() {
            return "<html>A basic lightning spell<br>Low Health Cost<br>Travels in a straight line<br>Deals low damage<br>Restores energy in blast vicinity</html>";
        }

        @Override
        public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    public static class LightningStorm extends Lightning {
        public LightningStorm() {
            ID = Server.LIGHTNING;
            subID = 1;
            locked = true;
            unlockXP = 500;
            try {
                icon = new ImageIcon(ResourceLoader.loadImage("https://west-it.webs.com/spells/lightningstorm.png",
                        "lightningstorm.png"));
            } catch (Exception ex) {
                Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void getAction(Client app) {
            // throw new UnsupportedOperationException("Not supported yet.");
            X = app.world.mouseX + app.world.viewX;
            Y = app.world.mouseY + app.world.viewY;
            mx = -Math.min(Math.max(((app.world.x - app.world.viewX) - app.world.mouseX) / 10, -2), 2);
            my = 0;
            maker = ID;
            app.HP -= 3;
            getMessage(app.out);
        }

        @Override
        public int getCost() {
            return 250;
        }

        @Override
        public String getName() {
            return "Lightning Storm";
        }

        @Override
        public void getPassiveAction(Client app) {
            // throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public String getTip() {
            return "<html>An intermediate lightning spell<br>Low-Moderate Energy Cost<br>Low Health Cost<br>Fires from overhead<br>Deals low damage<br>Restores energy in blast vicinity</html>";
        }

        @Override
        public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    public static class LightningBall extends Lightning {
        public LightningBall() {
            ID = Server.LIGHTNING;
            subID = 2;
            locked = true;
            unlockXP = 750;
            try {
                icon = new ImageIcon(ResourceLoader.loadImage("https://west-it.webs.com/spells/lightningball.png",
                        "lightningball.png"));
            } catch (Exception ex) {
                Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void getAction(Client app) {
            // throw new UnsupportedOperationException("Not supported yet.");
            X = app.world.x;
            Y = app.world.y - World.head;
            mx = app.world.viewX;
            my = app.world.mouseY - app.world.viewY;
            double direction = 360 - Client.pointDir(app.world.x - app.world.viewX,
                    app.world.y - World.head - app.world.viewY, app.world.mouseX, app.world.mouseY);
            // direction+=180;
            mx = ((int) (Client.lengthdir_x(8, direction)));
            my = ((int) (Client.lengthdir_y(8, direction)));
            maker = ID;
            app.HP -= 3;
            getMessage(app.out);
        }

        @Override
        public int getCost() {
            return 10;
        }

        @Override
        public String getName() {
            return "Lightning Ball";
        }

        @Override
        public void getPassiveAction(Client app) {
            // throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public String getTip() {
            return "<html>A basic lightning spell<br>Low Health Cost<br>Lightly affected by gravity<br>Deals low damage<br>High knockback</html>";
        }

        @Override
        public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    public static class LightningMine extends Lightning {
        public LightningMine() {
            ID = Server.LIGHTNING;
            subID = 3;
            locked = true;
            unlockXP = 1000;
            try {
                icon = new ImageIcon(ResourceLoader.loadImage("https://west-it.webs.com/spells/lightningmine.png",
                        "lightningmine.png"));
            } catch (Exception ex) {
                Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void getAction(Client app) {
            X = app.world.x;
            Y = app.world.y - World.head;
            mx = app.world.viewX;
            my = app.world.mouseY - app.world.viewY;
            double direction = 360 - Client.pointDir(app.world.x - app.world.viewX,
                    app.world.y - World.head - app.world.viewY, app.world.mouseX, app.world.mouseY);
            // direction+=180;
            mx = ((int) (Client.lengthdir_x(8, direction)));
            my = ((int) (Client.lengthdir_y(8, direction)));
            maker = ID;
            getMessage(app.out);
            app.HP -= 5;
        }

        @Override
        public int getCost() {
            return 100;
        }

        @Override
        public String getName() {
            return "Static Charge";
        }

        @Override
        public void getPassiveAction(Client app) {
        }

        @Override
        public String getTip() {
            return "<html>An intermediate lightning spell<br>Low Health Cost<br>Low Energy Cost<br>Creates a static charge<br>Deals low damage<br></html>";
        }

        @Override
        public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    public static class LightningRod extends Lightning {
        public LightningRod() {
            ID = Server.LIGHTNING;
            subID = 5;
            locked = true;
            unlockXP = 1250;
            try {
                icon = loadIcon("https://west-it.webs.com/spells/shocktower.png");
            } catch (Exception ex) {
                Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void getAction(Client app) {

            X = app.world.x;
            Y = app.world.y - World.head;
            mx = 0;
            my = 0;
            maker = ID;
            getMessage(app.out);
        }

        @Override
        public int getCost() {
            return 200;
        }

        @Override
        public String getName() {
            return "Lightning Rod";
        }

        @Override
        public void getPassiveAction(Client app) {
        }

        @Override
        public String getTip() {
            return "<html>An intermediate lightning spell<br>Low-Moderate Energy Cost<br>Strongly affected by gravity<br>Drains enemy energy</html>";
        }

        @Override
        public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    public static class LightningShield extends Lightning {
        int charge = 0;

        public LightningShield() {
            ID = Server.LIGHTNING;
            subID = 4;
            try {
                icon = (loadIcon("https://west-it.webs.com/spells/lightningshield.png"));
            } catch (Exception ex) {
                Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void getAction(Client app) {
            // throw new UnsupportedOperationException("Not supported yet.");
        }

        int number = 0;

        @Override
        public void onSpawn(Client me) {
            number = 0;
            for (Spell e : me.spellList[me.spellBook]) {
                if (e instanceof Lightning) {
                    number++;
                }
            }
            me.shockdrain = number;
        }

        @Override
        public void getPassiveAction(Client app) {

        }

        @Override
        public int getCost() {
            return 0;
        }

        @Override
        public String getName() {
            return "Lightning Shield";
        }

        @Override
        public String getTip() {
            return "<html>A passive lightning spell<br>Have a chance at converting health loss into energy loss</html>";
        }

        @Override
        public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    public static class LightningOvercharge extends Lightning {
        int charge = 0;

        public LightningOvercharge() {
            ID = Server.LIGHTNING;
            subID = 4;
            try {
                icon = (loadIcon("https://west-it.webs.com/spells/lightningovercharge.png"));
            } catch (Exception ex) {
                Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void getAction(Client app) {
        }

        int number = 0;

        @Override
        public void onSpawn(Client me) {
            number = 1;
            for (Spell e : me.spellList[me.spellBook]) {
                if (e instanceof Lightning) {
                    number++;
                }
            }

        }

        @Override
        public void getPassiveAction(Client app) {
            app.maxeng = 1000 + (number * 166);
        }

        @Override
        public int getCost() {
            return 0;
        }

        @Override
        public String getName() {
            return "Overcharged";
        }

        @Override
        public String getTip() {
            return "<html>A passive lightning spell<br>Have more energy</html>";
        }

        @Override
        public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    public static class Darkness extends Spell {
        public Darkness() {
            ID = Server.DARKNESS;
            subID = 0;
            locked = true;
            unlockXP = 2000;
            try {
                icon = loadIcon("https://west-it.webs.com/spells/salt.png");
            } catch (Exception ex) {
                Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void getAction(Client app) {
            app.turnVisible = 40 * 3;
            app.world.status |= World.ST_INVISIBLE;
            app.sendMovement();
        }

        @Override
        public int getCost() {
            return 300;
        }

        @Override
        public String getName() {
            return "Dark Cloak";
        }

        @Override
        public void getPassiveAction(Client app) {
        }

        @Override
        public String getTip() {
            return "<html>A basic darkness spell<br>Moderate Energy Cost<br>Turn invisible for a short time</html>";
        }

        @Override
        public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    public static class DarkSoulBall extends Spell {
        public DarkSoulBall() {
            ID = Server.DARKNESS;
            subID = 1;
            locked = true;
            unlockXP = 2500;
            try {
                icon = loadIcon("https://west-it.webs.com/spells/shadowBall.png");
            } catch (Exception ex) {
                Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void getAction(Client app) {
            X = app.world.x;
            Y = app.world.y - World.head;
            mx = app.world.viewX;
            my = app.world.mouseY - app.world.viewY;
            double direction = 360 - Client.pointDir(app.world.x - app.world.viewX,
                    app.world.y - World.head - app.world.viewY, app.world.mouseX, app.world.mouseY);
            // direction+=180;
            mx = ((int) (Client.lengthdir_x(8, direction)));
            my = ((int) (Client.lengthdir_y(8, direction)));
            maker = ID;
            getMessage(app.out);
        }

        @Override
        public int getCost() {
            return 400;
        }

        @Override
        public String getName() {
            return "Soul Sucker";
        }

        @Override
        public void getPassiveAction(Client app) {
        }

        @Override
        public String getTip() {
            return "<html>A basic darkness spell<br>Moderate-High Energy Cost<br>Steal the health of your foe<br>Unaffected by gravity</html>";
        }

        @Override
        public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    public static class DarkSummonBall extends Spell {
        public DarkSummonBall() {
            ID = Server.DARKNESS;
            subID = 2;
            locked = true;
            unlockXP = 3500;
            try {
                icon = loadIcon("https://west-it.webs.com/spells/shadowSummon.png");
            } catch (Exception ex) {
                Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void getAction(Client app) {
            X = app.world.x;
            Y = app.world.y - World.head;
            mx = app.world.viewX;
            my = app.world.mouseY - app.world.viewY;
            double direction = 360 - Client.pointDir(app.world.x - app.world.viewX,
                    app.world.y - World.head - app.world.viewY, app.world.mouseX, app.world.mouseY);
            mx = ((int) (Client.lengthdir_x(8, direction)));
            my = ((int) (Client.lengthdir_y(8, direction)));
            maker = ID;
            getMessage(app.out);
        }

        @Override
        public int getCost() {
            return 555;
        }

        @Override
        public String getName() {
            return "Necromancy";
        }

        @Override
        public void getPassiveAction(Client app) {
        }

        @Override
        public String getTip() {
            return "<html>An intermediate darkness spell<br>High Energy Cost<br>Summon an undead minion</html>";
        }

        @Override
        public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    public static class DarkAura extends Spell {
        public DarkAura() {
            ID = Server.DARKNESS;
            subID = 2;
            locked = true;
            unlockXP = 3000;
            try {
                icon = loadIcon("https://west-it.webs.com/spells/aura.png");
            } catch (Exception ex) {
                Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void getAction(Client app) {
            app.removeAura = 40 * 5;
            app.world.status |= World.ST_DRAIN;
            app.sendMovement();
        }

        @Override
        public int getCost() {
            return 500;
        }

        @Override
        public String getName() {
            return "Dark Aura";
        }

        @Override
        public void getPassiveAction(Client app) {
        }

        @Override
        public String getTip() {
            return "<html>An intermediate darkness spell<br>High Energy Cost<br>For five seconds, nearby players are hurt</html>";
        }

        @Override
        public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    public static class DarkTeleport extends Spell {
        public DarkTeleport() {
            ID = Server.DARKNESS;
            subID = 5;
            locked = true;
            unlockXP = 5000;
            try {
                icon = loadIcon("https://west-it.webs.com/spells/teleport.png");
            } catch (Exception ex) {
                Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void getAction(Client app) {
            mx = app.world.mouseX + app.world.viewX;
            my = app.world.mouseY + app.world.viewY;
            if (Client.pointDis(app.world.x, app.world.y, mx, my) < 600) {
                app.world.x = mx;
                app.world.y = my;
                app.sendMovement();
                app.energico = 0;
            } else {
                app.energico += getCost();
            }
        }

        @Override
        public int getCost() {
            return 1000;
        }

        @Override
        public String getName() {
            return "Dark Gate";
        }

        @Override
        public void getPassiveAction(Client app) {
        }

        @Override
        public String getTip() {
            return "<html>An intermediate darkness spell<br>Total Energy Cost<br>Teleport to a point of your choice</html>";
        }

        @Override
        public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    ImageIcon icon;

    public final ImageIcon getImage() {
        return icon;
    }

    public static class NOSPELL extends Spell {
        public NOSPELL() {
            try {
                icon = new ImageIcon(
                        ResourceLoader.loadImage("https://west-it.webs.com/spells/nospell.png", "nospell.png"));
            } catch (Exception ex) {
                Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void getAction(Client app) {

        }

        @Override
        public int getCost() {
            return 0;
        }

        @Override
        public String getName() {
            return "None";
        }

        @Override
        public void getPassiveAction(Client app) {
        }

        @Override
        public String getTip() {
            return "<html>Nothing</html>";
        }

        @Override
        public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    public static class Burito extends Spell {
        public Burito() {
            ID = Server.FIREBENDING;
            subID = 9;
            locked = true;
            try {
                icon = (loadIcon("https://west-it.webs.com/spells/fireball.png"));
            } catch (Exception ex) {
                Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void getAction(Client app) {
            X = app.world.x;
            Y = app.world.y - World.head;
            app.HP -= 15;
            mx = app.world.viewX;
            my = app.world.mouseY - app.world.viewY;
            double direction = 360 - Client.pointDir(app.world.x - app.world.viewX,
                    app.world.y - World.head - app.world.viewY, app.world.mouseX, app.world.mouseY);
            mx = ((int) (Client.lengthdir_x(12, direction)));
            my = ((int) (Client.lengthdir_y(12, direction)));
            maker = ID;
            getMessage(app.out);
        }

        @Override
        public int getCost() {
            return 0;
        }

        @Override
        public String getName() {
            return "Burito";
        }

        @Override
        public String getTip() {
            return "<html>HAX<br>Moderate Health Cost</html>";
        }

        @Override
        public void getPassiveAction(Client app) {
        }

        @Override
        public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }


    
    public static class FireMaster extends Spell {
        public FireMaster() {
            ID = Server.FIREBENDING;
            subID = 10;
            locked = true;
            try {
                icon = (loadIcon("https://west-it.webs.com/spells/fireball.png"));
            } catch (Exception ex) {
                Logger.getLogger(Spell.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void getAction(Client app) {
            X = app.world.x;
            Y = app.world.y - World.head;
            if (app.world.isLiquid(X, Y)) {
                app.energico += 50;
            }
            mx = app.world.viewX;
            my = app.world.mouseY - app.world.viewY;
            double direction = 360 - Client.pointDir(app.world.x - app.world.viewX,
                    app.world.y - World.head - app.world.viewY, app.world.mouseX, app.world.mouseY);
            mx = ((int) (Client.lengthdir_x(12, direction)));
            my = ((int) (Client.lengthdir_y(12, direction)));
            maker = ID;
            getMessage(app.out);
        }

        @Override
        public int getCost() {
            return 100;
        }

        @Override
        public String getName() {
            return "Fire Master";
        }

        @Override
        public String getTip() {
            return "<html>HAX</html>";
        }

        @Override
        public void getPassiveAction(Client app) {
        }

        @Override
        public void getActionNetwork(World world, int px, int py, int mx, int my, int pid, int eid, ByteBuffer buf) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}