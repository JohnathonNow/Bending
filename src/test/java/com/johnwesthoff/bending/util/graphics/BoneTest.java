package com.johnwesthoff.bending.util.graphics;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.Graphics2D;

import javax.imageio.ImageIO;

public class BoneTest {
    public static void main(String[] args) {
        BufferedImage image = new BufferedImage(128, 128, BufferedImage.TYPE_INT_ARGB);
        File outputfile = new File("image.png");
        Graphics2D g2d = image.createGraphics();
        BoneBuilder bb = new BoneBuilder();
        Bone spine = bb.createBone(0, 0, new Sprite("p1_1.png"));
        Bone arm1 = bb.attachBone(spine, -6, 0, new Sprite("p3_1.png", 7, 4));
        Bone arm2 = bb.attachBone(spine, 20, 0, new Sprite("p3_1.png", 7, 4));
        arm1.setAngle(45);
        arm2.setAngle(-45);
        spine.draw(g2d, 10, 10, 0);
        try {
            outputfile.createNewFile();
            ImageIO.write(image, "png", outputfile);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}