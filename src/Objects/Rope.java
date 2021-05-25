package Objects;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import Helpers.GeometryHelper;
import Helpers.ImageHelper;

public class Rope extends GameObject {
  public static final BufferedImage ROPE_IMAGE = ImageHelper.imageOrNull("icons\\Rope.png");

  public Rope(int layer) {
    
  }

  public void connect() {

  }

  @Override
  public void draw(Graphics2D graphics, int layer) {
    // TODO Auto-generated method stub

  }

  @Override
  public boolean contains(Point2D p) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean intersects(Shape object) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public int[] getLayers() {
    // TODO Auto-generated method stub
    return null;
  }
}
