package Properties;

import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Point2D;

/**
 * Defines components for every mesh.
 * File: AbstractMesh.java
 * @author Andrii Zahorulko
 */
public abstract class AbstractMesh extends Property {

  /**
   * @return shape in real coordinates
   */
  public abstract Shape getRelativeShape();

  /**
   * @return relative shape
   */
  public abstract Shape getShape();

  public abstract void setShape(Shape shape);

  /**
   * Finds if mesh contains this point.
   * @param p point in real coordinates
   * @return
   */
  public abstract boolean contains(Point2D p);

  /**
   * @return bounds of this shape in real coordinates
   */
  public abstract Shape getRelativeRectangleBounds();

  /**
   * Finds if this AbstractMesh intersects another
   * @param s
   * @return
   */
  public abstract boolean intersects(AbstractMesh s);

}
