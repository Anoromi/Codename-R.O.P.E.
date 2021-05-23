package Helpers;

public class GeometryHelper {
  public static double vectorToAngle(Vector2 vector) {
    Vector2 nVector = vector.normalized();
    double v = Math.acos(nVector.x);
    if (nVector.y < 0)
      return -v;
    else
      return v;
  }
}
