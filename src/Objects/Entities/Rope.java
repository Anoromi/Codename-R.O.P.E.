package Objects.Entities;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import Base.Game;
import Helpers.GeometryHelper;
import Helpers.ImageHelper;
import Helpers.Vector2;
import Objects.GameSettings;
import Objects.GameSprite;
import Objects.ObjectTag;
import Properties.Mesh;

/**
 * Responsible for creation and painting of rope. File: Rope.java
 *
 * @author Andrii Zahorulko
 */
public class Rope extends GameSprite {
  public static final BufferedImage ROPE_IMAGE = ImageHelper.imageOrNull("icons\\Rope.png");
  private GameBall ball;
  private HookComponent hook;
  private Vector2 direction;
  private Vector2 fullDirection;
  private boolean alive;
  private BufferedImage curImage;

  public Rope(GameBall ball, HookComponent hook) {
    super(ROPE_IMAGE, new Rectangle(), GameSettings.ROPE_LAYER);
    this.ball = ball;
    this.hook = hook;
    mesh = new Mesh(new Rectangle()) {

      @Override
      protected AffineTransform getTransform() {
        return Rope.this.getTransform().getFullAffine();
      }
    };
    addTags(ObjectTag.Disposable);
    updatePos();
  }

  /**
   * Invoked after {@link GameBall} update. Updates position of the rope.
   *
   * @param game
   */
  public void realUpdate(Game game) {
    updatePos();
    getMesh().setShape(new Rectangle(0, 0, (int) direction.magnitude(), GameSettings.ROPE_HEIGHT));
    if (game.getIntersectedObjects(mesh).stream()
        .anyMatch(x -> !x.hasTags(ObjectTag.GameBall) && x.hasTags(ObjectTag.Touchable))) {
      alive = false;
    } else
      alive = true;
  }

  /**
   * Updates position of the rope.
   */
  private void updatePos() {
    var ballBounds = ball.getMesh().getRelativeRectangleBounds().getBounds2D();
    var hookBallBounds = hook.getHookBall().getMesh().getRelativeRectangleBounds().getBounds2D();
    var ballCenter = new Vector2(ballBounds.getCenterX(), ballBounds.getCenterY());
    var hookBallCenter = new Vector2(hookBallBounds.getCenterX(), hookBallBounds.getCenterY());
    fullDirection = hookBallCenter.subtracted(ballCenter);
    var oposDir = ballCenter.subtracted(hookBallCenter).normalized();
    oposDir.multiplyBy(hookBallBounds.getWidth() / 2);
    hookBallCenter.add(oposDir);
    direction = hookBallCenter.subtracted(ballCenter);
    getTransform().setPosition(ballCenter);
    getTransform().setRotation(GeometryHelper.vectorToAngle(hookBallCenter.subtracted(ballCenter)));
    getTransform().translate(0, -GameSettings.ROPE_HEIGHT / 2 - 1);
  }

  /**
   * Draws rope in the direction.
   */
  @Override
  public void draw(Graphics2D graphics, int layer) {
    if (this.layer == layer && direction != null) {
      curImage = ImageHelper.rescale(image, (int) fullDirection.magnitude(), GameSettings.ROPE_HEIGHT);
      if (alive && curImage != null)
        graphics.drawImage(curImage, transform.getFullAffine(), null);
    }
  }

  @Override
  public int[] getLayers() {
    return new int[] { layer };
  }

  public Vector2 getDirection() {
    return direction;
  }

  public boolean isAlive() {
    return alive;
  }
}
