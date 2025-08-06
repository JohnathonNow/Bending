package com.johnwesthoff.bending.util.graphics;

import com.johnwesthoff.bending.util.network.ResourceLoader;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TextureAtlas {
    private static final int TEXTURE_SIZE = 256;
    private static final int ATLAS_WIDTH_IN_TEXTURES = 4;
    private static final int ATLAS_HEIGHT_IN_TEXTURES = 2;
    private static final int ATLAS_WIDTH = TEXTURE_SIZE * ATLAS_WIDTH_IN_TEXTURES;
    private static final int ATLAS_HEIGHT = TEXTURE_SIZE * ATLAS_HEIGHT_IN_TEXTURES;

    private final BufferedImage atlas;
    private final Map<String, float[]> textureCoordinates = new HashMap<>();

    public static final String[] TEXTURE_FILES = {
            "GrassTexture.jpg", "SandTexture.jpg", "StoneTexture.jpg", "BarkTexture.jpg",
            "iceTexture.png", "crystalTexture.png", "lavalandTexture.png", "ether.png"
    };

    public TextureAtlas() throws IOException {
        atlas = new BufferedImage(ATLAS_WIDTH, ATLAS_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = atlas.createGraphics();

        for (int i = 0; i < TEXTURE_FILES.length; i++) {
            String filename = TEXTURE_FILES[i];
            BufferedImage texture = ResourceLoader.loadImage(filename);
            BufferedImage scaledTexture = scale(texture, TEXTURE_SIZE, TEXTURE_SIZE);

            int x = (i % ATLAS_WIDTH_IN_TEXTURES) * TEXTURE_SIZE;
            int y = (i / ATLAS_WIDTH_IN_TEXTURES) * TEXTURE_SIZE;

            g2d.drawImage(scaledTexture, x, y, null);

            float u0 = (float) x / ATLAS_WIDTH;
            float v0 = (float) y / ATLAS_HEIGHT;
            float u1 = (float) (x + TEXTURE_SIZE) / ATLAS_WIDTH;
            float v1 = (float) (y + TEXTURE_SIZE) / ATLAS_HEIGHT;
            textureCoordinates.put(filename, new float[]{u0, v0, u1, v1});
        }
        g2d.dispose();
    }

    private BufferedImage scale(BufferedImage src, int w, int h) {
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(src, 0, 0, w, h, null);
        g2.dispose();
        return resizedImg;
    }

    public BufferedImage getAtlas() {
        return atlas;
    }

    public float[] getTextureCoordinates(String filename) {
        return textureCoordinates.get(filename);
    }
}
