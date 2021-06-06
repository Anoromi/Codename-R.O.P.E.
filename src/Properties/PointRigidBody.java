package Properties;

import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;

import Base.Game;
import Helpers.Vector2;

/**
 * Defines RigidBody with just one point. File: PointRigidBody.java
 *
 * @author Andrii Zahorulko
 */
public abstract class PointRigidBody extends RigidBody {

  private Vector2 acceleration;
  private double loss;
  private boolean linear;
  private Double maxSpeed;

  protected PointRigidBody(double loss) {
    super();
    acceleration = new Vector2();
    this.loss = loss;
    linear = false;
  }

  /**
   * Creates an instance of the ball with linear or quadratic loss
   *
   * @param loss
   * @param linear when true, the loss will always be the same
   */
  protected PointRigidBody(double loss, boolean linear) {
    super();
    acceleration = new Vector2();
    this.loss = loss;
    this.linear = linear;
  }

  /**
   * Creates an instance of the ball with linear or quadratic loss
   *
   * @param loss
   * @param linear the loss will always be the same.
   */
  protected PointRigidBody(double loss, boolean linear, double maxSpeed) {
    super();
    acceleration = new Vector2();
    this.loss = loss;
    this.linear = linear;
    this.maxSpeed = maxSpeed;
  }

  @Override
  public void updateForces(Game game) {
    if (maxSpeed != null && acceleration.magnitude() > maxSpeed)
      acceleration = acceleration.normalized().multipliedBy(maxSpeed);
    getTransform().translate(acceleration.getX(), acceleration.getY());
    if (linear && acceleration.magnitude() != 0) {
      acceleration.subtract(acceleration.normalized().multiplyBy(loss));
    } else if (!linear)
      acceleration.divideBy(loss);
  }

  @Override
  public void impulse(Vector2 vector) {
    acceleration.add(vector);
  }

  public void realImpulse(Vector2 vector) {
    var copy = vector.copy();
    try {
      getTransform().createInverse().deltaTransform(copy, copy);
    } catch (NoninvertibleTransformException e) {
      e.printStackTrace();
    }
    acceleration.add(copy.normalized().multipliedBy(vector.magnitude()));
  }

  @Override
  public Vector2 getSpeed() {
    return acceleration;
  }

  @Override
  public void setSpeed(Vector2 acceleration) {
    this.acceleration = acceleration;
  }

  public abstract AffineTransform getTransform();

}
