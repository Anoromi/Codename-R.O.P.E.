package Properties;

import java.awt.geom.AffineTransform;

import Helpers.Vector2;

public class Transform extends Property {
  protected AffineTransform t;
  protected Transform relative;

  public Transform() {
    t = new AffineTransform();
  }

  public Transform(Transform relative) {
    t = new AffineTransform();
    this.relative = relative;
  }

  public AffineTransform getAffine() {
    return t;
  }

  public AffineTransform getFullAffine() {
    if (relative == null)
      return getAffine();
    AffineTransform transformed = new AffineTransform(relative.getFullAffine());
    transformed.concatenate(getAffine());
    return transformed;
  }

  public void setTransform(AffineTransform transform) {
    this.t = transform;
  }

  public Transform translate(double dx, double dy) {
    t.translate(dx, dy);
    return this;
  }

  public Transform translate(Vector2 vector) {
    t.translate(vector.x, vector.y);
    return this;
  }

  public Transform setPosition(double x, double y) {
    t.setToTranslation(x, y);
    return this;
  }

  public Transform setPosition(Vector2 vector) {
    t.setToTranslation(vector.x, vector.y);
    return this;
  }

  public Transform setRotation(double theta) {
    t.rotate(theta - getRotation());
    return this;
  }

  public Transform rotate(double theta) {
    t.rotate(theta);
    return this;
  }

  public Transform getRelative() {
    return relative;
  }

  public void setRelative(Transform relative) {
    this.relative = relative;
  }

  public double getRotation() {
    return Math.atan2(t.getShearY(), t.getScaleY());
  }

  public double getFullRotation() {
    AffineTransform full = getFullAffine();
    return Math.atan2(full.getShearY(), full.getScaleY());
  }

}
