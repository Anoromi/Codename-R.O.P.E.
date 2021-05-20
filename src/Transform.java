import java.awt.geom.AffineTransform;

public class Transform extends Property {
  private AffineTransform transform;

  public Transform() {
    transform = new AffineTransform();
  }

  public AffineTransform getTransform() {
    return transform;
  }

  public void setTransform(AffineTransform transform) {
    this.transform = transform;
  }
}
