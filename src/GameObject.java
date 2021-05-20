import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.util.*;

import Helpers.Pair;

public abstract class GameObject {
  private int layer;
  private final Collection<ObjectTag> gameTags;
  private final Map<ObjectProperty, Property> gameProperty;

  protected GameObject(int layer) {
    this.layer = layer;
    gameTags = new HashSet<>();
    gameProperty = new EnumMap<>(ObjectProperty.class);

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

  public boolean hasTags(ObjectTag... tags) {
    return gameTags.containsAll(Arrays.asList(tags));
  }

  public boolean hasAny(ObjectTag... tags) {
    for (ObjectTag objectTag : tags) {
      for (ObjectTag realTags : gameTags) {
        if (objectTag == realTags)
          return true;
      }
    }
    return false;
  }

  public GameObject addProperty(ObjectProperty propertyName, Property property) {
    gameProperty.put(propertyName, property);
    return this;
  }

  public Property getProperty(ObjectProperty propertyName) {
    return gameProperty.get(propertyName);
  }

  public abstract void draw(Graphics2D graphics);

  public abstract boolean contains(Point2D p);

  public void update(Game game) {
    gameProperty.forEach((x, y) -> {
      y.update(game);
    });
  }

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
