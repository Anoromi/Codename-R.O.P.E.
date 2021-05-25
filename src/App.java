import java.awt.BorderLayout;

import javax.swing.JFrame;

import Base.Game;

public class App {
    public static void main(String[] args) throws Exception {
        JFrame f = new JFrame();
        f.setLayout(new BorderLayout());
        Game g = new Game();
        g.start();

        f.add(g.getCanvas());
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(1000, 1000);
        f.setVisible(true);

    }
}
