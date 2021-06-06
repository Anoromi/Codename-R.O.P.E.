package Objects.Entities;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.image.BufferedImage;

import Base.Game;
import Helpers.ImageHelper;
import Helpers.Vector2;
import Objects.*;
import Properties.Interaction;
import Properties.ObjectProperty;
import Properties.RigidBody;

/**
 * A class which describe jump pad. A special platform which increases ball
 * speed after collision. File: JumpPad.java.
 *
 * @author Danylo Nechyporchuk
 */

public class JumpPad extends GameSprite {
    public static final BufferedImage JUMP_PAD_IMAGE = ImageHelper.imageOrNull("icons/JumpPad.png");
    protected Interaction interaction;

    /**
     * Create jump pad which boost ball speed after collision with it
     */
    public JumpPad() {
        super(JUMP_PAD_IMAGE, 2);
        addTags(ObjectTag.Touchable);
        interaction = new Interaction() {

            @Override
            public void interact(GameObject object) {
                var rigidBody = (RigidBody) object.getProperty(ObjectProperty.RigidBody);
                if (rigidBody != null) {
                    Rectangle jumpSideOfAPad = new Rectangle(4, 0, JUMP_PAD_IMAGE.getWidth() - 8, 1);
                    Shape jumpSideBounds = getTransform().getFullAffine()
                            .createTransformedShape(jumpSideOfAPad.getBounds2D());
                    if (object.intersects(jumpSideBounds)) {
                        var directionAngle = getTransform().getFullRotation() - Math.PI / 2;
                        rigidBody.realImpulse(new Vector2(Math.cos(directionAngle), Math.sin(directionAngle))
                                .multipliedBy(GameSettings.JUMP_PAD_SPEED));
                    }
                }
            }

        };
        addProperty(ObjectProperty.Interaction, interaction);
    }

    /**
     * Process jump pad collisions with the ball
     */
    @Override
    public void update(Game game) {
        super.update(game);
    }

}
