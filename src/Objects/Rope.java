package Objects;

import static java.lang.System.out;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import Base.Game;
import Helpers.GeometryHelper;
import Helpers.ImageHelper;
import Helpers.Vector2;
import Objects.Hook.HookComponent;
import Properties.Mesh;
import Properties.RectangleMesh;

public class Rope extends GameSprite {
  public static final BufferedImage ROPE_IMAGE = ImageHelper.imageOrNull("icons\\Rope.png");
  private GameBall ball;
  private HookComponent hook;
  private Vector2 direction;
  private boolean alive;

  public Rope(GameBall ball, HookComponent hook) {
    super(ROPE_IMAGE, new Rectangle(), GameSettings.ROPE_LAYER);
    this.ball = ball;
    this.hook = hook;
    mesh = new RectangleMesh(new Rectangle()) {
      @Override
      protected AffineTransform getTransform() {
        return Rope.this.getTransform().getFullAffine();
      }
    };
    updatePos();
  }

  @Override
  public void update(Game game) {
    super.update(game);
    updatePos();
    if (game.getIntersectedObjects(mesh).stream()
        .anyMatch(x -> !x.hasTags(ObjectTag.GameBall) && x.hasTags(ObjectTag.Touchable))) {
      alive = false;
    } else
      alive = true;
  }

  private void updatePos() {
    var ballBounds = ball.getMesh().getRelativeRectangleBounds().getBounds2D();
    var hookBallBounds = hook.getHookBall().getMesh().getRelativeRectangleBounds().getBounds2D();
    var ballCenter = new Vector2(ballBounds.getCenterX(), ballBounds.getCenterY());
    var hookBallCenter = new Vector2(hookBallBounds.getCenterX(), hookBallBounds.getCenterY());
    direction = hookBallCenter.subtracted(ballCenter);
    getTransform().setPosition(ballCenter);
    getTransform().setRotation(GeometryHelper.vectorToAngle(hookBallCenter.subtracted(ballCenter)));
    getTransform().translate(0, -GameSettings.ROPE_HEIGHT / 2);
  }

  @Override
  public void draw(Graphics2D graphics, int layer) {
    if (this.layer == layer) {
      getMesh().setShape(new Rectangle(0, 0, (int) direction.magnitude(), GameSettings.ROPE_HEIGHT));
      if (alive)
        graphics.drawImage(ImageHelper.rescale(image, (int) direction.magnitude(), GameSettings.ROPE_HEIGHT),
            transform.getFullAffine(), null);
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

  @Override
  public RectangleMesh getMesh() {
    return (RectangleMesh) super.getMesh();
  }
}
