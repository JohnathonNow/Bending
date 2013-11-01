/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package destruct;

import java.awt.Graphics;
import javax.swing.JApplet;

/**
 *
 * @author Family
 */
public class JApples extends JApplet implements Runnable
{
    public static APPLET me;
    @Override
    public void start() {
        setSize(600,600);        
        stuff();
    }
public void stuff()
{
//    APPLET.main(new String[]{});
        me = APPLET.getMe(this);
        setContentPane(APPLET.immaKeepTabsOnYou);
//        add(APPLET.immaKeepTabsOnYou);
//        this.addKeyListener(APPLET.inputer);
//        me.addKeyListener(APPLET.inputer);
//        this.addMouseListener(APPLET.inputer);
//        this.addMouseMotionListener(APPLET.inputer);
//        this.setFocusable(true);
//        this.requestFocus();
}
    @Override
    public void run() {

    }
}
