package Objects;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import Helpers.ImageHelper;
import Helpers.Vector2;
import Properties.*;

public class GameSprite extends SingleGameObject {
  protected BufferedImage image;
  protected Transform transform;
  protected Mesh mesh;

  public GameSprite(BufferedImage image, int layer) {
    super(layer);
    this.image = image;
    transform = new Transform();
    addProperty(ObjectProperty.Transform, transform);
    mesh = new Mesh(ImageHelper.areaFromImage(image)) {
      @Override
      protected AffineTransform getTransform() {
        return transform.getFullAffine();
      }
    };
    addProperty(ObjectProperty.Mesh, mesh);
  }

  public GameSprite(BufferedImage image, Shape shape, int layer) {
    super(layer);
    this.image = image;
    transform = new Transform();
    addProperty(ObjectProperty.Transform, transform);
    mesh = new Mesh(shape) {
      @Override
      protected AffineTransform getTransform() {
        return transform.getFullAffine();
      }
    };
    addProperty(ObjectProperty.Mesh, mesh);
  }

  public GameSprite(String image, Shape shape, int layer) {
    this(ImageHelper.imageOrNull(image), shape, layer);
  }

  public GameSprite(String image, String shape, int layer) {
    this(ImageHelper.imageOrNull(image), ImageHelper.areaFromFile(shape), layer);
  }

  public GameSprite(String image, int layer) {
    this(ImageHelper.imageOrNull(image), layer);
  }

  @Override
  public void draw(Graphics2D graphics, int layer) {
    if (this.layer == layer)
      graphics.drawImage(image, transform.getFullAffine(), null);
  }

  @Override
  public GameSprite addTags(ObjectTag... tags) {
    return (GameSprite) super.addTags(tags);
  }

  @Override
  public GameSprite addProperty(ObjectProperty propertyName, Property property) {
    return (GameSprite) super.addProperty(propertyName, property);
  }

  public boolean contains(Point2D p) {
    return getMesh().contains(p);
  }

  public Shape getShape() {
    return getMesh().getShape();
  }

  public GameSprite setPosition(Vector2 vector) {
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

  public Mesh getMesh() {
    return mesh;
  }

  public Transform getTransform() {
    return transform;
  }

  public Shape getRelativeShape() {
    return getMesh().getRelativeShape();
  }
}
