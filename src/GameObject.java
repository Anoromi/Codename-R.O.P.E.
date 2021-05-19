import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.util.*;

public abstract class GameObject {
  private int layer;
  private final Collection<ObjectTag> gameTags;

  protected GameObject(int layer) {
    this.layer = layer;
    gameTags = new HashSet<>();
  }

  public GameObject addTags(ObjectTag... tags) {
    Collections.addAll(gameTags, tags);
    return this;
    }

  public void removeTags(ObjectTag... tags) {
    for (ObjectTag tag : tags) {
      gameTags.remove(tag);
    }
  }

  public boolean hasTag(ObjectTag tag) {
    for (ObjectTag objectTag : gameTags) {
      if (objectTag == tag)
        return true;
    }
    return false;
  }

  public abstract void draw(Graphics2D graphics);

  public abstract boolean contains(Point2D p);

  public abstract void update(Game game);

  public abstract void translate(double dx, double dy);

  public abstract GameObject setPosition(Vector2 vector);

  public abstract void translate(Vector2 vector);

  public abstract void rotate(double theta);

  public abstract boolean intersects(Shape object);

  public abstract Shape getRelativeShape();

  public int getLayer() {
    return layer;
  }

  public void setLayer(int layer) {
    this.layer = layer;
  }

}
