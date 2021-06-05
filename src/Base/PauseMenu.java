package Base;

import Helpers.ImageHelper;
import Helpers.ReactiveButton;

import javax.swing.*;
import java.awt.*;

/**
 * Panel which appear after game was paused
 * File: PauseMenu.java
 *
 * @author Danylo Nechyporchuk
 */
public class PauseMenu extends JPanel {

    private JButton resumeButton;
    private JButton mainMenuButton;
    private JButton musicOnButton;
    private JButton musicOffButton;
    private JButton exitButton;

    private JLabel pauseLabel;
    private GridBagConstraints gbc = new GridBagConstraints();

    /**
     * Create pause menu JPanel with "Pause" label, buttons resume and quit
     */
    public PauseMenu(FrameController frame) {
        super(new GridBagLayout());
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
        g.drawImage(ImageHelper.imageOrNull("icons/backgrounds/static/pause.jpg"), 0, 0, null);
    }

    /**
     * Initialize label with text "Pause"
     */
    private void labelsInit() {
        gbc.anchor = GridBagConstraints.CENTER;

        pauseLabel = new JLabel("PAUSE");
        pauseLabel.setFont(MainMenu.TITLE_FONT);
        pauseLabel.setForeground(Color.WHITE);
        add(pauseLabel, gbc);
    }

    /**
     * Initialize buttons resume and quit
     */
    private void buttonsInit(FrameController frame) {
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;

        resumeButton = new ReactiveButton("Resume", Color.WHITE, MainMenu.FOCUSED_COLOR);
        resumeButton.setForeground(Color.WHITE);
        resumeButton.setFont(MainMenu.BUTTON_FONT);
        gbc.gridy = 1;
        add(resumeButton, gbc);

        mainMenuButton = new ReactiveButton("Main menu", Color.WHITE, MainMenu.FOCUSED_COLOR);
        mainMenuButton.setForeground(Color.WHITE);
        mainMenuButton.setFont(MainMenu.BUTTON_FONT);
        gbc.gridy = 2;
        add(mainMenuButton, gbc);

        musicButtonsInit();

        gbc.gridy = 3;
        add(frame.isSoundsOn() ? musicOnButton : musicOffButton, gbc);


        exitButton = new ReactiveButton("Quit", Color.WHITE, MainMenu.FOCUSED_COLOR);
        exitButton.setForeground(Color.WHITE);
        exitButton.setFont(MainMenu.BUTTON_FONT);
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
     * Add listener to buttons resume and quit
     */
    private void addListeners(FrameController frame) {
        resumeButton.addActionListener(e -> frame.resumeGame());

        mainMenuButton.addActionListener(e -> frame.initMainMenu());

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
