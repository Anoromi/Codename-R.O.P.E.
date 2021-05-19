import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;

public class Camera {
  private AffineTransform pos;
  private double targetScale;
  private Vector2 target;

  public Camera(Vector2 pos) {
    super();
    this.pos = new AffineTransform();
    targetScale = 1;
    this.target = pos;
  }

  public void adjustCamera(Graphics2D graphics) {
    Vector2 posChange = target.subtracted(Vector2.v(pos.getTranslateX(), pos.getTranslateY())).dividedBy(10);
    pos.translate(posChange.x, posChange.y);
    double scaleChange = (targetScale - pos.getScaleX()) / 5;
    graphics.scale(scaleChange, scaleChange);

    graphics.setTransform(pos);
  }

  public void setTarget(Vector2 target) {
    this.target = target;
  }

  public Vector2 getTarget() {
    return target;
  }

  public double getTargetScale() {
    return targetScale;
  }

  public void setTargetFromWindow(Vector2 target) {
    pos.transform(target, this.target);
  }

  public void setTargetScale(double targetScale) {
    this.targetScale = targetScale;
  }

  public AffineTransform getPos() {
    return pos;
  }
}
