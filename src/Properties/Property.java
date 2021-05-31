package Properties;

import java.util.function.Consumer;

import Base.Game;

public class Property {
  protected Consumer<Game> update = g -> {
  };

  public void update(Game game) {
    update.accept(game);
  }

  public void restart() {
  }
}
