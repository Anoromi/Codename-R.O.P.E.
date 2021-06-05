package Helpers;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

import Base.FrameController;
import Base.Game;
import Objects.BlankSprite;
import Objects.GameSprite;
import Objects.ObjectTag;
import Objects.Entities.*;
import Objects.Entities.SmallSpike;

/**
 * Class will create level if we have file *.lvl; file can have line with tags:
 * <p>
 * start{x,y} - coordinate of the start to create there a ball.
 * goal{x,y} - coordinate of the finish
 * background{file.png} - file of the level background(0 layer)
 * staticBack{file.png} - file of the static background(behind background)
 * walls{file.png} - file which have walls(1 layer)
 * smallTrap{x,y,r,width,height} - coordinates of the small spikes(x,y) trap, rotation angle(r) and (optional) size to rescale
 * trap{x,y,r,width,height} - coordinates of the spikes(x,y) trap, rotation angle(r) and (optional) size to rescale
 * movingTrap{x1,y1,x2,y2,speed,width,height} - coordinates of the moving spikes trap (from x1,y1 to x2,y2), speed
 * and (optional) size to rescale
 * field{x,y,speed,width,height} - gravitational field coordinates, speed and (optional) size to rescale
 * jump{x,y,r} - coordinate of the jump pad (x,y) and rotation angle(r);
 * bullet{x,y} - coordinate of the bullet turret enemy
 * File: LevelReader.java
 *
 * @author Danylo Nechyporchuk
 */
public class LevelReader {
    private static ArrayList<File> LEVELS = new ArrayList<>();
    public static Vector2 startingPoint;

    static {
        File levelDirectory = new File("levels");
        Collections.addAll(LEVELS, levelDirectory.listFiles(pathname -> {
            if (pathname.getName().endsWith(".lvl"))
                return true;
            return false;
        }));
    }

    /**
     * Create level with all objects including background, ball, enemies, etc.
     *
     * @param game        object of class Game to fill DRAWABLES array
     * @param levelNumber number of level to create
     */
    public static void createLevel(Game game, FrameController controller,int levelNumber) {
        File file = LEVELS.get(levelNumber - 1);
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));

            while (true) {
                String line = br.readLine();
                if (line == null)
                    break;
                processLevelInfo(controller, game, line);
            }

            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Check line to add specific object to a game
     *
     * @param game object of class Game to fill DRAWABLES array
     * @param line which may contains info about the object
     */
    private static void processLevelInfo(FrameController controller, Game game, String line) {
        if (line.equals("") || line.charAt(0) == ' ' || line.indexOf('{') == -1 || line.indexOf('}') == -1)
            return;
        int endOfNameChar = line.indexOf('{');
        String name = line.substring(0, endOfNameChar);

        String info = line.substring(endOfNameChar + 1, line.indexOf('}'));
        try {
            if (name.equals("start")) {
                int indexOfComma = info.indexOf(',');
                double x = Double.parseDouble(info.substring(0, indexOfComma));
                double y = Double.parseDouble(info.substring(indexOfComma + 1));

                startingPoint = new Vector2(x, y);

                GameBall ball = (GameBall) game.DRAWABLES.get(0);
                ball.setStart(b -> {
                    GameBall rb = (GameBall) b;
                    rb.getTransform().setPosition(x, y);
                });

                return;
            }

            if (name.equals("goal")) {
                int indexOfComma = info.indexOf(',');
                double x = Double.parseDouble(info.substring(0, indexOfComma));
                double y = Double.parseDouble(info.substring(indexOfComma + 1));

                startingPoint = new Vector2(x, y);

                Goal goal = new Goal();
                goal.setStart(gl -> {
                    Goal g = (Goal) gl;
                    g.getTransform().setPosition(x, y);
                });
                game.DRAWABLES.add(goal);
                game.setGoal(goal);
                return;
            }

            if (name.equals("background")) {
                game.DRAWABLES.add(new BlankSprite(info, 0));
            }

            if (name.equals("staticBack")) {
                game.camera.setBackground(ImageHelper.imageOrNull(info));
            }

            if (name.equals("walls")) {
                game.DRAWABLES.add(new GameSprite(info, 1) {
                    {
                        addTags(ObjectTag.Touchable);
                    }
                });
            }

            if (name.equals("dangerWalls")) {
                game.DRAWABLES.add(new GameSprite(info, 1) {
                    {
                        addTags(ObjectTag.Touchable);
                        addTags(ObjectTag.Danger);
                    }
                });
            }

            if (name.equals("smallTrap")) {
                int indexOfComma = 0;
                double x = 0, y = 0, angle = 0;
                int width = 0, height = 0;
                for (int i = 0; i <= 4; i++) {
                    indexOfComma = info.indexOf(',');

                    if (indexOfComma == -1 && i == 2) {
                        indexOfComma = info.lastIndexOf(',');
                        angle = Double.parseDouble(info.substring(indexOfComma + 1));
                        break;
                    }

                    switch (i) {
                        case 0 -> x = Double.parseDouble(info.substring(0, indexOfComma));
                        case 1 -> y = Double.parseDouble(info.substring(0, indexOfComma));
                        case 2 -> angle = Double.parseDouble(info.substring(0, indexOfComma));
                        case 3 -> width = Integer.parseInt(info.substring(0, indexOfComma));
                        case 4 -> height = Integer.parseInt(info.substring(indexOfComma + 1));
                    }
                    info = info.substring(indexOfComma + 1);
                }

                if (width != 0 && height != 0)
                    SmallSpike.SMALL_SPIKE_IMAGE = ImageHelper.rescale(SmallSpike.SMALL_SPIKE_IMAGE, width, height);

                double finalX = x;
                double finalY = y;
                double finalAngle = angle;
                game.DRAWABLES.add(new SmallSpike().setStart(s -> {
                    SmallSpike sp = (SmallSpike) s;
                    sp.setPosition(finalX, finalY).setRotation(Math.toRadians(finalAngle),
                            SmallSpike.SMALL_SPIKE_IMAGE.getWidth() / 2.0,
                            SmallSpike.SMALL_SPIKE_IMAGE.getHeight() / 2.0);
                }));

                return;
            }

            if (name.equals("trap")) {
                int indexOfComma = 0;
                double x = 0, y = 0, angle = 0;
                int width = 0, height = 0;
                for (int i = 0; i <= 4; i++) {
                    indexOfComma = info.indexOf(',');

                    if (indexOfComma == -1 && i == 2) {
                        indexOfComma = info.lastIndexOf(',');
                        angle = Double.parseDouble(info.substring(indexOfComma + 1));
                        break;
                    }

                    switch (i) {
                        case 0 -> x = Double.parseDouble(info.substring(0, indexOfComma));
                        case 1 -> y = Double.parseDouble(info.substring(0, indexOfComma));
                        case 2 -> angle = Double.parseDouble(info.substring(0, indexOfComma));
                        case 3 -> width = Integer.parseInt(info.substring(0, indexOfComma));
                        case 4 -> height = Integer.parseInt(info.substring(indexOfComma + 1));
                    }
                    info = info.substring(indexOfComma + 1);
                }

                if (width != 0 && height != 0)
                    Spikes.SPIKES_IMAGE = ImageHelper.rescale(Spikes.SPIKES_IMAGE, width, height);

                double finalX = x;
                double finalY = y;
                double finalAngle = angle;
                game.DRAWABLES.add(new Spikes().setStart(s -> {
                    Spikes sp = (Spikes) s;
                    sp.setPosition(finalX, finalY).setRotation(Math.toRadians(finalAngle), Spikes.SPIKES_IMAGE.getWidth() / 2.0,
                            Spikes.SPIKES_IMAGE.getHeight() / 2.0);
                }));

                return;
            }

            if (name.equals("movingTrap")) {
                int indexOfComma = 0;
                double x1 = 0, y1 = 0, x2 = 0, y2 = 0, speed = 0;
                int width = 0, height = 0;
                for (int i = 0; i <= 6; i++) {
                    indexOfComma = info.indexOf(',');

                    if (indexOfComma == -1 && i == 4) {
                        indexOfComma = info.lastIndexOf(',');
                        speed = Double.parseDouble(info.substring(indexOfComma + 1));
                        break;
                    }

                    switch (i) {
                        case 0 -> x1 = Double.parseDouble(info.substring(0, indexOfComma));
                        case 1 -> y1 = Double.parseDouble(info.substring(0, indexOfComma));
                        case 2 -> x2 = Double.parseDouble(info.substring(0, indexOfComma));
                        case 3 -> y2 = Double.parseDouble(info.substring(0, indexOfComma));
                        case 4 -> speed = Double.parseDouble(info.substring(0, indexOfComma));
                        case 5 -> width = Integer.parseInt(info.substring(0, indexOfComma));
                        case 6 -> height = Integer.parseInt(info.substring(indexOfComma + 1));
                    }
                    info = info.substring(indexOfComma + 1);
                }

                if (width != 0 && height != 0)
                    MovingSpikes.MOVING_SPIKES_IMAGE = ImageHelper.rescale(MovingSpikes.MOVING_SPIKES_IMAGE, width, height);

                game.DRAWABLES.add(new MovingSpikes(x1, y1, x2, y2, speed).setStart(msp -> {
                    //MovingSpikes ms = (MovingSpikes) msp;
                    //ms.setRotation(Math.toRadians(finalAngle), MovingSpikes.MOVING_SPIKES_IMAGE.getWidth() / 2.0,
                    //        MovingSpikes.MOVING_SPIKES_IMAGE.getHeight() / 2.0);
                }));

                return;
            }

            if (name.equals("field")) {
                int indexOfComma = 0;
                double x = 0, y = 0, speed = 0;
                int width = 0, height = 0;
                for (int i = 0; i <= 4; i++) {
                    indexOfComma = info.indexOf(',');

                    if (indexOfComma == -1 && i == 2) {
                        indexOfComma = info.lastIndexOf(',');
                        speed = Double.parseDouble(info.substring(indexOfComma + 1));
                        break;
                    }

                    switch (i) {
                        case 0 -> x = Double.parseDouble(info.substring(0, indexOfComma));
                        case 1 -> y = Double.parseDouble(info.substring(0, indexOfComma));
                        case 2 -> speed = Double.parseDouble(info.substring(0, indexOfComma));
                        case 3 -> width = Integer.parseInt(info.substring(0, indexOfComma));
                        case 4 -> height = Integer.parseInt(info.substring(indexOfComma + 1));
                    }
                    info = info.substring(indexOfComma + 1);
                }

                if (width != 0 && height != 0)
                    GravityFields.GRAVITY_FIELDS_IMAGE = ImageHelper.rescale(GravityFields.GRAVITY_FIELDS_IMAGE, width, height);

                double finalX = x;
                double finalY = y;
                game.DRAWABLES.add(new GravityFields(speed).setStart(f -> {
                    GravityFields gf = (GravityFields) f;
                    gf.setPosition(finalX, finalY);
                }));

                return;
            }

            if (name.equals("jump")) {
                int indexOfComma = info.indexOf(',');
                double x = Double.parseDouble(info.substring(0, indexOfComma));

                info = info.substring(indexOfComma + 1);
                indexOfComma = info.indexOf(',');
                double y = Double.parseDouble(info.substring(0, indexOfComma));
                double angle = Double.parseDouble(info.substring(indexOfComma + 1));

                game.DRAWABLES.add(new JumpPad().setStart(jp -> {
                    JumpPad j = (JumpPad) jp;
                    j.setPosition(x, y).setRotation(Math.toRadians(angle), JumpPad.JUMP_PAD_IMAGE.getWidth() / 2.0,
                            JumpPad.JUMP_PAD_IMAGE.getHeight() / 2.0);
                }));
                /*
                 * game.DRAWABLES.add(new JumpPad() { { getTransform().setPosition(x, y);
                 * getTransform().getFullAffine().rotate(Math.toRadians(angle),
                 * JUMP_PAD_IMAGE.getWidth() / 2.0, JUMP_PAD_IMAGE.getHeight() / 2.0); } });
                 */

                return;
            }

            if (name.equals("bullet")) {
                int indexOfComma = info.indexOf(',');
                double x = Double.parseDouble(info.substring(0, indexOfComma));
                double y = Double.parseDouble(info.substring(indexOfComma + 1));

                game.DRAWABLES.add(new BulletTurret(controller, game, x, y));
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }
}
