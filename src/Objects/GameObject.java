package Objects;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.function.Consumer;

import Base.Game;
import Properties.ObjectProperty;
import Properties.Property;

/**
 * Defines the basis of every object. File: GameObject.java
 * @author Andrii Zahorulko
 */
public abstract class GameObject {
  private final Collection<ObjectTag> gameTags;
  private final Map<ObjectProperty, Property> gameProperty;

  protected GameObject() {
    gameTags = new HashSet<>();
    gameProperty = new EnumMap<>(ObjectProperty.class);

  }

  /**
   * Adds tags to GameObject
   * @param tags
   * @return
   */
  public GameObject addTags(ObjectTag... tags) {
    Collections.addAll(gameTags, tags);
    return this;
  }

  /**
   * Removes tags from GameObject
   * @param tags
   */
  public void removeTags(ObjectTag... tags) {
    for (ObjectTag tag : tags) {
      gameTags.remove(tag);
    }
  }

  /**
   * Says if GameObject has all tags
   * @param tags
   * @return
   */
  public boolean hasTags(ObjectTag... tags) {
    return gameTags.containsAll(Arrays.asList(tags));
  }

  /**
   * Says if GameObject has any of tags
   * @param tags
   * @return
   */
  public boolean hasAny(ObjectTag... tags) {
    for (ObjectTag objectTag : tags) {
      for (ObjectTag realTags : gameTags) {
        if (objectTag == realTags)
          return true;
      }
    }
    return false;
  }

  /**
   * Adds property to GameObject
   * @param propertyName
   * @param property
   * @return
   */
  public GameObject addProperty(ObjectProperty propertyName, Property property) {
    gameProperty.put(propertyName, property);
    return this;
  }

  /**
   * Gets property behind name
   * @param propertyName
   * @return property if it exists or null.
   */
  public Property getProperty(ObjectProperty propertyName) {
    return gameProperty.get(propertyName);
  }

  private Consumer<GameObject> start;

  /**
   * Sets starting configuration of this GameObject
   * @param start
   * @return
   */
  public GameObject setStart(Consumer<GameObject> start) {
    this.start = start;
    return this;
  }

  /**
   * Restarts GameObject and all properties
   */
  public void start() {
    for (var property : gameProperty.entrySet()) {
      property.getValue().restart();
    }
    if (start != null)
      start.accept(this);
  }

  /**
   * Updates all properties and GameObject
   * @param game
   */
  public void update(Game game) {
    gameProperty.forEach((x, y) -> y.update(game));
  }

  /**
   * Draws GameObject
   * @param graphics
   * @param layer
   */
  public abstract void draw(Graphics2D graphics, int layer);

  /**
   * Finds if GameObject contains point
   * @param p
   * @return
   */
  public abstract boolean contains(Point2D p);

  /**
   * Finds if GameObject intersects shape
   * @param shape
   * @return
   */
  public abstract boolean intersects(Shape shape);

  /**
   * Gets all layers on which GameObject is located
   * @return
   */
  public abstract int[] getLayers();

}
