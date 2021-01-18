package com.johnwesthoff.bending.util.graphics;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;

import com.johnwesthoff.bending.util.network.ResourceLoader;

public class Sprite implements Drawable {
    BufferedImage image;
    int x_pivot;
    int y_pivot;

    public Sprite(BufferedImage image, int x_pivot, int y_pivot) {
        this.image = image;
        this.x_pivot = x_pivot;
        this.y_pivot = y_pivot;
    }

    public Sprite(String filename, int x_pivot, int y_pivot) {
        this(ResourceLoader.loadImage(filename), x_pivot, y_pivot);
    }

    public Sprite(BufferedImage image) {
        this(image, 0, 0);
    }

    public Sprite(String filename) {
        this(ResourceLoader.loadImage(filename), 0, 0);
    }

    @Override
    public void draw(Graphics2D g2d, int x, int y, int angle) {
        AffineTransform at = new AffineTransform();
        at.translate(x, y);
        at.rotate(Math.toRadians(angle), this.x_pivot, this.y_pivot);
        g2d.drawRenderedImage(this.image, at);
    }
}
