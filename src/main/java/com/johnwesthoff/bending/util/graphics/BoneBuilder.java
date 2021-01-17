package com.johnwesthoff.bending.util.graphics;

public class BoneBuilder {
    public Bone attachBone(Bone parent, int offset_x, int offset_y, Drawable representation) {
        Bone b = new Bone(offset_x, offset_y, parent, representation);
        parent.addChild(b);
        return b;
    }
    
    public Bone createBone(int offset_x, int offset_y, Drawable representation) {
        Bone b = new Bone(offset_x, offset_y, null, representation);
        return b;
    }
}
