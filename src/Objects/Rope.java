package Objects;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import Base.Game;
import Helpers.GeometryHelper;
import Helpers.ImageHelper;
import Helpers.Vector2;
import Objects.Hook.HookComponent;

public class Rope extends GameSprite {
  public static final BufferedImage ROPE_IMAGE = ImageHelper.imageOrNull("icons\\Rope.png");
  private GameBall ball;
  private HookComponent hook;
  private Vector2 direction;

  public Rope(GameBall ball, HookComponent hook) {
    super(ROPE_IMAGE, new Rectangle(), GameSettings.ROPE_LAYER);
    this.ball = ball;
    this.hook = hook;
    updatePos();
  }

  @Override
  public void update(Game game) {
    super.update(game);
    updatePos();
  }

  private void updatePos() {
    var ballBounds = ball.getMesh().getRelativeRectangleBounds().getBounds2D();
    var hookBallBounds = hook.getHookBall().getMesh().getRelativeRectangleBounds().getBounds2D();
    var ballCenter = new Vector2(ballBounds.getCenterX(), ballBounds.getCenterY())
        .add(new Vector2(GameSettings.ROPE_HEIGHT / 2, 0));
    var hookBallCenter = new Vector2(hookBallBounds.getCenterX(), hookBallBounds.getCenterY())
        .add(new Vector2(GameSettings.ROPE_HEIGHT / 2, 0));
    direction = hookBallCenter.subtracted(ballCenter);
    transform.setPosition(ballCenter);
    transform.setRotation(GeometryHelper.vectorToAngle(hookBallCenter.subtracted(ballCenter).degRotateBy(-90)));
  }

  @Override
  public void draw(Graphics2D graphics, int layer) {
    if (this.layer == layer) {
      graphics.drawImage(ImageHelper.rescale(image, GameSettings.ROPE_HEIGHT, (int) direction.magnitude()),
          transform.getFullAffine(), null);
    }
  }

  @Override
  public boolean contains(Point2D p) {
    return false;
  }

  @Override
  public boolean intersects(Shape object) {
    return false;
  }

  @Override
  public int[] getLayers() {
    return new int[] { layer };
  }

  public Vector2 getDirection() {
    return direction;
  }
}
