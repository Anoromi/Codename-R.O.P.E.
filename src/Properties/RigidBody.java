package Properties;

import Base.Game;
import Helpers.Vector2;

/**
 * Defines RigidBody for Property. Allows to move object after every update. File: RigidBody.java
 * @author Andrii Zahorulko
 */
public abstract class RigidBody extends Property {

  protected RigidBody() {
    update = this::updateForces;
  }

  /**
   * Updates position of GameObject.
   * @param game
   */
  public abstract void updateForces(Game game);

  /**
   * Creates an impulse in relative direction
   * @param vector vector in relative coordinates
   */
  public abstract void impulse(Vector2 vector);

  /**
   * Creates an impulse in real direction
   * @param vector vector in relative coordinates
   */
  public abstract void realImpulse(Vector2 vector);

  public abstract void setSpeed(Vector2 speed);

  /**
   * Restarts RigidBody by setting speed to (0,0)
   */
  @Override
  public void restart() {
    setSpeed(new Vector2(0, 0));
  }

  public abstract Vector2 getSpeed();
}
