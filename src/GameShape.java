import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.AffineTransform;

public abstract class GameShape extends GameObject {

  protected Shape shape;
  protected AffineTransform transform;

  protected GameShape(Shape shape, int layer) {
    super(layer);
    this.shape = shape;
    transform = new AffineTransform();
  }

  public static GameShape createEmpty(Shape shape, int layer) {
    return new GameShape(shape, layer) {
      @Override
      public void update() {
      }
    };
  }

  @Override
  public void draw(Graphics2D graphics) {

    graphics.fill(transform.createTransformedShape(shape));
  }

  @Override
  public boolean contains(Point p) {
    return shape.contains(p.getX(), p.getY());
  }

  public Shape getShape() {
    return shape;
  }

}
