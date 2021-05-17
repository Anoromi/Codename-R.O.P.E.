import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;

public abstract class Sprite extends GameObject {
  protected int x;
  protected int y;
  protected int width;
  protected int height;
  protected double rotation;
  protected Image image;
  protected boolean rigid;

  protected Sprite(int layer) {
    super(layer);
  }

  public int getX() {
    return x;
  }

  public void setX(int x) {
    this.x = x;
  }

  public int getY() {
    return y;
  }

  public void setY(int y) {
    this.y = y;
  }

  public int getWidth() {
    return width;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public int getHeight() {
    return height;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  public boolean isRigid() {
    return rigid;
  }

  @Override
  public void draw(Graphics2D graphics) {
    graphics.rotate(rotation);
    graphics.drawImage(image, x, y, null);

  }

  @Override
  public boolean contains(Point p) {
    // TODO Do later
    return false;
  }


}
