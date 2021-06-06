package Properties;

import java.util.function.Consumer;

import Base.Game;

/**
 * Defines basis for every property. File: Property.java
 *
 * @author Andrii Zahorulko
 */
public class Property {
  protected Consumer<Game> update = g -> {
  };

  /**
   * Updates all properties
   *
   * @param game
   */
  public void update(Game game) {
    update.accept(game);
  }

  /**
   * Restarts property
   */
  public void restart() {
  }
}
