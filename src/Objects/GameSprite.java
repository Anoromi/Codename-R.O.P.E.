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

/**
 * Defines a sprite with a mesh. File: GameSprite.java
 *
 * @author Andrii Zahorulko
 */
public class GameSprite extends BlankSprite {
  protected AbstractMesh mesh;

  public GameSprite(BufferedImage image, int layer) {
    super(image, layer);
    mesh = new ImageMesh(ImageHelper.areaFromImage(image)) {
      @Override
      protected AffineTransform getTransform() {
        return transform.getFullAffine();
      }

      @Override
      protected BufferedImage getImage() {
        return GameSprite.this.getImage();
      }
    };
    addProperty(ObjectProperty.Mesh, mesh);
  }

  public GameSprite(BufferedImage image, Shape shape, int layer) {
    super(image, layer);
    mesh = new ImageMesh(shape) {
      @Override
      protected AffineTransform getTransform() {
        return transform.getFullAffine();
      }

      @Override
      protected BufferedImage getImage() {
        return GameSprite.this.getImage();
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

  @Override
  public GameSprite setPosition(Vector2 vector) {
    getTransform().translate(vector.x, vector.y);
    return this;
  }

  @Override
  public GameSprite setRotation(double theta, double anchorX, double anchorY) {
    getTransform().setRotation(theta, anchorX, anchorY);
    return this;
  }

  @Override
  public GameSprite setRotation(double theta) {
    getTransform().setRotation(theta);
    return this;
  }

  @Override
  public GameSprite translate(double dx, double dy) {
    getTransform().getAffine().translate(dx, dy);
    return this;
  }

  @Override
  public GameSprite translate(Vector2 vector) {
    getTransform().getAffine().translate(vector.x, vector.y);
    return this;
  }

  @Override
  public GameSprite rotate(double theta) {
    getTransform().getAffine().rotate(theta);
    return this;
  }

  @Override
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

  public AbstractMesh getMesh() {
    return mesh;
  }

  public Shape getRelativeShape() {
    return getMesh().getRelativeShape();
  }
}
