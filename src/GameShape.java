import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import Helper.GeometryOp;

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
  public void update() {
  }

}
