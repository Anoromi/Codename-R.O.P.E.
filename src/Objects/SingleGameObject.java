package Objects;

/**
 * Defines a GameObject which is located only on one layer.
 * File SingleGameObject.java
 * @author Andrii Zahorulko
 */
public abstract class SingleGameObject extends GameObject {
  protected int layer;

  protected SingleGameObject(int layer) {
    super();
    this.layer = layer;
  }

  @Override
  public int[] getLayers() {
    return new int[] { layer };
  }

  public void setLayer(int layer) {
    this.layer = layer;
  }
}
