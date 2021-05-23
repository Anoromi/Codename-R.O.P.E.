import java.awt.geom.AffineTransform;

public class Hook extends GameSprite {
  protected RigidBody rigidBody;

  public Hook(double loss, Vector2 pos, Vector2 dir, double rotation) {
    super("icons/Hook.png", 3);
    transform.setPosition(pos.x, pos.y).rotate(rotation);
    rigidBody = new PointRigidBody(1) {

      @Override
      public AffineTransform getTransform() {
        return transform.getTransform();
      }

    };
    addProperty(ObjectProperty.RigidBody, rigidBody);
    rigidBody.impulse(dir);
  }

}
