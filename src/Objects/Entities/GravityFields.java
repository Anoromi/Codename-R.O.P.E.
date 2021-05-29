/*
File: GravityFields.java
Author: Danylo Nechyporchuk
Task: make a class which describe gravitational fields. Special areas where ball gravitate to specific object
 */

package Objects.Entities;

import Helpers.ImageHelper;
import Objects.GameSprite;

import java.awt.image.BufferedImage;

public class GravityFields extends GameSprite {
    public static final BufferedImage GRAVITY_FIELDS_IMAGE = null;

    public GravityFields() {
        super(GRAVITY_FIELDS_IMAGE, 2);
    }
}
