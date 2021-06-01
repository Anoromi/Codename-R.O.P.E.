package Objects;

import static java.lang.System.out;

import java.awt.Toolkit;

public class GameSettings {
  private GameSettings() {
  }

  public static final double BALL_LOSS = 0.0025;
  public static final int COLLIDER_EDGES = 100;
  public static final int ROPE_LAYER = 2;
  public static final int BALL_LAYER = 3;
  public static final int ROPE_HEIGHT = 5;
  public static final double HOOK_SPEED = 15;
  public static final double HOOK_MAX_DISTANCE = 1000;
  public static final double APPROACH_SPEED = 0.01;
  public static final double SHIFT_SPEED = 3;
  public static final double SPIKES_SPEED = 0.3;
  public static final int FRAME_WIDTH;
  public static final int FRAME_HEIGHT;

  static {
    double scaling = (double)Toolkit.getDefaultToolkit().getScreenResolution() / 96;
    FRAME_WIDTH = (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() * scaling);
    FRAME_HEIGHT = (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() * scaling);
  }

}
