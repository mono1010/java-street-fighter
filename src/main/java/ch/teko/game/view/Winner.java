package ch.teko.game.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * The {@code Winner} class extends {@code JPanel} and represents a simple panel
 * that displays which player won the game and provides a button to proceed to
 * the next game. 
 */
public class Winner extends JPanel {

    // JLabel to display which player won
    private JLabel label;

    // Boolean flag to indicate when to exit the panel
    public boolean exit;

    /**
     * Constructs a {@code Winner} panel with a label and a "Next game" button.
     * The panel is hidden by default and is displayed when a player wins.
     */
    public Winner() {
        this.exit = false;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        label = new JLabel("", SwingConstants.CENTER);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton continueButton = new JButton("Next game");
        continueButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        continueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exit = true; // Set exit to true when the "Next game" button is pressed
            }
        });

        // Add spacing between the text and button
        add(Box.createVerticalGlue()); // Push the content to the center vertically
        add(label);
        add(Box.createRigidArea(new Dimension(0, 20))); // Space between label and button
        add(continueButton);
        add(Box.createVerticalGlue()); // Push the content to the center vertically

        this.setVisible(false); // Hide the panel initially
    }

    /**
     * Displays the panel and sets the label to indicate that player 1 won the game.
     * Waits for the "Next game" button to be pressed before hiding the panel again.
     */
    public void player1Won() {
        this.setVisible(true);
        label.setText("Player 1 Won");
        this.waitForExit();
        this.setVisible(false);
    }

    /**
     * Displays the panel and sets the label to indicate that player 2 won the game.
     * Waits for the "Next game" button to be pressed before hiding the panel again.
     */
    public void player2Won() {
        this.setVisible(true);
        label.setText("Player 2 Won");
        this.waitForExit();
        this.setVisible(false);
    }

    /**
     * Continuously waits until the "Next game" button is pressed by the user.
     * Once the button is pressed, the {@code exit} flag is set to {@code true}.
     */
    void waitForExit() {
        while (!this.exit) {
            try {
                Thread.sleep(100); // Pause for a short time before checking the flag again
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        this.exit = false; // Reset the exit flag after the user exits
    }
}
