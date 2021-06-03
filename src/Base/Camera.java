package Base;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import Helpers.Vector2;
import Objects.GameSettings;
import Objects.Entities.GameBall;

public class Camera {
  private AffineTransform pos;
  private double scale;
  private Vector2 target;

  public Camera(Vector2 pos) {
    super();
    this.pos = new AffineTransform();
    scale = 1;
    this.target = pos;
  }

  public void adjustCamera(Graphics2D graphics) {
    Vector2 posChange = target.subtracted(Vector2.v(pos.getTranslateX(), pos.getTranslateY()));// .dividedBy(20);
    // posChange.invert();
    pos = new AffineTransform();
    pos.translate(-target.x, -target.y);
    graphics.scale(scale, scale);
    double scaleChange = (scale - pos.getScaleX()) / 10;
    pos.scale(scaleChange + pos.getScaleX(), scaleChange + pos.getScaleY());
    graphics.setTransform(pos);
  }

  public void updateTarget(Game game, GameBall ball) {
    var ballBounds = ball.getRelativeShape().getBounds2D();
    setCenter(game, new Vector2(ballBounds.getCenterX(), ballBounds.getCenterY()));
  }

  private void setCenter(Game game, Vector2 newTarget) {
    newTarget.x -= GameSettings.FRAME_WIDTH / 2 / scale;
    newTarget.y -= GameSettings.FRAME_HEIGHT / 2 / scale;
    target = newTarget;
  }

  public void setTarget(Vector2 target) {
    this.target = target;
  }

  public Vector2 getTarget() {
    return target;
  }

  public double getTargetScale() {
    return scale;
  }

  public void setScale(double scale) {
    this.scale = scale;
  }

  public AffineTransform getPos() {
    return pos;
  }

  public Vector2 getUpperBound() {
    return target.added(new Vector2(GameSettings.FRAME_WIDTH, GameSettings.FRAME_HEIGHT));
  }

  public Vector2 getLowerBound() {
    return target.copy();
  }
}
