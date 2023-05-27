import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import javax.print.CancelablePrintJob;

/**
 * The level class represents an instance of a level. All the data that makes up
 * this class is read from an inputted file. It created the game board and adds
 * each appropriate element to this game board. It is also responsible for moving characters
 * and redrawing the board.
 * @author Fahim Samady (2035827), Shushovit Khanal (2023769), Ben Rees (2015742)
 */
public class Level {

    private static Tile[][] tiles; // 2D array of Tiles that represents the board
    private static int height; // Height of the board
    private static int width; // Width of the board
    private static int timeToCompleteLevel; // The time the player has to complete the level
    private static int playerStartPosX; // The players starting x position
    private static int playerStartPosY; // The players starting y position
    private static int floorFollowingThiefXPos; // FFT x position
    private static int floorFollowingThiefYPos; // FFT y position

    private static final int TILE_SIZE = 50; // Size of each Tile

    private static LootType[] totalAmountOfLoot; // Amount of loot on the level
    private static int totalAmountOfLevers = 0; // Amount of levers on the level

    private static Timeline characterMovement; // Timeline to move characters every second
    private static Timeline checkBombsForDetonation; // Timeline to detonate bombs every second

    private static final Image playerImage = new Image("./resources/player.png"); // Players image

    private static ArrayList<Character> characters = new ArrayList<Character>(); // Characters that are on the board
    private static ArrayList<Bomb> bombs = new ArrayList<Bomb>(); // Bombs that are on the board

    private static ArrayList<LootType> loot = new ArrayList<>(); // Loot that is on the board
    private static ArrayList<String> levers = new ArrayList<String>(); // Levers that are on the board

    private static GraphicsContext gc; // The Graphics context to draw the board
//    private ArrayList<Item> items = new ArrayList<>();

    public Level(int levelNumber) {
        ArrayList<String> levelData = new ArrayList<>(); // An ArrayList to store all the level data read from the file
        try {
            // Try to create a new file instance based off the file name gotten from the constructor parameters:
            File levelFile = new File("./src/resources/testLevel"+ levelNumber+".txt");
            Scanner readLine = new Scanner(levelFile);
            // Populate the levelData ArrayList with everything that is in the file:
            while (readLine.hasNextLine()) {
                levelData.add(readLine.nextLine());
            }
            readLine.close();
        } catch (FileNotFoundException exception) {
            // If the file cannot be found in ./src/resources display following message and exit program:
            System.out.println("Can't locate specified file.");
            System.exit(0);
        }

        /*
            Width and height of game board will always be on first line of file, meaning we can get the first index
            of the level data, split it into a new array using a space as the regex and then access the relevant index
            to get the width and height.
         */
        height = Integer.parseInt(levelData.get(0).split(" ")[1]);
        width = Integer.parseInt(levelData.get(0).split(" ")[0]);

        timeToCompleteLevel = Integer.parseInt(levelData.get(1)); // Time to complete level will always be at index 1
        tiles = new Tile[height][width]; // Initialise new array with board dimensions

        // Loop over array and populate 2D array with Tiles and parse relevant colours gotten from the level file:
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                tiles[y][x] = new Tile(levelData.get(2 + y).split(" ")[x], x, y);
            }
        }

        /*
            The code below gets the players starting X coordinate and starting Y coordinate.
            The players starting coordinates will always be located at index 'height + 2' in the levelData array.
            The code below gets the String located at index height + 2 in the levelData array and splits it using a
            comma as the regex and converts the relevant indexes to ints. Meaning 1,2 would return an array [1, 2],
            with index 0 being the X value, and index 1 being the Y value.
         */
        playerStartPosX = Integer.parseInt(levelData.get(height + 2).split(",")[0]);
        playerStartPosY = Integer.parseInt(levelData.get(height + 2).split(",")[1]);

        /*
            The code below deals with the NPC positions on the board.
            The NPC positions will always be at index 'height + 3' in the levelData ArrayList, so we split this string
            into an array of String where each element represents an NPCs position on the board.
            We then analyse the last 2 letters of each String, and determine what type of NPC needs to be placed where.
         */
        String[] characterPositionsArray = levelData.get(height + 3).split(" ");
        // Loop over each position in the character positions array:
        for (String characterPosition : characterPositionsArray) {
            // The last 2 letters of the position should ALWAYS tell us what the character is:
            String lastTwoLettersOfPosition = characterPosition.substring(characterPosition.length()-2);
            // Determine what character needs to be added to the board based on the above substring:
            switch (lastTwoLettersOfPosition) {
                // if the substring is ST, it is a SmartThief:
                case "ST" -> {
                    // Get the SmartThief's descriptive data (Coordinates), using comma as regex to split into array:
                    String[] smartThiefData = characterPosition.split(",");
                    // The x and y coordinates will ALWAYS be the first and second elements of the data array:
                    int xVal = Integer.parseInt(smartThiefData[0]);
                    int yVal = Integer.parseInt(smartThiefData[1]);
                    // Create a new instance of the SmartThief that will be added to its relevant Tile:
                    SmartThief smartThief = new SmartThief(xVal, yVal);
                    // Add the above SmartThief to the Tile that it is meant to be on, it will now be drawn:
                    tiles[yVal][xVal].setSmartThief(smartThief);
                    characters.add(smartThief); // Add it to the characters AL, it will now be moved
                }
                // if the substring is FT, it is a FloorFollowingThief:
                case "FT" -> {
                    // Get the FFT's descriptive data, using comma as regex to split into array:
                    String[] floorFollowingThiefData = characterPosition.split(",");
                    // The x and y coordinates will ALWAYS be the first and second elements of the data array:
                    int xVal = Integer.parseInt(floorFollowingThiefData[0]);
                    int yVal = Integer.parseInt(floorFollowingThiefData[1]);
                    // Get the colour that the FFT should follow, will always be 3rd in array:
                    Color colourToFollow = charToColour(floorFollowingThiefData[2].charAt(0));
                    // Get the direction that the FFT should go, will always be 4th in array:
                    String direction = charToDirection(floorFollowingThiefData[3].charAt(0));
                    // Create a new FFT instance to be placed on the appropriate Tile:
                    FloorFollowingThief floorFollowingThief =
                            new FloorFollowingThief(xVal, yVal, colourToFollow,direction);
                    characters.add(floorFollowingThief); // Add the FFT to the characters AL, so it can be moved
                    tiles[yVal][xVal].setFloorFollowingThief(floorFollowingThief); // set it to correct Tile
                }
                // if the substring is FA, it is a FlyingAssassin:
                case "FA" -> {
                    // Get the FA's descriptive data, using comma as regex to split into array:
                    String[] flyingAssassinData = characterPosition.split(",");
                    // The x and y coordinates will ALWAYS be the first and second elements of the data array:
                    int xVal = Integer.parseInt(flyingAssassinData[0]);
                    int yVal = Integer.parseInt(flyingAssassinData[1]);
                    // Get the direction that the FFT should go, will always be 3rd in array:
                    String direction = charToDirection(flyingAssassinData[2].charAt(0));
                    // Create a new FA instance to be placed on the appropriate Tile:
                    FlyingAssassin flyingAssassin = new FlyingAssassin(xVal, yVal, direction);
                    characters.add(flyingAssassin); // Add the FA to the characters AL, so it can be moved
                    tiles[yVal][xVal].setFlyingAssassin(flyingAssassin); // set it to correct Tile
                }
            }
        }

        /*
            The below code deals with items positions on the board
            The item positions will always be at index 'height + 4' in the levelData ArrayList, so we split this string
            into an array of String where each element represents an items position on the board.
            We then analyse the last 2 letters of each String, and determine what type of item needs to be placed where.
         */
        String[] itemPositionsArray = levelData.get(height + 4).split(" ");
        // Loop over every item position in the ArrayList
        for (String itemPosition : itemPositionsArray) {
            // The last to letters of the position should ALWAYS tell us the type of item:
            String lastTwoLettersOfPosition = itemPosition.substring(itemPosition.length()-2);
            // Determine what item needs to be added to the board based on the above substring:
            switch (lastTwoLettersOfPosition) {
                // if the substring is LD, it is a dollar:
                case "LD" -> {
                    // Get the dollars descriptive data, using comma as regex to split into array:
                    String[] dollarData = itemPosition.split(",");
                    // The x and y coordinates will ALWAYS be the first and second elements of the data array:
                    int xVal = Integer.parseInt(dollarData[0]);
                    int yVal = Integer.parseInt(dollarData[1]);
                    loot.add(LootType.DOLLAR); // Add dollar to the loot array
                    tiles[yVal][xVal].setDollar(); // set to appropriate tile
                }
                // if the substring is LC, it is a cent:
                case "LC" -> {
                    // Get the cents descriptive data, using comma as regex to split into array:
                    String[] centData = itemPosition.split(",");
                    // The x and y coordinates will ALWAYS be the first and second elements of the data array:
                    int xVal = Integer.parseInt(centData[0]);
                    int yVal = Integer.parseInt(centData[1]);
                    loot.add(LootType.CENT); // Add cent to the loot array
                    tiles[yVal][xVal].setCent(); // set to appropriate tile
                }
                case "LR" -> {
                    // Get the rubies descriptive data, using comma as regex to split into array:
                    String[] rubyData = itemPosition.split(",");
                    // The x and y coordinates will ALWAYS be the first and second elements of the data array:
                    int xVal = Integer.parseInt(rubyData[0]);
                    int yVal = Integer.parseInt(rubyData[1]);
                    loot.add(LootType.RUBIE); // Add ruby to the loot array
                    tiles[yVal][xVal].setRuby(); // set to appropriate tile
                }
                case "DI" -> {
                    // Get the diamonds descriptive data, using comma as regex to split into array:
                    String[] diamondData = itemPosition.split(",");
                    // The x and y coordinates will ALWAYS be the first and second elements of the data array:
                    int xVal = Integer.parseInt(diamondData[0]);
                    int yVal = Integer.parseInt(diamondData[1]);
                    loot.add(LootType.DIAMOND); // Add diamond to the loot array
                    tiles[yVal][xVal].setDiamond(); // set to appropriate tile
                }
                case ",C" -> {
                    // Get the clocks descriptive data, using comma as regex to split into array:
                    String[] clockData = itemPosition.split(",");
                    // The x and y coordinates will ALWAYS be the first and second elements of the data array:
                    int xVal = Integer.parseInt(clockData[0]);
                    int yVal = Integer.parseInt(clockData[1]);
                    tiles[yVal][xVal].setClock(); // set to appropriate tile
                }
                case "GG" -> {
                    // Get the gates descriptive data, using comma as regex to split into array:
                    String[] gateData = itemPosition.split(",");
                    // The x and y coordinates will ALWAYS be the first and second elements of the data array:
                    int xVal = Integer.parseInt(gateData[0]);
                    int yVal = Integer.parseInt(gateData[1]);
                    tiles[yVal][xVal].setGreenGate(); // set to appropriate tile
                }
                case "RG" -> {
                    // Get the gates descriptive data, using comma as regex to split into array:
                    String[] gateData = itemPosition.split(",");
                    // The x and y coordinates will ALWAYS be the first and second elements of the data array:
                    int xVal = Integer.parseInt(gateData[0]);
                    int yVal = Integer.parseInt(gateData[1]);
                    tiles[yVal][xVal].setRedGate(); // set to appropriate tile
                }
                case "RL" -> {
                    // Get the levers descriptive data, using comma as regex to split into array:
                    totalAmountOfLevers++;
                    String[] leverData = itemPosition.split(",");
                    // The x and y coordinates will ALWAYS be the first and second elements of the data array:
                    int xVal = Integer.parseInt(leverData[0]);
                    int yVal = Integer.parseInt(leverData[1]);
                    levers.add("RedLever"); // add to lever AL
                    tiles[yVal][xVal].setRedLever(); // set to appropriate tile
                }
                case "GL" -> {
                    // Get the levers descriptive data, using comma as regex to split into array:
                    totalAmountOfLevers++;
                    String[] leverData = itemPosition.split(",");
                    // The x and y coordinates will ALWAYS be the first and second elements of the data array:
                    int xVal = Integer.parseInt(leverData[0]);
                    int yVal = Integer.parseInt(leverData[1]);
                    levers.add("GreenLever"); // add to lever AL
                    tiles[yVal][xVal].setGreenLever(); // set to appropriate tile
                }
                case ",B" -> {
                    // Get the bombs descriptive data, using comma as regex to split into array:
                    String[] bombData = itemPosition.split(",");
                    // The x and y coordinates will ALWAYS be the first and second elements of the data array:
                    int xVal = Integer.parseInt(bombData[0]);
                    int yVal = Integer.parseInt(bombData[1]);
                    Bomb bomb = new Bomb(xVal, yVal); // Create a new instance of bomb to place on the
                    bombs.add(bomb); // add it to bomb AL to check for appropriate detonation
                    tiles[yVal][xVal].setBomb(bomb); // set to appropriate tile
                }
                case ",D" -> {
                    // Get the doors descriptive data, using comma as regex to split into array:
                    String[] doorData = itemPosition.split(",");
                    // The x and y coordinates will ALWAYS be the first and second elements of the data array:
                    int xVal = Integer.parseInt(doorData[0]);
                    int yVal = Integer.parseInt(doorData[1]);
                    tiles[yVal][xVal].setDoor(); // set to appropriate tile
                }
            }
        }
        totalAmountOfLoot = LootType.values();

        // The below code initialises a timeline that calls each Character's move method every second:
        characterMovement = new Timeline(new KeyFrame(Duration.seconds(1), event -> moveCharacters()));
        characterMovement.setCycleCount(Animation.INDEFINITE); // This plays indefinitely
        characterMovement.play();

        // The below code initialises a timeline that checks each bomb for detonation every second:
        checkBombsForDetonation = new Timeline(new KeyFrame(Duration.seconds(1), event -> checkBombs()));
        checkBombsForDetonation.setCycleCount(Animation.INDEFINITE); // This plays indefinitely
        checkBombsForDetonation.play();
    }

    /**
     * This method is responsible for moving the NPCs to their correct Tile's every second.
     * It is called by the 'characterMovement' timeline every second and calls the abstract 'move()' method
     * for every instance of Character in the level.
     * It then redraws the game, with the Characters in their new positions every second.
     */
    public static void moveCharacters() {
        // Only do this if the player has not won yet:
        if (!Main.hasPlayerWon()) {
            // Loop over every character in the level and call their abstract 'move()' method:
            ArrayList<Character> collidedCharacters = new ArrayList<Character>();
            for (Character character : characters) {
                if (character.hasCollided()) {
                    collidedCharacters.add(character);
                } else {
                    character.move();
                }
            }

            for (Character character : collidedCharacters) {
                characters.remove(character);
            }
            Main.drawGame(); // redraw the game
        }
    }

    /**
     * This method loops through all the bombs on the Level and checks to see if they have been detonated.
     * It is called by the checkBombsForDetonation Timeline every second and removes all items from the board
     * when a bomb has been detonated.
     */
    public static void checkBombs() {
        // Loop over all the bombs on the level and check to see if they have been detonated:
        for (Bomb bomb : bombs) {
            // Check to see if the current bomb in the iteration has been detonated (see bomb class):
            if (bomb.hasDetonated()) {
                // Loop through each Tile, removing all the relevant items from each Tile:
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        tiles[y][x].removeAllItems(); // Remove items from current Tile
                    }
                }
            }
        }
    }

    public static ArrayList<LootType> getLoot() {
        return loot;
    }

    public static ArrayList<String> getLevers() {
        return levers;
    }

    public static void setLoot(ArrayList<LootType> newLoot) {
        loot = newLoot;
    }

    public static void setLevers(ArrayList<String> newLevers) {
        levers = newLevers;
    }

    /**
     * This method is crucial for the NPC and Player movement methods. it takes an x and y value
     * and then returns the relevant Tile related to these coordinates.
     * @param x The x coordinate for the Tile.
     * @param y The y coordinate for the Tile.
     * @return The tile with the same coordinates as the ones parameterised.
     */
    public static Tile getTile(int x, int y) {
        // Try to return the Tile at the specified coordinates. Catch out of bounds exception:
        try {
            return tiles[y][x];
        } catch (Exception e) {
        }
        return tiles[0][0];
    }

    /**
     * This method loops through each Tile and draws everything that is on the Tile
     * @param gc Graphics Context to draw the item in the right frame
     */
    public static void drawLevel( GraphicsContext gc ) {
        // 2D nested loop that calls all draw methods for each Tile in the array:
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                tiles[i][j].drawTile(gc, i, j);
                tiles[i][j].drawLoots(gc,i,j);
                tiles[i][j].drawCharacters(gc,i,j);
                tiles[i][j].unCollectableItems(gc,i,j);
                tiles[i][j].checkCollision();
            }
        }
        // Draw player at the players x and y coordinates
        double direction = Player.getFacingDirection();
        drawRotatedImage(gc, playerImage, direction, ((Player.getPlayerX() * TILE_SIZE)),
                ((Player.getPlayerY() * TILE_SIZE)));
    }

    /**
     * Source of this method: "java2s.com/example/java/javafx/draw-rotated-javafx-image.html"
     * Draws the image after it has been rotated at angle 'direction' influenced by the key pressed
     * @param gc GraphicsContext2D of the Canvas in the current scene
     * @param image image of the player
     * @param angle angle to rotate the image at
     * @param topLeftCornerX, topLeftCornerY
     *                       x and y coordinate of the top left corner of the image, from where the image will be drawn
     */
    public static void drawRotatedImage(GraphicsContext gc, Image image,
                                        double angle, double topLeftCornerX, double topLeftCornerY) {
        gc.save();
        rotate(gc, angle, topLeftCornerX + image.getWidth() / 2,
                topLeftCornerY + image.getHeight() / 2);
        gc.drawImage(image, topLeftCornerX, topLeftCornerY);
        gc.restore();
    }

    /**
     * Source of this method: "java2s.com/example/java/javafx/draw-rotated-javafx-image.html"
     * Rotates the image
     * @param gc GraphicsContext2D of the canvas in the current scene
     * @param angle angle to rotate the image at
     * @param pivotPointX, pivotPointY
     *                     x and y coordinate of the point on which we want to set the pivot to rotate the image
     */
    private static void rotate(GraphicsContext gc, double angle, double pivotPointX,
                               double pivotPointY) {
        Rotate r = new Rotate(angle, pivotPointX, pivotPointY);
        gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(),
                r.getTx(), r.getTy());
    }

    /**
     * Increments time the player has to complete a level
     * This is used for when a player picks up a clock
     */
    public static void incrementTimeToCompleteLevel() {
        timeToCompleteLevel = timeToCompleteLevel + 5;
    }

    /**
     * Decrements time the player has to complete a level
     * This is used for when an NPC picks up a clock
     */
    public static void decrementTimeToCompleteLevel() {
        timeToCompleteLevel = timeToCompleteLevel - 5;
    }

    /**
     * This decreases the timer every second
     * Once the timer has reached 0, the player loses
     */
    public void decrementTime() {
        if (timeToCompleteLevel > 0) {
            timeToCompleteLevel -= 1;
        } else {
            Main.setPlayerLost();
            characterMovement.stop();
            checkBombsForDetonation.stop();
        }
    }

    public static void stopTimelines() {
        characterMovement.stop();
        checkBombsForDetonation.stop();
    }

    /**
     * Gets the time to complete the level
     * @return time to complete the level
     */
    public int getTimeToCompleteLevel() {
        return timeToCompleteLevel;
    }

    /**
     * Gets the player starting x position
     * @return players starting x postion
     */
    public static int getPlayerStartPosX() {
        return playerStartPosX;
    }

    /**
     * Gets the players starting y position
     * @return player starting y position
     */
    public static int getPlayerStartPosY() {
        return playerStartPosY;
    }

    /**
     * Gets floorFollowing thiefs x position
     * @return FFT x position
     */
    public static int getFloorFollowingThiefXPos(){
        return floorFollowingThiefXPos;
    }

    /**
     * Gets FFT y position
     * @return FFT y position
     */
    public static int getFloorFlowingThiefYPos(){
        return floorFollowingThiefYPos;
    }

    /**
     * Gets the total amount of levers
     * @return total amount of levers
     */
    public static int getTotalAmountOfLevers() {
        return totalAmountOfLevers;
    }

    /**
     * This method converts a character to a JavaFX colour
     * @param colourChar The character that needs to be converted
     * @return The colour that this character is converted to
     */
    private Color charToColour(char colourChar) {
        // Check the character and return a colour:
        return switch (colourChar) {
            case 'Y' -> Color.GOLD;
            case 'R' -> Color.CRIMSON;
            case 'G' -> Color.LIMEGREEN;
            case 'M' -> Color.MEDIUMORCHID;
            case 'C' -> Color.SKYBLUE;
            case 'B' -> Color.ROYALBLUE;
            default -> Color.BLACK;
        };
    }

    /**
     * This method converts a character to a direction
     * @param directionChar The character that needs to be converted
     * @return The direction that this character is converted to
     */
    private String charToDirection(char directionChar) {
        // Check the character and return direction
        return switch (directionChar) {
            case 'R' -> "Right";
            case 'L' -> "Left";
            case 'U' -> "Up";
            case 'D' -> "Down";
            default -> "INVALID";
        };
    }

    /**
     * Gets amount of loot
     * @return amount of loot
     */
    public static int getAmountOfLoot() {
        return loot.size();
    }

    /**
     * Gets amount of levers
     * @return amount of levers
     */
    public static int getAmountOfLevers() {
        return levers.size();
    }

    /**
     * Gets the 2D array of Tiles
     * @return 2D array of Tiles
     */
    public static Tile[][] getTiles() {
        return tiles;
    }

    /**
     * Gets loot arraylist
     * @return loot arraylist
     */
    public static ArrayList<LootType> loot(){
        return loot;
    }

    /**
     * Gets width of board
     * @return width of board
     */
    public static int getWidth() {
        return width;
    }

    /**
     * Gets height of board
     * @return height of board
     */
    public static int getHeight() {
        return height;
    }

    /**
     * Gets tile size
     * @return tile size
     */
    public static int getTileSize() {
        return TILE_SIZE;
    }

}
