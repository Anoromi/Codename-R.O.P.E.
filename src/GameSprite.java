import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import Helpers.ImageHelper;

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
        return transform.getTransform();
      }
    };
    addProperty(ObjectProperty.Mesh, mesh);
  }

  public GameSprite(String image, int layer) {
    this(ImageHelper.imageOrNull(image), layer);
  }

  @Override
  public void draw(Graphics2D graphics, int layer) {
    if (this.layer == layer)
      graphics.drawImage(image, transform.getTransform(), null);
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
    return mesh.contains(p);
  }

  public Shape getShape() {
    return mesh.getShape();
  }

  public GameSprite setPosition(Vector2 vector) {
    transform.getTransform().translate(vector.x, vector.y);
    return this;
  }

  public void translate(double dx, double dy) {
    transform.getTransform().translate(dx, dy);
  }

  public void translate(Vector2 vector) {
    transform.getTransform().translate(vector.x, vector.y);
  }

  public void rotate(double theta) {
    transform.getTransform().rotate(theta);
  }

  public boolean intersects(Shape object) {
    var area = new Area(object);
    area.intersect(new Area(getRelativeShape()));
    return !area.isEmpty();
  }

  public Transform getTransform() {
    return transform;
  }

  public Shape getRelativeShape() {
    return mesh.getRelativeShape();
  }

}
