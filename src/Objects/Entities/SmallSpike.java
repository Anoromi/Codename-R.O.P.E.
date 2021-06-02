/*
File: SmallSpike.java
Author: Danylo Nechyporchuk
Task: make a class which describe small spike. Deadly objects which kill the ball if they collide.
 */
package Objects.Entities;

import Helpers.ImageHelper;
import Objects.GameSprite;
import Objects.ObjectTag;

import java.awt.image.BufferedImage;

public class SmallSpike extends GameSprite {
    public static BufferedImage SMALL_SPIKE_IMAGE = ImageHelper.imageOrNull("icons/SmallSpike.png");

    public SmallSpike() {
        super(SMALL_SPIKE_IMAGE, 2);
        addTags(ObjectTag.Danger);
        addTags(ObjectTag.Touchable);
    }

}
