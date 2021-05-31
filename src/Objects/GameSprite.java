package Objects;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.function.Consumer;

import Helpers.ImageHelper;
import Helpers.Vector2;
import Properties.*;

public class GameSprite extends BlankSprite {
  protected Mesh mesh;

  public GameSprite(BufferedImage image, int layer) {
    super(image, layer);
    mesh = new Mesh(ImageHelper.areaFromImage(image)) {
      @Override
      protected AffineTransform getTransform() {
        return transform.getFullAffine();
      }
    };
    addProperty(ObjectProperty.Mesh, mesh);
  }

  public GameSprite(BufferedImage image, Shape shape, int layer) {
    super(image, layer);
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


  @Override
  public boolean contains(Point2D p) {
    return getMesh().contains(p);
  }

  public Shape getShape() {
    return getMesh().getShape();
  }

  @Override
  public GameSprite setPosition(double x, double y) {
    getTransform().translate(x, y);
    return this;
  }

  public GameSprite setPosition(Vector2 vector) {
    getTransform().translate(vector.x, vector.y);
    return this;
  }

  public GameSprite setRotation(double theta, double anchorX, double anchorY) {
    getTransform().setRotation(theta, anchorX, anchorY);
    return this;
  }

  public GameSprite setRotation(double theta) {
    getTransform().setRotation(theta);
    return this;
  }

  public GameSprite translate(double dx, double dy) {
    getTransform().getAffine().translate(dx, dy);
    return this;
  }

  public GameSprite translate(Vector2 vector) {
    getTransform().getAffine().translate(vector.x, vector.y);
    return this;
  }

  public GameSprite rotate(double theta) {
    getTransform().getAffine().rotate(theta);
    return this;
  }

  public GameSprite rotate(double theta, double anchorX, double anchorY) {
    getTransform().getAffine().rotate(theta, anchorX, anchorY);
    return this;
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
