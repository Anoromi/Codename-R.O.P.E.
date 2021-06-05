package Base;

import java.awt.*;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.*;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class FrameController {
  private Frame frame;
  private Game game;
  private boolean inGame;

  public FrameController() {
    frame = new Frame();
    initMenu();

    frame.setLayout(new BorderLayout());
    frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
    frame.setResizable(false);
    frame.setState(JFrame.MAXIMIZED_BOTH);
    frame.setUndecorated(true);
    game = new Game(this);
    game.getCanvas().setFocusable(true);
    frame.setFocusable(false);
    frame.setVisible(true);
    try {
      AudioInputStream audioInputStream;
      audioInputStream = AudioSystem.getAudioInputStream(new File("sounds/Back.wav").getAbsoluteFile());
      Clip backClip = AudioSystem.getClip();
      backClip.open(audioInputStream);
      FloatControl control = (FloatControl) backClip.getControl(FloatControl.Type.MASTER_GAIN);
      control.setValue((float) (Math.log(0.05) / Math.log(10.0) * 20.0));
      backClip.loop(-1);
      backClip.start();
    } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
      e.printStackTrace();
    }
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
    inGame = true;
    game.getCanvas().requestFocus();
  }

  public void startGame(int level) {
    showGame();
    game.loadLevel(level);
    game.start();
  }

  public void pauseScreen() {
    frame.removeAll();
    JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER));
    p.setAlignmentY(JPanel.CENTER_ALIGNMENT);
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
    inGame = false;
  }

  public Frame getFrame() {
    return frame;
  }

  public boolean isInGame() {
    return inGame;
  }

  public void setInGame(boolean inGame) {
    this.inGame = inGame;
  }
}
