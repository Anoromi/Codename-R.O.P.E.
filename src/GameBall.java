import java.awt.Image;

import Helpers.ImageHelper;

public class GameBall extends GameSprite.RigidSprite {

  public GameBall(String path, int layer) {
    new GameSprite(ImageHelper.imageOrNull(path), layer).super(0.2);
    addTags(ObjectTag.Touchable);
  }

  @Override
  public void update(Game game) {
    super.update(game);
    if (game.checkForCollision(this)) {

    }
  }
}
