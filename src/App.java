import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

import Base.Game;
import Objects.GameSettings;

public class App {
  public static void main(String[] args) throws Exception {
    JFrame f = new JFrame();
    f.setLayout(new BorderLayout());

    f.setSize(Toolkit.getDefaultToolkit().getScreenSize());
    f.setResizable(false);
    f.setState(JFrame.MAXIMIZED_BOTH);
    f.setUndecorated(true);
    Game g = new Game();
    JPanel p = new JPanel();
    p.add(g.getCanvas());
    f.add(g.getCanvas());
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.setVisible(true);
    g.start();

  }
}
