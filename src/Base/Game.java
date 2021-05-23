package Base;

import static java.lang.System.out;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.Timer;

import Helpers.Vector2;
import Objects.*;
import Objects.Hook.HookComponent;
import Properties.Mesh;
import Properties.ObjectProperty;
import Properties.PointRigidBody;

public class Game extends JPanel implements ActionListener {
  private Timer t;

  private static final double DELAY = 1000 / 60;
  public final List<GameObject> DRAWABLES;
  public final List<Runnable> CALL;
  public final Camera camera;

  public Game() {
    setDoubleBuffered(true);
    t = new Timer((int) DELAY, this);
    DRAWABLES = new ArrayList<>();
    CALL = new ArrayList<>();
    camera = new Camera(Vector2.v(0, 0));
    // camera.setTargetScale(0.9);

    DRAWABLES.add(new GameBall("icons\\Ball.png", 1) {
      {
        getTransform().setPosition(500, 500);
      }
    });

    DRAWABLES.add(new GameSprite("icons/Rect.png", 1) {
      {
        getTransform().setPosition(new Vector2(500, 0));
        addTags(ObjectTag.Touchable);
      }

      public boolean contains(java.awt.geom.Point2D p) {
        return super.contains(p);
      };

      @Override
      public void update(Game game) {
        super.update(game);
        // transform.rotate(0.01);
      }
    }.addTags(ObjectTag.Touchable));

    addMouseListener(new MouseAdapter() {
      Vector2 pressPoint;

      @Override
      public void mousePressed(MouseEvent e) {
        pressPoint = Vector2.v(e.getPoint());
        out.println(DRAWABLES.get(0).contains(pressPoint));
        out.println("Pr " + e.getPoint());

      }

      @Override
      public void mouseReleased(MouseEvent e) {
        CALL.add(() -> {
          out.println("Rel " + e.getPoint());
          Vector2 change = Vector2.v(e.getPoint()).subtract(pressPoint);
          if (change.magnitude() != 0) {
            // camera.setTarget(change.normalized().multiplyBy(10).add(camera.getTarget()));
            /*
             * ((PointRigidBody) ((GameBall)
             * DRAWABLES.get(0)).getProperty(ObjectProperty.RigidBody))
             * .impulse(change.normalized().multipliedBy(10));
             */
            GameBall g = (GameBall) DRAWABLES.get(0);
            Vector2 pos = new Vector2();
            g.getTransform().getFullAffine().transform(pos, pos);
            DRAWABLES.add(new HookComponent(pos, change));

          }
        });
      }
    });
  }

  public void start() {
    t.start();
  }

  @Override
  public void paint(Graphics g) {
    super.paint(g);
    Graphics2D graphics = (Graphics2D) g;
    graphics.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
    camera.adjustCamera(graphics);
    int min = 0;
    int max = 0;
    for (int i = 0; i < DRAWABLES.size(); i++) {
      var current = DRAWABLES.get(i);
      for (int l : current.getLayers()) {
        if (l > max)
          max = l;
        if (l < min)
          min = l;
      }
    }
    for (int layer = min; layer <= max; layer = nextSmallest(layer, max)) {
      for (int j = 0; j < DRAWABLES.size(); j++) {
        var next = DRAWABLES.get(j);
        next.draw(graphics, layer);
      }
    }

  }

  private int nextSmallest(int min, int max) {
    int curMin = max + 1;
    for (int i = 0; i < DRAWABLES.size(); i++) {
      GameObject next = DRAWABLES.get(i);
      for (int l : next.getLayers()) {
        if (l > min && l < curMin) {
          curMin = l;
        }
      }
    }
    return curMin;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    updateAll();
    processCalls();
    repaint();
  }

  private void processCalls() {
    for (Runnable runnable : CALL) {
      runnable.run();
    }
    CALL.clear();
  }

  private void updateAll() {
    for (GameObject gameObject : DRAWABLES) {

      gameObject.update(this);
    }
  }

  public List<GameObject> getElementsAt(Vector2 point) {
    return processElementsAt(DRAWABLES, point);
  }

  private List<GameObject> processElementsAt(List<GameObject> elements, Vector2 point) {
    ArrayList<GameObject> touchedObjects = new ArrayList<>();
    for (GameObject object : elements) {
      if (object.hasTags(ObjectTag.Compound)) {
        touchedObjects.addAll(processElementsAt(((Compound) object).getGameObjects(), point));
      }
      var pr = object.getProperty(ObjectProperty.Mesh);
      if (pr != null && ((Mesh) pr).contains(point))
        touchedObjects.add(object);
    }
    return touchedObjects;
  }

  public boolean checkForCollision(Mesh mesh) {
    for (GameObject collider : DRAWABLES) {
      var inter = collider.getProperty(ObjectProperty.Mesh);
      if (inter != mesh && inter != null && ((Mesh) inter).intersects(mesh)) {
        return true;
      }
    }
    return false;
  }

  public List<GameObject> getIntersected(Mesh mesh) {
    return processIntersectionsFor(DRAWABLES, mesh);
  }

  private List<GameObject> processIntersectionsFor(List<GameObject> elements, Mesh mesh) {
    ArrayList<GameObject> touchedObjects = new ArrayList<>();
    for (GameObject collider : elements) {
      if (collider.hasTags(ObjectTag.Compound)) {
        touchedObjects.addAll(processIntersectionsFor(((Compound) collider).getGameObjects(), mesh));
      }
      var inter = collider.getProperty(ObjectProperty.Mesh);
      if (inter != mesh && inter != null && ((Mesh) inter).intersects(mesh)) {
        touchedObjects.add(collider);
      }
    }
    return touchedObjects;
  }

}
