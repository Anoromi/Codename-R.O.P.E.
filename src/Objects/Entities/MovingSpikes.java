/*
File: MovingSpikes.java
Author: Danylo Nechyporchuk
Task: make a class which describe moving spikes. As deadly as the usual spikes,
but they also move from point1 to point2, then back to point1 and repeat movements from the beginning
 */

package Objects.Entities;

import Base.Game;
import Helpers.ImageHelper;
import Helpers.Vector2;
import Objects.GameSettings;
import Objects.GameSprite;
import Objects.ObjectTag;

import java.awt.image.BufferedImage;

public class MovingSpikes extends GameSprite {
    public static final BufferedImage MOVING_SPIKES_IMAGE = ImageHelper
            .rescale(ImageHelper.imageOrNull("icons/Spikes.png"), 100, 20);

    private double x1, y1, x2, y2;
    private double currentX1, currentY1, currentX2, currentY2;

    private double increaseX;
    private double increaseY;
    private double startingX1;
    private double startingY1;
    private boolean movingDown;

    public MovingSpikes(double x1, double y1, double x2, double y2) {
        super(MOVING_SPIKES_IMAGE, 3);
        addTags(ObjectTag.Danger);
        addTags(ObjectTag.Touchable);
        if (x1 > x2) {
            double tmp = x2;
            x2 = x1;
            x1 = tmp;
        }

        if (y1 > y2) {
            double tmp = y2;
            y2 = y1;
            y1 = tmp;
        }
        this.currentX1 = x1;
        this.currentY1 = y1;
        this.currentX2 = x2;
        this.currentY2 = y2;

        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;

    }

    @Override
    public void start() {
        super.start();

        currentX1 = x1;
        currentY1 = y1;
        currentX2 = x2;
        currentY2 = y2;

        increaseX = currentX2 - currentX1 > currentY2 - currentY1 ?
                GameSettings.SPIKES_SPEED : (currentX2 - currentX1) * GameSettings.SPIKES_SPEED / (currentY2 - currentY1);
        increaseY = increaseX == GameSettings.SPIKES_SPEED ?
                (currentY2 - currentY1) * GameSettings.SPIKES_SPEED / (currentX2 - currentX1) : GameSettings.SPIKES_SPEED;

        startingX1 = currentX1;
        startingY1 = currentY1;

        movingDown = true;
        setPosition(new Vector2(currentX1, currentY1));
    }

    @Override
    public void update(Game game) {
        super.update(game);
        moveSpikes();

    }

    /**
     * Move spikes from x1,y1 to x2,y2, then move them back from x2,y2 to x1,y1 and
     * repeat than movements
     */
    private void moveSpikes() {
        if (movingDown && (currentX1 >= currentX2 || currentY1 >= currentY2) || !movingDown && (currentX1 <= currentX2 || currentY1 <= currentY2)) {
            movingDown = !movingDown;

            increaseX = -increaseX;
            increaseY = -increaseY;

            currentX2 = startingX1;
            startingX1 = currentX1;
            currentY2 = startingY1;
            startingY1 = currentY1;
        }

        translate(increaseX, increaseY);
        currentX1 += increaseX;
        currentY1 += increaseY;
    }
}
