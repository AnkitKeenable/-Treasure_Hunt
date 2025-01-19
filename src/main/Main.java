//package main;
//
//import javax.swing.JFrame;
//
//public class Main {
//    public static void main(String[] args) {
//        JFrame window=new JFrame();
//        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        window.setResizable(false);
//        window.setTitle("2D Adventure Game");
//
//        GamePanel gamePanel=new GamePanel();
//        window.add(gamePanel);
//
//        window.pack();
//        window.add(gamePanel);
//
//        window.setLocationRelativeTo(null);
//        window.setVisible(true);
//    }
//}
//


package main;

import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("2D Adventure Game");

        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);  // Add panel once

        window.pack();  // Pack after adding panel
        window.setLocationRelativeTo(null);  // Center the window
        window.setVisible(true);

        gamePanel.setupGame();
        gamePanel.startGameThread();

    }
}
