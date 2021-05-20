import java.util.function.Consumer;

public class Property {
  protected Consumer<Game> update = g -> {
  };

  public void update(Game game) {
    update.accept(game);
  }
}
