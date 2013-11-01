/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package BlendModes;

/**
 *
 *Author: John
 */
import java.awt.*;
import java.awt.image.*;

public class AdditiveCompositeContext implements CompositeContext
{
  public AdditiveCompositeContext(){};
  public void compose(Raster src, Raster dstIn, WritableRaster dstOut)
  {
    int w1    = src.getWidth();
    int h1    = src.getHeight();
    int chan1 = src.getNumBands();
    int w2    = dstIn.getWidth();
    int h2    = dstIn.getHeight();
    int chan2 = dstIn.getNumBands();

    int minw  = Math.min(w1, w2);
    int minh  = Math.min(h1, h2);
    int minCh = Math.min(chan1, chan2);

    //This bit is horribly inefficient,
    //getting individual pixels rather than all at once.
    for(int x = 0; x < dstIn.getWidth(); x++) {
      for(int y = 0; y < dstIn.getHeight(); y++) {
        float[] pxSrc = null;
        pxSrc = src.getPixel(x, y, pxSrc);
        float[] pxDst = null;
        pxDst = dstIn.getPixel(x, y, pxDst);

        float alpha = 255;
        if(pxSrc.length > 3) {
          alpha = pxSrc[3];
        }

        for(int i = 0; i < 3 && i < minCh; i++) {
          pxDst[i] = Math.min(255, (pxSrc[i] * (alpha / 255)) + (pxDst[i]));
          dstOut.setPixel(x, y, pxDst);
        }
      }
    }
  }

  public void dispose(){}
}