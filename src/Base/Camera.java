package Base;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import Helpers.ImageHelper;
import Helpers.Vector2;
import Objects.GameObject;
import Objects.GameSettings;
import Objects.Entities.GameBall;

/**
 * Controls camera position and scaling.
 */
public class Camera {
  private AffineTransform pos;
  private double scale;
  private Vector2 target;
  private BufferedImage background;

  /**
   * Creates camera.
   *
   * @param pos
   */
  public Camera(Vector2 pos) {
    super();
    this.pos = new AffineTransform();
    scale = 1.3;
    this.target = pos;
  }

  /**
   * Sets transform for graphics. Also, adds background
   *
   * @param graphics
   */
  public void adjustCamera(Graphics2D graphics) {
    Vector2 posChange = target.subtracted(new Vector2(pos.getTranslateX(), pos.getTranslateY()));
    if (background != null) {
      graphics.drawImage(background, null, 0, 0);
    }
    pos = new AffineTransform();
    pos.translate(-target.x, -target.y);
    pos.scale(scale, scale);
    graphics.setTransform(pos);
  }

  /**
   * Updates position of the target.
   *
   * @param game
   * @param ball
   */
  public void updateTarget(Game game, GameBall ball) {
    var ballBounds = ball.getRelativeShape().getBounds2D();
    setCenter(game, new Vector2(ballBounds.getCenterX(), ballBounds.getCenterY()));
  }

  /**
   * Sets starting position of the camera.
   *
   * @param game
   * @param newTarget center of the camera in real coordinates.
   */
  private void setCenter(Game game, Vector2 newTarget) {
    newTarget.x *= scale;
    newTarget.y *= scale;
    newTarget.x -= GameSettings.FRAME_WIDTH / 2;
    newTarget.y -= GameSettings.FRAME_HEIGHT / 2;

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

  /**
   * Gets highest point inside camera.
   *
   * @return
   */
  public Vector2 getUpperBound() {
    return target.added(new Vector2(GameSettings.FRAME_WIDTH, GameSettings.FRAME_HEIGHT)).divideBy(scale);
  }

  /**
   * Gets lowest point inside camera.
   *
   * @return
   */
  public Vector2 getLowerBound() {
    return target.dividedBy(scale);
  }

  /**
   * Scales a point on screen to point in camera. It doesn't move point, however.
   */
  public Point toRealResolution(Point p) {
    Point n = new Point(p);
    n.x *= GameSettings.SCALING / scale;
    n.y *= GameSettings.SCALING / scale;
    return n;
  }

  public BufferedImage getBackground() {
    return background;
  }

  public void setBackground(BufferedImage background) {
    this.background = ImageHelper.rescale(background, (int) Math.ceil(GameSettings.FRAME_WIDTH / scale),
        (int) Math.ceil(GameSettings.FRAME_HEIGHT / scale));
  }

}
