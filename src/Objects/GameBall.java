package Objects;

import static java.lang.System.out;

import java.awt.RenderingHints.Key;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.List;

import Base.Game;
import Helpers.Vector2;
import Objects.Hook.HookComponent;
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

  public GameBall(Game game, String path) {
    super(path, GameSettings.BALL_LAYER);

    rigidBody = new PointRigidBody(GameSettings.LOSS) {
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
          game.CALL.add(x -> {
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
    processCollisions(game);
    processRope(game);
  }

  private void processRope(Game game) {
    if (hook != null && hook.isStuck()) {
      if (rope == null) {
        rope = new Rope(this, hook);
        Game.CALL.add(x -> x.DRAWABLES.add(rope));
        ropeLength = rope.getDirection().magnitude();
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
      }
      ropeLength = curLength;
    }
  }

  private void processCollisions(Game game) {
    if (!game.checkForCollision(mesh))
      return;
    double ballRadius = image.getWidth() / 2;
    double distanceToRadius = (ballRadius + 1) / Math.cos(3.14 / ballRadius / 2);
    double pointSumX = 0, pointSumY = 0;
    int colliderEdges = GameSettings.COLLIDER_EDGES;
    int counter = 0;
    boolean coll = false;
    for (double theta = -90; theta < 270; theta += 360.0 / colliderEdges) {
      Vector2 collision = Vector2.v(ballRadius + distanceToRadius * Math.cos(theta * 3.14 / 180),
          ballRadius + distanceToRadius * Math.sin(theta * 3.14 / 180));
      Vector2 transformed = new Vector2();
      getTransform().getFullAffine().transform(collision, transformed);
      List<GameObject> collided = game.getElementsAt(transformed);
      if (collided.stream().parallel().anyMatch(x -> (x != this && x.hasTags(ObjectTag.Touchable)))) {
        pointSumX += collision.x;
        pointSumY += collision.y;
        counter++;
      }
    }
    if (counter == 0)
      return;
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
      hook = null;
    });

  }

  public RigidBody getRigidBody() {
    return rigidBody;
  }
}
