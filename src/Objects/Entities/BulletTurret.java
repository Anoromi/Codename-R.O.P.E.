/*
File: BulletTurret.java
Author: Danylo Nechyporchuk
Task: make a class which describe bullet turret. An enemy which shoots high speed bullets, when ball is nearby.
Always look at the ball
 */

package Objects.Entities;

import Base.Game;
import Helpers.ImageHelper;
import Objects.GameSprite;
import Objects.ObjectTag;

import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class BulletTurret extends GameSprite {
    private static final BufferedImage BULLET_TURRET_IMAGE =
            ImageHelper.rescale(ImageHelper.imageOrNull("icons/BulletTurret.png"), 150, 137);

    public BulletTurret() {
        super(BULLET_TURRET_IMAGE, 2);
        addTags(ObjectTag.Danger);
        addTags(ObjectTag.Touchable);
    }

    @Override
    public void update(Game game) {
        super.update(game);
        //rotateTurret(game);
        if (ballInSight())
            openFire();
    }

    /**
     * Rotate the turret to make a barrel look at the ball
     *
     * @param game object of a class Game to get info about the ball
     */
    private void rotateTurret(Game game) {
        GameBall ball = (GameBall) game.DRAWABLES.get(0);
        Rectangle2D ballBounds = ball.getMesh().getRelativeRectangleBounds().getBounds2D();
        Rectangle2D turretBounds = getMesh().getRelativeRectangleBounds().getBounds2D();

        AffineTransform at = transform.getFullAffine();
        double angle = -(Math.atan2(ballBounds.getCenterX() - turretBounds.getCenterX(),
                ballBounds.getCenterY() - turretBounds.getCenterY()));
        //if (angle != 0) {
        at.rotate(angle, turretBounds.getCenterX(), turretBounds.getCenterY());
        // }

    }

    /**
     * Check if there is a ball in sight of turret to open fire
     *
     * @return true if ball is in sight
     */
    private boolean ballInSight() {
        return true;
    }

    /**
     * Shoot some bullets which are deadly for the ball
     */
    private void openFire() {

    }
}

class Bullet extends GameSprite {
    private static final BufferedImage BULLET_IMAGE =
            ImageHelper.rescale(ImageHelper.imageOrNull("icons/Bullet.png"), 200, 187);

    public Bullet() {
        super(BULLET_IMAGE, 2);
        addTags(ObjectTag.Danger);
        addTags(ObjectTag.Touchable);
    }
}
