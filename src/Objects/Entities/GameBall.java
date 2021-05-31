package Objects.Entities;

import static java.lang.System.out;

import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.*;

import Base.Game;
import Helpers.Vector2;
import Objects.*;
import Properties.ObjectProperty;
import Properties.PointRigidBody;
import Properties.RigidBody;

public class GameBall extends GameSprite {
  protected RigidBody rigidBody;
  protected HookComponent hook;
  protected Rope rope;
  protected boolean tied;
  protected boolean approach;
  protected boolean shift;

  protected double ropeLength;

  protected Clip approachClip;
  protected Clip bounceClip;

  public GameBall(Game game, String path) {
    super(path, GameSettings.BALL_LAYER);

    rigidBody = new PointRigidBody(GameSettings.BALL_LOSS, true) {
      @Override
      public AffineTransform getTransform() {
        return GameBall.this.getTransform().getFullAffine();
      }

    };
    addProperty(ObjectProperty.RigidBody, rigidBody);

    addTags(ObjectTag.Touchable);
    addTags(ObjectTag.GameBall);

    game.getCanvas().addMouseListener(new MouseAdapter() {
      Vector2 pressPoint;

      @Override
      public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
          pressPoint = Vector2.v(e.getPoint());
          out.println("Pr " + e.getPoint());
        } else if (e.getButton() == MouseEvent.BUTTON3)
          tied = true;

      }

      @Override
      public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
          Game.CALL.add(x -> {
            out.println("Rel " + e.getPoint());
            final Vector2 change = Vector2.v(e.getPoint()).subtract(pressPoint);
            if (change.magnitude() != 0) {
              // camera.setTarget(change.normalized().multiplyBy(10).add(camera.getTarget()));
              /*
               * ((PointRigidBody) ((GameBall)
               * DRAWABLES.get(0)).getProperty(ObjectProperty.RigidBody))
               * .impulse(change.normalized().multipliedBy(10));
               */
              Rectangle2D center = getMesh().getShape().getBounds2D();
              Vector2 pos = change.normalized().multipliedBy(image.getWidth() / 2)
                  .added(new Vector2(center.getCenterX(), center.getCenterY()));
              getTransform().getFullAffine().transform(pos, pos);
              if (hook != null) {
                Game.CALL.add(game -> {
                  game.DRAWABLES.remove(hook);
                  game.DRAWABLES.remove(rope);
                  rope = null;
                });
              }

              Game.CALL.add(game -> {
                hook = new HookComponent(GameBall.this, pos, change.normalized());
                hook.getTransform().translate(0, -HookComponent.getHeight() / 2);
                game.DRAWABLES.add(hook);
              });
            }
          });
        } else if (e.getButton() == MouseEvent.BUTTON3)
          tied = false;
      }
    });
    game.getCanvas().addKeyListener(new KeyAdapter() {

      @Override
      public void keyPressed(KeyEvent e) {
        checkSetValue(e, true);
      }

      @Override
      public void keyReleased(KeyEvent e) {
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

  @Override
  public void update(Game game) {
    super.update(game);
    processRope(game);
    processCollisions(game);
  }

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

  private void approachSound() {
    if (approachClip != null && !approachClip.isActive()) {
      approachClip.loop(-1);
      approachClip.start();
    }
  }

  private void stopApproachSound() {
    if (approachClip != null)
      approachClip.stop();
  }

  private void shiftSound() {
    if (approachClip == null) {
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
        control.setValue((float) (Math.log(0.05) / Math.log(10.0) * 20.0));
      } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
        e.printStackTrace();
      }
    } else
      approachClip.stop();
  }

  private void processCollisions(Game game) {
    var collided = game.getIntersectedObjects(getMesh());
    if (collided.isEmpty())
      return;
    for (GameObject gameObject : collided) {
      if (gameObject.hasTags(ObjectTag.Danger)) {
        game.restartGame();
        return;
      }
    }
    collided.removeIf(x -> !x.hasTags(ObjectTag.Touchable));
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
      boolean present = false;
      for (GameObject gameObject : collided) {
        if (gameObject.contains(transformed)) {
          present = true;
          break;
        }
      }
      if (present) {
        pointSumX += collision.x;
        pointSumY += collision.y;
        counter++;
      }
    }
    if (counter == 0)
      return;
    if (getRigidBody().getSpeed().magnitude() > 0.01)
      try {
        AudioInputStream audioInputStream;
        audioInputStream = AudioSystem.getAudioInputStream(new File("sounds/Bounce.wav").getAbsoluteFile());
        Clip clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        FloatControl control = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        control.setValue((float) (Math.log(0.05) / Math.log(10.0) * 20.0));
        clip.start();
      } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
        e.printStackTrace();
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
    rigidBody.setAcceleration(move);
  }

  public void removeHook() {
    Game.CALL.add(x -> {
      x.DRAWABLES.remove(hook);
      x.DRAWABLES.remove(rope);
      hook = null;
      rope = null;
    });

  }

  public RigidBody getRigidBody() {
    return rigidBody;
  }
}
