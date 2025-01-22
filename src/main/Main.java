
package main;

import javax.swing.JFrame;  // It is used to create a window for GUI

public class Main  // Entry Point of Program

{
    public static void main(String[] args) {
        JFrame window = new JFrame();  // Creating instance of JFrame, which represent the game window
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Application is closed when we close the game window
        window.setResizable(false);// prevent the user to resize the window
        window.setTitle("2D Treasure Hunt Game"); // tile of the game

        GamePanel gamePanel = new GamePanel();  // creating instance of game panel class, where I manage game component and logic
        window.add(gamePanel);  // byy doing this we visualize component where the game is rendered.

        window.pack();  // Pack after adding panel
        window.setLocationRelativeTo(null);  // Center the window
        window.setVisible(true); //window visible to user

        gamePanel.setupGame(); // calls  a method in GamePanel to setup the initial state of the game.
        gamePanel.startGameThread(); // start the game loop

    }
}
