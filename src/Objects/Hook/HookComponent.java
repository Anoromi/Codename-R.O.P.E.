package Objects.Hook;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import Helpers.GeometryHelper;
import Helpers.ImageHelper;
import Helpers.Vector2;
import Objects.GameCompound;
import Objects.GameShape;
import Objects.GameSprite;
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
    GameSprite hook = new GameSprite("icons\\Hook.png", 3) {
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

    GameSprite hookBall = new GameSprite("icons/HookBall.png", 3) {
      {
        transform = new Transform(HookComponent.this.transform);
        getTransform().setPosition(hook.getShape().getBounds2D().getWidth()-1, 0);
      }
    };
    GameShape collisionShape = new GameShape(new Rectangle(2, 10), 4) {
      {
        transform = new Transform(HookComponent.this.transform);
        getTransform().setPosition(hook.getShape().getBounds2D().getCenterX() - 4,
            hook.getShape().getBounds2D().getHeight() - 5);
      }
    };
    addProperty(ObjectProperty.Transform, transform);
    addProperty(ObjectProperty.RigidBody, rigidBody);
    gameObjects.add(hook);
    gameObjects.add(hookBall);
    // gameObjects.add(collisionShape);
  }
}
