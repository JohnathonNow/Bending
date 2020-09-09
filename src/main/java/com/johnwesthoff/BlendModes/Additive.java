/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.johnwesthoff.BlendModes;

/**
 *
 *Author: John
 */
import java.awt.*;
import java.awt.image.*;

public class Additive implements Composite {
  public static Additive additive = new Additive();

  public Additive() {
    super();
  }

  public CompositeContext createContext(ColorModel srcColorModel, ColorModel dstColorModel, RenderingHints hints) {
    return new AdditiveCompositeContext();
  }
}