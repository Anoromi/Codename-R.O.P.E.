package Properties;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;

public abstract class RectangleMesh extends Mesh {
  protected Rectangle shape;

  protected RectangleMesh(Rectangle shape) {
    super(shape);
    this.shape = shape;
  }

  @Override
  public Rectangle getShape() {
    return shape;
  }

  @Override
  public boolean intersects(Mesh s) {
    return s.getRelativeShape().intersects(shape);
  }

  public void setShape(Rectangle shape) {
    this.shape = shape;
  }

}
