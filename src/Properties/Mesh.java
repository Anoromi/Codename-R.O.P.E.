package Properties;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Point2D;

/**
 * Defines standard mesh. File: Mesh.java
 * @author Andrii Zahorulko
 */
public abstract class Mesh extends AbstractMesh {
  protected Shape shape;

  protected Mesh(Shape shape) {
    this.shape = shape;
  }

  public Shape getRelativeShape() {
    return getTransform().createTransformedShape(getShape());
  }

  public Shape getShape() {
    return shape;
  }

  public void setShape(Shape shape) {
    this.shape = shape;
  }

  public boolean contains(Point2D p) {
    return getRelativeShape().contains(p.getX(), p.getY());
  }

  public Shape getRelativeRectangleBounds() {
    return getTransform().createTransformedShape(getShape().getBounds2D());
  }

  public boolean intersects(AbstractMesh s) {
    if (!s.getRelativeRectangleBounds().intersects(getRelativeRectangleBounds().getBounds2D()))
      return false;
    var area = new Area(getRelativeShape());
    area.intersect(new Area(s.getRelativeShape()));
    return !area.isEmpty();
  }


  protected abstract AffineTransform getTransform();
}
