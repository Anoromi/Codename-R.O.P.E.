package Objects;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Base.Game;

/**
 * Defines a GameObject which is also a compound of GameObjects
 * File: GameCompound.java
 * @author Andrii Zahorulko
 */
public class GameCompound extends GameObject implements Compound {
  protected List<GameObject> gameObjects;

  public GameCompound(GameObject... gameObjects) {
    this.gameObjects = new ArrayList<>(Arrays.asList(gameObjects));
  }

  public GameCompound() {
    this(new GameObject[0]);
  }

  /**
   * Updates every object in the compound
   */
  @Override
  public void update(Game game) {
    super.update(game);
    for (GameObject gameObject : gameObjects) {
      gameObject.update(game);
    }
  }

  /**
   * Draws every GameObject in the compound
   */
  @Override
  public void draw(Graphics2D graphics, int layer) {
    for (GameObject gameObject : gameObjects) {
      gameObject.draw(graphics, layer);
    }
  }

  /**
   * Finds if any GameObject in the compound contains point.
   * @param p point in real coordinates
   * @return
   */
  @Override
  public boolean contains(Point2D p) {
    for (GameObject gameObject : gameObjects) {
      if (gameObject.contains(p))
        return true;
    }
    return false;
  }

  /**
   * Finds if any GameObject in the compound intersects object
   */
  @Override
  public boolean intersects(Shape shape) {
    for (GameObject gameObject : gameObjects) {
      if (gameObject.intersects(shape))
        return true;
    }
    return false;
  }

  @Override
  public int[] getLayers() {
    return gameObjects.stream().flatMapToInt(x -> Arrays.stream(x.getLayers())).toArray();
  }

  @Override
  public List<GameObject> getGameObjects() {
    return gameObjects;
  }
}
