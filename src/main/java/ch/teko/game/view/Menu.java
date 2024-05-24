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

public class Menu {

    JFrame frame;
    JPanel menuDialog;
    boolean isOpen = false;
    JButton startButton;

    public Menu(JFrame frame) {
        this.frame = frame;
        this.menuDialog = new JPanel();
        menuDialog.setSize(300, 200);
        startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                closeMenu();
            }
        });
        menuDialog.add(startButton);

        JButton settingsButton = new JButton("Settings");
        menuDialog.add(settingsButton);

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        menuDialog.add(exitButton);

        frame.add(menuDialog);
    }

    public void openMenu(boolean startMenu) {
        this.isOpen = true;
        this.startButton.setText(startMenu ? "Start" : "Resume");
        this.menuDialog.setVisible(true);
        this.menuDialog.requestFocus();
    }

    public void closeMenu() {
        this.isOpen = false;
        this.menuDialog.setVisible(false);
        this.frame.requestFocus();
    }

    public boolean isOpen() {
        return this.isOpen;
    }

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
