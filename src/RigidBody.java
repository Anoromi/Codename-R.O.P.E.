import java.awt.Point;

public interface RigidBody {
  void updateForces();

  void impulse(Vector2 vector);
}
