import java.awt.Point;
import java.awt.geom.AffineTransform;

public abstract class PointRigidBody implements RigidBody {

  private Vector2 acceleration;
  private double loss;

  protected PointRigidBody(double loss) {
    super();
    acceleration = new Vector2();
  }

  @Override
  public void updateForces() {
    getTransform().translate(acceleration.getX(), acceleration.getY());
    acceleration.setLocation(acceleration.getX() / loss, acceleration.getY() / loss);
  }

  @Override
  public void impulse(Vector2 vector) {
    acceleration.setLocation(acceleration.getX() + vector.getX(), acceleration.getY() + vector.getY());
  }

  public abstract AffineTransform getTransform();

}
