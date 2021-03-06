/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johnwesthoff.bending.util.network;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import com.johnwesthoff.bending.Client;
import com.johnwesthoff.bending.util.audio.RealClip;

/**
 *
 * @author John
 */
public class ResourceLoader {

    private static final String DOWNLOAD_URL_BASE = "https://github.com/JohnathonNow/Bending/raw/main/assets/Bending/";
    private static final String IMAGE_URL_BASE = DOWNLOAD_URL_BASE + "images/";
    private static final String SOUND_URL_BASE = DOWNLOAD_URL_BASE + "sounds/";

    public static String dir = getDir();
    public static HashMap<String, BufferedImage> imageTable = new HashMap<>();
    public static HashMap<String, RealClip> soundTable = new HashMap<>();

    public static void downloadResource(final String filename, final String urlString)
            throws FileNotFoundException, IOException {
        BufferedInputStream in = null;
        final FileOutputStream fout = null;
        final File f = new File(filename);
        if (!f.exists()) {
            try {
                f.mkdirs();
                final URLConnection openConnection = new URL(urlString).openConnection();
                openConnection.addRequestProperty("User-Agent",
                        "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
                in = new BufferedInputStream(openConnection.getInputStream());
                System.out.println(urlString);
                Files.copy(in, Paths.get(filename), StandardCopyOption.REPLACE_EXISTING);
                System.out.println(filename);
            } catch (final Exception ex) {
                System.err.println("ok...");
                ex.printStackTrace();
            } finally {
                if (in != null)
                    in.close();
                if (fout != null)
                    fout.close();
            }
        }

    }

    private static String getDir() {
        String x = System.getenv("APPDATA");
        if (x == null || x.equals("null")) {
            x = System.getProperty("user.home");
            return x + File.separator + ".bending" + File.separator;
        }
        return x + File.separator + "Bending" + File.separator;
    }

    public static BufferedImage loadImage(final String name) {
        if (imageTable.containsKey(name)) {
            return imageTable.get(name);
        }
        BufferedImage toReturn;
        while (true) {
            // System.out.println(name);
            final File f = new File(dir + "images" + File.separator + name);
            if (!f.exists()) {
                try {
                    // example:
                    // https://github.com/JohnathonNow/Bending/raw/main/assets/Bending/images/AgedPaper.png
                    downloadResource(dir + "images" + File.separator + name, IMAGE_URL_BASE + name);
                } catch (final Exception ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            try {
                toReturn = (BufferedImage) (ImageIO.read(f));
                imageTable.put(name, toReturn);
                return toReturn;
            } catch (final IOException ex) {
                // Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static ImageIcon loadIconBase(final String src) {
        return loadIcon(src);
    }

    public static ImageIcon loadIcon(final String name) {
        final String src = IMAGE_URL_BASE + name;

        if (imageTable.containsKey(src)) {
            return new ImageIcon(imageTable.get(src));
        }
        BufferedImage toReturn;
        while (true) {
            try {
                downloadResource(dir + "images" + File.separator + name, src);
            } catch (final Exception ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                toReturn = (BufferedImage) (ImageIO.read(new File(dir + "images" + File.separator + name)));
                imageTable.put(name, toReturn);
                return new ImageIcon(toReturn);
            } catch (final IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static BufferedImage loadImageNoHash(String src, final String name) {
        src = IMAGE_URL_BASE + name;
        BufferedImage toReturn;
        while (true) {
            try {
                downloadResource(dir + "images" + File.separator + name, src);
            } catch (final Exception ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                toReturn = (BufferedImage) (ImageIO.read(new File(dir + "images" + File.separator + name)));
                return toReturn;
            } catch (final IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static BufferedImage loadImageFromPC(final String name) {

        BufferedImage toReturn;
        while (true) {
            try {
                toReturn = (BufferedImage) (ImageIO.read(new File(name)));
                return toReturn;
            } catch (final IOException ex) {
                // Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static RealClip loadSound(final String name) {
        if (!soundTable.containsKey(name)) {
            try {
                final File f = new File(dir + "sounds" + File.separator + name);
                if (!f.exists()) {
                    downloadResource(dir + "sounds" + File.separator + name, SOUND_URL_BASE + name);
                }
                soundTable.put(name, new RealClip(f));
            } catch (final Exception ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return soundTable.get(name);
    }

}
