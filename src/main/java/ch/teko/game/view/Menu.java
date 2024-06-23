package ch.teko.game.view;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Represents the game menu.
 */
public class Menu {
    /**
     * The parent JFrame where the menu will be displayed.
     */
    private JFrame frame;

    /**
     * The panel containing the menu components.
     */
    private JPanel menuDialog;

    /**
     * Indicates whether the menu is currently open.
     */
    private boolean isOpen = false;

    /**
     * The button to start or resume the game.
     */
    private JButton startButton;

    /**
     * Constructs a new Menu instance.
     *
     * @param frame The parent JFrame where the menu will be displayed.
     */
    public Menu(JFrame frame) {
        this.frame = frame;
        this.menuDialog = new JPanel();
        menuDialog.setSize(300, 200);

        // Initialize and configure UI components
        startButton = new JButton("Start");
        startButton.addActionListener(e -> closeMenu());
        menuDialog.add(startButton);

        JButton settingsButton = new JButton("Settings");
        menuDialog.add(settingsButton);

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> System.exit(0));
        menuDialog.add(exitButton);

        frame.add(menuDialog);

        closeMenu();
    }

    /**
     * Opens the menu.
     *
     * @param startMenu Whether it's the initial start menu (true) or a resume menu (false).
     */
    public void openMenu(boolean startMenu) {
        this.isOpen = true;
        this.startButton.setText(startMenu ? "Start" : "Resume");
        this.menuDialog.setVisible(true);
        this.menuDialog.requestFocus();
    }

    /**
     * Closes the menu.
     */
    public void closeMenu() {
        this.isOpen = false;
        this.menuDialog.setVisible(false);
        this.frame.requestFocus();
    }

    /**
     * Checks if the menu is currently open.
     *
     * @return True if the menu is open, false otherwise.
     */
    public boolean isOpen() {
        return this.isOpen;
    }

    /**
     * Renders the menu.
     *
     * @param g The Graphics object for rendering.
     * @return True if the menu is open and rendering occurred, false otherwise.
     */
    public boolean onRender(Graphics g) {
        if (this.isOpen) {
            this.menuDialog.revalidate();
            this.menuDialog.repaint();
            return true;
        } else {
            return false;
        }
    }
}
