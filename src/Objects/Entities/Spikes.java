/*
File: Spikes.java
Author: Danylo Nechyporchuk
Task: make a class which describe spikes. Deadly objects which kill the ball if they collide.
 */

package Objects.Entities;

import Helpers.ImageHelper;
import Objects.GameSprite;
import Objects.ObjectTag;

import java.awt.image.BufferedImage;

/**
 * A class which describe spikes. Deadly objects which kill the ball if they collide.
 * File: Spikes.java
 *
 * @author Danylo Nechyporchuk
 */
public class Spikes extends GameSprite {
    public static BufferedImage SPIKES_IMAGE = ImageHelper.imageOrNull("icons/Spikes.png");

    /**
     * Create spike which can kill the ball
     */
    public Spikes() {
        super(SPIKES_IMAGE, 2);
        addTags(ObjectTag.Danger);
        addTags(ObjectTag.Touchable);
    }
}
