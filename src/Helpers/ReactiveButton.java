package Helpers;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;

/**
 * Defines a button which reacts to mouse being on top of it
 */
public class ReactiveButton extends JButton {

    Color standardColor;
    Color focusedColor;
    Color standardFontColor;
    Color focusedFontColor;
    boolean isSelected;

    public ReactiveButton(String text, Color standardColor, Color focusedColor, Color standardFontColor,
            Color focusedFontColor) {
        super(text);
        this.standardColor = standardColor;
        this.focusedColor = focusedColor;
        setBackground(standardColor);
        setForeground(standardColor);
        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                setForeground(focusedFontColor);
                setBackground(focusedColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setForeground(standardFontColor);
                setBackground(standardColor);
            }
        });
        setBorderPainted(false);
        setFocusPainted(false);
    }

    public ReactiveButton(String text, Color standardFontColor,
            Color focusedFontColor) {
        super(text);
        setForeground(standardColor);
        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                setForeground(focusedFontColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setForeground(standardFontColor);
            }
        });
        setBorderPainted(false);
        setFocusPainted(false);
        setContentAreaFilled(false);
    }
}
