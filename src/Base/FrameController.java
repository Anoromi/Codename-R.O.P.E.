package Base;

import java.awt.*;

import javax.swing.JButton;
import javax.swing.JFrame;

public class FrameController {
  private Frame frame;
  private Game game;

  public FrameController() {
    frame = new Frame();
    initMenu();

    frame.setLayout(new BorderLayout());
    frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
    frame.setResizable(false);
    frame.setState(JFrame.MAXIMIZED_BOTH);
    frame.setUndecorated(true);
    game = new Game(this);
    // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
    startGame(1);
  }

  private void initMenu() {
    // TODO Menu

  }

  public void showGame() {
    frame.removeAll();
    frame.add(game.getCanvas());
    frame.revalidate();
    frame.repaint();
  }

  public void startGame(int level) {
    showGame();
    game.loadLevel(level);
    game.start();
  }

  public void pauseScreen() {
    frame.removeAll();
    Panel p = new Panel(new FlowLayout(FlowLayout.CENTER));
    JButton resume = new JButton("Continue");
    resume.addActionListener(e -> {
      showGame();
      game.start();
    });
    JButton menu = new JButton("Main menu");
    menu.addActionListener(e -> {
    });

    p.add(resume);
    p.add(menu);
    frame.add(p, BorderLayout.CENTER);
    frame.revalidate();
    frame.repaint();
  }
}
