import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Point2D;

public class GameShape extends SingleGameObject {

  protected Transform transform;
  protected Mesh mesh;

  public GameShape(Shape shape, int layer) {
    super(layer);
    transform = new Transform();
    addProperty(ObjectProperty.Transform, transform);
    addProperty(ObjectProperty.Mesh, new Mesh(shape) {
      @Override
      protected AffineTransform getTransform() {
        return transform.getTransform();
      }
    });
  }

  @Override
  public void draw(Graphics2D graphics, int layer) {
    if (this.layer == layer)
      graphics.fill(mesh.getRelativeShape());
  }

  @Override
  public boolean contains(Point2D p) {
    return mesh.contains(p);
  }

  public Shape getShape() {
    return mesh.getShape();
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
  public boolean intersects(Shape object) {
    var area = new Area(object);
    area.intersect(new Area(mesh.getRelativeShape()));
    return !area.isEmpty();
  }

}
