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

public class GameSprite extends GameObject {
  protected Shape shape;
  protected BufferedImage image;
  protected Transform transform;

  public GameSprite(BufferedImage image, int layer) {
    super(layer);
    this.image = image;
    this.shape = ImageHelper.areaFromImage(image);
    transform = new Transform();
    addProperty(ObjectProperty.Transform, transform);
  }

  public GameSprite(String image, int layer) {
    this(ImageHelper.imageOrNull(image), layer);
  }

  @Override
  public void draw(Graphics2D graphics) {
    graphics.drawImage(image, transform.getTransform(), null);
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
    super.update(game);
  }

  @Override
  public GameObject addTags(ObjectTag... tags) {
    super.addTags(tags);
    return this;
  }

  @Override
  public GameSprite setPosition(Vector2 vector) {
    transform.getTransform().translate(vector.x, vector.y);
    return this;
  }

  @Override
  public void translate(double dx, double dy) {
    transform.getTransform().translate(dx, dy);
  }

  @Override
  public void translate(Vector2 vector) {
    transform.getTransform().translate(vector.x, vector.y);
  }

  @Override
  public void rotate(double theta) {
    transform.getTransform().rotate(theta);
  }

  @Override
  public boolean intersects(Shape object) {
    var area = new Area(object);
    area.intersect(new Area(getRelativeShape()));
    return !area.isEmpty();
  }

  @Override
  public Shape getRelativeShape() {
    return transform.getTransform().createTransformedShape(shape);
  }

}
