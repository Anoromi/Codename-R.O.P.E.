/*
File: JumpPad.java
Author: Danylo Nechyporchuk
Task: make a class which describe jump pad. A special platform which increase ball speed after collision
 */

package Objects.Entities;

import Base.Game;
import Helpers.ImageHelper;
import Helpers.Vector2;
import Objects.GameSettings;
import Objects.GameSprite;
import Objects.ObjectTag;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class JumpPad extends GameSprite {
    public static final BufferedImage JUMP_PAD_IMAGE =
            ImageHelper.rescale(ImageHelper.imageOrNull("icons/JumpPad.png"), 100, 20);

    public JumpPad() {
        super(JUMP_PAD_IMAGE, 2);
        addTags(ObjectTag.Touchable);
    }

    @Override
    public void update(Game game) {
        super.update(game);
        processCollisions(game);
    }

    /**
     * Check if there were any ball collisions.
     * Add impulse to the ball after collision with a special side of a jump pad
     *
     * @param game object of a class Game to get info about the ball and collisions
     */
    private void processCollisions(Game game) {
        if (!game.checkForCollision(mesh))
            return;

        GameBall ball = (GameBall) game.DRAWABLES.get(0);

        Rectangle jumpSideOfAPad = new Rectangle(0, 0, JUMP_PAD_IMAGE.getWidth(), 1);
        Shape jumpSideBounds = getTransform().getFullAffine().createTransformedShape(jumpSideOfAPad.getBounds2D());
        if (ball.intersects(jumpSideBounds)) {

            Rectangle2D ballBounds = ball.getMesh().getRelativeRectangleBounds().getBounds2D();
            Vector2 ballVector = new Vector2(ballBounds.getCenterX(), ballBounds.getCenterY());
            Rectangle2D jumpPadBounds = getMesh().getRelativeRectangleBounds().getBounds2D();
            Vector2 jumpPadVector = new Vector2(jumpPadBounds.getCenterX(), jumpPadBounds.getCenterY());

            Vector2 change = ballVector.subtract(jumpPadVector);
            ball.getRigidBody().realImpulse(change.normalized().multipliedBy(GameSettings.SHIFT_SPEED * 3));
        }
    }

}
