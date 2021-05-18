package Helper;

import java.awt.Rectangle;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class GeometryOp {

  public static Area areaFromImage(BufferedImage image) {
    Area area = new Area();
    for (int i = 0; i < image.getWidth(); i++) {
      for (int j = 0; j < image.getHeight(); j++) {
        if ((image.getRGB(i, j) >> 24) != 0x00) {
          Rectangle r = new Rectangle(i, j, 1, 1);

          area.add(new Area(r));
        }
      }
    }
    
    return area;
  }
}
