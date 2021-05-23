import java.awt.geom.AffineTransform;

public class Transform extends Property {
  protected AffineTransform t;

  public Transform() {
    t = new AffineTransform();
  }

  public AffineTransform getTransform() {
    return t;
  }

  public void setTransform(AffineTransform transform) {
    this.t = transform;
  }

  public Transform translate(double dx, double dy) {
    t.translate(dx, dy);
    return this;
  }

  public Transform translate(Vector2 vector) {
    t.translate(vector.x, vector.y);
    return this;
  }

  public Transform setPosition(double x, double y) {
    t.setToTranslation(x, y);
    return this;
  }

  public Transform setPosition(Vector2 vector) {
    t.setToTranslation(vector.x, vector.y);
    return this;
  }

  public Transform rotate(double theta) {
    t.rotate(theta);
    return this;
  }

}
