/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johnwesthoff.bending.logic;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.johnwesthoff.bending.Client;
import com.johnwesthoff.bending.Constants;
import com.johnwesthoff.bending.Server;
import com.johnwesthoff.bending.entity.Entity;
import com.johnwesthoff.bending.entity.ExplosionEntity;
import com.johnwesthoff.bending.entity.HouseEntity;
import com.johnwesthoff.bending.entity.WaterEntity;
import com.johnwesthoff.bending.util.network.ResourceLoader;

/**
 *
 * @author John
 */
public class World implements Serializable {
    public int incX;
    public int incY;
    public Random random = new Random();
    public CollisionChecker ground;
    public final CopyOnWriteArrayList<Entity> entityList = new CopyOnWriteArrayList<>();
    public float x = 450;
    public float y = 0;
    public int floatiness = 0;
    public short status = 0;
    /*
     * ~~~~~~~~~~~~~~~~~~~~~Status values~~~~~~~~~~~~~~~~~~~~~ Index: Effect: 0
     * Flaming 1 Shocked 8 Invisible 9 Dark Cloud
     * 
     * 
     */
    public static final int ST_FLAMING = 0b1;
    public static final int ST_SHOCKED = 0b10;
    public static final int ST_INVISIBLE = 0b100000000;
    public static final int ST_DRAIN = 0b1000000000;
    public static final int AURA_RADIUS = 96;
    public int wIdTh = 900, hEigHt = 900;
    public int viewX = 0;
    public int viewY = 0;
    int viewxX = 0;
    int viewyY = 0;
    public int viewdX = 0;
    public int viewdY = 0;
    public final ArrayList<Player> playerList = new ArrayList<>();
    public boolean serverWorld = false;
    public boolean dead = false;
    BufferedImage grass;
    BufferedImage sky;
    BufferedImage sand;
    BufferedImage stone;
    BufferedImage ice;
    BufferedImage night;
    BufferedImage crystal;
    BufferedImage ether;
    BufferedImage bark;
    public double vspeed = 0;
    public int mouseX = 150;
    public int mouseY = 0;
    int v;
    int previousX = 0;
    int previousY = 0;
    public int pressX;
    public int pressY;
    public double move = 0;
    public boolean keys[] = new boolean[3200];
    float jump = 0;
    boolean MB1;
    boolean MB2;
    boolean mb3;
    public int landTexSize = 256;
    public int fps = 0;
    public static final int head = 26;
    public static final int body = 13;
    int flowCount = 0;
    int maxFlow = 5000;
    public TexturePaint skyPaint;
    public TexturePaint grassPaint;
    TexturePaint sandPaint;
    TexturePaint stonePaint;
    TexturePaint barkPaint;
    TexturePaint icePaint;
    TexturePaint nightPaint;
    public static final byte AIR = 0;
    public static final byte GROUND = 1;
    public static final byte WATER = 2;
    public static final byte OIL = 3;
    public static final byte LAVA = 4;
    public static final byte SAND = 5;
    public static final byte STONE = 6;
    public static final byte TREE = 7;
    public static final byte ICE = 8;
    public static final byte CRYSTAL = 9;
    public static final byte ETHER = 10;
    public final byte[] liquidList = { WATER, OIL, LAVA, SAND, ETHER };
    public final int liquidStats[][] = new int[liquidList.length][6];
    public final byte[] solidList = { SAND, GROUND, STONE, TREE, ICE, CRYSTAL };
    public final byte[] aList = new byte[127];
    public int miGenH = 300;
    public int maGenH = 300;
    public Color waterColor = new Color(0, 255, 255, 127);
    public Color oilColor = new Color(12, 12, 12, 200);
    BufferedImage iter = new BufferedImage(Constants.WIDTH_INT+12, Constants.HEIGHT_INT+12, BufferedImage.TYPE_INT_ARGB);
    public Graphics2D gter = iter.createGraphics();
    public int idinator = 0;
    public double fr;
    public Server lol;

    public World() {
        this(true, 900, 900, null, null, null, null, null, null, null, null, null, null);
        x = 150;
        maxFlow = 150000;
    }

    public int ID = 0;

    public World(boolean server, int width, int height, Image terrai, BufferedImage grass, BufferedImage sand,
            BufferedImage sky, BufferedImage stone, BufferedImage bark, BufferedImage ice, BufferedImage lavaland,
            BufferedImage crystal, BufferedImage ether) {
        serverWorld = server;
        Arrays.sort(liquidList);
        for (int i = 0; i < aList.length; i++) {
            aList[i] = -1;
        }
        aList[WATER] = (byte) Arrays.binarySearch(liquidList, WATER);
        aList[LAVA] = (byte) Arrays.binarySearch(liquidList, LAVA);
        aList[SAND] = (byte) Arrays.binarySearch(liquidList, SAND);
        aList[OIL] = (byte) Arrays.binarySearch(liquidList, OIL);
        aList[ETHER] = (byte) Arrays.binarySearch(liquidList, ETHER);
        // 0 is the down speed
        // 1 is the horizontal speed
        // 2 is the color
        // 3 is deprecated
        liquidStats[aList[WATER]][0] = 20;// 5
        liquidStats[aList[WATER]][1] = 20;// 9
        liquidStats[aList[WATER]][2] = waterColor.getRGB();
        liquidStats[aList[WATER]][3] = 30;

        liquidStats[aList[ETHER]][0] = 20;// 5
        liquidStats[aList[ETHER]][1] = 20;// 9
        liquidStats[aList[ETHER]][2] = waterColor.getRGB();
        liquidStats[aList[ETHER]][3] = 30;

        liquidStats[aList[LAVA]][0] = 14;// 3
        liquidStats[aList[LAVA]][1] = 8;// 6
        liquidStats[aList[LAVA]][2] = Color.red.getRGB();
        liquidStats[aList[LAVA]][3] = 60;

        liquidStats[aList[OIL]][0] = 5;// 5
        liquidStats[aList[OIL]][1] = 6;// 6
        liquidStats[aList[OIL]][2] = oilColor.getRGB();
        liquidStats[aList[OIL]][3] = 10;

        liquidStats[aList[SAND]][0] = 8;// 1
        liquidStats[aList[SAND]][1] = 1;// 1
        liquidStats[aList[SAND]][2] = 0;
        liquidStats[aList[SAND]][3] = 50;

        wIdTh = width;
        hEigHt = height;
        this.sky = sky;
        this.grass = grass;
        this.sand = sand;
        this.stone = stone;
        this.bark = bark;
        this.ice = ice;
        night = lavaland;
        this.crystal = crystal;
        this.ether = ether;
        if (!serverWorld) {
            skyPaint = new TexturePaint(this.sky, new Rectangle(200, 200));
            grassPaint = new TexturePaint(this.grass, new Rectangle(256, 256));
            sandPaint = new TexturePaint(this.sand, new Rectangle(256, 256));
            stonePaint = new TexturePaint(this.stone, new Rectangle(256, 256));
            barkPaint = new TexturePaint(this.bark, new Rectangle(256, 256));
            icePaint = new TexturePaint(this.ice, new Rectangle(256, 256));
            nightPaint = new TexturePaint(night, new Rectangle(300, 300));
            landTexSize = grassPaint.getImage().getWidth();
        }
        ground = new CollisionChecker(wIdTh, hEigHt);
        if (!serverWorld) {
            ground.clearCircle(150, 1, 300);
            ground.clearCircle(450, 1, 300);
            ground.clearCircle(750, 1, 300);
            ground.clearCircle(750, 900, 450);
        } else {
            ground.clearCircle(150, 0, 150);
        }
    }

    Image[] bodyParts;// Body, head, ua, la, ul, ll
    Thread loader;
    boolean done = false;
    public String username;
    public int idddd;

    public String getPlayerName(int id) {
        if (id == idddd) {
            return username;
        }
        for (Player p : playerList) {
            if (p.ID == id) {
                return p.username;
            }
        }
        return "No One";
    }

    public void load(final byte parts[], final int colors[], final int colors2[]) {
        Runnable getStuff = new Runnable() {
            @Override
            public void run() {
                bodyParts = new Image[parts.length];
                try {
                    for (int i = 0; i < parts.length; i++) {
                        bodyParts[i] = ResourceLoader.loadImage(
                                "https://west-it.webs.com/bodyParts/p" + (i + 1) + "_" + parts[i] + ".png",
                                "p" + (i + 1) + "_" + parts[i] + ".png");
                        bodyParts[i] = World.changeColor((BufferedImage) bodyParts[i], Color.white,
                                new Color(colors[i]));
                        bodyParts[i] = World.changeColor((BufferedImage) bodyParts[i], Color.lightGray,
                                new Color(colors2[i]));
                        bodyParts[i] = World.changeColor((BufferedImage) bodyParts[i], new Color(0xBEBEBE),
                                new Color(colors2[i]).darker());
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                bodyParts[0] = World.changeColor((BufferedImage) bodyParts[0], Color.white, Color.red);
                done = true;
            }
        };
        loader = new Thread(getStuff);
        loader.start();
    }

    public final class CollisionChecker {
        public byte[][] cellData = new byte[wIdTh][hEigHt];
        public int w = wIdTh;
        public int h = hEigHt;
        public Thread collisionProcess;

        CollisionChecker(int sizex, int sizey) {
            cellData = new byte[sizex][sizey];
            w = sizex;
            h = sizey;
            this.fillRect(0, 0, w, h);
        }

        public void destroyExpansion(float x, float y, float type, float replace) {
            destroyExpansionI((int) x, (int) y, (int) type, (byte) replace);
        }

        public void destroyExpansionI(int x, int y, int type, byte replace) {
            if (!isType(x, y, type) || !inBounds(x, y))
                return;
            cellData[x][y] = replace;
            destroyExpansionI(x + 1, y, type, replace);
            destroyExpansionI(x, y + 1, type, replace);
            destroyExpansionI(x - 1, y, type, replace);
            destroyExpansionI(x, y - 1, type, replace);
        }

        byte flipped = 1;

        public void handleWater() {
            try {
                flowCount = 0;
                flipped *= -1;
                int minx;
                int maxx;
                int miny;
                int maxy;
                minx = 0;
                    maxx = w - 1;
                    miny = 0;
                    maxy = h - 2;
                for (int x1 = minx; x1 < maxx; x1++) {
                    for (int y1 = maxy; y1 > miny; y1--) {
                        if (aList[cellData[x1][y1]] == -1) {
                            continue;
                        }
                        if (cellData[x1][y1 + 1] == AIR) {
                            int i = 1;
                            while (cellData[x1][y1 + i] == AIR) {
                                if (y1 + i >= h - 2 || i > liquidStats[aList[cellData[x1][y1]]][0]) {
                                    break;
                                }
                                i++;
                            }
                            i--;
                            cellData[x1][y1 + i] = cellData[x1][y1];
                            cellData[x1][y1] = AIR;
                            if (flowCount++ > maxFlow)
                                return;
                        } else {
                            for (int i = 0; i <= liquidStats[aList[cellData[x1][y1]]][1]; i++) {
                                if (cellData[Math.min(Math.max(x1 + (i * flipped), 0), w - 1)][y1 + 1] == AIR) {
                                    cellData[Math.min(Math.max(x1 + (i * flipped), 0), w - 1)][y1 + 1] = cellData[x1][y1];
                                    cellData[x1][y1] = AIR;
                                    if (flowCount++ > maxFlow)
                                        return;
                                    break;
                                }
                                if (cellData[Math.min(Math.max(x1 - (i * flipped), 0), w - 1)][y1 + 1] == AIR) {
                                    cellData[Math.min(Math.max(x1 - (i * flipped), 0), w - 1)][y1 + 1] = cellData[x1][y1];
                                    cellData[x1][y1] = AIR;
                                    if (flowCount++ > maxFlow)
                                        return;
                                    break;
                                }
                            }
                            if (cellData[x1][y1] == LAVA)
                                for (int e = -1; e <= 1; e++) {
                                    if (inBounds(x1 + e, y1 + 1) && cellData[x1 + e][y1 + 1] == WATER) {
                                        cellData[x1][y1] = STONE;
                                        cellData[x1 + e][y1 + 1] = STONE;
                                        break;
                                    }
                                    if (inBounds(x1 + e, y1 - 1f) && cellData[x1 + e][y1 - 1] == WATER) {
                                        cellData[x1][y1] = STONE;
                                        cellData[x1 + e][y1 - 1] = STONE;
                                        break;
                                    }
                                }
                            for (int e = -1; e <= 1; e++) {
                                if (inBounds(x1 + e, y1 + 1f) && cellData[x1 + e][y1 + 1] == OIL
                                        && cellData[x1][y1] == WATER) {
                                    cellData[x1][y1] += cellData[x1 + e][y1 + 1];
                                    cellData[x1 + e][y1 + 1] = (byte) (cellData[x1][y1] - cellData[x1 + e][y1 + 1]);
                                    cellData[x1][y1] = (byte) (cellData[x1][y1] - cellData[x1 + e][y1 + 1]);
                                    e = 10;
                                }
                            }
                            if (cellData[x1][y1] == SAND && (inBounds(x1, y1 + 1f) && cellData[x1][y1 + 1] == WATER)) {
                                cellData[x1][y1 + 1] = SAND;
                                cellData[x1][y1] = WATER;
                            }


                        }

                    }
                }
            } catch (Exception e) {
                Logger.getLogger(World.class.getName()).log(Level.SEVERE, null, e);
            }
        }

        public void showData() {
            for (int i1 = 0; i1 < w; i1++) {
                for (int i2 = 0; i2 < h; i2++) {
                    Logger.getLogger(World.class.getName()).log(Level.INFO, "{0}",String.valueOf(cellData[i1][i2]));
                }
                System.out.println();
            }
        }

        /**
         * Clears a circle using the standard square method
         * 
         * @param x - X coordinate of the circle's center
         * @param y - Y coordinate of the circle's center
         * @param r - Radius of the circle
         */
        public void clearCircle(int x, int y, int r) {
            for (int i1 = Math.max(x - (r + 1), 0); i1 < Math.min(x + (r + 1), w); i1++) {
                for (int i2 = Math.max(y - (r + 1), 0); i2 < Math.min(y + (r + 1), h); i2++) {
                    if (Math.round(Math.sqrt(Math.pow(i1 - x, 2) + Math.pow(i2 - y, 2))) < (r / 2)) {
                        if (cellData[i1][i2] == OIL && random.nextInt(10) == 2) {
                                cellData[i1][i2] = AIR;
                                entityList.add(new ExplosionEntity(i1, i2, 8, 1));
                                clearCircle(i1, i2, 10);

                        }
                        if (cellData[i1][i2] != CRYSTAL && cellData[i1][i2] != ETHER) {
                            cellData[i1][i2] = AIR;
                        }
                    }
                }
            }
        }

        public void clearCircleStrong(int x, int y, int r) {
            for (int i1 = Math.max(x - (r + 1), 0); i1 < Math.min(x + (r + 1), w); i1++) {
                for (int i2 = Math.max(y - (r + 1), 0); i2 < Math.min(y + (r + 1), h); i2++) {
                    if (Math.round(Math.sqrt(Math.pow(i1 - x, 2) + Math.pow(i2 - y, 2))) < (r / 2)) {
                        if (cellData[i1][i2] == OIL && random.nextInt(10) == 2) {
                                cellData[i1][i2] = AIR;
                                entityList.add(new ExplosionEntity(i1, i2, 8, 1));
                                clearCircle(i1, i2, 10);

                        }
                        cellData[i1][i2] = AIR;
                    }
                }
            }

        }


        public void fillPolygon(Polygon polygon, byte type) {
            int localX;
            int localY;
            int minX = w;
            int minY = h;
            int maxX = 0;
            int maxY = 0;

            for (int i = 0; i < polygon.npoints; i++) {
                localX = polygon.xpoints[i];
                localY = polygon.ypoints[i];
                if (localX > maxX) {
                    maxX = localX > w ? w : localX;
                }
                if (localY > maxY) {
                    maxY = localY > h ? w : localY;
                }
                if (localX < minX) {
                    minX = localX < 0 ? 0 : localX;
                }
                if (localY < minY) {
                    minY = localY < 0 ? 0 : localY;
                }
            }
            for (localX = minX; localX <= maxX; localX++) {
                for (localY = minY; localY <= maxY; localY++) {
                    if (polygon.contains(localX, localY) && (cellData[localX][localY] != CRYSTAL && cellData[localX][localY] != ETHER)) {
                            cellData[localX][localY] = type;
                    }
                }
            }
        }

        public void fillCircle(int x, int y, int r) {
            for (int i1 = Math.max(x - (r + 1), 0); i1 < Math.min(x + (r + 1), w); i1++) {
                for (int i2 = Math.max(y - (r + 1), 0); i2 < Math.min(y + (r + 1), h); i2++) {
                    if (Math.round(Math.sqrt(Math.pow(i1 - x, 2) + Math.pow(i2 - y, 2))) < (r / 2f) + .1
                        && (cellData[i1][i2] != CRYSTAL && cellData[i1][i2] != ETHER)
                    ) {
                            cellData[i1][i2] = GROUND;
                    }
                }
            }
        }

        public void fillCircleW(int x, int y, int r, byte t) {
            for (int i1 = Math.max(x - (r + 1), 0); i1 < Math.min(x + (r + 1), w); i1++) {
                for (int i2 = Math.max(y - (r + 1), 0); i2 < Math.min(y + (r + 1), h); i2++) {
                    if (Math.round(Math.sqrt(Math.pow(i1 - x, 2) + Math.pow(i2 - y, 2))) < (r / 2f) + .1
                    && (cellData[i1][i2] != CRYSTAL && cellData[i1][i2] != ETHER))
                    {
                            cellData[i1][i2] = t;
                    }
                }
            }
        }

        public void freeze(int x, int y, int r) {
            for (int i1 = Math.max(x - (r + 1), 0); i1 < Math.min(x + (r + 1), w); i1++) {
                for (int i2 = Math.max(y - (r + 1), 0); i2 < Math.min(y + (r + 1), h); i2++) {
                    if (Math.round(Math.sqrt(Math.pow(i1 - x, 2) + Math.pow(i2 - y, 2))) < (r / 2f) + .1
                            && cellData[i1][i2] == WATER) {
                        cellData[i1][i2] = ICE;
                    }
                }
            }
        }

        public int sandinate(int x, int y, int r) {
            int toReturn = 0;
            for (int i1 = Math.max(x - (r + 1), 0); i1 < Math.min(x + (r + 1), w); i1++) {
                for (int i2 = Math.max(y - (r + 1), 0); i2 < Math.min(y + (r + 1), h); i2++) {
                    if (Math.round(Math.sqrt(Math.pow(i1 - x, 2) + Math.pow(i2 - y, 2))) < (r / 2f) + .1
                            && cellData[i1][i2] == SAND) {
                        cellData[i1][i2] = AIR;
                        toReturn++;
                    }
                }
            }
            return toReturn;
        }

        public void puddle(int x, int y, int r) {
            for (int i1 = Math.max(x - (r + 1), 0); i1 < Math.min(x + (r + 1), w); i1++) {
                for (int i2 = Math.max(y - (r + 1), 0); i2 < Math.min(y + (r + 1), h); i2++) {
                    if (Math.round(Math.sqrt(Math.pow(i1 - x, 2) + Math.pow(i2 - y, 2))) < (r / 2f) + .1
                            && cellData[i1][i2] == AIR) {
                        cellData[i1][i2] = WATER;
                    }
                }
            }
        }

        public void fillRect(int x, int y, int h, int w) {
            for (int i1 = Math.max(x, 0); i1 < Math.min(x + w, this.w); i1++) {
                for (int i2 = Math.max(y, 0); i2 < Math.min(y + h, this.h); i2++) {
                    cellData[i1][i2] = GROUND;
                }
            }
        }

        public void fillRectW(int x, int y, int h, int w, byte with) {
            for (int i1 = Math.max(x, 0); i1 < Math.min(x + w, this.w); i1++) {
                for (int i2 = Math.max(y, 0); i2 < Math.min(y + h, this.h); i2++) {
                    if (cellData[i1][i2] != CRYSTAL && cellData[i1][i2] != ETHER)
                        cellData[i1][i2] = with;
                }
            }
        }

        public void clearLine(int x1, int y1, int x2, int y2, int r) {
            double direction = (pointDir(x1, y1, x2, y2));
            double distance = pointDis(x1, y1, x2, y2);
            int f = (int) Math.ceil(distance / r);
            double localX = x1;
            double localY = y1;
            for (double i = 0; i <= f; i++) {
                clearCircle((int) localX, (int) localY, r);
                localX += lengthDirX(distance / f, direction);
                localY += lengthDirY(distance / f, direction);
            }
        }

        public void generater(int x, int w, byte type) {

            float vs = -random.nextInt(12);
            float grav = random.nextFloat();
            float time = 12;
            for (int e = x; e < x + w; e++) {
                if (random.nextInt(1200) == 5) {
                    entityList.add(new HouseEntity(5 + e, maGenH, 12 + random.nextInt(24), 12 + random.nextInt(24)));
                }
                maGenH = (maGenH > 700) ? 700 : (maGenH < 10) ? 10 : maGenH;
                for (int i = maGenH; i < h; i++)
                    cellData[e][i] = type;
                maGenH += vs;
                vs += grav;
                if (e % time == 0) {
                    time = random.nextInt(30) + 1f;
                    grav = random.nextFloat();
                    vs = -random.nextInt(8);
                }
            }
        }

        public void generatel(int x, int w, byte type) {
            float vs = -random.nextInt(12);
            float grav = random.nextFloat();
            float time = 12;
            for (int e = x; e < x + w; e++) {
                miGenH = (miGenH > 450) ? 450 : (miGenH < 10) ? 10 : miGenH;
                for (int i = miGenH; i < h; i++)
                    cellData[e][i] = type;
                miGenH += vs;
                vs += grav;
                if (e % time == 0) {
                    time = random.nextInt(30) + 1f;
                    grav = random.nextFloat();
                    vs = -random.nextInt(8);
                }
            }

        }

        public void clearRect(int x, int y, int h, int w) {
            for (int i1 = Math.max(x, 0); i1 < Math.min(x + w, this.w); i1++) {
                for (int i2 = Math.max(y, 0); i2 < Math.min(y + h, this.h); i2++) {
                    if (cellData[i1][i2] == GROUND)
                        cellData[i1][i2] = AIR;
                }
            }

        }

    }

    public int toKeepMove = 0;
    public int jumpHeight = 900;
    public int osc = 1;
    public boolean keepMoving = false;

    public boolean isSolid(double x, double y) {
        try {
            if (!inBounds(x, y))
                return true;
            return (ground.cellData[(int) x][(int) y] == GROUND || ground.cellData[(int) x][(int) y] == TREE
                    || ground.cellData[(int) x][(int) y] == SAND || ground.cellData[(int) x][(int) y] == STONE
                    || ground.cellData[(int) x][(int) y] == ICE || ground.cellData[(int) x][(int) y] == CRYSTAL);
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }

    public boolean isIce(int x, int y) {
        try {
            if (!inBounds(x, y))
                return false;
            return (ground.cellData[x][y] == ICE);
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }

    public boolean isLiquid(float x, float y) {
        try {
            if (!inBounds(x, y))
                return false;
            return (ground.cellData[(int) x][(int) y] == OIL || ground.cellData[(int) x][(int) y] == ETHER
                    || ground.cellData[(int) x][(int) y] == WATER || ground.cellData[(int) x][(int) y] == LAVA);
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }

    public boolean isType(int x, int y, int t) {
        try {
            return inBounds(x, y) && ground.cellData[x][y] == t;
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }

    double xVel = 0;

    public void onUpdate() {
        if (serverWorld) {
            for (int i = 0; i < entityList.size(); i++) {
                Entity e = entityList.get(i);
                e.move();
            }

            handleEntitiesForServer();

            for (int i = 0; i < entityList.size(); i++) {
                Entity e = entityList.get(i);
                e.onUpdate(this);
                if (!e.alive) {
                    entityList.remove(e);
                }
            }
        }
        flowCount = 0;
        move -= Math.signum(move) * fr;
        if (Math.abs(move) < fr) {
            move = 0;
        }
        if ((y > jumpHeight || x == previousX) && keepMoving) {
            vspeed = 0;
            keepMoving = false;
        }
        if (!isSolid(x, y - 4)) {
            y -= 4;
            if (inBounds(x + move, y + (int) vspeed)) {
                double toMove = move;
                double xxx1 = x + 3;
                double yyy1 = y - 4;
                double xxx2 = x - 3;
                double yyy2 = y - 4;
                if (isLiquid(x, y)) {
                    toMove *= Client.swimmingSpeed;
                } else {
                    toMove *= Client.runningSpeed;
                }
                while (true) {
                    yyy1 += 1;
                    if (!inBounds(xxx1, yyy1) || isSolid(xxx1, yyy1) ) {
                        break;
                    }
                }
                while (true) {
                    yyy2 += 1;
                    if (!inBounds(xxx2, yyy2) || isSolid(xxx2, yyy2)) {
                        break;
                    }
                }
                if (keepMoving) {
                    toMove = toKeepMove;
                }
                if (move > 4)
                    move = 4;
                if (move < -4)
                    move = -4;
                double deltaX = !(isSolid(x + toMove, y + (int) vspeed) || isSolid(x + toMove, y)) ? toMove : 0;
                x += deltaX * deltaTime();
            }
            for (int i = 0; i < 4; i++) {
                if (y > 0 && isSolid(x, y + 1)) {
                    break;
                }
                y += 1;
            }
        }
        boolean canifall = true;
        for (int i = 1; i <= 4; i++) {
            if (isSolid(x, y + i)) {
                canifall = false;
                break;
            }
        }
        if (canifall) {
            vspeed = Math.min(4f - floatiness, vspeed + 1);
        } else {
            vspeed = Math.min(0, vspeed);
            keepMoving = false;

            if (jump > 0) {
                vspeed = (int) (-10 * jump);
                move *= 2;

            }
        }
        int s = (int) Math.signum(vspeed);
        canifall = true;
        if (vspeed != 0) {
            for (int i = 0; Math.abs(i) <= Math.abs(vspeed); i += s) {
                if (isSolid(x, y + i)) {
                    canifall = false;
                    break;
                }
            }
        }
        if (canifall) {
            y += vspeed;
        } else {
            vspeed = 0;
            keepMoving = false;
        }
        if (isLiquid(x, y - (int) (head * .75))) {
            keepMoving = false;
            if (jump > 0) {
                vspeed = -2;
            } else {
                vspeed /= 2;
            }
        }

        if (burn++ > 2) {
            firePolygonred.reset();
            int[] xx = new int[12];
            int[] yy = new int[12];
            xx[0] = (0) * Constants.WIDTH_SCALE;
            yy[0] = (0);
            firePolygonred.addPoint(xx[0], yy[0]);
            int dir = 100 + random.nextInt(45);
            int len = 48 + random.nextInt(48);
            xx[1] = xx[0] + (int) Client.lengthdir_x(len, dir);
            yy[1] = yy[0] + (int) Client.lengthdir_y(len, dir);
            firePolygonred.addPoint(xx[1], yy[1]);
            for (int i = 2; i < xx.length; i++) {
                if (i % 2 == 1) {
                    dir = 35 + random.nextInt(90);
                    len = 48 + random.nextInt(36);
                    xx[i] = xx[i - 1] + (int) Client.lengthdir_x(len, dir);
                    yy[i] = yy[i - 1] + (int) Client.lengthdir_y(len, dir);
                } else {
                    dir = 225 + random.nextInt(90);
                    len = 20 + random.nextInt(36);
                    xx[i] = xx[i - 1] + (int) Client.lengthdir_x(len, dir);
                    yy[i] = yy[i - 1] + (int) Client.lengthdir_y(len, dir);
                }
                firePolygonred.addPoint(xx[i], yy[i]);
            }

            firePolygonorange.reset();
            xx = new int[12];
            yy = new int[12];
            xx[0] = (0);
            yy[0] = (0);
            firePolygonorange.addPoint(xx[0], yy[0]);
            dir = 100 + random.nextInt(45);
            len = 40 + random.nextInt(40);
            xx[1] = xx[0] + (int) Client.lengthdir_x(len, dir);
            yy[1] = yy[0] + (int) Client.lengthdir_y(len, dir);
            firePolygonorange.addPoint(xx[1], yy[1]);
            for (int i = 2; i < xx.length; i++) {
                if (i % 2 == 1) {
                    dir = 35 + random.nextInt(90);
                    len = 40 + random.nextInt(28);
                    xx[i] = xx[i - 1] + (int) Client.lengthdir_x(len, dir);
                    yy[i] = yy[i - 1] + (int) Client.lengthdir_y(len, dir);
                } else {
                    dir = 225 + random.nextInt(90);
                    len = 12 + random.nextInt(28);
                    xx[i] = xx[i - 1] + (int) Client.lengthdir_x(len, dir);
                    yy[i] = yy[i - 1] + (int) Client.lengthdir_y(len, dir);
                }
                firePolygonorange.addPoint(xx[i], yy[i]);
            }

            firePolygonyellow.reset();
            xx = new int[12];
            yy = new int[12];
            xx[0] = (0);
            yy[0] = (0);
            firePolygonyellow.addPoint(xx[0], yy[0]);
            dir = 100 + random.nextInt(45);
            len = 32 + random.nextInt(32);
            xx[1] = xx[0] + (int) Client.lengthdir_x(len, dir);
            yy[1] = yy[0] + (int) Client.lengthdir_y(len, dir);
            firePolygonyellow.addPoint(xx[1], yy[1]);
            for (int i = 2; i < xx.length; i++) {
                if (i % 2 == 1) {
                    dir = 35 + random.nextInt(90);
                    len = 32 + random.nextInt(20);
                    xx[i] = xx[i - 1] + (int) Client.lengthdir_x(len, dir);
                    yy[i] = yy[i - 1] + (int) Client.lengthdir_y(len, dir);
                } else {
                    dir = 225 + random.nextInt(90);
                    len = 4 + random.nextInt(20);
                    xx[i] = xx[i - 1] + (int) Client.lengthdir_x(len, dir);
                    yy[i] = yy[i - 1] + (int) Client.lengthdir_y(len, dir);
                }
                firePolygonyellow.addPoint(xx[i], yy[i]);
            }
            burn = 0;
        }
        if (x > wIdTh) {
            x--;
        }
        if (x < 0) {
            x++;
        }
    }

    public void onDraw(Graphics g) {

        viewX = (int) Math.min(Math.max((x - (Constants.WIDTH_INT+1)/2f) + incX, 0),Math.max(0,wIdTh - Constants.WIDTH_INT-1));

        viewY = (int) Math.min(Math.max((y - Constants.HEIGHT_INT/2f) + incY, 0), Math.max(0,hEigHt - Constants.HEIGHT_INT));
        if (dead) {
            viewX = viewdX;
            viewY = viewdY;
        }
        
        if (viewX < 0) {
            viewX = 0;
        }
        if (viewY < 0) {
            viewY = 0;
        }
        

        try {
            ground.handleWater();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            drawTerrain((Graphics2D) g);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    int left = 1;
    int burn = 0;
    public double leftArmAngle = 90;
    public double rightArmAngle = 90;

    public void drawEntities(Graphics g) {
        for (int i = 0; i < entityList.size(); i++) {
            Entity e = entityList.get(i);
            e.move();
            e.onUpdate(this);
            e.onDraw(g, viewX, viewY);
            if (!e.alive) {
                entityList.remove(e);
            }
        }
    }

    public static final Polygon firePolygonred = new Polygon();
    public static final Polygon firePolygonyellow = new Polygon();
    public static final Polygon firePolygonorange = new Polygon();

    public void drawPlayers(Graphics g) {
        float offs = x % 8;
        if ((status & World.ST_INVISIBLE) == 0) {
            if (!done) {
                g.drawArc((int) ((x - 2) - viewX) * Constants.WIDTH_SCALE, (int) ((y - 10) - viewY) * Constants.HEIGHT_SCALE, 4, 4, 0, 360);
                g.drawLine((int) ((x) - viewX) * Constants.WIDTH_SCALE, (int) ((y - 6) - viewY) * Constants.HEIGHT_SCALE, (int) ((x) - viewX) * Constants.WIDTH_SCALE,
                        (int) ((y - 3) - viewY) * Constants.HEIGHT_SCALE);
                g.drawLine((int) ((x - 2) - viewX) * Constants.WIDTH_SCALE, (int) ((y - 4) - viewY) * Constants.HEIGHT_SCALE, (int) ((x + 2) - viewX) * Constants.WIDTH_SCALE,
                        (int) ((y - 4) - viewY) * Constants.HEIGHT_SCALE);

                g.drawLine((int) ((x) - viewX) * Constants.WIDTH_SCALE, (int) ((y - 3) - viewY) * Constants.HEIGHT_SCALE, (int) ((x + offs - 2) - viewX) * Constants.WIDTH_SCALE,
                        (int) ((y) - viewY) * Constants.HEIGHT_SCALE);
                g.drawLine((int) ((x) - viewX) * Constants.WIDTH_SCALE, (int) ((y - 3) - viewY) * Constants.HEIGHT_SCALE, (int) ((x + 2 - offs) - viewX) * Constants.WIDTH_SCALE,                        (int) ((y) - viewY) * Constants.HEIGHT_SCALE);
            } else {
                int yUp = 20;

                if (Math.signum(move) == -1) {
                    left = -1;
                }
                if (Math.signum(move) == 1) {
                    left = 1;
                }
                Graphics2D g2 = (Graphics2D) g;
                AffineTransform swag = g2.getTransform();
                g2.scale(left, 1);
                g2.drawImage(bodyParts[0], (int) (x - viewX) * Constants.WIDTH_SCALE * left + (left < 0 ? -18 : 0),
                        (int) (y - yUp - 6 - viewY) * Constants.HEIGHT_SCALE, null);
                g2.drawImage(bodyParts[1],
                        (int) (x + 2 - ((bodyParts[1].getWidth(null) - 23) / 5f) - viewX) * Constants.WIDTH_SCALE * left
                                + (left < 0 ? -(6 + (bodyParts[1].getWidth(null) - 23)) : 0),
                        (int) ((y - yUp - 16 - (bodyParts[1].getHeight(null) - 31f) / 3) - viewY) * Constants.HEIGHT_SCALE, null);
                double ffs = Math.toRadians(((4 - offs) * 6));
                if (vspeed != 0)
                    ffs = Math.toRadians(((4 - 5) * 6));
                AffineTransform previousAT = g2.getTransform();
                int ddd = bodyParts[3].getWidth(null);
                if (ddd == 48) {
                    g2.translate((x - 3 - viewX) * Constants.WIDTH_SCALE * left, ((y - yUp - 6) - viewY) * Constants.HEIGHT_SCALE);
                    g2.rotate(Math.toRadians(leftArmAngle - 90), 4f * (left + 1), 2);
                    g2.drawImage(bodyParts[2], 0, 0, null);
                    g2.setTransform(previousAT);

                    g2.translate(
                            ((x - 3 - viewX) + this.lengthDirX(6f * left, leftArmAngle * left)) * left * (Constants.WIDTH_SCALE-0.05)
                                    + ((left - 1) * 10),
                            (((y - yUp - 6) - this.lengthDirY(6f * left, leftArmAngle * left)) - viewY) * Constants.HEIGHT_SCALE);
                    g2.rotate(Math.toRadians(leftArmAngle - 90), (left == 1) ? 17 : 13, 2);
                    g2.drawImage(bodyParts[3], 0, 0, null);
                    g2.setTransform(previousAT);

                    g2.translate((x + 9 - viewX) * Constants.WIDTH_SCALE * left, ((y - yUp - 6) - viewY) * Constants.HEIGHT_SCALE);
                    g2.rotate(Math.toRadians(rightArmAngle - 90), 0, 2);
                    g2.drawImage(bodyParts[2], 0, 0, null);
                    g2.setTransform(previousAT);

                    g2.translate(
                            (((x + 9 - viewX) + this.lengthDirX(6f * left, rightArmAngle * left)) * (Constants.WIDTH_SCALE-0.05)) * left
                                    + ((left - 1) * 10),
                            (((y - yUp - 6) - this.lengthDirY(6f * left, rightArmAngle * left)) - viewY) * Constants.HEIGHT_SCALE);
                    g2.rotate(Math.toRadians(rightArmAngle - 90), 12, 2);
                    g2.drawImage(bodyParts[3], 0, 0, null);
                    g2.setTransform(previousAT);
                } else {
                    g2.translate((x - 3 - viewX) * Constants.WIDTH_SCALE * left, ((y - yUp - 6) - viewY) * Constants.HEIGHT_SCALE);
                    g2.rotate(Math.toRadians(leftArmAngle - 90), 4f * (left + 1), 2);
                    g2.drawImage(bodyParts[2], 0, 0, null);
                    g2.setTransform(previousAT);

                    g2.translate((((x + 9 - viewX) + this.lengthDirX(6f * left, rightArmAngle * left)) * Constants.WIDTH_SCALE) * left,
                            (((y - yUp - 6) - this.lengthDirY(6f * left, rightArmAngle * left)) - viewY) * Constants.HEIGHT_SCALE);
                    g2.rotate(Math.toRadians(rightArmAngle - 90), 8f - left * 4, 4);
                    g2.drawImage(bodyParts[3], 0, 0, null);
                    g2.setTransform(previousAT);

                    g2.translate((x + 9 - viewX) * Constants.WIDTH_SCALE * left, ((y - yUp - 6) - viewY) * Constants.HEIGHT_SCALE);
                    g2.rotate(Math.toRadians(rightArmAngle - 90), 8f - left * 4, 4);
                    g2.drawImage(bodyParts[2], 0, 0, null);
                    g2.setTransform(previousAT);

                    g2.translate(((x - 3 - viewX) + this.lengthDirX(6f * left, leftArmAngle * left)) * left * Constants.WIDTH_SCALE,
                            (((y - yUp - 6) - this.lengthDirY(6f * left, leftArmAngle * left)) - viewY) * Constants.HEIGHT_SCALE);
                    g2.rotate(Math.toRadians(leftArmAngle - 90), 4f * (left + 1), 2);
                    g2.drawImage(bodyParts[3], 0, 0, null);
                    g2.setTransform(previousAT);
                }

                g2.drawImage(bodyParts[4], (int) (x + 1 - viewX) * Constants.WIDTH_SCALE * left, (int) ((y - yUp + 7) - viewY) * Constants.HEIGHT_SCALE, null);
                g2.drawImage(bodyParts[4], (int) (x + 5 - viewX) * Constants.WIDTH_SCALE * left, (int) ((y - yUp + 7) - viewY) * Constants.HEIGHT_SCALE, null);

                g2.translate((x + 5 - viewX) * Constants.WIDTH_SCALE * left, ((y + 13) - viewY - yUp) * Constants.HEIGHT_SCALE);
                g2.rotate(ffs);
                g2.drawImage(bodyParts[5], 0, 0, null);
                g2.setTransform(previousAT);

                g2.translate((x + 1 - viewX) * Constants.WIDTH_SCALE * left, ((y + 13) - viewY - yUp) * Constants.HEIGHT_SCALE);
                g2.rotate(-ffs);
                g2.drawImage(bodyParts[5], 0, 0, null);
                g2.setTransform(previousAT);
                g2.scale(left, 1);
                if ((status & ST_FLAMING) != 0) {
                    drawFire(g2, (int) (x + 4 - viewX) * Constants.WIDTH_SCALE, (int) (y - viewY) * Constants.HEIGHT_SCALE);
                }
                if ((status & ST_DRAIN) != 0) {
                    g2.setColor(Color.BLACK);
                    g2.drawArc((int) (x - viewX - (AURA_RADIUS / 2f)) * Constants.WIDTH_SCALE, (int) (y - viewY - (AURA_RADIUS)) * Constants.HEIGHT_SCALE,
                            AURA_RADIUS * Constants.WIDTH_SCALE, AURA_RADIUS * Constants.HEIGHT_SCALE, random.nextInt(360), random.nextInt(90));
                }
                g2.setTransform(swag);
            }
        }
        if (!playerList.isEmpty()) {
            movePlayers();
            for (Player r : playerList) {
                r.onDraw(g, viewX, viewY);
                if ((r.status & ST_FLAMING) != 0) {
                    drawFire(g, (r.x + 4 - viewX) * Constants.WIDTH_SCALE, (r.y - viewY) * Constants.HEIGHT_SCALE);
                }
                if ((r.status & ST_DRAIN) != 0) {
                    g.setColor(Color.BLACK);
                    g.drawArc((r.x - viewX - (AURA_RADIUS / 2)) * Constants.WIDTH_SCALE, (r.y - viewY - (AURA_RADIUS)) * Constants.WIDTH_SCALE, AURA_RADIUS * Constants.HEIGHT_SCALE,
                            AURA_RADIUS * Constants.HEIGHT_SCALE, random.nextInt(360), random.nextInt(90));
                }
            }
        }
    }

    public int map;

    public static void drawFire(Graphics g2, int xx, int yy) {
        firePolygonred.translate(xx, yy);
        firePolygonorange.translate(xx, yy);
        firePolygonyellow.translate(xx, yy);
        g2.setColor(Color.RED);
        g2.fillPolygon(firePolygonred);
        g2.setColor(Color.ORANGE);
        g2.fillPolygon(firePolygonorange);
        g2.setColor(Color.YELLOW);
        g2.fillPolygon(firePolygonyellow);
        g2.setColor(Color.BLACK);
        g2.drawPolygon(firePolygonred);
        g2.drawPolygon(firePolygonorange);
        g2.drawPolygon(firePolygonyellow);
        firePolygonred.translate(-xx, -yy);
        firePolygonorange.translate(-xx, -yy);
        firePolygonyellow.translate(-xx, -yy);
    }

    public synchronized void drawTerrain(Graphics2D g2) {
        try {
            int xx = viewX;
            int yy = viewY;

            gter.setPaint(map == 1 ? nightPaint : skyPaint);//
            gter.fillRect(0, 0, Constants.WIDTH_INT, Constants.HEIGHT_INT);


            for (int X = xx; X < Math.min(xx + Constants.WIDTH_INT, wIdTh); X++) {
                for (int Y = yy; Y < Math.min(yy + Constants.HEIGHT_INT, hEigHt); Y++) {
                    switch (ground.cellData[X][Y]) {

                        case GROUND:
                            iter.setRGB(Math.min(X + 3 - xx, Constants.WIDTH_INT), Math.min(Y + 3 - yy, Constants.HEIGHT_INT),
                                    grass.getRGB(X % landTexSize, Y % landTexSize));
                            break;
                        case SAND:
                            iter.setRGB(Math.min(X + 3 - xx, Constants.WIDTH_INT), Math.min(Y + 3 - yy, Constants.HEIGHT_INT),
                                    sand.getRGB(X % landTexSize, Y % landTexSize));
                            break;
                        case STONE:
                            iter.setRGB(Math.min(X + 3 - xx, Constants.WIDTH_INT), Math.min(Y + 3 - yy, Constants.HEIGHT_INT),
                                    stone.getRGB(X % landTexSize, Y % landTexSize));
                            break;
                        case TREE:
                            iter.setRGB(Math.min(X + 3 - xx, Constants.WIDTH_INT), Math.min(Y + 3 - yy, Constants.HEIGHT_INT),
                                    bark.getRGB(X % landTexSize, Y % landTexSize));
                            break;
                        case ICE:
                            iter.setRGB(Math.min(X + 3 - xx, Constants.WIDTH_INT), Math.min(Y + 3 - yy, Constants.HEIGHT_INT),
                                    ice.getRGB(X % landTexSize, Y % landTexSize));
                            break;
                        case CRYSTAL:
                            iter.setRGB(Math.min(X + 3 - xx, Constants.WIDTH_INT), Math.min(Y + 3 - yy, Constants.HEIGHT_INT),
                                    crystal.getRGB(X % landTexSize, Y % landTexSize));
                            break;
                        case ETHER:
                            iter.setRGB(Math.min(X + 3 - xx, Constants.WIDTH_INT), Math.min(Y + 3 - yy, Constants.HEIGHT_INT),
                                    ether.getRGB(X % 100, Y % 100));
                            break;
                        case WATER:
                            iter.setRGB(X + 4 - xx, Y + 4 - yy, liquidStats[aList[ground.cellData[X][Y]]][2]);
                            break;
                        case LAVA:
                            iter.setRGB(X + 4 - xx, Y + 4 - yy, liquidStats[aList[ground.cellData[X][Y]]][2]);
                            break;
                        case OIL:
                            iter.setRGB(X + 4 - xx, Y + 4 - yy, liquidStats[aList[ground.cellData[X][Y]]][2]);
                            break;
                        default:
                            break;
                    }
                }
            }
            g2.drawImage(iter, -3, -3, null);
        } catch (Exception e) {
            Logger.getLogger(World.class.getName()).log(Level.SEVERE, MessageFormat.format("X: {0} \tY: {1}" ,viewX , viewY));
            e.printStackTrace();
        }
    }

    public void loseFocus() {
        MB1 = false;
        MB2 = false;
        mb3 = false;
        for (int i = 0; i < keys.length; i++) {
            keys[i] = false;
        }
        move = 0;
        jump = 0;
        vspeed = 0;
    }

    public boolean inBounds(double i1, double i2) {
        return (i1 >= 0 && i1 < wIdTh && i2 >= 0 && i2 < hEigHt);
    }

    public boolean checkCollision(float x, float y) {
        return (isSolid(x, y));
    }

    public boolean checkCollision(float x, float y, float r) {
        if (!inBounds(x, y))
            return true;
        for (float i1 = Math.max(x - (r + 1), 0); i1 < Math.min(x + (r + 1), ground.w); i1++) {
            for (float i2 = Math.max(y - (r + 1), 0); i2 < Math.min(y + (r + 1), ground.h); i2++) {
                if (Math.round(Math.sqrt(Math.pow(i1 - x, 2) + Math.pow(i2 - y, 2))) < (r / 2) + .1) {
                    return (isSolid(i1, i2));
                }
            }
        }
        return false;
    }

    public void explode(int x, int y, int r, int n, int c) {
        ground.clearCircle(x, y, r);
        entityList.add(new ExplosionEntity(x, y, r / 2, 2));
        int tobe;
        int xx;
        int yy;
        for (int i = 0; i <= n; i++) {
            tobe = (int) (.5 + (r * random.nextInt()));
            tobe += tobe % 2 == 0 ? 1 : 0;
            xx = x + c - random.nextInt(c * 2);
            yy = y + c - random.nextInt(c * 2);
            entityList.add(new ExplosionEntity(xx, yy, tobe / 2, 2));
            ground.clearCircle(x + c - random.nextInt(c * 2), y + c - random.nextInt(c * 2), tobe);
        }
    }

    public void unexplode(int x, int y, int r, int n, int c) {
        ground.fillCircle(x, y, r);
        int tobe;
        int xx;
        int yy;
        for (int i = 0; i <= n; i++) {
            tobe = (int) (.5 + (r * random.nextInt()));
            tobe += tobe % 2 == 0 ? 1 : 0;
            xx = x + c - random.nextInt(c * 2);
            yy = y + c - random.nextInt(c * 2);
            ground.fillCircle(xx, yy, tobe);
        }
    }

    public double lengthDirX(double r, double t) {
        return (r * Math.cos(t * Math.PI / 180));
    }

    public double lengthDirY(double r, double t) {
        return (-r * Math.sin(t * Math.PI / 180));
    }

    public double pointDir(double x1, double y1, double x2, double y2) {
        return Math.toDegrees(Math.atan2(y2 - y1, x2 - x1));
    }

    public double pointDis(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    public void generateSandAndWater() {
        for (int i = 0; i <= 6; i++) {
            entityList.add(new WaterEntity(random.nextInt(wIdTh), random.nextInt(hEigHt), this,
                    random.nextBoolean() ? SAND : WATER));
        }
    }

    public void movePlayers() {
        for (Player P : playerList) {
            if (!isSolid(P.x, P.y - 4f)) {
                P.y -= 4;
                if (inBounds(P.x + P.move, P.y + P.vspeed)) {
                    int toMove = P.move;
                    int xxx1 = P.x + 3;
                    int yyy1 = P.y - 4;
                    int xxx2 = P.x - 3;
                    int yyy2 = P.y - 4;
                    while (true) {
                        yyy1 += 1;
                        if (!inBounds(xxx1, yyy1) || isSolid(xxx1, yyy1)) {
                            break;
                        }
                    }
                    while (true) {
                        yyy2 += 1;
                        if (!inBounds(xxx2, yyy2) || isSolid(xxx2, yyy2)) {
                            break;
                        }
                    }
                    if (toMove > 4)
                        toMove = 4;
                    if (toMove < -4)
                        toMove = -4;
                    P.x += !isSolid(P.x + toMove, P.y + P.vspeed) ? toMove : 0;
                }
                for (int i = 0; i < 4; i++) {
                    if (P.y > 0 && isSolid(P.x, P.y + 1)) {
                        break;
                    }
                    P.y += 1;
                }
            }
            if (!isSolid(P.x, P.y + 4f)) {
                P.vspeed = Math.min(4, P.vspeed + 1);
            } else {
                P.vspeed = Math.min(0, P.vspeed);
            }
            if (!isSolid(P.x, P.y + P.vspeed)) {
                P.y += P.vspeed;
            } else {
                P.vspeed = 0;
            }
        }
    }

    public void handleEntitiesForServer() {
        for (int i = 0; i < entityList.size(); i++) {
            Entity e = entityList.get(i);
            e.move();
            e.onServerUpdate(lol);
            e.onUpdate(this);
            if (!e.alive) {
                entityList.remove(e);
            }
        }
    }

    public void generateForest(int ix, int iy, int iw, int density) {
        for (int i = ix; i < ix + iw; i += (density / 2) + random.nextInt(density / 2)) {
            generateTree(i, iy, 32, 8);
        }
    }

    public void generateTree(int ix, int iy, int mH, int mW) {
        while (!isSolid(ix, iy + 1f)) {
            iy++;
            if (iy > hEigHt) {
                break;
            }
        }
        int w = (mW / 2) + random.nextInt(mW / 2);
        int h = (mH / 2) + random.nextInt(mH / 2);
        Polygon polygon = new Polygon();
        polygon.addPoint(ix - w, iy);
        int branch1Y;
        int branch1W;
        int branch1L;
        int branch2Y;
        int branch2W;
        int branch2L;
        branch1Y = iy - random.nextInt(h);
        branch1W = random.nextInt(w * 2);
        branch1L = random.nextInt(h / 2);
        branch2Y = iy - random.nextInt(h);
        branch2W = random.nextInt(w * 2);
        branch2L = random.nextInt(h / 2);
        polygon.addPoint(ix - w, iy);
        polygon.addPoint(ix - w, branch1Y);
        polygon.addPoint((ix - w) + (int) lengthDirX(branch1L, 215), branch1Y + (int) lengthDirY(branch1L, 215));
        polygon.addPoint((ix - w) + (int) lengthDirX(branch1L, 215), branch1Y + (int) lengthDirY(branch1L, 215) - branch1W);
        polygon.addPoint((ix - w), branch1Y - branch1W);
        polygon.addPoint((ix - w), iy - h);
        polygon.addPoint((ix + w), iy - h);
        polygon.addPoint((ix + w), branch2Y - branch2W);
        polygon.addPoint((ix + w) + (int) lengthDirX(branch2L, 45), branch2Y + (int) lengthDirY(branch1L, 215) - branch2W);
        polygon.addPoint((ix + w) + (int) lengthDirX(branch2L, 45), branch2Y + (int) lengthDirY(branch1L, 215));
        polygon.addPoint(ix + w, branch2Y);
        polygon.addPoint(ix + w, iy);
        ground.fillPolygon(polygon, TREE);
        unexplode(ix, iy - h, 16, 8, 8);
    }

    public static BufferedImage changeColor(BufferedImage image, Color mask, Color replacement) {

        int g = mask.getRGB();
        int r = replacement.getRGB();
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                if (image.getRGB(x, y) == g) {
                    image.setRGB(x, y, r);
                }
            }
        }
        return image;
    }





    static long oldTime = 0;

    public static void setTime() {
        oldTime = System.nanoTime();
    }

    public static float deltaTime() {
        return 1f;
    }

    public static final int ALPHA = 0;
    public static final int RED = 1;
    public static final int GREEN = 2;
    public static final int BLUE = 3;

    public static final int HUE = 0;
    public static final int SATURATION = 1;
    public static final int BRIGHTNESS = 2;

    public static final int TRANSPARENT = 0;
}
