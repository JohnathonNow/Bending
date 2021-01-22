package com.johnwesthoff.bending.util.graphics;

import java.awt.Graphics2D;

public interface Drawable {
    public void draw(Graphics2D g2d, int x, int y, int angle);
}
