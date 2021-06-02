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
    public static final BufferedImage JUMP_PAD_IMAGE = ImageHelper.imageOrNull("icons/JumpPad.png");

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
     * Check if there were any ball collisions. Add impulse to the ball after
     * collision with a special side of a jump pad
     *
     * @param game object of a class Game to get info about the ball and collisions
     */
    private void processCollisions(Game game) {
        if (!game.checkForCollision(mesh))
            return;

        GameBall ball = (GameBall) game.DRAWABLES.get(0);

        Rectangle jumpSideOfAPad = new Rectangle(4, 0, JUMP_PAD_IMAGE.getWidth() - 8, 1);
        Shape jumpSideBounds = getTransform().getFullAffine().createTransformedShape(jumpSideOfAPad.getBounds2D());
        if (ball.intersects(jumpSideBounds)) {
            var directionAngle = getTransform().getFullRotation() - Math.PI / 2;
            ball.getRigidBody().realImpulse(new Vector2(Math.cos(directionAngle), Math.sin(directionAngle))
                    .multipliedBy(GameSettings.JUMP_PAD_SPEED));
        }
    }

}
