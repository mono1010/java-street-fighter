package ch.teko.game.view;

import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ch.teko.game.controllers.*;

/**
 * Represents the game menu.
 */
public class Menu extends JPanel {
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
     * Settings view JPanel
     */
    private SettingsView settingsView;

    /**
     * Constructs a new Menu instance.
     *
     * @param frame The parent JFrame where the menu will be displayed.
     */
    public Menu(JFrame frame) {
        this.frame = frame;
        this.settingsView = new SettingsView(frame);

        this.menuDialog = new JPanel();
        menuDialog.setSize(frame.getWidth(), frame.getHeight());

        // Initialize and configure UI components
        startButton = new JButton("Start");
        startButton.addActionListener(e -> closeMenu());
        menuDialog.add(startButton);

        JButton settingsButton = new JButton("Settings");
        settingsButton.addActionListener(e -> this.showSettings());
        menuDialog.add(settingsButton);

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> System.exit(0));
        menuDialog.add(exitButton);

        this.add(menuDialog);

        this.settingsView.setVisible(false);
        this.add(this.settingsView);

        closeMenu();
    }

    /**
     * Opens the menu.
     *
     * @param startMenu Whether it's the initial start menu (true) or a resume menu
     *                  (false).
     */
    public void openMenu(boolean startMenu) {
        this.isOpen = true;
        this.startButton.setText(startMenu ? "Start" : "Resume");
        this.setVisible(true);
    }

    private void showSettings() {
        this.menuDialog.setVisible(false);
        this.settingsView.open();
        this.wasSettingsOpen = true;
    }

    /**
     * Closes the menu.
     */
    public void closeMenu() {
        this.isOpen = false;
        this.setVisible(false);
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
     * Flag to indicate if the settings tab was opened
     */
    boolean wasSettingsOpen = false;

    /**
     * Renders the menu.
     *
     * @param g The Graphics object for rendering.
     */
    @Override
    protected void paintComponent(Graphics g) {
        if (settingsView.isOpen()) {
            settingsView.repaint();
            return;
        }

        if (this.wasSettingsOpen) {
            this.menuDialog.setVisible(true);
            this.wasSettingsOpen = false;
        }

        this.menuDialog.revalidate();
        this.menuDialog.repaint();
    }
}

/**
 * The SettingsView class represents the settings panel where the user can
 * modify key bindings for
 * two players.
 */
class SettingsView extends JPanel {

    /**
     * Flag indicating whether the settings view is open.
     */
    private boolean isOpen = false;

    /**
     * The parent JFrame where the settings will be displayed.
     */
    private JFrame frame;

    // Buttons to hold the key binds for Player 1
    private JButton leftP1Button;
    private JButton rightP1Button;
    private JButton jumpP1Button;
    private JButton downP1Button;
    private JButton attack1P1Button;
    private JButton attack2P1Button;

    // Buttons to hold the key binds for Player 2
    private JButton leftP2Button;
    private JButton rightP2Button;
    private JButton jumpP2Button;
    private JButton downP2Button;
    private JButton attack1P2Button;
    private JButton attack2P2Button;

    /**
     * Constructs a SettingsView instance, which allows the user to configure key
     * bindings
     * for both players in the given JFrame.
     *
     * @param frame the parent JFrame where this SettingsView will be displayed.
     */
    public SettingsView(JFrame frame) {
        this.frame = frame;

        this.setLayout(new GridBagLayout());
        JPanel keyBindPanel = this;
        GridBagConstraints gbc = new GridBagConstraints();

        // Setting title
        JLabel titleLabel = new JLabel("Settings");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 8; // Spanning all columns
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(5, 0, 10, 0); // Smaller insets
        keyBindPanel.add(titleLabel, gbc);

        // Reset gridwidth after the title row
        gbc.gridwidth = 1;

        // Initialize Buttons
        FighterSettings f1 = Settings.getInstance().getFighter1();
        leftP1Button = createKeyBindButton(f1, Keys.LEFT);
        rightP1Button = createKeyBindButton(f1, Keys.RIGHT);
        jumpP1Button = createKeyBindButton(f1, Keys.UP);
        downP1Button = createKeyBindButton(f1, Keys.DOWN);
        attack1P1Button = createKeyBindButton(f1, Keys.ATTACK1);
        attack2P1Button = createKeyBindButton(f1, Keys.ATTACK2);

        FighterSettings f2 = Settings.getInstance().getFighter2();
        leftP2Button = createKeyBindButton(f2, Keys.LEFT);
        rightP2Button = createKeyBindButton(f2, Keys.RIGHT);
        jumpP2Button = createKeyBindButton(f2, Keys.UP);
        downP2Button = createKeyBindButton(f2, Keys.DOWN);
        attack1P2Button = createKeyBindButton(f2, Keys.ATTACK1);
        attack2P2Button = createKeyBindButton(f2, Keys.ATTACK2);

        // Adjust alignment and add components

        // Row 1 - Jump and Down for Player 1 and Player 2
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(2, 5, 2, 5); // Reduced insets
        keyBindPanel.add(new JLabel("Jump (P1):"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        keyBindPanel.add(jumpP1Button, gbc);

        gbc.gridx = 4;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        keyBindPanel.add(new JLabel("Jump (P2):"), gbc);

        gbc.gridx = 5;
        gbc.anchor = GridBagConstraints.WEST;
        keyBindPanel.add(jumpP2Button, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        keyBindPanel.add(new JLabel("Down (P1):"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        keyBindPanel.add(downP1Button, gbc);

        gbc.gridx = 4;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        keyBindPanel.add(new JLabel("Down (P2):"), gbc);

        gbc.gridx = 5;
        gbc.anchor = GridBagConstraints.WEST;
        keyBindPanel.add(downP2Button, gbc);

        // Row 2 - Left and Right for Player 1 and Player 2
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        keyBindPanel.add(new JLabel("Left (P1):"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        keyBindPanel.add(leftP1Button, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.EAST;
        keyBindPanel.add(new JLabel("Right (P1):"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        keyBindPanel.add(rightP1Button, gbc);

        gbc.gridx = 4;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        keyBindPanel.add(new JLabel("Left (P2):"), gbc);

        gbc.gridx = 5;
        gbc.anchor = GridBagConstraints.WEST;
        keyBindPanel.add(leftP2Button, gbc);

        gbc.gridx = 4;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.EAST;
        keyBindPanel.add(new JLabel("Right (P2):"), gbc);

        gbc.gridx = 5;
        gbc.anchor = GridBagConstraints.WEST;
        keyBindPanel.add(rightP2Button, gbc);

        // Row 3 - Attack 1 and Attack 2 for Player 1 and Player 2
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.EAST;
        keyBindPanel.add(new JLabel("Attack 1 (P1):"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        keyBindPanel.add(attack1P1Button, gbc);

        gbc.gridx = 4;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.EAST;
        keyBindPanel.add(new JLabel("Attack 1 (P2):"), gbc);

        gbc.gridx = 5;
        gbc.anchor = GridBagConstraints.WEST;
        keyBindPanel.add(attack1P2Button, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.EAST;
        keyBindPanel.add(new JLabel("Attack 2 (P1):"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        keyBindPanel.add(attack2P1Button, gbc);

        gbc.gridx = 4;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.EAST;
        keyBindPanel.add(new JLabel("Attack 2 (P2):"), gbc);

        gbc.gridx = 5;
        gbc.anchor = GridBagConstraints.WEST;
        keyBindPanel.add(attack2P2Button, gbc);

        // Row 4 - Back and Save buttons
        gbc.gridwidth = 2;
        gbc.gridx = 1;
        gbc.gridy = 8;
        gbc.insets = new Insets(10, 5, 5, 5);
        gbc.anchor = GridBagConstraints.CENTER;
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> close());
        keyBindPanel.add(backButton, gbc);

        gbc.gridx = 4;
        JButton saveButton = new JButton("Saved automatic");
        saveButton.setEnabled(false);
        keyBindPanel.add(saveButton, gbc);
    }

    /**
     * Creates a key binding button for the given fighter and key.
     * When pressed, the button allows the user to reassign the key for the specific
     * action.
     *
     * @param fighter the FighterSettings object that holds the key bindings for the
     *                player.
     * @param key     the specific action key to be bound.
     * @return the JButton that allows key binding configuration.
     */
    private JButton createKeyBindButton(FighterSettings fighter, Keys key) {
        JButton button = new JButton(KeyEvent.getKeyText(fighter.getKey(key)));
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                button.setText("Press a key...");
                button.requestFocus();
                button.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                            // Revert to the initial key if Escape is pressed
                            button.setText(KeyEvent.getKeyText(fighter.getKey(key)));
                        } else {
                            // Set the new key
                            fighter.setKey(e.getKeyCode(), key);
                            button.setText(KeyEvent.getKeyText(e.getKeyCode()));
                        }
                        // Remove the key listener after setting the key
                        button.removeKeyListener(this);
                    }
                });
            }
        });
        return button;
    }

    /**
     * Returns whether the settings view is currently open.
     *
     * @return true if the settings view is open, false otherwise.
     */
    boolean isOpen() {
        return this.isOpen;
    }

    /**
     * Opens the settings view by making it visible.
     */
    void open() {
        this.isOpen = true;
        this.setVisible(true);
    }

    /**
     * Closes the settings view by hiding it from the display.
     */
    void close() {
        this.isOpen = false;
        this.setVisible(false);
    }

    /**
     * Overrides the paintComponent method. Currently does nothing but can be
     * customized
     * to paint the settings panel.
     *
     * @param g the Graphics object used for rendering the panel.
     */
    @Override
    protected void paintComponent(Graphics g) {
        // Custom rendering logic can be added here
    }
}
