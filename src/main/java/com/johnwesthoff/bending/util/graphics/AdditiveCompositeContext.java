/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johnwesthoff.bending.util.graphics;

/**
 *
 *Author: John
 */
import java.awt.*;
import java.awt.image.*;

public class AdditiveCompositeContext implements CompositeContext {
  public AdditiveCompositeContext() {
  };

  public void compose(Raster src, Raster dstIn, WritableRaster dstOut) {
    int chan1 = src.getNumBands();
    int chan2 = dstIn.getNumBands();

    int minCh = Math.min(chan1, chan2);

    // This bit is horribly inefficient,
    // getting individual pixels rather than all at once.
    for (int x = 0; x < dstIn.getWidth(); x++) {
      for (int y = 0; y < dstIn.getHeight(); y++) {
        float[] pxSrc = null;
        pxSrc = src.getPixel(x, y, pxSrc);
        float[] pxDst = null;
        pxDst = dstIn.getPixel(x, y, pxDst);

        float alpha = 255;
        if (pxSrc.length > 3) {
          alpha = pxSrc[3];
        }

        for (int i = 0; i < 3 && i < minCh; i++) {
          pxDst[i] = Math.min(255, (pxSrc[i] * (alpha / 255)) + (pxDst[i]));
          dstOut.setPixel(x, y, pxDst);
        }
      }
    }
  }

  public void dispose() {
  }
}