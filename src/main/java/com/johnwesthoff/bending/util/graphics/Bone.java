package com.johnwesthoff.bending.util.graphics;

import java.awt.Graphics2D;
import java.util.ArrayList;

public class Bone {
    final int offset_x;
    final int offset_y;
    final Bone parent;
    final ArrayList<Bone> children;
    final Drawable representation;
    int angle;
    
    public Bone(int offset_x, int offset_y, Bone parent, Drawable representation) {
        this.offset_x = offset_x;
        this.offset_y = offset_y;
        this.parent = parent;
        this.children = new ArrayList<>();
        this.representation = representation;
        this.angle = 0;
    }

    public void draw(Graphics2D g2d, int x, int y, int angle) {
        x += this.offset_x;
        y += this.offset_y;
        angle += this.angle;
        this.representation.draw(g2d, x, y, angle);
        for (Bone b : children) {
            b.draw(g2d, x, y, angle);
        }
    }

    public void addChild(Bone b) {
        this.children.add(b);
    }

    public int getAngle() {
        return angle;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }
}
