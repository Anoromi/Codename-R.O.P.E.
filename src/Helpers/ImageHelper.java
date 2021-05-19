package Helpers;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
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
          Rectangle r = new Rectangle(i, j, 1, 1);
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
}
