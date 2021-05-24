package Helpers;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageHelper {

  public static Area areaFromImage(BufferedImage image) {
    Area area = new Area();
    int width = image.getWidth();
    int height = image.getHeight();
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        if (isOutline(image, i, j)) {
          int h = 1;
          for (; j + h < height && image.getRGB(i, j + h) >> 24 != 0x00; h++) {
          }
          Rectangle r = new Rectangle(i, j, 1, h);
          area.add(new Area(r));
        }
      }
    }
    return area;
  }

  public static boolean isOutline(BufferedImage image, int x, int y) {
    if (image.getRGB(x, y) >> 24 != 0x00 && ((x == 0 || (image.getRGB(x - 1, y) >> 24 == 0x00))
        || (x == image.getWidth() - 1 || (image.getRGB(x + 1, y) >> 24 == 0x00))
        || (y == 0 || (image.getRGB(x, y - 1) >> 24 == 0x00))
        || (y == image.getHeight() - 1 || (image.getRGB(x, y + 1) >> 24 == 0x00)))) {
      return true;
    }
    return false;
  }

  public static BufferedImage imageOrNull(String path) {
    try {
      return ImageIO.read(new File(path));
    } catch (IOException e) {
      return null;
    }
  }

  public static BufferedImage rescale(BufferedImage image, int width, int height) {
    BufferedImage changed = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    Graphics2D gr = changed.createGraphics();
    gr.drawImage(image.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);
    gr.dispose();
    return changed;

  }
}
