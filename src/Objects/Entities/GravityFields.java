package Objects.Entities;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import Base.Game;
import Helpers.ImageHelper;
import Helpers.Vector2;
import Objects.GameObject;
import Objects.GameSprite;
import Objects.ObjectTag;
import Properties.*;

/**
 * A class which describe gravitational fields. Special areas where ball
 * gravitate to it center File: GravityFields.java
 *
 * @author Danylo Nechyporchuk
 */
public class GravityFields extends GameSprite {
    public static BufferedImage GRAVITY_FIELDS_IMAGE = ImageHelper.imageOrNull("icons/Field.png");
    private Interaction interaction;

    private double speed;

    /**
     * Create gravitational fields which make the ball move towards the fields
     * center
     *
     * @param speed of the ball movement towards the field center
     */
    public GravityFields(double speed) {
        super(GRAVITY_FIELDS_IMAGE, 2);
        this.speed = speed;
        interaction = new Interaction() {

            @Override
            public void interact(GameObject object) {
                var rigidBody = (RigidBody) object.getProperty(ObjectProperty.RigidBody);
                var mesh = (Mesh) object.getProperty(ObjectProperty.Mesh);
                if (mesh != null && rigidBody != null) {
                    Rectangle2D ballBounds = mesh.getRelativeRectangleBounds().getBounds2D();
                    Vector2 ballVector = new Vector2(ballBounds.getCenterX(), ballBounds.getCenterY());

                    Rectangle2D fieldBounds = getMesh().getRelativeRectangleBounds().getBounds2D();
                    Vector2 fieldVector = new Vector2(fieldBounds.getCenterX(), fieldBounds.getCenterY());
                    rigidBody.realImpulse(fieldVector.subtract(ballVector).normalized().multipliedBy(speed));
                }
            }

        };
        addProperty(ObjectProperty.Interaction, interaction);
    }

    /**
     * Check collisions with gravity field
     */
    @Override
    public void update(Game game) {
        super.update(game);
        checkBallInField(game);
    }

    /**
     * Check if the ball collides the gravity field. If them collide, the ball start
     * moving towards the center of the field
     *
     * @param game object of class Game to check ball posit
     */
    private void checkBallInField(Game game) {
        game.DRAWABLES.forEach(n -> {
            if (n.getProperty(ObjectProperty.Mesh) != null
                    && ((Mesh) n.getProperty(ObjectProperty.Mesh)).intersects(getMesh())
                    && n.hasTags(ObjectTag.GameBall)) {
                GameBall ball = (GameBall) n;
            }
        });
    }

}
