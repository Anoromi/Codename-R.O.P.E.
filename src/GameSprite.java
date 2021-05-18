import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import Helper.GeometryOp;

public class GameSprite extends GameObject {
  protected Shape shape;
  protected BufferedImage image;
  protected AffineTransform transform;
  protected AffineTransform imageTransform;

  public static GameSprite createFrom(String imagePath, int layer) {
    return createFrom(new File(imagePath), layer);
  }

  public static GameSprite createFrom(File image, int layer) {
    try {
      return new GameSprite(ImageIO.read(image), layer);
    } catch (IOException e) {
      return null;
    }
  }

  public GameSprite(BufferedImage image, int layer) {
    super(layer);
    this.image = image;
    this.shape = GeometryOp.areaFromImage(image);
    transform = new AffineTransform();
  }

  @Override
  public void draw(Graphics2D graphics) {
    graphics.drawImage(image, imageTransform, null);
  }

  @Override
  public boolean contains(Point p) {
    return shape.contains(p.getX(), p.getY());
  }

  public Shape getShape() {
    return shape;
  }

  @Override
  public void update(Game game) {
  }

  @Override
  public void translate(double dx, double dy) {
    transform.translate(dx, dy);

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
