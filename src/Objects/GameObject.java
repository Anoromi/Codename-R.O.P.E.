package Objects;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.function.Consumer;

import Base.Game;
import Properties.ObjectProperty;
import Properties.Property;

public abstract class GameObject {
  private final Collection<ObjectTag> gameTags;
  private final Map<ObjectProperty, Property> gameProperty;

  protected GameObject() {
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

  private Consumer<GameObject> start;

  public GameObject setStart(Consumer<GameObject> start) {
    this.start = start;
    return this;
  }

  public void start() {
    for (var property : gameProperty.entrySet()) {
      property.getValue().restart();
    }
    if (start != null)
      start.accept(this);
  }

  public void update(Game game) {
    gameProperty.forEach((x, y) -> y.update(game));
  }

  public abstract void draw(Graphics2D graphics, int layer);

  public abstract boolean contains(Point2D p);

  public abstract boolean intersects(Shape shape);

  public abstract int[] getLayers();

}
