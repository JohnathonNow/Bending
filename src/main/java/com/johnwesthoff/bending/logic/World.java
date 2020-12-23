/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johnwesthoff.bending.logic;

import static com.johnwesthoff.bending.Constants.AIR;
import static com.johnwesthoff.bending.Constants.AURA_RADIUS;
import static com.johnwesthoff.bending.Constants.CRYSTAL;
import static com.johnwesthoff.bending.Constants.ETHER;
import static com.johnwesthoff.bending.Constants.GROUND;
import static com.johnwesthoff.bending.Constants.HEAD;
import static com.johnwesthoff.bending.Constants.ICE;
import static com.johnwesthoff.bending.Constants.LAND_TEX_SIZE;
import static com.johnwesthoff.bending.Constants.LAVA;
import static com.johnwesthoff.bending.Constants.LIQUID_LIST;
import static com.johnwesthoff.bending.Constants.OIL;
import static com.johnwesthoff.bending.Constants.OIL_COLOR;
import static com.johnwesthoff.bending.Constants.SAND;
import static com.johnwesthoff.bending.Constants.STONE;
import static com.johnwesthoff.bending.Constants.ST_DRAIN;
import static com.johnwesthoff.bending.Constants.ST_FLAMING;
import static com.johnwesthoff.bending.Constants.ST_INVISIBLE;
import static com.johnwesthoff.bending.Constants.TREE;
import static com.johnwesthoff.bending.Constants.UGROUND;
import static com.johnwesthoff.bending.Constants.UICE;
import static com.johnwesthoff.bending.Constants.USTONE;
import static com.johnwesthoff.bending.Constants.WATER;
import static com.johnwesthoff.bending.Constants.WATER_COLOR;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import com.johnwesthoff.bending.Client;
import com.johnwesthoff.bending.Constants;
import com.johnwesthoff.bending.Server;
import com.johnwesthoff.bending.entity.Entity;
import com.johnwesthoff.bending.entity.ExplosionEntity;
import com.johnwesthoff.bending.entity.HouseEntity;
import com.johnwesthoff.bending.entity.WaterEntity;
import com.johnwesthoff.bending.spells.Spell;
import com.johnwesthoff.bending.util.Coordinate;
import com.johnwesthoff.bending.util.network.ResourceLoader;

/**
 * @author John
 */
public class World implements Serializable {
    private static final long serialVersionUID = -5361438813968515971L;
    public int incX, incY, floatiness = 0, viewX = 0, viewY = 0, viewdX = 0, viewdY = 0, flowCount = 0, maxFlow = 5000;
    public Random random = new Random();
    public CollisionChecker ground;
    public final CopyOnWriteArrayList<Entity> entityList = new CopyOnWriteArrayList<>();
    public float x = 450, y = 0;
    public short status = 0; //See Constants


    public final ArrayList<Player> playerList = new ArrayList<>();
    public boolean serverWorld = false, dead = false;
    // public Image terrain;
    public BufferedImage Grass, Sky, Sand, Stone, Ice, Night, Crystal, Ether, Bark;// at the moon
    // public Graphics2D G2D;
    public double vspeed = 0, move = 0, fr;
    public int mouseX = 150, mouseY = 0, previousX = 0, previousY = 0, pressX, pressY;
    public boolean[] keys = new boolean[3200];
    public float jump = 0;
    public boolean MB1, MB2, MB3;
    public boolean fallingTerrain = false;
    public int landTexSize = 256;
    public int FPS = 0;
    public static int head = 26;
    public static int body = 13;
    public TexturePaint skyPaint, grassPaint, sandPaint, stonePaint, barkPaint, icePaint, nightPaint;
    public Color waterColor = new Color(0, 255, 255, 127), oilColor = new Color(12, 12, 12, 200);
    public BufferedImage Iter = new BufferedImage(Constants.WIDTH_INT + 12, Constants.HEIGHT_INT + 12,
            BufferedImage.TYPE_INT_ARGB);

    public final int[][] liquidStats = new int[Constants.LIQUID_LIST.length][6];
    public final byte[] aList = new byte[127];
    public int miGenH = 300, maGenH = 300, wIdTh = 900, hEigHt = 900;
    public final byte liquidList[] = { WATER, OIL, LAVA, SAND, ETHER, UGROUND, USTONE, UICE };
    public final byte solidList[] = { SAND, GROUND, STONE, TREE, ICE, CRYSTAL };

    public int ID = 0;
    public boolean cameraMoved = false;
    public Server lol = null;
    public Player following = null;
    public Graphics2D Gter = Iter.createGraphics();
    public World() {
        this(true, 900, 900, null, null, null, null, null, null, null, null, null, null);
        x = 150;
        maxFlow = 150000;
    }

    public World(boolean server, int width, int height, Image terrai, BufferedImage grass, BufferedImage sand,
            BufferedImage sky, BufferedImage stone, BufferedImage bark, BufferedImage ice, BufferedImage lavaland,
            BufferedImage crystal, BufferedImage ether) {
        serverWorld = server;
        Arrays.sort(LIQUID_LIST);
        for (int i = 0; i < aList.length; i++) {
            aList[i] = -1;
        }
        aList[WATER] = (byte) Arrays.binarySearch(LIQUID_LIST, WATER);
        aList[LAVA] = (byte) Arrays.binarySearch(LIQUID_LIST, LAVA);
        aList[SAND] = (byte) Arrays.binarySearch(LIQUID_LIST, SAND);
        aList[OIL] = (byte) Arrays.binarySearch(LIQUID_LIST, OIL);
        aList[ETHER] = (byte) Arrays.binarySearch(LIQUID_LIST, ETHER);
        aList[UGROUND] = (byte) Arrays.binarySearch(LIQUID_LIST, UGROUND);
        aList[UICE] = (byte) Arrays.binarySearch(LIQUID_LIST, UICE);
        aList[USTONE] = (byte) Arrays.binarySearch(LIQUID_LIST, USTONE);
        // 0 is the down speed
        // 1 is the horizontal speed
        // 2 is the color
        // 3 is deprecated
        liquidStats[aList[WATER]][0] = 20;// 5
        liquidStats[aList[WATER]][1] = 20;// 9
        liquidStats[aList[WATER]][2] = WATER_COLOR.getRGB();
        liquidStats[aList[WATER]][3] = 30;

        liquidStats[aList[ETHER]][0] = 20;// 5
        liquidStats[aList[ETHER]][1] = 20;// 9
        liquidStats[aList[ETHER]][2] = WATER_COLOR.getRGB();
        liquidStats[aList[ETHER]][3] = 30;

        liquidStats[aList[LAVA]][0] = 14;// 3
        liquidStats[aList[LAVA]][1] = 8;// 6
        liquidStats[aList[LAVA]][2] = Color.red.getRGB();
        liquidStats[aList[LAVA]][3] = 60;

        liquidStats[aList[OIL]][0] = 5;// 5
        liquidStats[aList[OIL]][1] = 6;// 6
        liquidStats[aList[OIL]][2] = OIL_COLOR.getRGB();
        liquidStats[aList[OIL]][3] = 10;

        liquidStats[aList[SAND]][0] = 8;// 1
        liquidStats[aList[SAND]][1] = 1;// 1
        liquidStats[aList[SAND]][2] = 0;
        liquidStats[aList[SAND]][3] = 50;

        // Unsupported terrain should fall
        for (int i : new int[] { USTONE, UICE, UGROUND }) {
            liquidStats[aList[i]][0] = 8;
            liquidStats[aList[i]][1] = 0;
            liquidStats[aList[i]][2] = 0;
            liquidStats[aList[i]][3] = 50;
        }

        wIdTh = width;
        hEigHt = height;
        // terrain = terrai;//createImage(wIdTh,hEigHt);
        Sky = sky;
        Grass = grass;
        Sand = sand;
        Stone = stone;
        Bark = bark;
        Ice = ice;
        Night = lavaland;
        Crystal = crystal;
        Ether = ether;
        if (!serverWorld) {
            skyPaint = new TexturePaint(Sky, new Rectangle(200, 200));
            grassPaint = new TexturePaint(Grass, new Rectangle(LAND_TEX_SIZE, LAND_TEX_SIZE));
            sandPaint = new TexturePaint(Sand, new Rectangle(LAND_TEX_SIZE, LAND_TEX_SIZE));
            stonePaint = new TexturePaint(Stone, new Rectangle(LAND_TEX_SIZE, LAND_TEX_SIZE));
            barkPaint = new TexturePaint(Bark, new Rectangle(LAND_TEX_SIZE, LAND_TEX_SIZE));
            icePaint = new TexturePaint(Ice, new Rectangle(LAND_TEX_SIZE, LAND_TEX_SIZE));
            nightPaint = new TexturePaint(Night, new Rectangle(300, 300));
        }
        // G2D = (Graphics2D)terrain.getGraphics();
        // G2D.setPaint(new TexturePaint(Grass,new Rectangle(256,256)));
        // G2D.fill(new Rectangle(0,0,wIdTh,hEigHt));
        ground = new CollisionChecker(wIdTh, hEigHt);
        // G2D.setPaint(null);
        if (!serverWorld) {
            ground.ClearCircle(150, 1, 300);
            ground.ClearCircle(450, 1, 300);
            ground.ClearCircle(750, 1, 300);
            ground.ClearCircle(750, 900, 450);
            /*
             * entityList.add(new HouseEntity(450,150,20,20)); entityList.add(new
             * HouseEntity(500,140,20,20)); entityList.add(new
             * HouseEntity(400,140,20,20).setLook(Color.red, Color.ORANGE, Color.gray,
             * Color.cyan, true)); entityList.add(new HouseEntity(750,150,30,60));
             */

            // entityList.add(new HouseEntity(750,899,60,30).setLook(Color.red,
            // Color.darkGray, Color.blue, Color.cyan, true));
        } else {
            ground.ClearCircle(150, 0, 150);
        }
    }

    public Image[] bodyParts;// Body, head, ua, la, ul, ll
    public Thread loader;
    public boolean done = false;
    public String username;
    public int idddd;


    public Player getPlayer(int id) {
        for (Player p : playerList) {
            if (p.ID == id) {
                return p;
            }
        }
        return null;
    }

    /**
     * Get the player's name
     * 
     * @param id id to the player
     * @return the username of the player
     */
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

    /**
     * Load the needed parts
     * 
     * @param parts
     * @param colors
     * @param colors2
     */
    public void load(final byte parts[], final int colors[], final int colors2[]) {
        Runnable getStuff =
            /* Per sonic-lint, declaring a new instance of a single-method class was a bad smell, and for readability
               it is more appropriate to replace with a lambda method */
            () -> {
                bodyParts = new Image[parts.length];
                try {
                    for (int i = 0; i < parts.length; i++) {
                        bodyParts[i] = ResourceLoader.loadImage("p" + (i + 1) + "_" + parts[i] + ".png");
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
            };
        loader = new Thread(getStuff);
        loader.start();
    }

    /**
     * Determine "Inc" value for key events
     */
    public void determineInc() {
        if (this.keys[KeyEvent.VK_E]) {
            this.incX += 10;
            cameraMoved = true;
        }
        if (this.keys[KeyEvent.VK_Q]) {
            this.incX -= 10;
            cameraMoved = true;
        }
        if (this.keys[KeyEvent.VK_Z]) {
            this.incX = 0;
            this.incY = 0;
            cameraMoved = false;
        }
    }

    /**
     * Increment or reset current dig and return it
     *
     * @TODO : be carefull of SRP && OCP
     *
     * @param dig
     * @param spell
     * @param main
     * @return short
     */
    public short getIncrementedDig(short dig, Spell spell, Client main) {
        if (this.keys[KeyEvent.VK_S]) {
            // this.move = 0;
            if ((dig += 2) >= 100) {
                dig = 0;
                spell.getAction(main);
            }
        }
        return dig;
    }

    public final class CollisionChecker {
        public byte[][] cellData = new byte[wIdTh][hEigHt];
        public int w, h;
        public Thread collisionProcess;

        CollisionChecker(int sizex, int sizey) {
            cellData = new byte[sizex][sizey];
            w = sizex;
            h = sizey;
            this.FillRect(0, 0, w, h);
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
                int minx, maxx, miny, maxy;
                minx = Math.max(viewX - 25, 0);
                maxx = Math.min(viewX + 325, w);
                miny = Math.max(viewY - 325, 0);
                maxy = Math.min(viewY + 625, h - 2);
                // minx = 0;
                // if (serverWorld) {
                minx = 0;
                maxx = w - 1;
                miny = 0;
                maxy = h - 2;
                // }
                for (int x = minx; x < maxx; x++) {
                    for (int y = maxy; y > miny; y--) {
                        if (aList[cellData[x][y]] == -1) {
                            continue;
                        }
                        if (cellData[x][y + 1] == AIR) {
                            int i = 1;
                            while (cellData[x][y + i] == AIR) {
                                if (y + i >= h - 2 || i > liquidStats[aList[cellData[x][y]]][0]) {
                                    break;
                                }
                                i++;
                            }
                            i--;
                            cellData[x][y + i] = cellData[x][y];
                            cellData[x][y] = AIR;
                            if (flowCount++ > maxFlow)
                                return;
                        } else {
                            for (int i = 0; i <= liquidStats[aList[cellData[x][y]]][1]; i++) {
                                if (cellData[Math.min(Math.max(x + (i * flipped), 0), w - 1)][y + 1] == AIR) {
                                    cellData[Math.min(Math.max(x + (i * flipped), 0), w - 1)][y + 1] = cellData[x][y];
                                    cellData[x][y] = AIR;
                                    if (flowCount++ > maxFlow)
                                        return;
                                    break;
                                }
                                if (cellData[Math.min(Math.max(x - (i * flipped), 0), w - 1)][y + 1] == AIR) {
                                    cellData[Math.min(Math.max(x - (i * flipped), 0), w - 1)][y + 1] = cellData[x][y];
                                    cellData[x][y] = AIR;
                                    if (flowCount++ > maxFlow)
                                        return;
                                    break;
                                }
                            }
                            if (cellData[x][y] == LAVA) {
                                for (int e = -1; e <= 1; e++) {
                                    if (inBounds(x + e, y + 1) && cellData[x + e][y + 1] == WATER) {
                                        cellData[x][y] = STONE;
                                        cellData[x + e][y + 1] = STONE;
                                        break;
                                    }
                                    if (inBounds(x + e, y - 1) && cellData[x + e][y - 1] == WATER) {
                                        cellData[x][y] = STONE;
                                        cellData[x + e][y - 1] = STONE;
                                        break;
                                    }
                                    // if (inBounds(x+e,y+1)&&cellData[x+e][y+1]==AIR)
                                    // {
                                    // cellData[x][y]=LAVA;
                                    // cellData[x+e][y+1]=LAVA;
                                    // break;
                                    // }
                                    // if (inBounds(x+e,y-1)&&cellData[x+e][y-1]==AIR)
                                    // {
                                    // cellData[x][y]=LAVA;
                                    // cellData[x+e][y-1]=LAVA;
                                    // break;
                                    // }
                                }
                            }
                            // if (cellData[x][y]==OIL)
                            if (cellData[x][y] < 64) {
                                for (int e = -1; e <= 1; e++) {
                                    if (inBounds(x + e, y + 1) && cellData[x + e][y + 1] == OIL
                                            && cellData[x][y] == WATER) {
                                        cellData[x][y] += cellData[x + e][y + 1];
                                        cellData[x + e][y + 1] = (byte) (cellData[x][y] - cellData[x + e][y + 1]);
                                        cellData[x][y] = (byte) (cellData[x][y] - cellData[x + e][y + 1]);
                                        e = 10;
                                    }
                                }
                                if (cellData[x][y] == SAND) {
                                    if (inBounds(x, y + 1) && cellData[x][y + 1] == WATER) {
                                        cellData[x][y + 1] = SAND;
                                        cellData[x][y] = WATER;

                                    }
                                }
                            }
                        }

                    }
                }
            } catch (Exception e) {

            }
        }

        /**
         * Shows and prints cell data
         */
        public void ShowData() {
            for (int i1 = 0; i1 < w; i1++) {
                for (int i2 = 0; i2 < h; i2++) {
                    System.out.print(cellData[i1][i2]);
                }
                System.out.println();
            }
        }

        /**
         * Gets whether a pixel is able to be converted to unsupported
         * 
         * @param x - X coordinate
         * @param y - Y coordinate
         */
        private boolean isUnsupportablePixel(int x, int y) {
            if (!inBounds(x, y)) {
                return false;
            }
            byte b = this.cellData[x][y];
            return (b == GROUND || b == STONE || b == ICE);
        }

        /**
         * Converts a single pixel to unsupported terrain
         * 
         * @param x - X coordinate of where we want to floodfill
         * @param y - Y coordinate of where we want to floodfill
         */
        private void unsupportPixel(int x, int y) {
            this.cellData[x][y] |= 64;
        }

        /**
         * Flood fills terrain converting solid pieces into unsupported pieces
         * 
         * @param x - X coordinate of where we want to floodfill
         * @param y - Y coordinate of where we want to floodfill
         */
        private void unsupport(int x, int y) {
            if (!isUnsupportablePixel(x, y) || !fallingTerrain)
                return;

            LinkedList<Coordinate> q = new LinkedList<>();
            unsupportPixel(x, y);
            q.add(new Coordinate(x, y));
            while (!q.isEmpty()) {
                Coordinate c = q.pop();
                for (Coordinate t : c.getNeighbors()) {
                    if (isUnsupportablePixel(t.x, t.y)) {
                        unsupportPixel(t.x, t.y);
                        q.add(t);
                    }
                }
            }
        }

        /**
         * Clears a circle using the standard square method
         *
         * @param X - X coordinate of the circle's center
         * @param Y - Y coordinate of the circle's center
         * @param R - Radius of the circle
         */
        public void ClearCircle(int X, int Y, int R) {
            // long time = System.nanoTime();
            for (int i1 = Math.max(X - (R + 1), 0); i1 < Math.min(X + (R + 1), w); i1++) {
                for (int i2 = Math.max(Y - (R + 1), 0); i2 < Math.min(Y + (R + 1), h); i2++) {
                    if (Math.pow(i1 - X, 2) + Math.pow(i2 - Y, 2) < Math.pow((R / 2), 2)) {
                        if (cellData[i1][i2] == OIL) {
                            if (random.nextInt(10) == 2) {
                                cellData[i1][i2] = AIR;
                                // oilExplode(i1, i2);
                                entityList.add(new ExplosionEntity(i1, i2, 8, 1));
                                ClearCircle(i1, i2, 10);
                            }
                        } // if (cellData[i1][i2]!=STONE)
                        if (cellData[i1][i2] != CRYSTAL && cellData[i1][i2] != ETHER) {
                            cellData[i1][i2] = AIR;
                        }
                    }
                }
            }
            for (int i1 = Math.max(X - (R + 2), 0); i1 < Math.min(X + (R + 2), w); i1++) {
                for (int i2 = Math.max(Y - (R + 2), 0); i2 < Math.min(Y + (R + 2), h); i2++) {
                    // try to floodfill
                    if (cellData[i1][i2] < 64 && isSolid(i1, i2)) {
                        // not unsupported and is solid
                        unsupport(i1, i2);
                    }
                }
            }
        }

        /**
         * Clears the circle at the given position
         * 
         * @param X X coordinate
         * @param Y Y coordinate
         * @param R
         */
        public void ClearCircleStrong(int X, int Y, int R) {
            // long time = System.nanoTime();
            for (int i1 = Math.max(X - (R + 1), 0); i1 < Math.min(X + (R + 1), w); i1++) {
                for (int i2 = Math.max(Y - (R + 1), 0); i2 < Math.min(Y + (R + 1), h); i2++) {
                    if (Math.round(Math.sqrt(Math.pow(i1 - X, 2) + Math.pow(i2 - Y, 2))) < (R / 2)) {
                        if (cellData[i1][i2] == OIL) {
                            if (random.nextInt(10) == 2) {
                                cellData[i1][i2] = AIR;
                                // oilExplode(i1, i2);
                                entityList.add(new ExplosionEntity(i1, i2, 8, 1));
                                ClearCircle(i1, i2, 10);
                            }
                        }
                        cellData[i1][i2] = AIR;
                    }
                }
            }
            // G2D.setColor(Color.white);
            // G2D.setPaint(skyPaint);
            // G2D.fillArc(X-(R/2), Y-(R/2), R, R, 0, 360);
            // G2D.setPaint(null);
            // System.out.println(System.nanoTime()-time);

        }

        /**
         * Clears the given polygon
         * 
         * @param P Polygon to clear
         */
        public void ClearPolygon(Polygon P) {
            int x, y, minX = w, minY = h, maxX = 0, maxY = 0;

            for (int i = 0; i < P.npoints; i++) {
                x = P.xpoints[i];
                y = P.ypoints[i];
                if (x > maxX) {
                    maxX = Math.min(x, w);
                }
                if (y > maxY) {
                    maxY = y > h ? w : y;
                }
                if (x < minX) {
                    minX = Math.max(x, 0);
                }
                if (y < minY) {
                    minY = Math.max(y, 0);
                }
            }
            for (x = minX; x <= minX; x++) {
                for (y = minY; y <= minY; y++) {
                    if (P.contains(x, y)) {
                        if (cellData[x][y] != CRYSTAL && cellData[x][y] != ETHER)
                            cellData[x][y] = AIR;
                    }
                }
            }
            // G2D.setColor(Color.white);
            // G2D.setPaint(skyPaint);
            // G2D.fillPolygon(P);
            // G2D.setPaint(null);
        }

        /**
         * Fills the given polygon
         * 
         * @param P    Polygon to fill
         * @param type
         */
        public void FillPolygon(Polygon P, byte type) {
            int x, y, minX = w, minY = h, maxX = 0, maxY = 0;

            for (int i = 0; i < P.npoints; i++) {
                x = P.xpoints[i];
                y = P.ypoints[i];
                if (x > maxX) {
                    maxX = Math.min(x, w);
                }
                if (y > maxY) {
                    maxY = y > h ? w : y;
                }
                if (x < minX) {
                    minX = Math.max(x, 0);
                }
                if (y < minY) {
                    minY = Math.max(y, 0);
                }
            }
            for (x = minX; x <= maxX; x++) {
                for (y = minY; y <= maxY; y++) {
                    if (P.contains(x, y)) {
                        if (cellData[x][y] != CRYSTAL && cellData[x][y] != ETHER)
                            cellData[x][y] = type;
                    }
                }
            }
            // G2D.setColor(Color.white);
            // G2D.setPaint(grassPaint);
            // G2D.fillPolygon(P);
            // G2D.setPaint(null);
        }

        /**
         * Sets the circle's terrain to GROUND
         * 
         * @param X X coordinate
         * @param Y Y coordinate
         * @param R
         */
        public void FillCircle(int X, int Y, int R) {
            // long time = System.nanoTime();
            for (int i1 = Math.max(X - (R + 1), 0); i1 < Math.min(X + (R + 1), w); i1++) {
                for (int i2 = Math.max(Y - (R + 1), 0); i2 < Math.min(Y + (R + 1), h); i2++) {
                    if (Math.round(Math.sqrt(Math.pow(i1 - X, 2) + Math.pow(i2 - Y, 2))) < (R / 2) + .1) {
                        if (cellData[i1][i2] != CRYSTAL && cellData[i1][i2] != ETHER)
                            cellData[i1][i2] = GROUND;
                    }
                }
            }
            // G2D.setColor(Color.white);
            // G2D.setPaint(grassPaint);
            // G2D.fillArc(X-(R/2), Y-(R/2), R, R, 0, 360);
            // G2D.setPaint(null);
            // System.out.println(System.nanoTime()-time);
        }

        /**
         * Fills a given circle and sets the terrain of the cell to the new value
         * 
         * @param X X coordinate
         * @param Y Y coordinate
         * @param R
         * @param T Value for the cell data
         */
        public void FillCircleW(int X, int Y, int R, byte T) {
            // long time = System.nanoTime();
            for (int i1 = Math.max(X - (R + 1), 0); i1 < Math.min(X + (R + 1), w); i1++) {
                for (int i2 = Math.max(Y - (R + 1), 0); i2 < Math.min(Y + (R + 1), h); i2++) {
                    if (Math.round(Math.sqrt(Math.pow(i1 - X, 2) + Math.pow(i2 - Y, 2))) < (R / 2) + .1) {
                        if (cellData[i1][i2] != CRYSTAL && cellData[i1][i2] != ETHER)
                            cellData[i1][i2] = T;
                    }
                }
            }
            // G2D.setColor(Color.white);
            // G2D.setPaint(grassPaint);
            // G2D.fillArc(X-(R/2), Y-(R/2), R, R, 0, 360);
            // G2D.setPaint(null);
            // System.out.println(System.nanoTime()-time);
        }

        /**
         * Changes water to ice
         * 
         * @param X
         * @param Y
         * @param R
         */
        public void freeze(int X, int Y, int R) {
            // long time = System.nanoTime();
            for (int i1 = Math.max(X - (R + 1), 0); i1 < Math.min(X + (R + 1), w); i1++) {
                for (int i2 = Math.max(Y - (R + 1), 0); i2 < Math.min(Y + (R + 1), h); i2++) {
                    if (Math.round(Math.sqrt(Math.pow(i1 - X, 2) + Math.pow(i2 - Y, 2))) < (R / 2) + .1
                            && cellData[i1][i2] == WATER) {
                        cellData[i1][i2] = ICE;
                    }
                }
            }
        }

        /**
         * Changes sandy terrain to AIR
         * 
         * @param X X coordinate
         * @param Y Y coordinate
         * @param R
         * @return
         */
        public int sandinate(int X, int Y, int R) {
            int toReturn = 0;
            // long time = System.nanoTime();
            for (int i1 = Math.max(X - (R + 1), 0); i1 < Math.min(X + (R + 1), w); i1++) {
                for (int i2 = Math.max(Y - (R + 1), 0); i2 < Math.min(Y + (R + 1), h); i2++) {
                    if (Math.round(Math.sqrt(Math.pow(i1 - X, 2) + Math.pow(i2 - Y, 2))) < (R / 2) + .1
                            && cellData[i1][i2] == SAND) {
                        cellData[i1][i2] = AIR;
                        toReturn++;
                    }
                }
            }
            return toReturn;
        }

        /**
         * Changes air to WATER
         * 
         * @param X X coordinate
         * @param Y Y coordinate
         * @param R
         */
        public void puddle(int X, int Y, int R) {
            // long time = System.nanoTime();
            for (int i1 = Math.max(X - (R + 1), 0); i1 < Math.min(X + (R + 1), w); i1++) {
                for (int i2 = Math.max(Y - (R + 1), 0); i2 < Math.min(Y + (R + 1), h); i2++) {
                    if (Math.round(Math.sqrt(Math.pow(i1 - X, 2) + Math.pow(i2 - Y, 2))) < (R / 2) + .1
                            && cellData[i1][i2] == AIR) {
                        cellData[i1][i2] = WATER;
                    }
                }
            }
        }

        /**
         * Fills a rectangle at the given position with GROUND
         * 
         * @param X X coordinate
         * @param Y Y coordinate
         * @param H Height
         * @param W Width
         */
        public void FillRect(int X, int Y, int H, int W) {
            // long time = System.nanoTime();
            for (int i1 = Math.max(X, 0); i1 < Math.min(X + W, w); i1++) {
                for (int i2 = Math.max(Y, 0); i2 < Math.min(Y + H, h); i2++) {
                    cellData[i1][i2] = GROUND;
                }
            }
            // G2D.setColor(Color.white);
            // G2D.setPaint(grassPaint);
            // G2D.fillRect(X, Y, W, H);
            // G2D.setPaint(null);
        }

        /**
         * Fills the rectangle at the given position with a new texture
         * 
         * @param X    X coordinate
         * @param Y    Y coordinate
         * @param H    Height
         * @param W    Width
         * @param with New texture
         */
        public void FillRectW(int X, int Y, int H, int W, byte with) {
            for (int i1 = Math.max(X, 0); i1 < Math.min(X + W, w); i1++) {
                for (int i2 = Math.max(Y, 0); i2 < Math.min(Y + H, h); i2++) {
                    if (cellData[i1][i2] != CRYSTAL && cellData[i1][i2] != ETHER)
                        cellData[i1][i2] = with;
                }
            }
        }

        /**
         * Draws a clear line from the current position to a given destination
         * 
         * @param X1 X Coordinate (current position)
         * @param Y1 Y Coordinate (current position)
         * @param X2 X Coordinate (given destination)
         * @param Y2 Y Coordinate (given destination)
         * @param R
         * @param F
         */
        public void ClearLine(int X1, int Y1, int X2, int Y2, int R, int F) {
            double direction = (pointDir(X1, Y1, X2, Y2));
            double distance = pointDis(X1, Y1, X2, Y2);
            F = (int) Math.ceil(distance / R);
            double x = X1, y = Y1;
            for (double i = 0; i <= F; i++) {
                ClearCircle((int) x, (int) y, R);
                x += lengthdir_x(distance / F, direction);
                y += lengthdir_y(distance / F, direction);
            }
        }

        /**
         * Generates a random entity
         * 
         * @param x
         * @param w
         * @param type Type for the cell data
         */
        public void generater(int x, int w, byte type) {
            /*
             * for (int i = 0; i <= 30; i++) {
             * ClearCircle(x+random.nextInt(w),y+random.nextInt(h),random.nextInt(w/2)); }
             * generateSandAndWater();
             */
            float vs = -random.nextInt(12), grav = random.nextFloat(), time = 12;
            for (int e = x; e < x + w; e++) {
                if (random.nextInt(1200) == 5) {
                    entityList.add(new HouseEntity(5 + e, maGenH, 12 + random.nextInt(24), 12 + random.nextInt(24)));
                }
                maGenH = (maGenH > 700) ? 700 : Math.max(maGenH, 10);
                for (int i = maGenH; i < h; i++)
                    cellData[e][i] = type;
                maGenH += vs;
                vs += grav;
                if (e % time == 0) {
                    time = random.nextInt(30) + 1;
                    grav = random.nextFloat();
                    vs = -random.nextInt(8);
                }
            }
        }

        public void generatel(int x, int w, byte type) {
            float vs = -random.nextInt(12), grav = random.nextFloat(), time = 12;
            for (int e = x; e < x + w; e++) {
                miGenH = (miGenH > 450) ? 450 : Math.max(miGenH, 10);
                for (int i = miGenH; i < h; i++)
                    cellData[e][i] = type;
                miGenH += vs;
                vs += grav;
                if (e % time == 0) {
                    time = random.nextInt(30) + 1;
                    grav = random.nextFloat();
                    vs = -random.nextInt(8);
                }
            }

        }

        /**
         * Changes the rectangles texture from GROUND to AIR
         * 
         * @param X X coordinate
         * @param Y Y coordinate
         * @param H Height
         * @param W Width
         */
        public void ClearRect(int X, int Y, int H, int W) {
            // long time = System.nanoTime();
            for (int i1 = Math.max(X, 0); i1 < Math.min(X + W, w); i1++) {
                for (int i2 = Math.max(Y, 0); i2 < Math.min(Y + H, h); i2++) {
                    if (cellData[i1][i2] == GROUND)
                        cellData[i1][i2] = AIR;
                }
            }
            // G2D.setColor(Color.white);
            // G2D.setPaint(skyPaint);
            // G2D.fillRect(X, Y, W, H);
            // G2D.setPaint(null);
            // System.out.println(System.nanoTime()-time);

        }

    }

    public int toKeepMove = 0, jumpHeight = 900;
    public int osc = 1;
    public boolean keepMoving = false;

    /**
     * Checks if the cell is solid
     * 
     * @param x X value of the cell
     * @param y Y value of the cell
     * @return true (if the current cell is solid) or false (else)
     */
    public boolean isSolid(double x, double y) {
        try {
            if (!inBounds(x, y))
                return true;
            return (ground.cellData[(int) x][(int) y] == GROUND || ground.cellData[(int) x][(int) y] == TREE
                    || ground.cellData[(int) x][(int) y] == SAND || ground.cellData[(int) x][(int) y] == STONE
                    || ground.cellData[(int) x][(int) y] == ICE || ground.cellData[(int) x][(int) y] == CRYSTAL
                    || ground.cellData[(int) x][(int) y] > 64);
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }

    /**
     * Checks if the cell is ice
     * 
     * @param x X value of the cell
     * @param y Y value of the cell
     * @return true (if the current cell is ice) or false (else)
     */
    public boolean isIce(int x, int y) {
        try {
            if (!inBounds(x, y))
                return false;
            return (ground.cellData[x][y] == ICE);
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }

    /**
     * Checks if the cell is liquid
     * 
     * @param x X value of the cell
     * @param y Y value of the cell
     * @return true (if the current cell is liquid) or false (else)
     */
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

    /**
     * Checks if the cell is of a given type
     * 
     * @param x X value of the cell
     * @param y Y value of the cell
     * @param t Type
     * @return true (if the cell is of the given type) or false (else)
     */
    public boolean isType(int x, int y, int t) {
        try {
            return inBounds(x, y) && ground.cellData[x][y] == t;
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }

    public void onUpdate() {
        if (serverWorld) {
            handleEntitiesForServer();
        } else {
            handleEntitiesForClient();
        }
        flowCount = 0;
        /*
         * if (MB1) { MB1 = false; double dis = pointDis(x,y,mouseX,mouseY)/10, dir =
         * -pointDir(x,y,mouseX,mouseY); entityList.add(new
         * MissileEntity(x,y,(int)lengthdir_x(dis,dir),(int)lengthdir_y(dis,dir))); } if
         * (MB3) { MB3 = false; double dis = pointDis(x,y,mouseX,mouseY)/10, dir =
         * -pointDir(x,y,mouseX,mouseY); entityList.add(new
         * GroundinatorEntity(x,y,(int)lengthdir_x(dis,dir),(int)lengthdir_y(dis,dir)));
         * }
         */
        // move -= Math.signum(move) * fr;
        if (fr != 0) {
            move = 0;
        }
        if ((y > jumpHeight || x == previousX) && keepMoving) {
            vspeed = 0;
            keepMoving = false;
        }
        if (!isSolid(x, y - 4)) {
            y -= 4;
            if (inBounds(x + move, y + (int) vspeed)) {
                double toMove = move, XXX1 = x + 3, YYY1 = y - 4, XXX2 = x - 3, YYY2 = y - 4;
                if (isLiquid(x, y)) {
                    toMove *= Client.swimmingSpeed;
                } else {
                    toMove *= Client.runningSpeed;
                }
                while (true) {
                    YYY1 += 1;
                    if (!inBounds(XXX1, YYY1)) {
                        break;
                    }
                    if (isSolid(XXX1, YYY1)) {
                        break;
                    }
                }
                while (true) {
                    YYY2 += 1;
                    if (!inBounds(XXX2, YYY2)) {
                        break;
                    }
                    if (isSolid(XXX2, YYY2)) {
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
            vspeed = Math.min(4 - floatiness, vspeed + Constants.GRAVITY);
        } else {
            vspeed = Math.min(0, vspeed);
            keepMoving = false;

            if (jump > 0) {
                vspeed = (int) (-10 * jump);
                move *= 2;
                // toKeepMove = move*3;
                // jumpHeight = (int)y;
                // if (Client.shortJump)
                // {
                // toKeepMove = 0;
                // }
                // keepMoving = (toKeepMove!=0);
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
        if (isLiquid(x, y - (int) (HEAD * .75))) {
            keepMoving = false;
            if (jump > 0) {
                vspeed = -2;
            } else {
                vspeed /= 2;
            }
        }

        // if (keys[KeyEvent.VK_SPACE])
        // {
        // ground.destroyExpansion(x,y,ETHER);
        // }
        if (burn++ > 2) {
            firePolygonred.reset();
            int[] xx = new int[12];
            int[] yy = new int[12];
            xx[0] = (0) * Constants.WIDTH_SCALE;
            yy[0] = (0);
            firePolygonred.addPoint(xx[0], yy[0]);
            int dir = 100 + random.nextInt(45), len = 48 + random.nextInt(48);
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
        // int incX, incY;
        // incX = (int)Client.lengthdir_x(Client.pointDis(x, y, mouseX,
        // mouseY)/8,Client.pointDir(x, y, mouseX, mouseY));
        // incY = (int)Client.lengthdir_y(Client.pointDis(x, y, mouseX,
        // mouseY)/8,Client.pointDir(x, y, mouseX, mouseY));
        float followx = x;
        float followy = y;
        if (following != null) {
            incX = 0;
            incY = 0;
            cameraMoved = false;
            followx = (int)following.showx;
            followy = (int)following.showy;
        }
        viewX = (int) Math.min(Math.max((followx - (Constants.WIDTH_INT + 1) / 2) + incX, 0),
                Math.max(0, wIdTh - Constants.WIDTH_INT - 1));
        /*
         * if ((x-150)+incX>wIdTh-300) { incX=(wIdTh-300)-(x-150); } if ((x-150)+incX<0)
         * { incX=(-(x-150)); }
         */
        viewY = (int) Math.min(Math.max((followy - Constants.HEIGHT_INT / 2) + incY, 0),
                Math.max(0, hEigHt - Constants.HEIGHT_INT));
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
        ;
        try {
            drawTerrain((Graphics2D) g);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ;
        // g.drawImage(terrain,-viewX,-viewY, null);

    }

    public int left = 1;
    public static int burn = 0;
    public double leftArmAngle = 90, rightArmAngle = 90;

    /**
     * Draws the entities
     * 
     * @param g Graphics
     */
    public void drawEntities(Graphics g) {
        for (int i = 0; i < entityList.size(); i++) {
            Entity e = entityList.get(i);
            e.onDraw(g, viewX, viewY);
        }
    }

    public static Polygon firePolygonred = new Polygon(), firePolygonyellow = new Polygon(),
            firePolygonorange = new Polygon();

    /**
     * Draws the players
     * 
     * @param g Graphics
     */
    public void drawPlayers(Graphics g) {
        float offs = x % 8;
        if ((status & ST_INVISIBLE) == 0) {
            if (!done) {
                // x+=move;
                g.drawArc((int) ((x - 2) - viewX) * Constants.WIDTH_SCALE,
                        (int) ((y - 10) - viewY) * Constants.HEIGHT_SCALE, 4, 4, 0, 360);
                g.drawLine((int) ((x) - viewX) * Constants.WIDTH_SCALE,
                        (int) ((y - 6) - viewY) * Constants.HEIGHT_SCALE, (int) ((x) - viewX) * Constants.WIDTH_SCALE,
                        (int) ((y - 3) - viewY) * Constants.HEIGHT_SCALE);
                g.drawLine((int) ((x - 2) - viewX) * Constants.WIDTH_SCALE,
                        (int) ((y - 4) - viewY) * Constants.HEIGHT_SCALE,
                        (int) ((x + 2) - viewX) * Constants.WIDTH_SCALE,
                        (int) ((y - 4) - viewY) * Constants.HEIGHT_SCALE);

                g.drawLine((int) ((x) - viewX) * Constants.WIDTH_SCALE,
                        (int) ((y - 3) - viewY) * Constants.HEIGHT_SCALE,
                        (int) ((x + offs - 2) - viewX) * Constants.WIDTH_SCALE,
                        (int) ((y) - viewY) * Constants.HEIGHT_SCALE);
                g.drawLine((int) ((x) - viewX) * Constants.WIDTH_SCALE,
                        (int) ((y - 3) - viewY) * Constants.HEIGHT_SCALE,
                        (int) ((x + 2 - offs) - viewX) * Constants.WIDTH_SCALE,
                        (int) ((y) - viewY) * Constants.HEIGHT_SCALE);
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
                        (int) (x + 2 - ((bodyParts[1].getWidth(null) - 23) / 5) - viewX) * Constants.WIDTH_SCALE * left
                                + (left < 0 ? -(6 + (bodyParts[1].getWidth(null) - 23)) : 0),
                        (int) ((y - yUp - 16 - ((bodyParts[1].getHeight(null) - 31)) / 3) - viewY)
                                * Constants.HEIGHT_SCALE,
                        null);
                double ffs = Math.toRadians(((4 - offs) * 6));
                if (vspeed != 0)
                    ffs = Math.toRadians(((4 - 5) * 6));
                AffineTransform previousAT = g2.getTransform();
                int ddd = bodyParts[3].getWidth(null);
                // System.out.println(ddd);
                if (ddd == 48) {
                    g2.translate((x - 3 - viewX) * Constants.WIDTH_SCALE * left,
                            ((y - yUp - 6) - viewY) * Constants.HEIGHT_SCALE);
                    g2.rotate(Math.toRadians(leftArmAngle - 90), 4 * (left + 1), 2);
                    g2.drawImage(bodyParts[2], 0, 0, null);
                    g2.setTransform(previousAT);

                    g2.translate(
                            (((x - 3 - viewX) + this.lengthdir_x(6 * left, leftArmAngle * left))) * left
                                    * (Constants.WIDTH_SCALE - 0.05) + ((left - 1) * 10),
                            (((y - yUp - 6) - this.lengthdir_y(6 * left, leftArmAngle * left)) - viewY)
                                    * Constants.HEIGHT_SCALE);
                    g2.rotate(Math.toRadians(leftArmAngle - 90), (left == 1) ? 17 : 13, 2);
                    g2.drawImage(bodyParts[3], 0, 0, null);
                    g2.setTransform(previousAT);

                    g2.translate((x + 9 - viewX) * Constants.WIDTH_SCALE * left,
                            ((y - yUp - 6) - viewY) * Constants.HEIGHT_SCALE);
                    g2.rotate(Math.toRadians(rightArmAngle - 90), 0, 2);
                    g2.drawImage(bodyParts[2], 0, 0, null);
                    g2.setTransform(previousAT);

                    g2.translate(
                            (((x + 9 - viewX) + this.lengthdir_x(6 * left, rightArmAngle * left))
                                    * (Constants.WIDTH_SCALE - 0.05)) * left + ((left - 1) * 10),
                            (((y - yUp - 6) - this.lengthdir_y(6 * left, rightArmAngle * left)) - viewY)
                                    * Constants.HEIGHT_SCALE);
                    g2.rotate(Math.toRadians(rightArmAngle - 90), 12, 2);
                    g2.drawImage(bodyParts[3], 0, 0, null);
                    g2.setTransform(previousAT);
                } else {
                    g2.translate((x - 3 - viewX) * Constants.WIDTH_SCALE * left,
                            ((y - yUp - 6) - viewY) * Constants.HEIGHT_SCALE);
                    g2.rotate(Math.toRadians(leftArmAngle - 90), 4 * (left + 1), 2);
                    g2.drawImage(bodyParts[2], 0, 0, null);
                    g2.setTransform(previousAT);

                    g2.translate(
                            (((x + 9 - viewX) + this.lengthdir_x(6 * left, rightArmAngle * left))
                                    * Constants.WIDTH_SCALE) * left,
                            (((y - yUp - 6) - this.lengthdir_y(6 * left, rightArmAngle * left)) - viewY)
                                    * Constants.HEIGHT_SCALE);
                    g2.rotate(Math.toRadians(rightArmAngle - 90), 8 - left * 4, 4);
                    g2.drawImage(bodyParts[3], 0, 0, null);
                    g2.setTransform(previousAT);

                    g2.translate((x + 9 - viewX) * Constants.WIDTH_SCALE * left,
                            ((y - yUp - 6) - viewY) * Constants.HEIGHT_SCALE);
                    g2.rotate(Math.toRadians(rightArmAngle - 90), 8 - left * 4, 4);
                    g2.drawImage(bodyParts[2], 0, 0, null);
                    g2.setTransform(previousAT);

                    g2.translate(
                            (((x - 3 - viewX) + this.lengthdir_x(6 * left, leftArmAngle * left))) * left
                                    * Constants.WIDTH_SCALE,
                            (((y - yUp - 6) - this.lengthdir_y(6 * left, leftArmAngle * left)) - viewY)
                                    * Constants.HEIGHT_SCALE);
                    g2.rotate(Math.toRadians(leftArmAngle - 90), 4 * (left + 1), 2);
                    g2.drawImage(bodyParts[3], 0, 0, null);
                    g2.setTransform(previousAT);
                }
                //
                // g2.translate((x+9-viewX)*3*left, ((y-yUp-6)-viewY)*3);
                // g2.rotate(Math.toRadians(rightArmAngle-90),0,2);
                // g2.drawImage(bodyParts[2], 0, 0, null);
                // g2.setTransform(previousAT);
                //
                // g2.translate((((x+9-viewX)+this.lengthdir_x(6*left,
                // rightArmAngle*left))*3)*left,
                // (((y-yUp-6)-this.lengthdir_y(6*left,rightArmAngle*left))-viewY)*3);
                // g2.rotate(Math.toRadians(rightArmAngle-90),0,2);
                // g2.drawImage(bodyParts[3], 0, 0, null);
                // g2.setTransform(previousAT);

                g2.drawImage(bodyParts[4], (int) (x + 1 - viewX) * Constants.WIDTH_SCALE * left,
                        (int) ((y - yUp + 7) - viewY) * Constants.HEIGHT_SCALE, null);
                g2.drawImage(bodyParts[4], (int) (x + 5 - viewX) * Constants.WIDTH_SCALE * left,
                        (int) ((y - yUp + 7) - viewY) * Constants.HEIGHT_SCALE, null);

                g2.translate((x + 5 - viewX) * Constants.WIDTH_SCALE * left,
                        ((y + 13) - viewY - yUp) * Constants.HEIGHT_SCALE);
                g2.rotate(ffs);
                g2.drawImage(bodyParts[5], 0, 0, null);
                g2.setTransform(previousAT);

                g2.translate((x + 1 - viewX) * Constants.WIDTH_SCALE * left,
                        ((y + 13) - viewY - yUp) * Constants.HEIGHT_SCALE);
                g2.rotate(-ffs);
                g2.drawImage(bodyParts[5], 0, 0, null);
                g2.setTransform(previousAT);
                g2.scale(left, 1);
                if (((status & ST_FLAMING)) != 0) {
                    drawFire(g2, (int) (x + 4 - viewX) * Constants.WIDTH_SCALE,
                            (int) (y - viewY) * Constants.HEIGHT_SCALE);
                }
                if (((status & ST_DRAIN)) != 0) {
                    g2.setColor(Color.BLACK);
                    g2.drawArc((int) (x - viewX - (AURA_RADIUS / 2)) * Constants.WIDTH_SCALE,
                            (int) (y - viewY - (AURA_RADIUS)) * Constants.HEIGHT_SCALE,
                            AURA_RADIUS * Constants.WIDTH_SCALE, AURA_RADIUS * Constants.HEIGHT_SCALE,
                            random.nextInt(360), random.nextInt(90));
                }
                g2.setTransform(swag);
            }
        }
        if (!playerList.isEmpty()) {
            movePlayers();
            for (Player r : playerList) {
                r.onDraw(g, viewX, viewY);
                if (((r.status & ST_FLAMING)) != 0) {
                    drawFire(g, ((int)r.x + 4 - viewX) * Constants.WIDTH_SCALE, ((int)r.y - viewY) * Constants.HEIGHT_SCALE);
                }
                if (((r.status & ST_DRAIN)) != 0) {
                    g.setColor(Color.BLACK);
                    g.drawArc(((int)r.x - viewX - (AURA_RADIUS / 2)) * Constants.WIDTH_SCALE,
                            ((int)r.y - viewY - (AURA_RADIUS)) * Constants.WIDTH_SCALE, AURA_RADIUS * Constants.HEIGHT_SCALE,
                            AURA_RADIUS * Constants.HEIGHT_SCALE, random.nextInt(360), random.nextInt(90));
                }
            }
        }
    }

    public int map;

    /**
     * Draws fire
     * 
     * @param g2 Graphics
     * @param xx
     * @param yy
     */
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

    /**
     * Draws terrain
     * 
     * @param G2 Graphics (in 2D)
     */
    public synchronized void drawTerrain(Graphics2D G2) {
        try {
            int xx = viewX, yy = viewY;

            // G2.drawImage(Iter, -3, -3, null);
            Gter.setPaint(map == 1 ? nightPaint : skyPaint);//
            Gter.fillRect(0, 0, Constants.WIDTH_INT, Constants.HEIGHT_INT);

            // for (int X = xx; X<xx+300; X++)
            // {
            // for (int Y = yy; Y<yy+300; Y++)
            // {
            // if (aList[ground.cellData[X][Y]]!=-1)
            // {
            // if (ground.cellData[X][Y]==liquidList[aList[ground.cellData[X][Y]]])
            // {
            // Iter.setRGB(X+4-xx, Y+4-yy, liquidStats[aList[ground.cellData[X][Y]]][2]);
            // }
            // }
            // }
            // }

            for (int X = xx; X < Math.min(xx + Constants.WIDTH_INT, wIdTh); X++) {
                for (int Y = yy; Y < Math.min(yy + Constants.HEIGHT_INT, hEigHt); Y++) {
                    //final int minX = Math.min(X + 3 - xx, WIDTH_INT);
                    //final int minY = Math.min(Y + 3 - yy, HEIGHT_INT);
                    switch (ground.cellData[X][Y]) {
                        default:
                            break;
                        case GROUND:
                        case UGROUND:
                            Iter.setRGB(Math.min(X - xx, Constants.WIDTH_INT),
                                    Math.min(Y - yy, Constants.HEIGHT_INT),
                                    Grass.getRGB(X % landTexSize, Y % landTexSize));
                            break;
                        case SAND:
                            Iter.setRGB(Math.min(X - xx, Constants.WIDTH_INT),
                                    Math.min(Y - yy, Constants.HEIGHT_INT),
                                    Sand.getRGB(X % landTexSize, Y % landTexSize));
                            break;
                        case STONE:
                        case USTONE:
                            Iter.setRGB(Math.min(X - xx, Constants.WIDTH_INT),
                                    Math.min(Y - yy, Constants.HEIGHT_INT),
                                    Stone.getRGB(X % landTexSize, Y % landTexSize));
                            break;
                        case TREE:
                            Iter.setRGB(Math.min(X - xx, Constants.WIDTH_INT),
                                    Math.min(Y - yy, Constants.HEIGHT_INT),
                                    Bark.getRGB(X % landTexSize, Y % landTexSize));
                            break;
                        case ICE:
                        case UICE:
                            Iter.setRGB(Math.min(X - xx, Constants.WIDTH_INT),
                                    Math.min(Y - yy, Constants.HEIGHT_INT),
                                    Ice.getRGB(X % landTexSize, Y % landTexSize));
                            break;
                        case CRYSTAL:
                            Iter.setRGB(Math.min(X - xx, Constants.WIDTH_INT),
                                    Math.min(Y  - yy, Constants.HEIGHT_INT),
                                    Crystal.getRGB(X % landTexSize, Y % landTexSize));
                            break;
                        case ETHER:
                            Iter.setRGB(Math.min(X - xx, Constants.WIDTH_INT),
                                    Math.min(Y - yy, Constants.HEIGHT_INT), Ether.getRGB(X % 100, Y % 100));
                            break;
                        case WATER:
                        case LAVA:
                        case OIL:
                            Iter.setRGB(X - xx, Y - yy, liquidStats[aList[ground.cellData[X][Y]]][2]);
                            break;
                    }
                }
            }
            G2.drawImage(Iter, 0, 0, null);
        } catch (Exception e) {
            System.err.println("X: " + viewX + "\tY:" + viewY);
            e.printStackTrace();
        }
    }

    public void loseFocus() {
        MB1 = false;
        MB2 = false;
        MB3 = false;
        for (int i = 0; i < keys.length; i++) {
            keys[i] = false;
        }
        move = 0;
        jump = 0;
        vspeed = 0;
    }

    /**
     * Checks if the given location is still in bounds
     * 
     * @param i1 X coordinate
     * @param i2 Y coordinate
     * @return true (if it is still in bounds) or false (else)
     */
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

    /**
     * Creates a new explosion entity
     * 
     * @param x X coordinate
     * @param y Y coordinate
     * @param r
     * @param n
     * @param c
     */
    public void explode(int x, int y, int r, int n, int c) {
        ground.ClearCircle(x, y, r);
        entityList.add(new ExplosionEntity(x, y, r / 2, 2));
        int tobe, xx, yy;
        for (int i = 0; i <= n; i++) {
            tobe = (int) (.5 + (r * random.nextFloat()));
            tobe += tobe % 2 == 0 ? 1 : 0;
            xx = x + c - random.nextInt(c * 2);
            yy = y + c - random.nextInt(c * 2);
            entityList.add(new ExplosionEntity(xx, yy, tobe / 2, 2));
            ground.ClearCircle(x + c - random.nextInt(c * 2), y + c - random.nextInt(c * 2), tobe);
        }
    }

    /**
     * Removes the explosion entity
     * 
     * @param x
     * @param y
     * @param r
     * @param n
     * @param c
     */
    public void unexplode(int x, int y, int r, int n, int c) {
        ground.FillCircle(x, y, r);
        int tobe, xx, yy;
        for (int i = 0; i <= n; i++) {
            tobe = (int) (.5 + (r * random.nextFloat()));
            tobe += tobe % 2 == 0 ? 1 : 0;
            xx = x + c - random.nextInt(c * 2);
            yy = y + c - random.nextInt(c * 2);
            ground.FillCircle(xx, yy, tobe);
        }
    }

    public double lengthdir_x(double R, double T) {
        return (R * Math.cos(T * Math.PI / 180));
    }

    public double lengthdir_y(double R, double T) {
        return (-R * Math.sin(T * Math.PI / 180));
    }

    public double pointDir(double x1, double y1, double x2, double y2) {
        return Math.toDegrees(Math.atan2(y2 - y1, x2 - x1));
    }

    public double pointDis(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    /**
     * Randomly generates either sand or water
     */
    public void generateSandAndWater() {
        for (int i = 0; i <= 6; i++) {
            entityList.add(new WaterEntity(random.nextInt(wIdTh), random.nextInt(hEigHt), this,
                    random.nextBoolean() ? SAND : WATER));
        }
    }

    /**
     * Moves the players
     */
    public void movePlayers() {
        for (Player P : playerList) {
            if (!isSolid(P.x, P.y - 4)) {
                P.y -= 4;
                if (inBounds(P.x + P.move, P.y + P.vspeed)) {
                    int toMove = (int)P.move, XXX1 = (int)P.x + 3, YYY1 = (int)P.y - 4, XXX2 = (int)P.x - 3, YYY2 = (int)P.y - 4;
                    while (true) {
                        YYY1 += 1;
                        if (!inBounds(XXX1, YYY1)) {
                            break;
                        }
                        if (isSolid(XXX1, YYY1)) {
                            break;
                        }
                    }
                    while (true) {
                        YYY2 += 1;
                        if (!inBounds(XXX2, YYY2)) {
                            break;
                        }
                        if (isSolid(XXX2, YYY2)) {
                            break;
                        }
                    }
                    // toMove*=slope==0?1:(Math.signum(slope)==Math.signum(P.move))?1.5:.75;
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
            if (!isSolid(P.x, P.y + 4)) {
                P.vspeed = Math.min(4, P.vspeed + Constants.GRAVITY);
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
    
    public void handleEntitiesForClient() {
        for (int i = 0; i < entityList.size(); i++) {
            Entity e = entityList.get(i);
            e.move();
            e.onUpdate(this);
        }
        for (int i = 0; i < entityList.size(); i++) {
            Entity e = entityList.get(i);
            if (!e.alive) {
                entityList.remove(e);
                continue;
            }
        }
    }
    public void handleEntitiesForServer() {
        for (int i = 0; i < entityList.size(); i++) {
            Entity e = entityList.get(i);
            e.move();
            e.onServerUpdate(lol);
            e.onUpdate(this);
        }
        for (int i = 0; i < entityList.size(); i++) {
            Entity e = entityList.get(i);
            if (!e.alive) {
                entityList.remove(e);
                continue;
            }
        }
    }

    public void generateForest(int ix, int iy, int iw, int density) {
        for (int i = ix; i < ix + iw; i += (density / 2) + random.nextInt(density / 2)) {
            generateTree(i, iy, 32, 8);
        }
    }

    /**
     * Generates a tree at the given destination
     * 
     * @param ix X coordinate
     * @param iy Y coordinate
     * @param mH Height
     * @param mW Width
     */
    public void generateTree(int ix, int iy, int mH, int mW) {
        while (!isSolid(ix, iy + 1)) {
            iy++;
            if (iy > hEigHt) {
                break;
            }
        }
        int W = (mW / 2) + random.nextInt(mW / 2);
        int H = (mH / 2) + random.nextInt(mH / 2);
        Polygon P = new Polygon();
        P.addPoint(ix - W, iy);
        int branch1Y, branch1W, branch1L, branch2Y, branch2W, branch2L;
        branch1Y = iy - random.nextInt(H);
        branch1W = random.nextInt(W * 2);
        branch1L = random.nextInt(H / 2);
        branch2Y = iy - random.nextInt(H);
        branch2W = random.nextInt(W * 2);
        branch2L = random.nextInt(H / 2);
        P.addPoint(ix - W, iy);
        P.addPoint(ix - W, branch1Y);
        P.addPoint((ix - W) + (int) lengthdir_x(branch1L, 215), branch1Y + (int) lengthdir_y(branch1L, 215));
        P.addPoint((ix - W) + (int) lengthdir_x(branch1L, 215), branch1Y + (int) lengthdir_y(branch1L, 215) - branch1W);
        P.addPoint((ix - W), branch1Y - branch1W);
        P.addPoint((ix - W), iy - H);
        P.addPoint((ix + W), iy - H);
        P.addPoint((ix + W), branch2Y - branch2W);
        P.addPoint((ix + W) + (int) lengthdir_x(branch2L, 45), branch2Y + (int) lengthdir_y(branch1L, 215) - branch2W);
        P.addPoint((ix + W) + (int) lengthdir_x(branch2L, 45), branch2Y + (int) lengthdir_y(branch1L, 215));
        P.addPoint(ix + W, branch2Y);
        P.addPoint(ix + W, iy);
        ground.FillPolygon(P, TREE);
        unexplode(ix, iy - H, 16, 8, 8);
    }

    /**
     * Replaces a given color of an image with a new one
     * 
     * @param image       Image to change the color
     * @param mask        Color to change
     * @param replacement New color
     * @return The new image
     */
    public static BufferedImage changeColor(BufferedImage image, Color mask, Color replacement) {
        /*
         * BufferedImage destImage = new BufferedImage(image.getWidth(),
         * image.getHeight(), BufferedImage.TYPE_INT_ARGB); //image.getColorModel().
         * Graphics2D g = destImage.createGraphics(); g.drawImage(image, null, 0, 0);
         * g.dispose(); int[] colors = image.getRGB(0, 0, image.getWidth(),
         * image.getHeight(), null, 0, 1); for (int i = 0; i < destImage.getWidth();
         * i++) { for (int j = 0; j < destImage.getHeight(); j++) {
         *
         * int destRGB = destImage.getRGB(i, j);
         *
         * if (matches(mask.getRGB(), destRGB)) { int rgbnew =
         * getNewPixelRGB(replacement.getRGB(), destRGB); destImage.setRGB(i, j,
         * rgbnew); } } }
         *
         * return destImage; int[] colors = image.getRGB(0, 0, image.getWidth(),
         * image.getHeight(), null, 0, 1); int colorChange = mask.getRGB(); int colorTo
         * = replacement.getRGB(); for (int i = 0; i < colors.length; i++) { if
         * (colors[i] == colorChange) { colors[i] = colorTo; } } image.setRGB(0, 0,
         * image.getWidth(), image.getHeight(), colors, 0, 1); return image;
         */
        int g = mask.getRGB(), r = replacement.getRGB();
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
        float lol = ((System.nanoTime() - oldTime)) / (25f * 1000000f);
        // System.out.println("Delta Time: "+lol);
        lol = 1;
        return lol;
    }
}
