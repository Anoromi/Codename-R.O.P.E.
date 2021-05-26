package Objects.Hook;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
  private boolean stuck = false;
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
            ball.removeHook();
            return;
          }
          if (!intersected.isEmpty()) {
            stuck = true;
            rigidBody.setAcceleration(new Vector2());
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
                // .setRotation(0)
                .setRelative(parentTransform);
            // HookComponent.this.transform.setRelative();
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

    rigidBody.impulse(new Vector2(-1, 0).multipliedBy(10));

    hookBall = new GameSprite(hookBallImage, hookBallShape, 3) {
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
      public void draw(Graphics2D graphics, int layer) {
      }
    };
    addProperty(ObjectProperty.Transform, transform);
    addProperty(ObjectProperty.RigidBody, rigidBody);

    gameObjects.add(hook);
    gameObjects.add(hookBall);
  }

  public Transform getTransform() {
    return transform;
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
