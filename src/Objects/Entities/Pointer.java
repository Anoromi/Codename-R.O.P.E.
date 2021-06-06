package Objects.Entities;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import Base.Camera;
import Base.Game;
import Helpers.GeometryHelper;
import Helpers.ImageHelper;
import Helpers.Vector2;
import Objects.BlankSprite;

/**
 * Responsible for creation and disposition of pointer to goal.
 * File: Pointer.java
 * @author Andrii Zahorulko
 */
public class Pointer extends BlankSprite {
  public static final BufferedImage POINTER_IMAGE = ImageHelper.imageOrNull("icons/Pointer.png");
  private boolean draw;
  private Goal goal;
  private GameBall ball;
  private Camera camera;

  public Pointer(Goal goal, GameBall ball, Camera camera) {
    super(POINTER_IMAGE, 6);
    draw = true;
    this.goal = goal;
    this.ball = ball;
    this.camera = camera;
  }

  @Override
  public void update(Game game) {
    super.update(game);
  }

  @Override
  public void draw(Graphics2D graphics, int layer) {
    if (layer == this.layer) {
      var goalBounds = goal.getMesh().getRelativeRectangleBounds().getBounds2D();
      var ballBounds = ball.getMesh().getRelativeRectangleBounds().getBounds2D();
      var goalCenter = new Vector2(goalBounds.getCenterX(), goalBounds.getCenterY());
      var ballCenter = new Vector2(ballBounds.getCenterX(), ballBounds.getCenterY());
      double angle = GeometryHelper.vectorToAngle(goalCenter.subtracted(ballCenter).normalized().invert());
      var upperBound = camera.getUpperBound().subtracted(new Vector2(50, 50));
      var lowerBound = camera.getLowerBound().add(new Vector2(50, 50));
      var intersection = GeometryHelper.tryIntersection(new Vector2(upperBound.x, upperBound.y),
          new Vector2(upperBound.x, lowerBound.y), goalCenter, ballCenter);
      if (intersection == null) {
        intersection = GeometryHelper.tryIntersection(new Vector2(lowerBound.x, upperBound.y),
            new Vector2(lowerBound.x, lowerBound.y), goalCenter, ballCenter);
      }
      if (intersection == null) {
        intersection = GeometryHelper.tryIntersection(new Vector2(lowerBound.x, upperBound.y),
            new Vector2(upperBound.x, upperBound.y), goalCenter, ballCenter);

      }
      if (intersection == null) {
        intersection = GeometryHelper.tryIntersection(new Vector2(lowerBound.x, lowerBound.y),
            new Vector2(upperBound.x, lowerBound.y), goalCenter, ballCenter);
      }
      if (intersection != null) {
        setPosition(intersection.x, intersection.y);
        setRotation(angle);
        draw = true;
      } else
        draw = false;
      if (draw) {
        super.draw(graphics, layer);
      }
    }
  }
}
