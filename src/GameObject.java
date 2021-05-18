import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;

public abstract class GameObject {
  private int layer;

  protected GameObject(int layer) {
    this.layer = layer;
  }

  public abstract void draw(Graphics2D graphics);

  public abstract boolean contains(Point p);

  public abstract void update(Game game);

  public abstract void translate(double dx, double dy);

  public abstract void rotate(double theta);

  public abstract boolean intersects(Shape object);

  public abstract Shape getRelativeShape();

  public int getLayer() {
    return layer;
  }

  public void setLayer(int layer) {
    this.layer = layer;
  }

}
