package Base;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import javax.swing.Timer;

import Helpers.LevelReader;
import Helpers.Vector2;
import Objects.Compound;
import Objects.GameObject;
import Objects.ObjectTag;
import Objects.Entities.GameBall;
import Objects.Entities.Goal;
import Objects.Entities.Pointer;
import Properties.AbstractMesh;
import Properties.Mesh;
import Properties.ObjectProperty;

/**
 * Controls game flow, initiation, update and render. File: Game.java
 *
 * @author Andrii Zahorulko
 */
public class Game implements Runnable {
  private Thread t;
  private Canvas canvas;
  private FrameController frameController;

  public final List<GameObject> DRAWABLES;
  public static final List<Consumer<Game>> CALL = new ArrayList<>();
  public final Camera camera;
  public final double STEP = 1.1;
  public double currentStep = STEP;
  private Goal goal;

  public int frames = 0;
  public int updates = 0;
  public final int second = 500;
  private AtomicBoolean running;

  private GameBall ball;

  private int curLayer;
  private Graphics2D curGraphics;

  private Consumer<GameObject> draw = x -> x.draw(curGraphics, curLayer);

  private KeyListener keyReactions;
  private int currentLevel;

  /**
   * Initializes and loads all important components for the game.
   */
  public Game(FrameController frameController) {
    this.frameController = frameController;
    canvas = new Canvas();
    canvas.setBackground(Color.black);
    DRAWABLES = new ArrayList<>();
    camera = new Camera(Vector2.v(0, 0));
    ball = new GameBall(camera, this, frameController);
    keyReactions = new KeyAdapter() {

      @Override
      public void keyReleased(KeyEvent e) {
        if (frameController.isInGame()) {
          if (e.getKeyCode() == KeyEvent.VK_R)
            restartGame();
          else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            stop();
            frameController.pauseScreen();
          }
        }
      }
    };
    canvas.addKeyListener(keyReactions);
    running = new AtomicBoolean(false);

    new Timer(1000, e ->

    {
      System.out.println(frames);
      System.out.println(updates);
      frames = 0;
      updates = 0;
    }).start();

    t = new Thread(this);

  }

  /**
   * Restarts game and loads assets for specific level
   * @param level
   */
  public void loadLevel(int level) {
    currentLevel = level;
    renderBlack();
    DRAWABLES.clear();
    CALL.clear();
    DRAWABLES.add(ball);
    goal = null;
    LevelReader.createLevel(this, frameController, level);
    if (goal != null)
      DRAWABLES.add(new Pointer(goal, ball, camera));
    initObj();
  }

  /**
   * Starts a thread for the game.
   */
  public void start() {
    running.set(true);
    t = new Thread(this);
    t.start();
  }

  public void stop() {
    running.set(false);
  }

  /**
   * Initializes all GameObjects.
   */
  public void initObj() {
    DRAWABLES.removeIf(x -> x.hasTags(ObjectTag.Disposable));
    for (GameObject object : DRAWABLES) {
      object.start();
    }
    processCalls();
  }

  /**
   * Draws all the objects on screen.
   */
  public void render() {
    frames++;

    BufferStrategy bs = null;

    try {
      bs = canvas.getBufferStrategy();
      if (bs == null) {
        canvas.createBufferStrategy(2);
        bs = canvas.getBufferStrategy();
      }
    } catch (IllegalStateException e) {
      e.printStackTrace();
      return;
    }
    Graphics2D graphics = (Graphics2D) bs.getDrawGraphics();
    graphics.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    graphics.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    curGraphics = graphics;
    graphics.addRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF));
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
      DRAWABLES.parallelStream().forEachOrdered(draw);
    }

    bs.show();
    graphics.dispose();
  }

  /**
   * Finds nex smallest layer to work with.
   *
   * @param min
   * @param max
   * @return
   */
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

  /**
   * Runs updates and renders.
   */
  @Override
  public void run() {
    double timePerTick = 1000000000 / 60;
    double delta = 0;
    long now;
    long lastTime = System.nanoTime();
    while (running.get()) {
      now = System.nanoTime();
      delta += (now - lastTime) / timePerTick;
      lastTime = now;
      if (delta >= 1) {
        for (; currentStep < STEP; currentStep++) {
          updateAll();
          processCalls();
        }
        currentStep -= STEP;
        render();
        delta = 0;
      }
    }
  }

  /**
   * Process calls that were made during update.
   */
  private void processCalls() {
    for (int i = 0; i < CALL.size(); i++) {
      CALL.get(i).accept(this);
    }
    CALL.clear();
  }

  /**
   * Updates all GameObjects.
   */
  private void updateAll() {
    DRAWABLES.parallelStream().forEachOrdered(x -> x.update(this));
    updates++;
  }

  /**
   * Gets all GameObjects with mesh on point.
   *
   * @param point point in real coordinates.
   * @return
   */
  public List<GameObject> getElementsAt(Vector2 point) {
    return processElementsAt(DRAWABLES, point);
  }

  /**
   * Gets all GameObjects from the list with mesh on point.
   *
   * @param elements
   * @param point    point in real coordinates.
   * @return
   */
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

  /**
   * Checks for collision of any GameObject with mesh.
   *
   * @param mesh
   * @return
   */
  public boolean checkForCollision(AbstractMesh mesh) {
    return DRAWABLES.parallelStream().anyMatch(collider -> {
      var inter = collider.getProperty(ObjectProperty.Mesh);
      if (inter != mesh && inter != null && ((AbstractMesh) inter).intersects(mesh)) {
        return true;
      }
      return false;
    });
  }

  /**
   * Gets all GameObjects that were intersected.
   *
   * @param mesh
   * @return
   */
  public List<GameObject> getIntersectedObjects(AbstractMesh mesh) {
    return processIntersectionsFor(DRAWABLES, mesh);
  }

  /**
   * Gets all GameObjects from the list that were intersected.
   *
   * @param elements
   * @param mesh
   * @return
   */
  private List<GameObject> processIntersectionsFor(List<GameObject> elements, AbstractMesh mesh) {
    ArrayList<GameObject> touchedObjects = new ArrayList<>();
    elements.stream().parallel().forEachOrdered(collider -> {
      if (collider.hasTags(ObjectTag.Compound)) {
        touchedObjects.addAll(processIntersectionsFor(((Compound) collider).getGameObjects(), mesh));
      }
      var inter = (AbstractMesh) collider.getProperty(ObjectProperty.Mesh);
      if (inter != mesh && inter != null && inter.intersects(mesh)) {
        touchedObjects.add(collider);
      }
    });
    return touchedObjects;
  }

  /**
   * Restarts current level.
   */
  public void restartGame() {
    CALL.add(game -> {
      CALL.clear();
      initObj();
    });
  }

  public void nextLevel() {
    CALL.add(game -> {
      renderBlack();
      game.loadLevel(currentLevel + 1);
    });
  }

  private void renderBlack() {
    BufferStrategy bs = null;

    try {
      bs = canvas.getBufferStrategy();
      if (bs == null) {
        canvas.createBufferStrategy(2);
        bs = canvas.getBufferStrategy();
      }
    } catch (IllegalStateException e) {
      e.printStackTrace();
      return;
    }
    Graphics2D graphics = (Graphics2D) bs.getDrawGraphics();
    graphics.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    bs.show();
    graphics.dispose();
  }

  public Canvas getCanvas() {
    return canvas;
  }

  public GameBall getBall() {
    return ball;
  }

  public Goal getGoal() {
    return goal;
  }

  public void setGoal(Goal goal) {
    this.goal = goal;
  }

  public FrameController getFrameController() {
    return frameController;
  }
}
