public abstract class RigidBody extends Property {

  protected RigidBody() {
    update = this::updateForces;
  }

  public abstract void updateForces(Game game);

  public abstract void impulse(Vector2 vector);

  public abstract void setAcceleration(Vector2 acceleration);

  public abstract Vector2 getAcceleration();
}
