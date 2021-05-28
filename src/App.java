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
    //f.getContentPane().setPreferredSize(new Dimension(GameSettings.FRAME_WIDTH, GameSettings.FRAME_HEIGHT));
    //f.getContentPane().setMinimumSize(new Dimension(GameSettings.FRAME_WIDTH, GameSettings.FRAME_HEIGHT));
    //f.getContentPane().setMaximumSize(new Dimension(GameSettings.FRAME_WIDTH, GameSettings.FRAME_HEIGHT));
    //f.getContentPane().setSize(0, 0);
    f.setSize(Toolkit.getDefaultToolkit().getScreenSize());
    f.setResizable(false);
    f.setState(JFrame.MAXIMIZED_BOTH);
    f.setUndecorated(true);
    //f.setLocationRelativeTo(null);
    Game g = new Game();
    JPanel p = new JPanel();
    p.add(g.getCanvas());
    // f.add(p);
    f.add(g.getCanvas());
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    // f.setResizable(false);
    // f.setPreferredSize(new Dimension(1500, 1500));
    // g.getCanvas().setPreferredSize(new Dimension(1000, 1000));
    //f.pack();
    // f.setSize(new Dimension(1500, 1500));
    f.setVisible(true);
    f.show();;
    g.start();

  }
}
