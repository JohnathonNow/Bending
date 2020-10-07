package com.johnwesthoff.bending;

import java.awt.*;

public class Constants {
    public static final int WIDTH_INT = 640; //960
    public static final int HEIGHT_INT = 360; //540
    public static final int WIDTH_EXT = 1920;
    public static final int HEIGHT_EXT = 1080;
    public static final int WIDTH_SCALE = WIDTH_EXT / WIDTH_INT;
    public static final int HEIGHT_SCALE = HEIGHT_EXT / HEIGHT_INT;
    public static final double FPS = 60.0d;

    public static final int POINTER_WORLD_MOUSE_X_LIMIT = 172;
    public static final int POINTER_WORLD_MOUSE_Y_LIMIT = 17;

    public static final int ALPHA = 0;
    public static final int RED = 1;
    public static final int GREEN = 2;
    public static final int BLUE = 3;

    public static final int HUE = 0;
    public static final int SATURATION = 1;
    public static final int BRIGHTNESS = 2;

    public static final int TRANSPARENT = 0;

    public static final int ST_FLAMING = 0b1;
    public static final int ST_SHOCKED = 0b10;
    public static final int ST_INVISIBLE = 0b100000000;
    public static final int ST_DRAIN = 0b1000000000;
    public static final int AURA_RADIUS = 96;

    public static final int LAND_TEX_SIZE = 256;
    public static final byte FALLING = 64;
    public static final byte AIR = 0, GROUND = 1, WATER = 2, OIL = 3, LAVA = 4, SAND = 5, STONE = 6, TREE = 7, ICE = 8,
            CRYSTAL = 9, ETHER = 10;
    public static final byte UGROUND = GROUND | FALLING;
    public static final byte UICE = ICE | FALLING;
    public static final byte USTONE = STONE | FALLING;
    //public static final byte[] SOLID_LIST = {SAND, GROUND, STONE, TREE, ICE, CRYSTAL};
    public static final byte[] LIQUID_LIST = {WATER, OIL, LAVA, SAND, ETHER, UGROUND, UICE, USTONE};

    public static final Color WATER_COLOR = new Color(0, 255, 255, 127);
    public static final Color OIL_COLOR = new Color(12, 12, 12, 200);
    public static final Color PURPLE = new Color(0xA024C2);
    public static final Color BACKGROUND_CHAT = new Color(0, 0, 0, 200);
    public static final Color DEADBG = new Color(255, 255, 255, 127);
    public static final Color DARK = new Color(0, 0, 0, 128);

    public static int HEAD = 26;
    public static int BODY = 13;
    public static int WALK_CYCLE = 13;
    public static int MULTIPLIER = 3;
    public static int PLAYER_WIDTH = 20;
    public static int PLAYER_HEIGHT = 40;
    public static int FULL_COLOR_VALUE = 255;
    public static int RADIUS_REGULAR = 16;

    public static int FIFTEEN_DEGREE_ANGLE = 15;
    public static int SIXTY_DEGREE_ANGLE = 60;
    public static int RIGHT_ANGLE = 90;
    public static int ONE_THIRD_FULL_ANGLE = 120;
    public static int HALF_FULL_ANGLE = 180;
    public static int FULL_ANGLE = 360;
}
