package Objects.Entities;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.swing.text.StyledEditorKit.BoldAction;

import Base.Game;
import Helpers.GeometryHelper;
import Helpers.ImageHelper;
import Helpers.Vector2;
import Objects.BlankSprite;
import Objects.ObjectTag;

public class Pointer extends BlankSprite {
  public static final BufferedImage POINTER_IMAGE = ImageHelper.imageOrNull("icons/Pointer.png");
  private boolean draw;

  public Pointer() {
    super(POINTER_IMAGE, 6);
    draw = true;
  }

  @Override
  public void update(Game game) {
    super.update(game);
    var goalBounds = game.getGoal().getMesh().getRelativeRectangleBounds().getBounds2D();
    var ballBounds = game.getBall().getMesh().getRelativeRectangleBounds().getBounds2D();
    var goalCenter = new Vector2(goalBounds.getCenterX(), goalBounds.getCenterY());
    var ballCenter = new Vector2(ballBounds.getCenterX(), ballBounds.getCenterY());
    double angle = GeometryHelper.vectorToAngle(goalCenter.subtracted(ballCenter).normalized().invert());
    var upperBound = game.camera.getUpperBound().subtracted(new Vector2(50, 50));
    var lowerBound = game.camera.getLowerBound().add(new Vector2(50, 50));
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
  }

  @Override
  public void draw(Graphics2D graphics, int layer) {
    if (draw) {
      super.draw(graphics, layer);
    }
  }
}
