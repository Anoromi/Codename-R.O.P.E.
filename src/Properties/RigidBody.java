package Properties;

import Base.Game;
import Helpers.Vector2;

public abstract class RigidBody extends Property {

  protected RigidBody() {
    update = this::updateForces;
  }

  public abstract void updateForces(Game game);

  public abstract void impulse(Vector2 vector);

  public abstract void realImpulse(Vector2 vector);

  public abstract void setAcceleration(Vector2 acceleration);

  @Override
  public void restart() {
    setAcceleration(new Vector2(0, 0));
  }

  public abstract Vector2 getSpeed();
}
