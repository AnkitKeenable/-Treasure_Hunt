package main;

import entity.Player;
import object.SuperObject;
import title.TileManager;

import javax.swing.JPanel;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable  // extend JPanel and implement runnable to manage game thread

{
    final int originalTileSize=16;
    final int scale=3;
    public final int tileSize=originalTileSize*scale;
    public final int maxScreenCol=16;
    public final int maxScreenRow=12;
    public final int screenWidth=tileSize*maxScreenCol;
    public final int screenHeight=tileSize*maxScreenRow;

    //the screen size in terms of tile dimensions and pixel dimensions.

//    WORLD SETTING

    public final int maxWorldCol=50;
    public final int maxWorldRow=50;
    public final int worldWidth=tileSize*maxWorldCol;
    public final int worldHeight= tileSize*maxWorldRow;

    KeyHandler keyH=new KeyHandler();  // for handling keyboard input

    Thread gameThread;   // main loop run within this thread
    public   CollisionChecker collisionChecker=new CollisionChecker(this);   // check for collision between objects
    public AssetSetter aSetter=new AssetSetter(this);  // it manages the placement of the object in the game
   public Player player=new Player(this,keyH);    // it represents the player character
    TileManager tileM=new TileManager(this);  // manage the drawing and behaviour of tiles in the game

    int FPS=60;

    public SuperObject obj[]=new SuperObject[10];      // arrays holds up to 10 objects


    public  GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth,screenHeight));  // screen dimention
        this.setBackground(Color.BLACK);   // set backgrounf clour black
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);  // handler for keyboard input
        this.setFocusable(true);
    }

    public void setupGame(){
        aSetter.setObject();    // use AssertSetter to place object
    }

    public  void startGameThread(){
        gameThread=new Thread(this);
        gameThread.start();  // crate new thread and calling the run method
    }
    @Override
    public void run() {
        // Implement the game loop to update and repaint the game
        double drawInterval= (double) 1000000000 /FPS;     // Time travel for each frame in nanosecond
        double delta=0;  //for tracking how many updating frame need updating
        long lastTime=System.nanoTime(); // record time of the last frame
        long currentTime;
        long timer=0;
        int drawCount=0;

        while (gameThread !=null){
        currentTime=System.nanoTime();
        delta+=(currentTime-lastTime)/drawInterval;  //Accumulates time since the last frame to determine if an update is needed.
        timer+=(currentTime-lastTime);      //Accumulates total time passed for FPS calculation.
        lastTime=currentTime;

      if(delta>=1){
          update();
          repaint();
          delta--;
          drawCount++;
      }

      if (timer>=1000000000){
          System.out.println("FPS: "+drawCount);
          drawCount=0;
          timer=0;
      }
        }
    }

    public  void update(){          // Updates the state of the game objects.


        player.update();  // update the player movement


    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);   // not know

        Graphics2D g2=(Graphics2D) g;  // cast the graphic to 2d
        //Tile
        tileM.draw(g2);    // Draw the tile of game world

        //Object
        for (int i = 0; i <obj.length ; i++) {
            if(obj[i] !=null){
                obj[i].draw(g2,this);   // draw each objects if exits
            }
        }

        //Player
        player.draw(g2);
        // draw the player on the screen

        g2.dispose();

    }
}


