package Properties;

import static java.lang.System.out;

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
  private BufferedImage image;

  protected ImageMesh(Shape shape, BufferedImage image) {
    super(shape);
    this.image = image;
  }

  /**
   * Finds if image contains this point
   */
  @Override
  public boolean contains(Point2D p) {
    try {
      super.contains(p);
      var relP = getTransform().inverseTransform(p, null);
      return ((image.getRGB((int) relP.getX(), (int) relP.getY()) >> 24) & 0xff) != 0;
    } catch (NoninvertibleTransformException e) {
      e.printStackTrace();
    }
    return false;
  }

}
