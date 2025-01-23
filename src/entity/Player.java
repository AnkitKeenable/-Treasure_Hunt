package entity;

import main.GamePanel;
import main.KeyHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Player extends Entity {

    GamePanel gp;
    KeyHandler keyH;

    // Screen position of the player (centered)
    public final int screenX;
    public final int screenY;

    // tracks the number of keys collected
    int hasKey = 0;

    // Make Flags for jump and fall states
    private boolean isJumping = false;
    private boolean isFalling = false;

    // Jump parameters
    private int jumpHeight = 40;       // Maximum height the player can jump
    private int jumpSpeed = 4;         // Speed of the jump animation
    private int currentJumpHeight = 0; // Tracks the current height during the jump
    private int verticalOffset = 0;    // Temporary vertical offset for the jump animation

    // Constructor to initialize the player
    public Player(GamePanel gp, KeyHandler keyH) {
        this.gp = gp; // Reference to the game panel
        this.keyH = keyH; // Reference to the key handler

        // Calculate the screen position of the player put in center
        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

        // Define the collision area for the player
        solidArea = new Rectangle();
        solidArea.x = 8;  // X-offset of the collision box
        solidArea.y = 16; // Y-offset of the collision box
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 32;  // Width of the collision box
        solidArea.height = 32; // Height of the collision box

        setDefaultValue(); // Set initial player values
        getPlayerImage();  // Load player images
    }

    // Set the player's default position and speed
    public void setDefaultValue() {
        worldX = gp.tileSize * 23; // Initial X position in the game world
        worldY = gp.tileSize * 21; // Initial Y position in the game world
        speed = 4;                 // Player's movement speed
        direction = "down";        // Initial direction the player is facing
    }

    // Load the player's images for different directions
    public void getPlayerImage() {
        try {
            up1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/src/main/resources/player/boy_up_1.png")));
            up2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/src/main/resources/player/boy_up_2.png")));
            down1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/src/main/resources/player/boy_down_1.png")));
            down2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/src/main/resources/player/boy_down_2.png")));
            left1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/src/main/resources/player/boy_left_1.png")));
            left2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/src/main/resources/player/boy_left_2.png")));
            right1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/src/main/resources/player/boy_right_1.png")));
            right2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/src/main/resources/player/boy_right_2.png")));
        } catch (IOException e) {
            e.printStackTrace(); // Print any errors that occur during image loading
        }
    }

    // Update player's state it called every frame
    public void update() {
        // If space is pressed and the player isn't already jumping or falling
        if (!isJumping && !isFalling && keyH.spacePressed) {
            isJumping = true;        // Start the jump
            currentJumpHeight = 0;   // Reset the current jump height
            verticalOffset = 0;      // Reset the vertical offset
        }

        // Perform the jump or handle movement
        if (isJumping || isFalling) {
            performJump(); // Handle jumping and falling
        } else {
            handleMovement(); // Handle player movement
        }

        // Handle sprite animation (switch between two images)
        spriteCounter++;
        if (spriteCounter > 14) {
            spriteNum = (spriteNum == 1) ? 2 : 1; // Alternate sprite image
            spriteCounter = 0; // Reset the counter
        }
    }

    // Handle player movement based on key presses
    private void handleMovement() {
        if (keyH.upPressed || keyH.rightPressed || keyH.leftPressed || keyH.downPressed) {
            // Set the direction based on the key pressed
            if (keyH.upPressed) direction = "up";
            else if (keyH.downPressed) direction = "down";
            else if (keyH.leftPressed) direction = "left";
            else if (keyH.rightPressed) direction = "right";

            collision = false; // Reset collision flag
            gp.collisionChecker.checkTile(this); // Check for tile collisions

            // Check for object collisions and handle pickups
            int objIndex = gp.collisionChecker.checkObject(this, true);
            pickUpObjectIndex(objIndex);

            // Move the player in the specified direction if there's no collision
            if (!collision) {
                switch (direction) {
                    case "up" -> worldY -= speed;
                    case "down" -> worldY += speed;
                    case "left" -> worldX -= speed;
                    case "right" -> worldX += speed;
                }
            }
        }
    }

    // Perform the jump and fall animations
    private void performJump() {
        if (isJumping) {
            // Move up during the jump
            if (currentJumpHeight < jumpHeight) {
                verticalOffset -= jumpSpeed;    // Move the player up
                currentJumpHeight += jumpSpeed; // Increase jump height
            } else {
                isJumping = false; // Stop jumping when the max height is reached
                isFalling = true;  // Start falling
            }
        } else if (isFalling) {
            // Move down during the fall
            if (verticalOffset < 0) {
                verticalOffset += jumpSpeed; // Move the player down
            } else {
                verticalOffset = 0; // Reset the offset when reaching the ground
                isFalling = false;  // Stop falling
            }
        }
    }

    // Handle object pickups (keys, doors, boots, etc.)
    private void pickUpObjectIndex(int objIndex) {
        if (objIndex != 999) { // Check if there's an object at the current position
            String objectName = gp.obj[objIndex].name;

            // Perform actions based on the object type
            switch (objectName) {
                case "Key" -> {
                    hasKey++;                // Increase the key count
                    gp.obj[objIndex] = null; // Remove the key from the game world
                    System.out.println("Key " + hasKey);
                }
                case "Door" -> {
                    if (hasKey > 0) {        // Check if the player has a key
                        gp.obj[objIndex] = null; // Unlock the door
                        hasKey--;            // Decrease the key count
                    }
                    System.out.println("Door " + hasKey);
                }
                case "Boots" -> {
                    speed += 2;             // Increase the player's speed
                    gp.obj[objIndex] = null; // Remove the boots from the game world
                }
            }
        }
    }

    BufferedImage image = null; // Holds the current sprite image

    // Draw the player on the screen
    public void draw(Graphics2D g2) {
        // Select the correct sprite image based on the direction
        switch (direction) {
            case "up" -> image = (spriteNum == 1) ? up1 : up2;
            case "down" -> image = (spriteNum == 1) ? down1 : down2;
            case "left" -> image = (spriteNum == 1) ? left1 : left2;
            case "right" -> image = (spriteNum == 1) ? right1 : right2;
        }

        // Draw the player with the vertical offset applied (for jumps)
        g2.drawImage(image, screenX, screenY + verticalOffset, gp.tileSize, gp.tileSize, null);
    }
}
