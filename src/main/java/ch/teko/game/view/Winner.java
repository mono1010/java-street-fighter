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

public class Winner extends JPanel {
    private JLabel label;
    public boolean exit;

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
                exit = true;
            }
        });

        // Add spacing between the text and button
        add(Box.createVerticalGlue()); // Push the content to the center vertically
        add(label);
        add(Box.createRigidArea(new Dimension(0, 20))); // Space between label and button
        add(continueButton);
        add(Box.createVerticalGlue()); // Push the content to the center vertically

        this.setVisible(false);
    }

    public void player1Won() {
        this.setVisible(true);
        label.setText("Player 1 Won");
        this.waitForExit();
        this.setVisible(false);
    }

    public void player2Won() {
        this.setVisible(true);
        label.setText("Player 2 Won");
        this.waitForExit();
        this.setVisible(false);
    }

    void waitForExit() {
        while (!this.exit) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        this.exit = false;
    }
}
