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
                    
                    File f = new File(filename);
                    if (f.exists())
                    {
                        return;
                    }
                    fout = new FileOutputStream(f);
                    in = new BufferedInputStream(new URL(urlString).openStream());
                    byte data[] = new byte[1024];
                    int count;
                    while ((count = in.read(data, 0, 1024)) != -1)
                    {
                            fout.write(data, 0, count);
                    }
            }
        catch (MalformedURLException ex) {
//            ex.printStackTrace();
        }            finally
            {
                    if (in != null)
                            in.close();
                    if (fout != null)
                            fout.close();
            }

    }
 static String dir = System.getenv("APPDATA")+"\\Bending\\";
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
                    //bimage = ImageIO.read(new URL("http://west-it.webs.com/AgedPaper.png"));
                    downloadResource(dir+"images\\"+name,src);
            } 
            catch (Exception ex) 
            {
                //Logger.getLogger(APPLET.class.getName()).log(Level.SEVERE, null, ex);
            }
            try 
            {
                toReturn = (BufferedImage)(ImageIO.read(new File(dir+"images\\"+name)));
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
        String name = src.replaceAll("http://west-it.webs.com/spells/","");
        if (imageTable.containsKey(src))
        {
            return new ImageIcon(imageTable.get(src));
        }
        BufferedImage toReturn;
                while (true)
                {
//                    System.out.println(src);
            try {
                    //bimage = ImageIO.read(new URL("http://west-it.webs.com/AgedPaper.png"));
                    downloadResource(dir+"images\\"+name,src);
            } 
            catch (Exception ex) 
            {
                //Logger.getLogger(APPLET.class.getName()).log(Level.SEVERE, null, ex);
            }
            try 
            {
                toReturn = (BufferedImage)(ImageIO.read(new File(dir+"images\\"+name)));
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
                    //bimage = ImageIO.read(new URL("http://west-it.webs.com/AgedPaper.png"));
                    downloadResource(dir+"images\\"+name,src);
            } 
            catch (Exception ex) 
            {
                //Logger.getLogger(APPLET.class.getName()).log(Level.SEVERE, null, ex);
            }
            try 
            {
                toReturn = (BufferedImage)(ImageIO.read(new File(dir+"images\\"+name)));
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
            downloadResource(dir+"sounds\\"+name,src);
            clip =  new RealClip(new File(dir+"sounds\\"+name));
        } catch (Exception ex) {
            Logger.getLogger(APPLET.class.getName()).log(Level.SEVERE, null, ex);
        }
        return clip;
    }
    
}
