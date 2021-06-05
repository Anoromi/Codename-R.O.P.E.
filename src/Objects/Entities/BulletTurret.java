package Objects.Entities;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

import javax.sound.sampled.*;

import Base.FrameController;
import Base.Game;
import Helpers.ImageHelper;
import Helpers.Vector2;
import Objects.GameObject;
import Objects.GameSettings;
import Objects.GameSprite;
import Objects.ObjectTag;
import Properties.*;

/**
 * A class which describe bullet turret. An enemy which shoots high speed bullets, when ball is in sight.
 * Always look towards the ball
 * File: BulletTurret.java
 *
 * @author Danylo Nechyporchuk
 */
public class BulletTurret extends GameSprite {
    public static final BufferedImage BULLET_TURRET_IMAGE = ImageHelper.imageOrNull("icons/BulletTurret.png");

    private GameBall ball;
    private Rectangle2D ballBounds;

    private Rectangle2D turretBounds;
    private Vector2 turretCenter;

    private Bullet bullet;
    private boolean bulletOnScreen = false;
    private boolean tmpBoolean;

    private double prevAngle;
    private FrameController controller;

    /**
     * Create bullet turret, which shoots deadly bullet towards the ball
     *
     * @param x coordinate to set turret position
     * @param y coordinate to set turret position
     */
    public BulletTurret(FrameController controller, Game game, double x, double y) {
        super(BULLET_TURRET_IMAGE, 3);

        this.controller = controller;

        addTags(ObjectTag.Danger);
        addTags(ObjectTag.Touchable);
        setStart(bt -> {
            Game.CALL.add(g -> g.DRAWABLES.remove(bullet));
            setPosition(new Vector2(x, y));
            turretCenter = new Vector2(getMesh().getRelativeRectangleBounds().getBounds2D().getCenterX() - 2.5,
                    getMesh().getRelativeRectangleBounds().getBounds2D().getCenterY() - 3.9);
            prevAngle = 157;
            bulletOnScreen = false;
            rotateTurret(game);
        });
    }

    /**
     * Update turret rotation and shoot the bullet if it is possible
     */
    @Override
    public void update(Game game) {
        super.update(game);
        rotateTurret(game);
        if (ballInSight(game))
            openFire();
    }

    /**
     * Rotate the turret to make a barrel look at the ball
     *
     * @param game object of a class Game to get info about the ball
     */
    private void rotateTurret(Game game) {
        ball = game.getBall();

        ballBounds = ball.getMesh().getRelativeRectangleBounds().getBounds2D();
        turretBounds = getMesh().getRelativeRectangleBounds().getBounds2D();

        AffineTransform at = transform.getFullAffine();
        double angle = -(Math.atan2(turretCenter.getY() - ballBounds.getCenterY(),
                ballBounds.getCenterX() - turretCenter.getX()));
        angle = angle - prevAngle;
        if (angle != 0) {
            at.rotate(angle, (BULLET_TURRET_IMAGE.getWidth() / 2.0) - 2.5, (BULLET_TURRET_IMAGE.getHeight() / 2.0) - 3.9);
            prevAngle = angle + prevAngle;
        }
    }

    /**
     * Check if there is a ball in sight of turret to open fire
     *
     * @param game object of a class Game to get DRAWABLES collection
     * @return true if ball is in sight
     */
    private boolean ballInSight(Game game) {
        tmpBoolean = true;

        double distance = new Vector2(ballBounds.getCenterX(), ballBounds.getCenterY()).subtract(turretCenter)
                .magnitude();

        Rectangle line = new Rectangle((int) turretBounds.getWidth() / 2, (int) turretBounds.getHeight() / 2,
                (int) distance, 1);
        Shape checkLine = getTransform().getFullAffine().createTransformedShape(line.getBounds2D());
        Mesh lineMesh = new Mesh(checkLine) {
            @Override
            protected AffineTransform getTransform() {
                return new AffineTransform();
            }
        };

        game.DRAWABLES.forEach(n -> {
            if (n.getProperty(ObjectProperty.Mesh) != null
                && ((AbstractMesh) n.getProperty(ObjectProperty.Mesh)).intersects(lineMesh)
                && !n.hasTags(ObjectTag.GameBall) && !n.equals(this))
                // if (n.intersects(checkLine) && !n.equals(ball) && !n.equals(this))
                tmpBoolean = false;
        });
        return tmpBoolean;
    }

    /**
     * Shoot some bullets which are deadly for the ball
     */
    private void openFire() {
        if (bulletOnScreen)
            return;

        if (controller.isSoundsOn())
            try {
                AudioInputStream audioInputStream;
                audioInputStream = AudioSystem.getAudioInputStream(new File("sounds/Shoot.wav").getAbsoluteFile());
                var shootClip = AudioSystem.getClip();
                shootClip.open(audioInputStream);
                FloatControl control = (FloatControl) shootClip.getControl(FloatControl.Type.MASTER_GAIN);
                control.setValue((float) (Math.log(0.3) / Math.log(10.0) * 20.0));
                shootClip.start();
            } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
                e.printStackTrace();
            }
        initBullet();

        Game.CALL.add(x -> x.DRAWABLES.add(bullet));

        bulletOnScreen = !bulletOnScreen;
    }

    /**
     * Create a bullet which will move towards the ball
     */
    private void initBullet() {
        Vector2 ballVector = new Vector2(ballBounds.getCenterX(), ballBounds.getCenterY());
        Vector2 bulletStart = new Vector2(turretCenter.getX() - Bullet.BULLET_IMAGE.getWidth() / 2.0,
                turretCenter.getY() - Bullet.BULLET_IMAGE.getHeight() / 2.0);
        bullet = new Bullet(this, bulletStart, ballVector);
    }

    public void setBulletOnScreen(boolean bulletOnScreen) {
        this.bulletOnScreen = bulletOnScreen;
    }

}

/**
 * Object which kill the ball after collision. Move towards the ball
 * File: BulletTurret.java
 *
 * @author Danylo Nechyporchuk
 */
class Bullet extends GameSprite {
    static final BufferedImage BULLET_IMAGE = ImageHelper.rescale(ImageHelper.imageOrNull("icons/Bullet.png"), 15, 15);

    private RigidBody rigidBody;
    private BulletTurret turret;

    /**
     * Create bullet which will move towards the ball
     *
     * @param turret    turret which shoot this bullet
     * @param position  start position for bullet
     * @param direction position of the ball
     */
    public Bullet(BulletTurret turret, Vector2 position, Vector2 direction) {
        super(BULLET_IMAGE, 2);
        this.turret = turret;
        addTags(ObjectTag.Danger);
        setPosition(new Vector2(position.x, position.y));

        double x = Math.cos(turret.getTransform().getFullRotation()) * 88;
        double y = Math.sin(turret.getTransform().getFullRotation()) * 88;

        getTransform().translate(x, y);

        rigidBody = new PointRigidBody(1) {
            @Override
            public AffineTransform getTransform() {
                return transform.getFullAffine();
            }

        };
        rigidBody.impulse(direction.subtract(position).normalized().multipliedBy(GameSettings.BULLET_SPEED));

        addProperty(ObjectProperty.RigidBody, rigidBody);
        addTags(ObjectTag.Disposable);
    }

    /**
     * Check bullet collisions
     */
    @Override
    public void update(Game game) {
        super.update(game);
        if (game.getIntersectedObjects(mesh).stream()
                .anyMatch(x -> !x.hasTags(ObjectTag.GameBall) && !x.equals(turret) && x.hasTags(ObjectTag.Touchable))) {
            processCollisions();
        }
    }

    /**
     * Remove the bullet if it collide any Touchable object
     */
    private void processCollisions() {
        Game.CALL.add(x -> x.DRAWABLES.remove(this));
        turret.setBulletOnScreen(false);
    }
}
