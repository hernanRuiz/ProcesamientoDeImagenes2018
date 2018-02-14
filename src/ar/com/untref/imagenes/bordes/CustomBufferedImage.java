package ar.com.untref.imagenes.bordes;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Enzo on 06.09.15.
 */
public class CustomBufferedImage extends BufferedImage {

    private static final int GRAY_LEVELS = 256;

    private int redMax = -1;
    private int redMin = 10000;
    private int[] red;

    private int greenMax = -1;
    private int greenMin = 10000;
    private int[] green;

    private int blueMax = -1;
    private int blueMin = 10000;
    private int[] blue;

    private final int RED_MASK = 0x00FF0000;
    private final int GREEN_MASK = 0x0000FF00;
    private final int BLUE_MASK = 0x000000FF;

    private final int imgSize;
    private int[] frequenciesHisogram;

    public CustomBufferedImage(int width, int height, int imageType) {
        super(width, height, imageType);
        imgSize = width * height;
        red = new int[width * height];
        green = new int[width * height];
        blue = new int[width * height];
    }

    public CustomBufferedImage(CustomBufferedImage customImg) {
        super(customImg.getWidth(), customImg.getHeight(), customImg.getType());
        imgSize = customImg.getWidth() * customImg.getHeight();
        red = new int[customImg.getWidth() * customImg.getHeight()];
        green = new int[customImg.getWidth() * customImg.getHeight()];
        blue = new int[customImg.getWidth() * customImg.getHeight()];
        int i, r, g, b;
        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                setRGBCustom(x, y, customImg.getRed(x, y),
                        customImg.getGreen(x, y),
                        customImg.getBlue(x, y));
            }
        }
    }

    public CustomBufferedImage(BufferedImage img) {
        super(img.getWidth(), img.getHeight(), img.getType());
        imgSize = img.getWidth() * img.getHeight();
        red = new int[img.getWidth() * img.getHeight()];
        green = new int[img.getWidth() * img.getHeight()];
        blue = new int[img.getWidth() * img.getHeight()];
        loadColorBuffers(img);
    }

    public int getGray(int x, int y) {
        return red[getWidth() * y + x];
    }

    public void setGray(int x, int y, int grayLevel) {
        red[getWidth() * y + x] = green[getWidth() * y + x] = blue[getWidth() * y + x] = 0xFF & grayLevel;
        int rgb = (0xFF & grayLevel) << 16 | (0xFF & grayLevel) << 8 | 0xFF & grayLevel;
        setRGB(x, y, rgb);
    }
    
    
    public int getRed(Point p) {
        return getRed(p.x, p.y);
    }
    
    public int getRed(int x, int y) {
        return red[y * getWidth() + x];
    }

    public int getGreen(Point p) {
        return getGreen(p.x, p.y);
    }
    
    public int getGreen(int x, int y) {
        return green[y * getWidth() + x];
    }

    public int getBlue(Point p) {
        return getBlue(p.x, p.y);
    }
    
    public int getBlue(int x, int y) {
        return blue[y * getWidth() + x];
    }
    
    public void loadColorBuffers() {
        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                setRGBCustom(x, y, getRGB(x, y));
            }
        }
    }

   private void loadColorBuffers(BufferedImage img) {
       for (int x = 0; x < img.getWidth(); x++) {
           for (int y = 0; y < img.getHeight(); y++) {
               setRGB(x, y, img.getRGB(x, y));
               setRGBCustom(x, y, img.getRGB(x, y));
           }
       }
   }

   public void setRGBCustom(int x, int y, int r, int g, int b) {
       red[getWidth() * y + x] = r;
       green[getWidth() * y + x] = g;
       blue[getWidth() * y + x] = b;
       updateMaxMinValues(r, g, b);
   }

   public void setRGBCustom(int x, int y, int rgb) {
       setRGBCustom(x, y, (RED_MASK & rgb) >>> 16, (GREEN_MASK & rgb) >>> 8, BLUE_MASK & rgb);
   }
   
   private void updateMaxMinValues(int r, int g, int b) {
       if (r > redMax) {
           redMax = r;
       } else if (r < redMin) {
           redMin = r;
       }

       if (g > greenMax) {
           greenMax = g;
       } else if (g < greenMin) {
           greenMin = g;
       }

       if (b > blueMax) {
           blueMax = b;
       } else if (b < blueMin) {
           blueMin = b;
       }
   }
}
