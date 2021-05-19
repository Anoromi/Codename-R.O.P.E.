import java.awt.Point;
import java.awt.geom.Point2D;

public class Vector2 extends Point2D.Double {

  public Vector2() {
  }

  public Vector2(double x, double y) {
    super(x, y);
  }

  /**
   * Returns magnitude of this vector
   *
   * @return - returns magnitude
   */
  public double magnitude() {
    return Math.sqrt(x * x + y * y);
  }

  /**
   * returns a normalized vector of this one
   *
   * @return - returns a normalized vector
   */
  public Vector2 normalized() {
    double magnitude = this.magnitude();
    return new Vector2(x / magnitude, y / magnitude);
  }

  /**
   * Multiplies current vector on some number
   *
   * @param magnitude - number on which the vector is muliplied
   */
  public void multiplyBy(double magnitude) {
    x *= magnitude;
    y *= magnitude;
  }
}
