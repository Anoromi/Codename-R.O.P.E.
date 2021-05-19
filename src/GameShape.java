import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Point2D;

public class GameShape extends GameObject {

  protected Shape shape;
  protected AffineTransform transform;

  public GameShape(Shape shape, int layer) {
    super(layer);
    this.shape = shape;
    transform = new AffineTransform();
  }

  @Override
  public void draw(Graphics2D graphics) {
    graphics.fill(transform.createTransformedShape(shape));
  }

  @Override
  public boolean contains(Point2D p) {
    return shape.contains(p.getX(), p.getY());
  }

  public Shape getShape() {
    return shape;
  }

  @Override
  public void update(Game game) {
  }

  @Override
  public GameObject addTags(ObjectTag... tags) {
    super.addTags(tags);
    return this;
  }

  @Override
  public GameShape setPosition(Vector2 vector) {
    transform.translate(vector.x, vector.y);
    return this;
  }

  @Override
  public void translate(double dx, double dy) {
    transform.translate(dx, dy);
  }

  @Override
  public void translate(Vector2 vector) {
    translate(vector.x, vector.y);

  }

  @Override
  public void rotate(double theta) {
    transform.rotate(theta);
  }

  @Override
  public boolean intersects(Shape object) {
    var area = new Area(object);
    area.intersect(new Area(getRelativeShape()));
    return !area.isEmpty();
  }

  @Override
  public Shape getRelativeShape() {
    return transform.createTransformedShape(shape);
  }

}
