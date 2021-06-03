package Properties;

import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Point2D;

public abstract class AbstractMesh extends Property {

  public abstract Shape getRelativeShape();

  public abstract Shape getShape();

  public abstract void setShape(Shape shape);

  public abstract boolean contains(Point2D p);

  public abstract Shape getRelativeRectangleBounds();

  public abstract boolean intersects(AbstractMesh s);

}
