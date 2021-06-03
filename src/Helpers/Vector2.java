package Helpers;

import java.awt.Point;
import java.awt.geom.Point2D;

/**
 * A wrapping for Point2D with useful methods.
 * File: Vector2.java
 * @author Andrii Zahorulko
 */
public class Vector2 extends Point2D.Double {

  public Vector2() {
  }

  public Vector2(double x, double y) {
    super(x, y);
  }

  public Vector2(Point p) {
    super(p.x, p.y);
  }

  public static Vector2 v(double x, double y) {
    return new Vector2(x, y);
  }

  public static Vector2 v(double xy) {
    return new Vector2(xy, xy);
  }

  public static Vector2 v(Point p) {
    return new Vector2(p.getX(), p.getY());
  }

  public double magnitude() {
    return Math.sqrt(x * x + y * y);
  }

  /**
   * returns a normalized vector of this one
   *
   * @return - returns a normalized vector
   */
  public Vector2 normalized() {
    return copy().dividedBy(magnitude());
  }

  public Vector2 invert() {
    return this.multiplyBy(-1);
  }

  public Vector2 inverted() {
    return this.multipliedBy(-1);
  }

  /**
   * Multiplies current vector on some number
   *
   * @param magnitude - number on which the vector is muliplied
   */
  public Vector2 multiplyBy(double magnitude) {
    x *= magnitude;
    y *= magnitude;
    return this;
  }

  public Vector2 multipliedBy(double magnitude) {
    return copy().multiplyBy(magnitude);
  }

  public Vector2 divideBy(double magnitude) {
    x /= magnitude;
    y /= magnitude;
    return this;
  }

  public Vector2 dividedBy(double magnitude) {
    return copy().divideBy(magnitude);
  }

  public Vector2 subtract(Vector2 value) {
    x -= value.x;
    y -= value.y;
    return this;
  }

  public Vector2 subtracted(Vector2 value) {
    return copy().subtract(value);
  }

  public Vector2 add(Vector2 value) {
    x += value.x;
    y += value.y;
    return this;
  }

  public Vector2 added(Vector2 value) {
    return copy().add(value);
  }

  /**
   * Rotates a vector around angle
   * @param angle angle in degrees
   * @return
   */
  public Vector2 degRotateBy(double angle) {
    double rad = Math.toRadians(angle);
    double curX = x;
    double curY = y;
    x = curX * Math.cos(rad) - curY * Math.sin(rad);
    y = curX * Math.sin(rad) + curY * Math.cos(rad);
    return this;
  }

  public Vector2 degRotatedBy(double angle) {
    return copy().degRotateBy(angle);
  }

  public Vector2 copy() {
    return new Vector2(x, y);
  }

  public Point toPoint() {
    return new Point(Math.round((float) x), Math.round((float) y));
  }

  public double dotProduct(Vector2 vector) {
    return x * vector.x + y * vector.y;
  }
}
