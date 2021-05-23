package Objects.Hook;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import Base.Game;
import Helpers.GeometryHelper;
import Helpers.ImageHelper;
import Helpers.Vector2;
import Objects.*;
import Properties.*;

public class HookComponent extends GameCompound {
  protected Transform transform;
  protected RigidBody rigidBody;
  private static BufferedImage hookImage;
  private static BufferedImage hookBallImage;
  private static Shape hookShape;
  private static Shape hookBallShape;

  static {
    hookImage = ImageHelper.imageOrNull("icons/Hook.png");
    hookBallImage = ImageHelper.imageOrNull("icons/HookBall.png");
    hookShape = ImageHelper.areaFromImage(hookImage);
    hookBallShape = ImageHelper.areaFromImage(hookBallImage);
  }

  public HookComponent(Vector2 pos, Vector2 dir) {
    super();
    transform = new Transform();
    transform.setPosition(pos.x, pos.y).rotate(GeometryHelper.vectorToAngle(dir.inverted()));
    GameSprite hook = new GameSprite(hookImage, hookShape, 3) {
      {
        transform = new Transform(HookComponent.this.transform);
      }
    };

    rigidBody = new PointRigidBody(1) {
      @Override
      public AffineTransform getTransform() {
        return transform.getFullAffine();
      }
    };

    rigidBody.impulse(new Vector2(-1, 0).multipliedBy(50));

    GameSprite hookBall = new GameSprite(hookBallImage, hookBallShape, 3) {
      {
        transform = new Transform(HookComponent.this.transform);
        getTransform().setPosition(hook.getShape().getBounds2D().getWidth() - 1, 0);
      }
    };
    GameShape collisionShape = new GameShape(new Rectangle(4, 10), 4) {
      {
        transform = new Transform(HookComponent.this.transform);
        getTransform().setPosition(hook.getShape().getBounds2D().getCenterX() - 4,
            hook.getShape().getBounds2D().getHeight() - 5);
      }

      @Override
      public void update(Game game) {
        super.update(game);
        var intersected = game.getIntersectedObjects(mesh);
        if (intersected.stream().filter(x -> !x.hasAny(ObjectTag.Danger) && x.hasTags(ObjectTag.Touchable))
            .count() > 0) {
          rigidBody.setAcceleration(new Vector2());
        }
      }
    };
    addProperty(ObjectProperty.Transform, transform);
    addProperty(ObjectProperty.RigidBody, rigidBody);

    gameObjects.add(hook);
    gameObjects.add(hookBall);
    gameObjects.add(collisionShape);
  }
}
