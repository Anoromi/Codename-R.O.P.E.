import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Point2D;

public abstract class Mesh extends Property {
  protected Shape shape;

  protected Mesh(Shape shape) {
    this.shape = shape;
  }

  public Shape getRelativeShape() {
    return getTransform().createTransformedShape(shape);
  }

  public Shape getShape() {
    return shape;
  }

  public boolean contains(Point2D p) {
    return shape.contains(p.getX(), p.getY());
  }

  public boolean intersects(Mesh s) {
    var area = new Area(getRelativeShape());
    area.intersect(new Area(s.getRelativeShape()));
    return !area.isEmpty();
  }

  protected abstract AffineTransform getTransform();
}
