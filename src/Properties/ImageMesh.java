package Properties;

import java.awt.Shape;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

/**
 * Mesh with image acceleration. File: ImageMesh.java
 *
 * @author Andrii Zahorulko
 */
public abstract class ImageMesh extends Mesh {

  protected ImageMesh(Shape shape) {
    super(shape);
  }

  /**
   * Finds if image contains this point
   */
  @Override
  public boolean contains(Point2D p) {
    try {
      var relP = getTransform().inverseTransform(p, null);
      var im = getImage();
      if (relP.getX() >= im.getWidth() || relP.getX() < 0 || relP.getY() >= im.getHeight() || relP.getY() < 0)
        return false;
      return ((getImage().getRGB((int) relP.getX(), (int) relP.getY()) >> 24) & 0xff) != 0;
    } catch (NoninvertibleTransformException e) {
      e.printStackTrace();
    }
    return false;
  }

  protected abstract BufferedImage getImage();
}
