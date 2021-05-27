package Properties;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public abstract class RectangleMesh extends Mesh {

  protected RectangleMesh(Rectangle shape) {
    super(shape);
    this.shape = shape;
  }

  @Override
  public Rectangle getShape() {
    return (Rectangle) shape;
  }

  @Override
  public boolean intersects(Mesh s) {
    return super.intersects(s);
  }

  public void setShape(Rectangle shape) {
    this.shape = shape;
  }

}
