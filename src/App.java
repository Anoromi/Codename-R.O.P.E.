import java.awt.BorderLayout;

import javax.swing.JFrame;

import Base.Game;

public class App {
  public static void main(String[] args) throws Exception {
    JFrame f = new JFrame();
    f.setLayout(new BorderLayout());
    Game g = new Game();
    f.add(g.getCanvas());
    f.setVisible(true);
    g.start();
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.setSize(1000, 1000);

  }
}
