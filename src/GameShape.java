import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import Helpers.ImageHelper;

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
