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

  public static Vector2 intersection(Vector2 p1, Vector2 p2, Vector2 p3, Vector2 p4) {
    var denom = (p1.x - p2.x) * (p3.y - p4.y) - (p1.y - p2.y) * (p3.x - p4.x);
    return new Vector2(
        ((p1.x * p2.y - p1.y * p2.x) * (p3.x - p4.x) - (p1.x - p2.x) * (p3.x * p4.y - p3.y * p4.x)) / denom,
        ((p1.x * p2.y - p1.y * p2.x) * (p3.y - p4.y) - (p1.y - p2.y) * (p3.x * p4.y - p3.y * p4.x)) / denom);
  }

  public static Vector2 tryIntersection(Vector2 p1, Vector2 p2, Vector2 p3, Vector2 p4) {
    Vector2 intr = intersection(p1, p2, p3, p4);
    if (insideRect(p1, p2, intr) && insideRect(p3, p4, intr)) {
      return intr;
    } else
      return null;
  }

  public static boolean insideRect(Vector2 r1, Vector2 r2, Vector2 point) {
    return ((r1.x + 0.01 > point.x && r2.x - 0.01 < point.x) || (r1.x - 0.01 < point.x && r2.x + 0.01 > point.x))
        && ((r1.y + 0.01 > point.y && r2.y - 0.01 < point.y) || (r1.y - 0.01 < point.y && r2.y + 0.01 > point.y));
  }

  public static double determinant(Vector2 f, Vector2 s) {
    return f.x * s.y - f.y * s.x;
  }
}
