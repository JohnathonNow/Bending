/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package destruct;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
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

/**
 *
 * @author John
 */
public class ResourceLoader {
    public static void downloadResource(String filename, String urlString) throws FileNotFoundException, IOException
    {
    	BufferedInputStream in = null;
    	FileOutputStream fout = null;
            try
            {
                    new File(filename).mkdirs();
                    URLConnection openConnection = new URL(urlString).openConnection();
                    openConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
                    in = new BufferedInputStream(openConnection.getInputStream());
                    System.out.println(urlString);
                    Files.copy(in, Paths.get(filename), StandardCopyOption.REPLACE_EXISTING);
                    System.out.println(filename);
            }
        catch (Exception ex) {
            System.err.println("ok...");
            ex.printStackTrace();
        }            finally
            {
                    if (in != null)
                            in.close();
                    if (fout != null)
                            fout.close();
            }

    }
 static String dir = System.getenv("APPDATA")+File.separator+"Bending"+File.separator;
 public static HashMap<String, BufferedImage> imageTable = new HashMap<>();
    public static  BufferedImage loadImage(String src, String name)
    {
        
        if (imageTable.containsKey(name))
        {
            return imageTable.get(name);
        }
        BufferedImage toReturn;
                while (true)
                {
//                    System.out.println(name);
            try {
                    //bimage = ImageIO.read(new URL("https://west-it.webs.com/AgedPaper.png"));
                    downloadResource(dir+"images"+File.separator+name,src);
            } 
            catch (Exception ex) 
            {
                Logger.getLogger(APPLET.class.getName()).log(Level.SEVERE, null, ex);
            }
            try 
            {
                toReturn = (BufferedImage)(ImageIO.read(new File(dir+"images"+File.separator+name)));
                imageTable.put(name, toReturn);
                return toReturn;
            } 
            catch (IOException ex)
            {
                //Logger.getLogger(APPLET.class.getName()).log(Level.SEVERE, null, ex);
            }
                }
    }
     public static  ImageIcon loadIcon(String src)
    {
        String name = src.replaceAll("https://west-it.webs.com/spells/","");
        if (imageTable.containsKey(src))
        {
            return new ImageIcon(imageTable.get(src));
        }
        BufferedImage toReturn;
                while (true)
                {
//                    System.out.println(src);
            try {
                    //bimage = ImageIO.read(new URL("https://west-it.webs.com/AgedPaper.png"));
                    downloadResource(dir+"images"+File.separator+name,src);
            } 
            catch (Exception ex) 
            {
                //Logger.getLogger(APPLET.class.getName()).log(Level.SEVERE, null, ex);
            }
            try 
            {
                toReturn = (BufferedImage)(ImageIO.read(new File(dir+"images"+File.separator+name)));
                imageTable.put(name, toReturn);
                return new ImageIcon(toReturn);
            } 
            catch (IOException ex)
            {
                //Logger.getLogger(APPLET.class.getName()).log(Level.SEVERE, null, ex);
            }
                }
    }
        public static  BufferedImage loadImageNoHash(String src, String name)
    {
        
        BufferedImage toReturn;
                while (true)
                {
//                    System.out.println(name);
            try {
                    //bimage = ImageIO.read(new URL("https://west-it.webs.com/AgedPaper.png"));
                    downloadResource(dir+"images"+File.separator+name,src);
            } 
            catch (Exception ex) 
            {
                //Logger.getLogger(APPLET.class.getName()).log(Level.SEVERE, null, ex);
            }
            try 
            {
                toReturn = (BufferedImage)(ImageIO.read(new File(dir+"images"+File.separator+name)));
                return toReturn;
            } 
            catch (IOException ex)
            {
                //Logger.getLogger(APPLET.class.getName()).log(Level.SEVERE, null, ex);
            }
                }
    }
    public static  BufferedImage loadImageFromPC(String name)
    {
        
        BufferedImage toReturn;
                while (true)
                {
            try 
            {
                toReturn = (BufferedImage)(ImageIO.read(new File(name)));
                return toReturn;
            } 
            catch (IOException ex)
            {
                //Logger.getLogger(APPLET.class.getName()).log(Level.SEVERE, null, ex);
            }
                }
    }
    public static RealClip loadSound(String src, String name)
    {
        RealClip clip = null;
        try {
            downloadResource(dir+"sounds"+File.separator+name,src);
            clip =  new RealClip(new File(dir+"sounds"+File.separator+name));
        } catch (Exception ex) {
            Logger.getLogger(APPLET.class.getName()).log(Level.SEVERE, null, ex);
        }
        return clip;
    }
    
}
