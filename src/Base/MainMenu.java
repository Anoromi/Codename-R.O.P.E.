package Base;

import Helpers.ImageHelper;
import Helpers.ReactiveButton;
import Objects.GameSettings;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Panel which appear after game was opened File: MainMenu.java
 *
 * @author Danylo Nechyporchuk
 */
public class MainMenu extends JPanel {
    public static Font BUTTON_FONT = new Font("Monospaced", Font.BOLD, 40);
    public static Font TITLE_FONT = new Font("SansSerif", Font.BOLD, 60);
    public static Color FOCUSED_COLOR = new Color(51, 102, 204);
    public static Color MUSIC_OFF_FOCUSED_COLOR = new Color(51, 102, 204, 100);

    private JButton newGameButton;
    private JButton musicOnButton;
    private JButton musicOffButton;
    private JButton exitButton;

    private JLabel title;
    private JLabel madeBy;

    private GridBagConstraints gbc = new GridBagConstraints();

    private BufferedImage im = ImageHelper.rescale(ImageHelper.imageOrNull("icons/backgrounds/static/main.jpg"),
            GameSettings.FRAME_WIDTH, GameSettings.FRAME_HEIGHT);

    /**
     * Create start menu JPanel with game name, made by label, buttons new game and
     * quit
     */
    public MainMenu(FrameController frame) {
        super(new GridBagLayout());
        setBackground(Color.black);
        setDoubleBuffered(true);
        labelsInit();
        buttonsInit(frame);
        addListeners(frame);
    }

    /**
     * Draw background of the menu
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(im, 0, 0, null);
    }

    /**
     * Initialize game name label and made be label
     */
    private void labelsInit() {
        gbc.anchor = GridBagConstraints.CENTER;

        title = new JLabel("CODENAME: R.O.P.E.");
        title.setFont(TITLE_FONT);
        title.setForeground(Color.WHITE);
        add(title, gbc);

        madeBy = new JLabel("made by Rocket DADy");
        madeBy.setFont(new Font("Monospaced", Font.BOLD, 30));
        madeBy.setForeground(Color.WHITE);
        gbc.gridy = 5;
        gbc.insets = new Insets(500, 0, 0, 0);
        gbc.anchor = GridBagConstraints.SOUTH;
        add(madeBy, gbc);
    }

    /**
     * Initialize buttons new game and quit
     */
    private void buttonsInit(FrameController frame) {
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;

        newGameButton = new ReactiveButton("Start", Color.WHITE, FOCUSED_COLOR);
        newGameButton.setForeground(Color.WHITE);
        newGameButton.setFont(BUTTON_FONT);
        gbc.gridy = 2;
        gbc.insets = new Insets(200, 0, -200, 0);
        add(newGameButton, gbc);

        musicButtonsInit();

        gbc.gridy = 3;
        add(frame.isSoundsOn() ? musicOnButton : musicOffButton, gbc);

        exitButton = new ReactiveButton("Quit", Color.WHITE, FOCUSED_COLOR);
        exitButton.setForeground(Color.WHITE);
        exitButton.setFont(BUTTON_FONT);
        gbc.gridy = 4;
        add(exitButton, gbc);
    }

    /**
     * Initialize music turned on and music turned off buttons
     */
    private void musicButtonsInit() {
        musicOnButton = new ReactiveButton("Sounds on", Color.WHITE, MainMenu.FOCUSED_COLOR);
        musicOnButton.setForeground(Color.WHITE);
        musicOnButton.setFont(MainMenu.BUTTON_FONT);

        musicOffButton = new ReactiveButton("Sounds off", Color.GRAY, MainMenu.MUSIC_OFF_FOCUSED_COLOR);
        musicOffButton.setForeground(Color.GRAY);
        musicOffButton.setFont(MainMenu.BUTTON_FONT);
    }

    /**
     * Add listeners to buttons new game and quit
     */
    private void addListeners(FrameController frame) {
        newGameButton.addActionListener(e -> frame.startGame(12));

        musicOnButton.addActionListener(e -> {
            frame.stopOrStartSounds();
            remove(musicOnButton);
            gbc.gridy = 3;
            add(musicOffButton, gbc);
            revalidate();
            repaint();
        });

        musicOffButton.addActionListener(e -> {
            frame.stopOrStartSounds();
            remove(musicOffButton);
            gbc.gridy = 3;
            add(musicOnButton, gbc);
            revalidate();
            repaint();
        });

        exitButton.addActionListener(e -> System.exit(0));
    }
}
