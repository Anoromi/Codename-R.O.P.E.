package Objects.Entities;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.image.BufferedImage;
import java.util.List;

import Base.Game;
import Helpers.GeometryHelper;
import Helpers.ImageHelper;
import Helpers.Vector2;
import Objects.*;
import Properties.*;

/**
 * Responsible for hook movement, collision. File: HookComponent.java
 *
 * @author Andrii Zahorulko
 */
public class HookComponent extends GameCompound {
  protected Transform transform;
  protected RigidBody rigidBody;
  private static BufferedImage hookImage;
  private static BufferedImage hookBallImage;
  private static Shape hookShape;
  private static Shape hookBallShape;
  private boolean stuck = false;
  private double distance = 0;
  private GameSprite hookBall;
  static {
    hookImage = ImageHelper.imageOrNull("icons/Hook.png");
    hookBallImage = ImageHelper.imageOrNull("icons/HookBall.png");
    hookShape = ImageHelper.areaFromImage(hookImage);
    hookBallShape = ImageHelper.areaFromImage(hookBallImage);
  }

  public HookComponent(GameBall ball, Vector2 pos, Vector2 dir) {
    super();
    transform = new Transform();
    transform.setPosition(pos.x, pos.y).rotate(GeometryHelper.vectorToAngle(dir.inverted()));
    GameSprite hook = new GameSprite(hookImage, hookShape, 3) {
      {
        transform = new Transform(HookComponent.this.transform);
      }

      @Override
      public void update(Game game) {
        super.update(game);
        if (!stuck) {
          List<GameObject> intersected = game.getIntersectedObjects(mesh);
          boolean danger = false;
          intersected.removeIf(x -> x.hasAny(ObjectTag.GameBall) || !x.hasAny(ObjectTag.Touchable));
          for (GameObject gameObject : intersected) {
            if (gameObject.hasTags(ObjectTag.Danger))
              danger = true;
          }
          if (danger) {
            ball.removeNewHook();
            return;
          }
          if (!intersected.isEmpty()) {
            stuck = true;
            ball.newHookStuck();
            rigidBody.setSpeed(new Vector2());
            GameObject parent = intersected.get(0);
            Transform parentTransform = ((Transform) parent.getProperty(ObjectProperty.Transform));
            Vector2 pos = new Vector2();
            HookComponent.this.transform.getFullAffine().transform(pos, pos);
            try {
              parentTransform.getFullAffine().inverseTransform(pos, pos);
            } catch (NoninvertibleTransformException e) {
              e.printStackTrace();
            }
            double fullRotation = HookComponent.this.transform.getFullRotation();
            HookComponent.this.transform.setPosition(pos).setRotation(fullRotation - parentTransform.getFullRotation())
                .setRelative(parentTransform);
          } else {
            distance += getRigidBody().getSpeed().magnitude();
            if (distance >= GameSettings.HOOK_MAX_DISTANCE)
              ball.removeNewHook();
          }
        }
      }
    };

    rigidBody = new PointRigidBody(1) {
      @Override
      public AffineTransform getTransform() {
        return transform.getFullAffine();
      }
    };

    rigidBody.impulse(new Vector2(-1, 0).multipliedBy(GameSettings.HOOK_SPEED));

    hookBall = new GameSprite(hookBallImage, hookBallShape, 3) {
      {
        transform = new Transform(HookComponent.this.transform);
        getTransform().setPosition(hook.getShape().getBounds2D().getWidth() - 1, 0);
      }
    };

    addProperty(ObjectProperty.Transform, transform);
    addProperty(ObjectProperty.RigidBody, rigidBody);

    addTags(ObjectTag.Disposable);

    gameObjects.add(hook);
    gameObjects.add(hookBall);
  }

  public Transform getTransform() {
    return transform;
  }

  public RigidBody getRigidBody() {
    return rigidBody;
  }

  public static int getHeight() {
    return hookImage.getHeight();
  }

  public GameSprite getHookBall() {
    return hookBall;
  }

  public boolean isStuck() {
    return stuck;
  }
}
