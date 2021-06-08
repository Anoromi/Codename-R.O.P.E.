package Base;

import java.awt.*;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.*;
import javax.swing.JFrame;

/**
 * Responsible for the creation of the frame and control. File:
 * FrameController.java
 *
 * @author Andrii Zahorulko
 * @author Danylo Nechyporchuk
 */
public class FrameController {
    public static boolean soundsOn = true;
    private Frame frame;
    private Game game;
    private boolean inGame;
    private Clip backClip;
    private PauseMenu pauseMenu = new PauseMenu(this);
    private MainMenu mainMenu = new MainMenu(this);

    public FrameController() {
        initFrame();
        initMainMenu();
        frame.setVisible(true);
        frame.repaint();

        try {
            AudioInputStream audioInputStream;
            audioInputStream = AudioSystem.getAudioInputStream(new File("sounds/Back.wav").getAbsoluteFile());
            backClip = AudioSystem.getClip();
            backClip.open(audioInputStream);
            FloatControl control = (FloatControl) backClip.getControl(FloatControl.Type.MASTER_GAIN);
            control.setValue((float) (Math.log(0.05) / Math.log(10.0) * 20.0));
            backClip.loop(-1);
            backClip.start();
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
    }

    private void initFrame() {
        frame = new Frame();
        frame.setBackground(Color.black);
        frame.setLayout(new BorderLayout());
        frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        frame.setResizable(false);
        frame.setState(JFrame.MAXIMIZED_BOTH);
        frame.setUndecorated(true);

    }

    public void initMainMenu() {
        frame.removeAll();
        frame.add(mainMenu, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
        game = new Game(this);
        game.getCanvas().setFocusable(true);
        frame.setFocusable(false);
    }

    public void stopOrStartSounds() {
        if (soundsOn)
            backClip.stop();
        else {
            backClip.loop(-1);
            backClip.start();
        }
        soundsOn = !soundsOn;
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
        frame.add(pauseMenu, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
        inGame = false;
    }

    public void resumeGame() {
        showGame();
        game.start();
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

    public boolean isSoundsOn() {
        return soundsOn;
    }

}
