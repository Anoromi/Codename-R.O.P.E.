package Objects;

import static java.lang.System.out;

import java.awt.Toolkit;

/**
 * Defines settings for the game.
 * File: GameSettings.java
 * @author Andrii Zahorulko
 */
public class GameSettings {
  private GameSettings() {
  }

  public static final double SCALING = (double) Toolkit.getDefaultToolkit().getScreenResolution() / 96;
  public static final double BALL_LOSS = 0.01;
  public static final int COLLIDER_EDGES = 30;
  public static final int ROPE_LAYER = 2;
  public static final int BALL_LAYER = 3;
  public static final int ROPE_HEIGHT = 5;
  public static final double HOOK_SPEED = 21;
  public static final double HOOK_MAX_DISTANCE = 1000;
  public static final double APPROACH_SPEED = 0.1;
  public static final double SHIFT_SPEED = 6;
  public static final int FRAME_WIDTH;
  public static final int FRAME_HEIGHT;
  public static final double JUMP_PAD_SPEED = 6;
  public static final double BULLET_SPEED = 6;

  static {
    FRAME_WIDTH = (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() * SCALING);
    FRAME_HEIGHT = (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() * SCALING);
  }

}
