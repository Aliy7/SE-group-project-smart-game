import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import java.awt.*;
import java.util.ArrayList;

/**
 * The Player class represents the users movable object on the board. It is responsible for
 * implementing the necessary movement algorithm that determines the next appropriate Tile for a Player
 * to stand upon. It also determines the win condition for the Player and collaborates with the Tile and
 * Level classes to do this.
 * @author Fahim Samady (2035827), Shushovit Khanal (2023769), Ben Rees (2015742)
 */
public class Player {
    private static int score; // The players score
    private static Point playerPos = new Point(Level.getPlayerStartPosX(), Level.getPlayerStartPosY()); // Players pos
    private static int totalAmountOfLeversCollected = 0; // Total amount of Levers collected by the player
    private static boolean playerCollectedRedLever; // Whether the player has a red lever
    private static boolean playerCollectedGreenLever; // Whether the player has a green lever
    private static double facingDirection = 0;

    // These two ArrayLists are essential for working out the win condition when player stands on a door:
    private static ArrayList<String> collectedLoot = new ArrayList<String>(); // Keeps track of players loot
    private static ArrayList<String> collectedLevers = new ArrayList<String>(); // Keeps track of collected levers

    /**
     * The Player constructor assigns the Player starting coordinates and initialises the position of the Player.
     * @param startingX X coordinate that the Player starts on.
     * @param startingY Y coordinate that the Player starts on.
     */
    public Player(int startingX, int startingY) {
        playerPos = new Point(startingX, startingY); // Initialise starting coordinates
    }

    /**
     * This method is responsible for the player's movement across the board.
     * The Player movement method is quite complex. It does not follow the standard convention of processing
     * a single input and then incrementing the x and y coordinates accordingly, it needs to take into consideration
     * the colour patterns of the Tile(s) in the direction that the player wants to move. For example, if the user
     * wants to move right, we need to find the closest Tile to the right that shares a common colour with the current
     * Tile that the Player is standing on.
     * This method also needs to take into consideration whether the player is trying to move onto a gate or not and
     * whether they are allowed (do they have the relevant lever).
     * It also checks the adjacent Tiles around the player for a bomb.
     * @param dir A key that has been pressed by the user that potentially represents a direction.
     * @param gc The graphics context that we will need to draw the Player onto once they have moved.
     */
    public static void playerMove(KeyEvent dir, GraphicsContext gc) {
        // Only allow the player to move if they haven't won or lost yet:
        if (!Main.hasPlayerLost() && !Main.hasPlayerWon()) {
            KeyCode key = dir.getCode(); // Translate the input into a key
            Tile currentTile = Level.getTile(getPlayerX(), getPlayerY()); // Get the Tile the player is currently on

            // Get the adjacent Tiles to the player, so we can see whether there is a bomb on one of them:
            Tile aboveTile = Level.getTile(getPlayerX(), getPlayerY()-1);
            Tile belowTile = Level.getTile(getPlayerX(), getPlayerY()+1);
            Tile rightTile = Level.getTile(getPlayerX()+1, getPlayerY());
            Tile leftTile = Level.getTile(getPlayerX()-1, getPlayerY());

            Bomb bomb = null; // Initialise a new bomb, so we can detonate it if there is one on the Tiles.
            // If the right Tile has a bomb, get the bomb, and remove it from the Tile:
            if (rightTile.hasBomb()) {
                bomb = rightTile.getBomb();
                rightTile.setBomb(null);
                // If the left Tile has a bomb, get the bomb, and remove it from the Tile:
            } else if (leftTile.hasBomb()) {
                bomb = leftTile.getBomb();
                leftTile.setBomb(null);
                // If the above Tile has a bomb, get the bomb, and remove it from the Tile:
            } else if (aboveTile.hasBomb()) {
                bomb = aboveTile.getBomb();
                aboveTile.setBomb(null);
                // If the below Tile has a bomb, get the bomb, and remove it from the Tile:
            } else if (belowTile.hasBomb()) {
                bomb = aboveTile.getBomb();
                belowTile.setBomb(null);
            }

            // If we found a bomb above, detonate it!
            if (bomb != null) {
                bomb.detonate();
            }

            /*
                The coordinates below allow us to set a general offset to the players coordinates.
                For example, if the user wants to move right, to do this we need to increment the current
                x coordinate by 1. If they want to move up, we decrease y by 1. These coordinates allow us
                just add the current x and y by these new x and y and move the player accordingly.
             */
            int dx;
            int dy;
            // If the key is either the right arrow key, or D then we need to increase the x coord by 1 to move right:
            if (key == KeyCode.RIGHT || key == KeyCode.D) {
                facingDirection = 0;
                dx = 1;
                dy = 0;
            // If the key is either the left arrow key, or A then we need to decrease the x coord by 1 to move left:
            } else if (key == KeyCode.LEFT || key == KeyCode.A) {
                facingDirection = 180;
                dx = -1;
                dy = 0;
            // If the key is either the down or S, then we need to increase y by 1 to move down:
            } else if (key == KeyCode.DOWN || key == KeyCode.S) {
                facingDirection = 90;
                dx = 0;
                dy = 1;
            // If the key is up arrow key or W, then we need to decrease y by 1 to move up:
            } else if (key == KeyCode.UP || key == KeyCode.W) {
                facingDirection = -90;
                dx = 0;
                dy = -1;
            // If the key was something else, do nothing:
            } else {
                dx = 0;
                dy = 0;
            }
            // Now offset the players current coordinates with the offset coordinates that we found above:
            int tx = getPlayerX() + dx;
            int ty = getPlayerY() + dy;
            // The initial next Tile is the Tile next to the current one, based on the offsets calculated above:
            Tile next = Level.getTile(tx, ty);

            int whileLoopCounter = 0; // A counter to stop the Player from bugging the game
            /*
                Do this loop while we haven't found a Tile with common colour, and we haven't iterated over the width.
                This loop will increment the 'next' Tile coordinates until the 'next' Tile has a common colour.
                Once this loop finds a Tile with a common colour in the direction that the user wants to go, we have
                our next Tile.
             */
            while (!currentTile.hasCommonColour(next) && whileLoopCounter < Level.getWidth()) {
                // Below, we are constantly incrementing the 'new' coordinates by the offset until we get a match:
                tx += dx;
                ty += dy;
                next = Level.getTile(tx, ty); // The new 'next' Tile is the one with our new coordinates from above
                whileLoopCounter++;
            }

            // If the determined next Tile is within the board dimensions:
            if ((tx < Level.getWidth()) & (ty < Level.getHeight()) & (tx >= 0) & (ty >= 0)) {
                next.checkCollision(); // Check to see if there is a collision
                boolean movePlayer = false; // This flag helps us implement lever and gate functionality
                // If the next Tile has a red Gate we need to check if the Player has red Lever:
                if(next.hasRedGate()) {
                    // If the Player has red Lever then they can move onto the next Tile:
                    if(Player.getPlayerCollectedRedLever()) {
                        movePlayer = true;
                    }
                // If the next Tile has a green Gate we need to check if the Player has green lever:
                }else if(next.hasGreenGate()) {
                    // If the Player has green Lever then they can move onto the next Tile:
                    if(Player.getPlayerCollectedGreenLever()) {
                        movePlayer = true;
                    }
                // If the next Tile has no gates, player can move freely:
                }else {
                    movePlayer = true;
                }

                // If 'movePlayer' is true, update players position to next coordinates and draw the board:
                if (movePlayer) {
                    playerPos.setLocation(tx, ty);
                    Level.drawLevel(gc);
                }
            }
        }
    }

    /**
     * Gets the players current Point on the board
     * @return The point of the Player
     */
    public static Point getPlayerPos() {
        return playerPos;

    }

    /**
     * Can be used to change the players x coordinate
     * @param xVal New x coordinate for the player
     */
    public static void setPlayerX(int xVal) {
        playerPos.x = xVal;
    }

    /**
     * Can be used to change the player y coordinate
     * @param yVal New y coordinate for the player
     */
    public static void setPlayerY(int yVal) {
        playerPos.y = yVal;
    }

    /**
     * Gets the players current x coordinate
     * @return Players current x coordinate
     */
    public static int getPlayerX() {
        return playerPos.x;
        //return this.playerX;
    }

    /**
     * Gets the player current y coordinate
     * @return Players current y coordinate
     */
    public static int getPlayerY() {
        return playerPos.y;
        //return this.playerY;
    }

    /**
     * Gets the player current score
     * @return Score of player
     */
    public static int getScore() {
        return score;
    }

    /**
     * Increases the score by a certain number
     * @param n The number to increase score by
     */
    public static void incrementScore(int n) {
        score = score + n;
    }

    /**
     * Change to true when player collides with a red lever:
     */
    public static void setPlayerCollectedRedLever() {
        playerCollectedRedLever = true;
    }

    /**
     * Whether the player has a red lever
     * @return If the player has a red lever
     */
    public static boolean getPlayerCollectedRedLever() {
        return playerCollectedRedLever;
    }

    /**
     * Change to true when player collides with a red lever:
     */
    public static void setPlayerCollectedGreenLever() {
        playerCollectedGreenLever = true;
    }

    /**
     * Whether the player has a green lever
     * @return If the player has a green lever
     */
    public static boolean getPlayerCollectedGreenLever() {
        return playerCollectedGreenLever;
    }

    /**
     * Adds loot item to the players ArrayList when they pick up loot
     * @param loot Name of the loot item that they picked up
     */
    public static void addLoot(String loot) {
        collectedLoot.add(loot);
    }

    /**
     * Gets the amount of loot that the player has picked up
     * @return Size of loot picked up ArrayList
     */
    public static int getAmountOfLoot() {
        return collectedLoot.size();
    }

    /**
     * Adds lever to the players ArrayList when they pick up lever
     * @param lever Lever colour that they picked up
     */
    public static void addLever(String lever) {
        collectedLevers.add(lever);
    }

    /**
     * Gets the amount of levers that the Player has picked up
     * @return Size of lever picked up ArrayList
     */
    public static int getAmountOfLevers() {
        return collectedLevers.size();
    }

    /**
     * Gets the direction player is facing
     * @return direction of player in terms of degrees
     */
    public static double getFacingDirection() {
        return facingDirection;
    }

    /**
     * Resets players coordinates to the starting coordinates
     */
    public static void resetPlayer() {
        playerPos.x = Level.getPlayerStartPosX();
        playerPos.y = Level.getPlayerStartPosY();
    }
}
