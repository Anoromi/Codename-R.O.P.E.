package Objects;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameCompound extends GameObject implements Compound {
  protected List<GameObject> gameObjects;

  public GameCompound(GameObject... gameObjects) {
    this.gameObjects = new ArrayList<>(Arrays.asList(gameObjects));
  }

  public GameCompound() {
    this(new GameObject[0]);
  }

  @Override
  public void draw(Graphics2D graphics, int layer) {
    for (GameObject gameObject : gameObjects) {
      gameObject.draw(graphics, layer);
    }
  }

  @Override
  public boolean contains(Point2D p) {
    for (GameObject gameObject : gameObjects) {
      if (gameObject.contains(p))
        return true;
    }
    return false;
  }

  @Override
  public boolean intersects(Shape object) {
    for (GameObject gameObject : gameObjects) {
      if (gameObject.intersects(object))
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
