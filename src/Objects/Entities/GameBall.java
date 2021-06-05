package Objects.Entities;

import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.sound.sampled.*;

import Base.Camera;
import Base.FrameController;
import Base.Game;
import Helpers.Vector2;
import Objects.*;
import Properties.ObjectProperty;
import Properties.PointRigidBody;
import Properties.RigidBody;

/**
 * Responsible for control, collision of the main ball and creation of hooks.
 * File: GameBall.java
 *
 * @author Andrii Zahorulko
 */
public class GameBall extends GameSprite {
  protected RigidBody rigidBody;
  protected HookComponent hook;
  protected HookComponent flyingHook;
  protected Rope rope;
  protected boolean tied;
  protected boolean approach;
  protected boolean shift;

  protected double ropeLength;

  protected Clip approachClip;
  protected Clip bounceClip;
  protected FrameController controller;

  /**
   * Creates an instance of game ball.
   *
   * @param camera
   * @param game
   */
  public GameBall(Camera camera, Game game, FrameController controller) {
    super("icons\\Ball.png", GameSettings.BALL_LAYER);

    this.controller = controller;

    rigidBody = new PointRigidBody(GameSettings.BALL_LOSS, false) {
      @Override
      public AffineTransform getTransform() {
        return GameBall.this.getTransform().getFullAffine();
      }

    };
    addProperty(ObjectProperty.RigidBody, rigidBody);

    addTags(ObjectTag.Touchable);
    addTags(ObjectTag.GameBall);

    game.getCanvas().addMouseListener(new MouseAdapter() {

      @Override
      public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3)
          tied = true;

      }

      @Override
      public void mouseReleased(MouseEvent e) {
        if (game.getFrameController().isInGame()) {
          if (e.getButton() == MouseEvent.BUTTON1) {
            Game.CALL.add(x -> {
              Rectangle2D ballBounds = getMesh().getRelativeShape().getBounds2D();
              Vector2 ballCenter = new Vector2(ballBounds.getCenterX(), ballBounds.getCenterY());
              final Vector2 change = new Vector2(camera.toRealResolution(e.getPoint())).added(camera.getLowerBound())
                  .subtract(ballCenter);
              if (change.magnitude() != 0) {
                Vector2 pos = change.normalized().multipliedBy(image.getWidth() / 2).added(ballCenter);
                Game.CALL.add(game -> {
                  game.DRAWABLES.remove(flyingHook);
                  flyingHook = new HookComponent(GameBall.this, pos, change.normalized());
                  flyingHook.getTransform().translate(0, -HookComponent.getHeight() / 2);
                  game.DRAWABLES.add(flyingHook);
                });
              }
            });
          } else if (e.getButton() == MouseEvent.BUTTON3)
            tied = false;
        }
      }
    });
    game.getCanvas().addKeyListener(new KeyAdapter() {

      @Override
      public void keyPressed(KeyEvent e) {
        if (game.getFrameController().isInGame())
          checkSetValue(e, true);
      }

      @Override
      public void keyReleased(KeyEvent e) {
        if (game.getFrameController().isInGame())
          checkSetValue(e, false);
      }

      private synchronized void checkSetValue(KeyEvent e, boolean value) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE)
          shift = value;
        else if (e.getKeyCode() == KeyEvent.VK_C)
          approach = value;
      }
    });
  }

  /**
   * Updates GameBall, collisions and rope.
   */
  @Override
  public void update(Game game) {
    super.update(game);
    processRope(game);
    processCollisions(game);
  }

  /**
   * Reacts to rope changes.
   *
   * @param game
   */
  private void processRope(Game game) {
    if (hook == null || !hook.isStuck()) {
      stopApproachSound();
    }
    if (hook != null && hook.isStuck()) {
      if (rope == null) {
        rope = new Rope(this, hook);
        Game.CALL.add(x -> x.DRAWABLES.add(rope));
        ropeLength = rope.getDirection().magnitude();
        rope.update(game);
      }
      rope.realUpdate(game);
      if (!rope.isAlive()) {
        stopApproachSound();
        return;
      }
      var curLength = rope.getDirection().magnitude();
      var hookBallBounds = hook.getHookBall().getMesh().getRelativeRectangleBounds().getBounds2D();
      var hookBallCenter = new Vector2(hookBallBounds.getCenterX(), hookBallBounds.getCenterY());
      var ballBounds = getMesh().getRelativeRectangleBounds().getBounds2D();
      var ballCenter = new Vector2(ballBounds.getCenterX(), ballBounds.getCenterY());
      var ropeDir = ballCenter.subtracted(hookBallCenter);
      if (tied && curLength > ropeLength) {
        var speed = getRigidBody().getSpeed();
        if (speed.magnitude() != 0 && ropeDir.magnitude() != 0) {
          var nRopeDir = ropeDir.normalized();
          getTransform().getFullAffine().deltaTransform(speed, speed);
          var cos = speed.normalized().dotProduct(ballCenter.subtracted(hookBallCenter).normalized());
          nRopeDir.multiplyBy(-speed.magnitude() * cos);
          getRigidBody().realImpulse(nRopeDir);
        }
      }
      if (approach && ropeDir.magnitude() != 0) {
        getRigidBody().realImpulse(ropeDir.normalized().invert().multiplyBy(GameSettings.APPROACH_SPEED));
        approachSound();
      } else if (!approach)
        stopApproachSound();
      if (shift && ropeDir.magnitude() != 0) {
        getRigidBody().realImpulse(ropeDir.normalized().invert().multiplyBy(GameSettings.SHIFT_SPEED));
        removeHook();
        shift = false;
        shiftSound();
      }
      ropeLength = curLength;
    }
  }

  /**
   * Starts approach sound.
   */
  private void approachSound() {
    if (approachClip != null && !approachClip.isActive() && controller.isSoundsOn()) {
      approachClip.loop(-1);
      approachClip.start();
    }
  }

  /**
   * Stops approach sound.
   */
  private void stopApproachSound() {
    if (approachClip != null)
      approachClip.stop();
  }

  /**
   * Creates shift sound.
   */
  private void shiftSound() {
    if(controller.isSoundsOn())
        try {
                AudioInputStream audioInputStream;
                audioInputStream = AudioSystem.getAudioInputStream(new File("sounds/Shift.wav").getAbsoluteFile());
                var shiftClip = AudioSystem.getClip();
                shiftClip.open(audioInputStream);
                FloatControl control = (FloatControl) shiftClip.getControl(FloatControl.Type.MASTER_GAIN);
                control.setValue((float) (Math.log(0.3) / Math.log(10.0) * 20.0));
                shiftClip.start();
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
  }

  private void deathSound() {
      if(controller.isSoundsOn())
          try {
                AudioInputStream audioInputStream;
                audioInputStream = AudioSystem.getAudioInputStream(new File("sounds/Death.wav").getAbsoluteFile());
                var deathClip = AudioSystem.getClip();
                deathClip.open(audioInputStream);
                FloatControl control = (FloatControl) deathClip.getControl(FloatControl.Type.MASTER_GAIN);
                control.setValue((float) (Math.log(0.2) / Math.log(10.0) * 20.0));
                deathClip.start();
          } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
             e.printStackTrace();
          }
  }

  @Override
  public void start() {
    super.start();
    removeHook();
    if (bounceClip == null) {
      try {
        AudioInputStream audioInputStream;
        audioInputStream = AudioSystem.getAudioInputStream(new File("sounds/Bounce.wav").getAbsoluteFile());
        bounceClip = AudioSystem.getClip();
        bounceClip.open(audioInputStream);
        FloatControl control = (FloatControl) bounceClip.getControl(FloatControl.Type.MASTER_GAIN);
        control.setValue((float) (Math.log(0.05) / Math.log(10.0) * 20.0));
      } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
        e.printStackTrace();
      }
    } else
      bounceClip.stop();
    if (approachClip == null) {
      try {
        AudioInputStream audioInputStream;
        audioInputStream = AudioSystem.getAudioInputStream(new File("sounds/Approach.wav").getAbsoluteFile());
        approachClip = AudioSystem.getClip();
        approachClip.open(audioInputStream);
        FloatControl control = (FloatControl) approachClip.getControl(FloatControl.Type.MASTER_GAIN);
        control.setValue((float) (Math.log(0.3) / Math.log(10.0) * 20.0));
      } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
        e.printStackTrace();
      }
    } else
      approachClip.stop();
  }

  /**
   * Processes collisions of the ball with objects.
   *
   * @param game
   */
  private void processCollisions(Game game) {
    double ballRadius = image.getWidth() / 2;
    double distanceToRadius = (ballRadius) / Math.cos(3.14 / ballRadius / 2);
    double pointSumX = 0, pointSumY = 0;
    int colliderEdges = GameSettings.COLLIDER_EDGES;
    int counter = 0;
    for (double theta = -90; theta < 270; theta += 360.0 / colliderEdges) {
      Vector2 collision = Vector2.v(ballRadius + distanceToRadius * Math.cos(theta * 3.14 / 180),
          ballRadius + distanceToRadius * Math.sin(theta * 3.14 / 180));
      Vector2 transformed = new Vector2();
      getTransform().getFullAffine().transform(collision, transformed);
      List<GameObject> o = game.getElementsAt(transformed);
      for (GameObject gameObject : o) {
        if (gameObject.hasTags(ObjectTag.Danger)) {
          deathSound();
          game.restartGame();
          return;
        }
        if (gameObject.hasTags(ObjectTag.Goal)) {
          game.nextLevel();
          return;
        }
      }
      o.removeIf(x -> !x.hasTags(ObjectTag.Touchable) || x.hasTags(ObjectTag.GameBall));
      if (!o.isEmpty()) {
        pointSumX += collision.x;
        pointSumY += collision.y;
        counter++;
      }
    }
    if (counter == 0)
      return;
    if (getRigidBody().getSpeed().magnitude() > 0.8 && controller.isSoundsOn()) {
      try {
        AudioInputStream audioInputStream;
        audioInputStream = AudioSystem.getAudioInputStream(new File("sounds/Bounce.wav").getAbsoluteFile());
        bounceClip = AudioSystem.getClip();
        bounceClip.open(audioInputStream);
        FloatControl control = (FloatControl) bounceClip.getControl(FloatControl.Type.MASTER_GAIN);
        control.setValue((float) (Math.log(0.05) / Math.log(10.0) * 20.0));
        bounceClip.start();
      } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
        e.printStackTrace();
      }
    }
    switchDirection(new Vector2(pointSumX / counter, pointSumY / counter), ballRadius);
  }

  private void switchDirection(Vector2 collisionPoint, double ballRadius) {
    Vector2 realPos = new Vector2();
    getTransform().getFullAffine().transform(new Vector2(0, 0), realPos);
    Vector2 nCollVect = new Vector2(collisionPoint.x - ballRadius, collisionPoint.y - ballRadius).normalized();
    Vector2 nMoveDir = rigidBody.getSpeed().normalized();
    double cosine = Math.abs(nCollVect.x * nMoveDir.x + nCollVect.y * nMoveDir.y);
    nCollVect.multiplyBy(cosine * 2);
    Vector2 move = new Vector2(nMoveDir.x - nCollVect.x, nMoveDir.y - nCollVect.y).normalized();
    move.multiplyBy(rigidBody.getSpeed().magnitude());
    rigidBody.setSpeed(move);
  }

  public void removeHook() {
    Game.CALL.add(x -> {
      x.DRAWABLES.remove(hook);
      x.DRAWABLES.remove(rope);
      hook = null;
      rope = null;
    });

  }

  public void newHookStuck() {
    Game.CALL.add(game -> {
      game.DRAWABLES.remove(hook);
      game.DRAWABLES.remove(rope);
      rope = null;
      hook = flyingHook;
      flyingHook = null;
    });
  }

  public RigidBody getRigidBody() {
    return rigidBody;
  }

  public void removeNewHook() {
    Game.CALL.add(x -> {
      x.DRAWABLES.remove(flyingHook);
      flyingHook = null;
    });
  }
}
