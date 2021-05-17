import java.awt.Graphics2D;
import java.awt.Point;

public abstract class GameObject {
  private int layer;
  

  protected GameObject(int layer) {
    this.layer = layer;
  }

  public abstract void draw(Graphics2D graphics);

  public abstract boolean contains(Point p);

  public abstract void update();

  public int getLayer() {
    return layer;
  }

  public void setLayer(int layer) {
    this.layer = layer;
  }

}
