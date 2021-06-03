package Objects;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.util.function.Consumer;

import Base.Game;
import Helpers.Vector2;
import Properties.*;

public class GameShape extends SingleGameObject {

  protected Transform transform;
  protected AbstractMesh mesh;

  public GameShape(Shape shape, int layer) {
    super(layer);
    transform = new Transform();
    mesh = new Mesh(shape) {

      @Override
      protected AffineTransform getTransform() {
        return transform.getFullAffine();
      }

    };
    addProperty(ObjectProperty.Transform, transform);
    addProperty(ObjectProperty.Mesh, mesh);
  }

  @Override
  public void draw(Graphics2D graphics, int layer) {
    if (this.layer == layer)
      graphics.fill(getMesh().getRelativeShape());
  }

  @Override
  public boolean contains(Point2D p) {
    return getMesh().contains(p);
  }

  public Shape getShape() {
    return getMesh().getShape();
  }

  @Override
  public void update(Game game) {
  }

  @Override
  public GameObject addTags(ObjectTag... tags) {
    super.addTags(tags);
    return this;
  }

  public GameShape setPosition(Vector2 vector) {
    getTransform().getAffine().translate(vector.x, vector.y);
    return this;
  }

  public void translate(double dx, double dy) {
    getTransform().getAffine().translate(dx, dy);
  }

  public void translate(Vector2 vector) {
    getTransform().getAffine().translate(vector.x, vector.y);
  }

  public void rotate(double theta) {
    getTransform().getAffine().rotate(theta);
  }

  @Override
  public boolean intersects(Shape shape) {
    if (!shape.getBounds2D().intersects(getMesh().getRelativeRectangleBounds().getBounds2D()))
      return false;
    var area = new Area(shape);
    area.intersect(new Area(getMesh().getRelativeShape()));
    return !area.isEmpty();
  }

  public AbstractMesh getMesh() {
    return mesh;
  }

  public Transform getTransform() {
    return transform;
  }

}
