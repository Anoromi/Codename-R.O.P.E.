package Properties;
import java.awt.Point;
import java.awt.geom.AffineTransform;

import Base.Game;
import Helpers.Vector2;

public abstract class PointRigidBody extends RigidBody {

  private Vector2 acceleration;
  private double loss;

  protected PointRigidBody(double loss) {
    super();
    acceleration = new Vector2();
    this.loss = loss;
  }

  @Override
  public void updateForces(Game game) {
    getTransform().translate(acceleration.getX(), acceleration.getY());
    acceleration.divideBy(loss);
  }

  @Override
  public void impulse(Vector2 vector) {
    acceleration.add(vector);
  }



  @Override
  public Vector2 getAcceleration() {
    return acceleration;
  }

  @Override
  public void setAcceleration(Vector2 acceleration) {
    this.acceleration = acceleration;
  }

  public abstract AffineTransform getTransform();

}