package Objects;

import static java.lang.System.out;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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

  public GameBall(Game game, String path, int layer) {
    super(path, layer);

    rigidBody = new PointRigidBody(GameSettings.LOSS) {
      @Override
      public AffineTransform getTransform() {
        return GameBall.this.getTransform().getFullAffine();
      }

    };
    addProperty(ObjectProperty.RigidBody, rigidBody);

    addTags(ObjectTag.Touchable);
    addTags(ObjectTag.GameBall);

    game.addMouseListener(new MouseAdapter() {
      Vector2 pressPoint;

      @Override
      public void mousePressed(MouseEvent e) {
        pressPoint = Vector2.v(e.getPoint());
        out.println("Pr " + e.getPoint());

      }

      @Override
      public void mouseReleased(MouseEvent e) {
        game.CALL.add(() -> {
          out.println("Rel " + e.getPoint());
          Vector2 change = Vector2.v(e.getPoint()).subtract(pressPoint);
          if (change.magnitude() != 0) {
            // camera.setTarget(change.normalized().multiplyBy(10).add(camera.getTarget()));
            /*
             * ((PointRigidBody) ((GameBall)
             * DRAWABLES.get(0)).getProperty(ObjectProperty.RigidBody))
             * .impulse(change.normalized().multipliedBy(10));
             */
            change = change.normalized();
            Rectangle2D center = getMesh().getShape().getBounds2D();
            Vector2 pos = change.multipliedBy(image.getWidth()/2)
                .added(new Vector2(center.getCenterX(), center.getCenterY()));
            getTransform().getFullAffine().transform(pos, pos);
            game.DRAWABLES.add(new HookComponent(pos, change));
          }
        });
      }
    });
  }

  @Override
  public void update(Game game) {
    super.update(game);
    processCollisions(game);
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
    Vector2 nMoveDir = rigidBody.getAcceleration().normalized();
    double cosine = Math.abs(nCollVect.x * nMoveDir.x + nCollVect.y * nMoveDir.y);
    nCollVect.multiplyBy(cosine * 2);
    Vector2 move = new Vector2(nMoveDir.x - nCollVect.x, nMoveDir.y - nCollVect.y).normalized();
    move.multiplyBy(rigidBody.getAcceleration().magnitude());
    rigidBody.setAcceleration(move);
  }
}
