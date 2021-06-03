package Objects;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.function.Consumer;

import Helpers.ImageHelper;
import Helpers.Vector2;
import Properties.ObjectProperty;
import Properties.Property;
import Properties.Transform;

/**
 * Defines a sprite without mesh.
 * File: BlankSprite.java
 * @author Andrii Zahorulko
 */
public class BlankSprite extends SingleGameObject {
  protected BufferedImage image;
  protected Transform transform;

  public BlankSprite(String image, int layer) {
    this(ImageHelper.imageOrNull(image), layer);
  }

  public BlankSprite(BufferedImage image, int layer) {
    super(layer);
    this.image = image;
    transform = new Transform();
    addProperty(ObjectProperty.Transform, transform);
  }

  @Override
  public void draw(Graphics2D graphics, int layer) {
    if (this.layer == layer)
      graphics.drawImage(image, transform.getFullAffine(), null);
  }

  @Override
  public BlankSprite addTags(ObjectTag... tags) {
    return (BlankSprite) super.addTags(tags);
  }

  @Override
  public BlankSprite addProperty(ObjectProperty propertyName, Property property) {
    return (BlankSprite) super.addProperty(propertyName, property);
  }

  @Override
  public boolean contains(Point2D p) {
    return false;
  }

  @Override
  public boolean intersects(Shape shape) {
    return false;
  }

  public BlankSprite setPosition(double x, double y) {
    getTransform().setPosition(x, y);
    return this;
  }

  public BlankSprite setPosition(Vector2 vector) {
    getTransform().setPosition(vector.x, vector.y);
    return this;
  }

  public BlankSprite setRotation(double theta, double anchorX, double anchorY) {
    getTransform().setRotation(theta, anchorX, anchorY);
    return this;
  }

  public BlankSprite setRotation(double theta) {
    getTransform().setRotation(theta);
    return this;
  }

  public BlankSprite translate(double dx, double dy) {
    getTransform().getAffine().translate(dx, dy);
    return this;
  }

  public BlankSprite translate(Vector2 vector) {
    getTransform().getAffine().translate(vector.x, vector.y);
    return this;
  }

  public BlankSprite rotate(double theta) {
    getTransform().getAffine().rotate(theta);
    return this;
  }

  public BlankSprite rotate(double theta, double anchorX, double anchorY) {
    getTransform().getAffine().rotate(theta, anchorX, anchorY);
    return this;
  }

  public Transform getTransform() {
    return transform;
  }

  @Override
  public int[] getLayers() {
    return new int[] { layer };
  }

}
