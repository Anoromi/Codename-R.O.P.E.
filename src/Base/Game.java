package Base;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.Timer;

import Helpers.Vector2;
import Objects.*;
import Objects.Entities.BulletTurret;
import Objects.Entities.GameBall;
import Objects.Entities.JumpPad;
import Objects.Entities.Spikes;
import Properties.Mesh;
import Properties.ObjectProperty;

public class Game implements Runnable {
  private Thread t;
  private Canvas canvas;

  public final List<GameObject> DRAWABLES;
  public static final List<Consumer<Game>> CALL = new ArrayList<>();
  public final Camera camera;
  public final int STEP = 4;
  public int currentStep = STEP;

  private static final double DELAY = 1000 / 500;
  public int frames = 0;
  public int updates = 0;
  public final int second = 500;
  private boolean running;

  private GameBall ball;

  private int curLayer;
  private Graphics2D curGraphics;

  private Consumer<GameObject> draw = x -> x.draw(curGraphics, curLayer);

  public Game() {
    canvas = new Canvas();
    DRAWABLES = new ArrayList<>();
    camera = new Camera(Vector2.v(0, 0));

    ball = new GameBall(this, "icons\\Ball.png") {
      {
        getTransform().setPosition(500, 500);
      }
    };
    DRAWABLES.add(ball);

    DRAWABLES.add(new GameSprite("icons/QRect.png", 1) {
      {
        getTransform().setPosition(new Vector2(500, 200));
        addTags(ObjectTag.Touchable);
      }

      @Override
      public void update(Game game) {
        super.update(game);
        // transform.rotate(0.001);
      }
    }.addTags(ObjectTag.Touchable));

    DRAWABLES.add(new GameSprite("icons/QRect.png", 1) {
      {
        getTransform().setPosition(200, 200);
        addTags(ObjectTag.Touchable);
      }
    });
    DRAWABLES.add(new GameSprite("icons/Lvldec.png", 1) {
      {
        addTags(ObjectTag.Touchable);
      }
    });
    DRAWABLES.add(new JumpPad() {
      {
        getTransform().setPosition(150,500);
        getTransform().setRotation(Math.toRadians(-90));
      }
    });

    DRAWABLES.add(new JumpPad() {
      {
        getTransform().setPosition(250,700);
        getTransform().setRotation(Math.toRadians(0));
      }
    });

    DRAWABLES.add(new Spikes() {
      {
        getTransform().setPosition(600,700);
        getTransform().setRotation(Math.toRadians(0));
      }
    });

    DRAWABLES.add(new BulletTurret() {
      {
        getTransform().setPosition(600,100);
        getTransform().setRotation(Math.toRadians(0));
      }
    });

    new Timer(1000, e -> {
      System.out.println(frames);
      System.out.println(updates);
      frames = 0;
      updates = 0;
    }).start();

    t = new Thread(this);
  }

  public void start() {
    running = true;
    t.start();
  }

  public void render() {
    frames++;
    BufferStrategy bs = canvas.getBufferStrategy();
    if (bs == null) {
      canvas.createBufferStrategy(3);
      bs = canvas.getBufferStrategy();
    }
    Graphics2D graphics = (Graphics2D) bs.getDrawGraphics();
    graphics.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    curGraphics = graphics;
    graphics.addRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
    graphics.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED));
    graphics.addRenderingHints(
        new RenderingHints(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED));
    camera.updateTarget(this, ball);
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
    for (curLayer = min; curLayer <= max; curLayer = nextSmallest(curLayer, max)) {
      DRAWABLES.parallelStream().forEach(draw);
    }

    bs.show();
    graphics.dispose();
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
  public void run() {
    double timePerTick = 1000000000 / 60;
    double delta = 0;
    long now;
    long lastTime = System.nanoTime();
    while (running) {
      now = System.nanoTime();
      delta += (now - lastTime) / timePerTick;
      lastTime = now;
      // System.out.println(delta);
      if (delta >= 1) {
        for (int i = 0; i < STEP; i++) {
          updateAll();
          processCalls();
        }
        render();
        currentStep = 0;
        currentStep++;
        delta = 0;
      }
    }
  }

  private void processCalls() {
    for (int i = 0; i < CALL.size(); i++) {
      CALL.get(i).accept(this);
    }
    CALL.clear();
  }

  private void updateAll() {
    DRAWABLES.parallelStream().forEach(x -> x.update(this));
    updates++;
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
    return DRAWABLES.parallelStream().anyMatch(collider -> {
      var inter = collider.getProperty(ObjectProperty.Mesh);
      if (inter != mesh && inter != null && ((Mesh) inter).intersects(mesh)) {
        return true;
      }
      return false;
    });
  }

  public List<GameObject> getIntersectedObjects(Mesh mesh) {
    return processIntersectionsFor(DRAWABLES, mesh);
  }

  private List<GameObject> processIntersectionsFor(List<GameObject> elements, Mesh mesh) {
    ArrayList<GameObject> touchedObjects = new ArrayList<>();
    elements.stream().parallel().forEach(collider -> {
      if (collider.hasTags(ObjectTag.Compound)) {
        touchedObjects.addAll(processIntersectionsFor(((Compound) collider).getGameObjects(), mesh));
      }
      if (!collider.hasAny(ObjectTag.Touchable))
        return;
      var inter = collider.getProperty(ObjectProperty.Mesh);
      if (inter != mesh && inter != null && ((Mesh) inter).intersects(mesh)) {
        touchedObjects.add(collider);
      }
    });
    return touchedObjects;
  }

  public Canvas getCanvas() {
    return canvas;
  }
}
