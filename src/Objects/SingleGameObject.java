package Objects;
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
