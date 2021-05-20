import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.util.List;

public class GameCompound extends GameObject {

  List<GameObject> gameObjects;
  public GameCompound(int layer) {
    super(layer);
  }

  @Override
  public void draw(Graphics2D graphics) {
    // TODO Auto-generated method stub

  }

  @Override
  public boolean contains(Point2D p) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public void translate(double dx, double dy) {
    // TODO Auto-generated method stub

  }

  @Override
  public GameObject setPosition(Vector2 vector) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void translate(Vector2 vector) {
    // TODO Auto-generated method stub

  }

  @Override
  public void rotate(double theta) {
    // TODO Auto-generated method stub

  }

  @Override
  public boolean intersects(Shape object) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public Shape getRelativeShape() {
    // TODO Auto-generated method stub
    return null;
  }
}
