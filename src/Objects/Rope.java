package Objects;

import java.awt.image.BufferedImage;

import Helpers.GeometryHelper;
import Helpers.ImageHelper;

public class Rope extends GameSprite {
  public static final BufferedImage ROPE_IMAGE = ImageHelper.imageOrNull("icons\\Rope.png");

  public Rope(int layer) {
    super("", layer);
  }

  public void connect() {

  }
}
