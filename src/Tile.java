import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

/**
 * The Tile class represents an individual tile on the board.
 * Storing all the things that are on the Tile in this class.
 * @author Fahim Samady (2035827), Shushovit Khanal (2023769), Ben Rees (2015742), Ali Muktar(2149459)
 * @version v1.1.0
 */
public class Tile extends Rectangle {

    //Constants used in the class
    private static final int MAX_COLOURS_IN_TILE = 4;
    private static final int TILE_SIZE = 50;
    private static final int HALF_OF_TILE_SIZE = TILE_SIZE / 2;

    // x and y coordinate of the Tile:
    private final int tileXCoordinate;
    private final int tileYCoordinate;

    private final Color[] colours = new Color[MAX_COLOURS_IN_TILE]; // Colours that the Tile has
    private Tile[][] otherTiles; // Other Tiles on the board
    private boolean hasLoot = false; // Whether the Tile contains loot

    // Objects that are stored on the Tile:
    private Player player;
    private FlyingAssassin flyingAssassin;
    private FloorFollowingThief floorFollowingThief;
    private SmartThief smartThief;

    // Has booleans for loot:
    private boolean hasCent;
    private boolean hasDollar;
    private boolean hasRuby;
    private boolean hasDiamond;

    private boolean hasClock; // Has boolean for clock

    // Has boolean for bomb
    private boolean hasBomb;
    // Bomb that is on the Tile
    private Bomb bomb;

    // Has booleans for Items
    private boolean hasDoor;
    private boolean hasRedLever;
    private boolean hasGreenLever;
    private boolean hasGreenGate;
    private boolean hasRedGate;

    // Character to colour representations of each Tile segment
    private final char topLeft;
    private final char topRight;
    private final char bottomLeft;
    private final char bottomRight;

    // Images for the NPCs:
    private static final Image floorFollowingThiefImage = new Image("./resources/FFT2.png");
    private static final Image smartThiefImage = new Image("./resources/ST2.png");
    private static final Image flyingAssassinImage = new Image("./resources/Assassin3.png");

    // Images for the loot:
    private static final Image diamondImage = new Image("./resources/diamond.png");
    private static final Image rubyImage = new Image("./resources/rubies.png");
    private static final Image dollarImage = new Image("./resources/dollar2.png");
    private static final Image centImage = new Image("./resources/cent5.png");

    // Images for the items:
    private static final Image clockImage = new Image("./resources/clock3.png");
    private static final Image doorImage = new Image("./resources/door3.png");
    private static final Image bombImage = new Image("./resources/bomb3.png");
    private static final Image redLeverImage = new Image("./resources/redLever.png");
    private static final Image greenLeverImage = new Image("./resources/greenLever.png");
    private static final Image greenGateImage = new Image("./resources/greenGate.png");
    private static final Image redGateImage = new Image("./resources/redGate.png");

    /**
     * Constructor takes in a colour string, x coordinate and y coordinate.
     * @param color Colour string that represents the colour of each segment
     * @param tileXCoordinate // x coordinate
     * @param tileYCoordinate // y coordinate
     */
    public Tile(String color, int tileXCoordinate, int tileYCoordinate){

        this.tileXCoordinate = tileXCoordinate;
        this.tileYCoordinate = tileYCoordinate;

        topLeft = color.toCharArray()[0];
        topRight = color.toCharArray()[1];
        bottomLeft = color.toCharArray()[2];
        bottomRight = color.toCharArray()[3];

        colours[0] = charToColour(topLeft);
        colours[1] = charToColour(topRight);
        colours[2] = charToColour(bottomLeft);
        colours[3] = charToColour(bottomRight);
    }

    /**
     * This method draws a tile by xCoordinate by tileSize and xCoordinate-
     * by tileSize which is initialised to 50 then splits the tile into smaller square.
     * @param gc This class is used to issue draw calls to a Canvas using a buffer.
     * @param yCoord an int value of y axis of the tile.
     * @param xCoord this an int value x axis of the tile.
     */
    public void drawTile(GraphicsContext gc, int yCoord, int xCoord) {
        gc.setFill(charToColour(topLeft));
        gc.fillRect(xCoord * TILE_SIZE, yCoord * TILE_SIZE, HALF_OF_TILE_SIZE, HALF_OF_TILE_SIZE);
        gc.setFill(charToColour(topRight));
        gc.fillRect(
                xCoord * TILE_SIZE + HALF_OF_TILE_SIZE,
                yCoord * TILE_SIZE, HALF_OF_TILE_SIZE, HALF_OF_TILE_SIZE
        );
        gc.setFill(charToColour(bottomLeft));
        gc.fillRect(
                xCoord * TILE_SIZE,
                yCoord * TILE_SIZE + HALF_OF_TILE_SIZE,
                HALF_OF_TILE_SIZE,
                HALF_OF_TILE_SIZE
        );
        gc.setFill(charToColour(bottomRight));
        gc.fillRect(xCoord * TILE_SIZE + HALF_OF_TILE_SIZE,
                yCoord * TILE_SIZE + HALF_OF_TILE_SIZE,
                HALF_OF_TILE_SIZE,
                HALF_OF_TILE_SIZE
        );
        gc.setStroke(Color.DARKGREY);
        gc.setLineWidth(1);
        gc.strokeLine(
                xCoord * TILE_SIZE + HALF_OF_TILE_SIZE,
                yCoord * TILE_SIZE, xCoord * TILE_SIZE + HALF_OF_TILE_SIZE,
                (yCoord + 1) * TILE_SIZE
        );
        gc.strokeLine(
                xCoord * TILE_SIZE,
                yCoord * TILE_SIZE + HALF_OF_TILE_SIZE,
                (xCoord + 1) * TILE_SIZE,
                yCoord * TILE_SIZE + HALF_OF_TILE_SIZE
        );
        gc.setLineWidth(2);
        gc.setStroke(Color.BLACK);
        gc.strokeRect(xCoord * TILE_SIZE, yCoord * TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }

    /**
     * This method draws a character images on the tile by xCoordinate by tileSize and xCoordinate-
     * @param gc This class is used to issue draw calls to a Canvas using a buffer
     * @param yCoord an int value of y axis of the tile
     * @param xCoord this an int value x axis of the tile
     */
    public void drawCharacters(GraphicsContext gc, int yCoord, int xCoord) {
        if (hasFloorFollowingThief()) {
            gc.drawImage(floorFollowingThiefImage, xCoord * TILE_SIZE, yCoord * TILE_SIZE);
        }
        if (hasSmartThief()) {
            gc.drawImage(smartThiefImage, xCoord * TILE_SIZE, yCoord * TILE_SIZE);
        }
        if (hasFlyingAssassin()) {
            gc.drawImage(flyingAssassinImage, xCoord * TILE_SIZE, yCoord * TILE_SIZE);
        }
    }

    /**
     * This method enables loot images to be displayed on xCoordinate and xCoordinate of the tile
     * @param gc This class is used to issue draw calls to a Canvas using a buffer
     * @param yCoord an int value of y axis of the tile
     * @param xCoord this an int value x axis of the tile
     */
    public void drawLoots(GraphicsContext gc, int yCoord, int xCoord) {
        if (hasDiamond()) {
            gc.drawImage(diamondImage, xCoord * TILE_SIZE, yCoord * TILE_SIZE);
        }
        if (hasRuby()) {
            gc.drawImage(rubyImage, xCoord * TILE_SIZE, yCoord * TILE_SIZE);
        }
        if (hasDollar()) {
            gc.drawImage(dollarImage, xCoord * TILE_SIZE, yCoord * TILE_SIZE);
        }
        if (hasCent()) {
            gc.drawImage(centImage, xCoord * TILE_SIZE, yCoord * TILE_SIZE);
        }
        if (hasClock()) {
            gc.drawImage(clockImage, xCoord * TILE_SIZE, yCoord * TILE_SIZE);
        }
    }

    /**
     * This method draws unCollectable items on the tile.
     * @param gc This class is used to issue draw calls to a Canvas using a buffer
     * @param yCoord an int value of y axis of the tile
     * @param xCoord this an int value x axis of the tile
     */
    public void unCollectableItems(GraphicsContext gc, int yCoord, int xCoord) {
        if (hasDoor()) {
            gc.drawImage(doorImage, xCoord * TILE_SIZE, yCoord * TILE_SIZE);
        }
        if (hasBomb()) {
            gc.drawImage(bombImage, xCoord * TILE_SIZE, yCoord * TILE_SIZE);
        }
        if (hasRedLever()) {
            gc.drawImage(redLeverImage, xCoord * TILE_SIZE, yCoord * TILE_SIZE);
        }
        if (hasGreenLever()) {
            gc.drawImage(greenLeverImage, xCoord * TILE_SIZE, yCoord * TILE_SIZE);
        }
        if (hasGreenGate()) {
            gc.drawImage(greenGateImage, xCoord * TILE_SIZE, yCoord * TILE_SIZE);
        }
        if (hasRedGate()) {
            gc.drawImage(redGateImage, xCoord * TILE_SIZE, yCoord * TILE_SIZE);
        }
    }

    /**
     * Checks the collisions of items, NPCs and the Player. It does this using a vast if-statement structure, which
     * checks to see if a player is on a Tile, and then if there is a player on the Tile, check to see if there is
     * anything else on the Tile simultaneously and act upon this appropriately. Do this for every possible collision.
     * This is made possible by the boolean storage method in the Tile class, if a Tile has an item, it will flip a
     * boolean to true, saying it has this item.
     */
    public void checkCollision() {
        // If the Tile has a player, check all other possible things it could have:
        if (hasPlayer()) {
            // If Tile has player and dollar:
            if (hasDollar()) {
                Player.incrementScore(Loot.getDollarScore());
                // Add dollar to the players loot ArrayList
                Player.addLoot("Dollar");
                // Set dollar to false, so it will not be drawn in the next frame:
                hasDollar = false;
            }
            // If Tile has player and cent:
            if (hasCent()) {
                Player.incrementScore(Loot.getCentScore());
                // Add cent to the players loot ArrayList
                Player.addLoot("Cent");
                // Set cent to false, so it will not be drawn in the next frame:
                hasCent = false;
            }
            // If Tile has player and diamond:
            if (hasDiamond()) {
                Player.incrementScore(Loot.getDiamondScore());
                // Add diamond to the players loot ArrayList
                Player.addLoot("Diamond");
                // Set cent to false, so it will not be drawn in the next frame:
                hasDiamond = false;
            }
            // If Tile has player and ruby:
            if (hasRuby()) {
                Player.incrementScore(Loot.getRubyScore());
                // Add ruby to the players loot ArrayList
                Player.addLoot("Ruby");
                // Set ruby to false, so it will not be drawn in the next frame:
                hasRuby = false;
            }
            // If Tile has a red lever and a player:
            if (hasRedLever()) {
                Player.setPlayerCollectedRedLever(); // Player now has a red lever
                Player.addLever("RedLever"); // Add it to lever ArrayList, so we can check win condition
                hasRedLever = false; // Set has red lever to false, so it is not drawn in the next frame
            }
            // If Tile has player and green lever:
            if (hasGreenLever()) {
                Player.setPlayerCollectedGreenLever(); // Player now has a green lever
                Player.addLever("GreenLever"); // Add it to lever ArrayList, so we can check win condition
                hasGreenLever = false; // Set has green lever to false, so it is not drawn in the next frame
            }
            // If Tile has player and clock:
            if (hasClock()) {
                // Increment players time to complete level
                Level.incrementTimeToCompleteLevel();
                // Change has clock to false, so it is not drawn next frame:
                hasClock = false;
            }
            // If Tile has player and door:
            if (hasDoor()) {
                // Check to see if all loot has been collected and levers collected, if it has player wins, if not, do nothing
                if (
                        (Player.getAmountOfLevers() == Level.getAmountOfLevers())
                                &&
                                (Player.getAmountOfLoot() == Level.getAmountOfLoot())
                ) {
                    Main.setPlayerWin();
                }
            }
            // If Tile has player and FlyingAssassin:
            if (hasFlyingAssassin()) {
                Main.setPlayerLost(); // Player has lost
                Level.stopTimelines(); // Stop all timelines
            }
            // If the Tile has a smart thief follow the same process as above:
        } else if (hasSmartThief()) {
            if (hasDollar()) {
                // remove dollar from board
                hasDollar = false;
            }
            if (hasCent()) {
                // remove cent from board
                hasCent = false;
            }
            if (hasRuby()) {
                // remove ruby from board
                hasRuby = false;
            }
            if (hasDiamond()) {
                // Remove diamond from board
                hasDiamond = false;
            }
            if (hasGreenLever()) {
                // Smart Thief now has green lever
                hasGreenLever = false;
            }
            if (hasRedLever()) {
                // Smart Thief now has red lever
                hasRedLever = false;
            }
            if (hasClock()) {
                // remove clock and reduce players time
                Level.decrementTimeToCompleteLevel();
            }
            if (hasDoor()) {
                // check to see if thief has collected all loot, if it has player loses, if not, do nothing
            }
            if (hasFlyingAssassin()) {
                // remove Smart Thief from board
                smartThief = null;
            }
        }
        // If Tile has FA (Seeing as FA picks up no items it only deals with FFT now):
        if (hasFlyingAssassin()) {
            if (hasFloorFollowingThief()) {
                // remove Floor Following Thief from board
                floorFollowingThief.setCollided();
                floorFollowingThief = null;
                System.out.println("FFT and FA collision");
            }
        }
        // If Tile has FFT, follow some procedures as above:
        if (hasFloorFollowingThief()) {
            if (hasDollar()) {
                // remove dollar from board
                ArrayList<LootType> loot = Level.getLoot();
                loot.remove(LootType.DOLLAR);
                Level.setLoot(loot);
                hasDollar = false;
            }
            if (hasCent()) {
                // remove cent from board
                ArrayList<LootType> loot = Level.getLoot();
                loot.remove(LootType.CENT);
                Level.setLoot(loot);
                hasCent = false;
            }
            if (hasRuby()) {
                // remove ruby from board
                ArrayList<LootType> loot = Level.getLoot();
                loot.remove(LootType.RUBIE);
                Level.setLoot(loot);
                hasRuby = false;
            }
            if (hasDiamond()) {
                // remove diamond from board
                ArrayList<LootType> loot = Level.getLoot();
                loot.remove(LootType.DIAMOND);
                Level.setLoot(loot);
                hasDiamond = false;
            }
            if (hasGreenLever()) {
                // remove lever from board
                hasGreenLever = false;
            }
            if (hasRedLever()) {
                // remove lever from board
                hasRedLever = false;
            }
            if (hasClock()) {
                // remove clock and reduce players time
                Level.decrementTimeToCompleteLevel();
            }
        }
    }

    /**
     * This method loops to through length of color of arrays checks.
     * If otherTileColours equals to other tile.
     * @param otherTile
     * @return true if the colours at the index equals the other colours at index x.
     * otherwsie return false.
     */
    public boolean hasCommonColour(Tile otherTile) {
        Color[] otherTileColours = otherTile.getColours();
        for (int i = 0; i < colours.length; i++) {
            for (int x = 0; x < colours.length; x++) {
                if (colours[i] == otherTileColours[x]) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if player exists on the Tile
     * @return True, if the x and y coordinate of Tile match with that of Player
     *          False, otherwise
     */
    public boolean hasPlayer() {
        boolean valueToReturn = false;
        int playerXCoordinate = Player.getPlayerX();
        int playerYCoordinate = Player.getPlayerY();
        if (playerXCoordinate == getTileXCoordinate() && playerYCoordinate == getTileYCoordinate()) {
            valueToReturn = true;
        }
        return valueToReturn;
    }

    /**
     * This method checks if the tile has flyAssassin.
     * @return True, if FlyingAssassin exists
     *          False, otherwise
     */
    public boolean hasFlyingAssassin() {
        return flyingAssassin != null;
    }

    /**
     * Gets the Flying Assassin on the Tile
     * @return flyingAssassin - instance of FlyingAssassin on the Tile
     */
    public FlyingAssassin getFlyingAssassin() {
        return flyingAssassin;
    }

    /**
     * Places a Flying Assassin on the Tile
     * @param flyingAssassin - instance of FLying Assassin to be placed on the Tile
     */
    public void setFlyingAssassin(FlyingAssassin flyingAssassin) {
        this.flyingAssassin = flyingAssassin;
    }

    /**
     * Removes FlyingAssassin from the Tile
     */
    public void removeFlyingAssassin() {
        this.flyingAssassin = null;
    }


    /**
     * Checks if a bomb exists on the Tile
     * @return True, if bomb exists
     *          Otherwise False
     */
    public boolean hasBomb() {
        return bomb != null;
    }

    /**
     * Gets the bomb on the Tile
     * @return bomb - instance of Bomb on the Tile
     */
    public Bomb getBomb() {
        return bomb;
    }

    /**
     * This method places a bomb on tile.
     * @param bomb - instance of Bomb to be placed on the Tile
     */
    public void setBomb(Bomb bomb) {
        this.bomb = bomb;
    }

    /**
     * Removes Bomb from Tile
     */
    public void removeBomb() {
        this.bomb = null;
    }

    /**
     * This method checks if the level has FloorFollowingThief
     * @return  True, if Floor Following Thief exists
     *          False, otherwise
     */
    public boolean hasFloorFollowingThief() {
        return floorFollowingThief != null;
    }

    /**
     * Gets the instance of FloorFollowingThief on the Tile
     * @return floorFollowingThief - instance of FloorFollowingThief (if it exits, otherwise returns null)
     */
    public FloorFollowingThief getFloorFollowingThief() {
        return floorFollowingThief;
    }

    /**
     * Places a new instance of FloorFollowingThief to the Tile
     * @param floorFollowingThief - instance of FloorFollowingThief to be set
     */
    public void setFloorFollowingThief(FloorFollowingThief floorFollowingThief) {
        this.floorFollowingThief = floorFollowingThief;
    }

    /**
     * This method removes floorFollowingThief from the level.
     * Sets the attribute floorFollowingThief to null
     */
    public void removeFloorFollowingThief(){
        this.floorFollowingThief = null;
    }

    /**
     * Checks if an instance of SmartThief exists on the Tile
     * @return True (if there exists an instance of SmartThief)
     *          False(if it is null)
     */
    public boolean hasSmartThief() {
        return smartThief != null;
    }

    /**
     * Gets the SmartThief placed on the Tile (if it exists)
     * @return smartThief - instance of SmartThief placed on the Tile
     */
    public SmartThief getSmartThief() {
        return smartThief;
    }

    /**
     * Places a new instance of Smart Thief on the Tile.
     * @param smartThief - Instance of SmartThief to be placed
     */
    public void setSmartThief(SmartThief smartThief) {
        this.smartThief = smartThief;
    }

    /**
     * Places a Green Lever on the instance of Tile
     */
    public void setGreenLever() {
        hasGreenLever = true;
    }

    /**
     * Checks if there is a Green Lever on the instance of Tile
     * @return True, if Green Lever exists
     *          False, otherwise
     */
    public boolean hasGreenLever() {
        return hasGreenLever;
    }

    /**
     * Places a Red Lever on the instance of Tile
     */
    public void setRedLever() {
        hasRedLever = true;
    }

    /**
     * Checks if there is a Red Lever on the current instance of Tile
     * @return True, if Red Lever exists
     *          False, otherwise
     */
    public boolean hasRedLever() {
        return hasRedLever;
    }

    /**
     * Places a door on the instance of Tile
     */
    public void setDoor() {
        hasDoor = true;
    }

    /**
     * Checks if there is a door on the instance of Tile
     * @return True, if door exists
     *          False otherwise
     */
    public boolean hasDoor(){
        return hasDoor;
    }

    /**
     * Places a Green Gate on the instance of Tile
     */
    public void setGreenGate() {
        hasGreenGate = true;
    }

    /**
     * Checks if there is Green Gate on the instance of Tile
     * @return True, if Green Gate exists
     *          False, otherwise
     */
    public boolean hasGreenGate() {
        return hasGreenGate;
    }

    /**
     * Places a Red Gate on the instance of Tile
     */
    public void setRedGate() {
        hasRedGate = true;
    }

    /**
     * Checks if there is a Red Gate on the instance of Tile
     *@return True, if Red Gate exists
     *          False, otherwise
     */
    public boolean hasRedGate() {
        return hasRedGate;
    }

    /**
     * Places a clock on the instance of Tile
     */
    public void setClock() {
        hasClock = true;
    }

    /**
     *  Checks if there is a clock on the instance of Tile
     * @return True, if clock exists
     *          False, otherwise
     */
    public boolean hasClock() {
        return hasClock;
    }

    /**
     * Places a diamond on the instance of Tile
     */
    public void setDiamond() {
        hasDiamond = true;
    }

    /**
     * The method checks if there is a diamond on the tile
     * @return True, if diamond exists
     *          False, otherwise
     */
    public boolean hasDiamond() {
        return hasDiamond;
    }

    /**
     * Places a cent on the instance of Tile
     */
    public void setCent() {
        hasCent = true;
    }

    /**
     * Checks if the instance of Tile has a cent coin on it
     * @return True, if cent exists
     *          False, otherwise
     */
    public boolean hasCent() {
        return hasCent;
    }

    /**
     * Places a ruby on the instance of Tile
     */
    public void setRuby() {
        hasRuby = true;
    }

    /**
     * Checks if the instance of Tile has a ruby on it
     * @return True, if ruby exists
     *          False, otherwise
     */
    public boolean hasRuby() {
        return hasRuby;
    }

    /**
     * Places a dollar on the instance of Tile
     */
    public void setDollar() {
        hasDollar = true;
    }

    /**
     * Checks if the instance of Tile has dollar on it
     * @return True, if dollar exists
     *          False, otherwise
     */
    public boolean hasDollar() {
        return hasDollar;
    }

    /**
     * This method allows each Tile to know about other Tiles on the board
     * @param otherTiles A 2D array of tiles
     */
    public void setOtherTiles(Tile[][] otherTiles) {
        this.otherTiles = otherTiles;
    }

    /**
     * Returns the other Tiles on the board
     * @return An array of other Tiles
     */
    public Tile[][] getOtherTiles() {
        return this.otherTiles;
    }

    /**
     * If hasLoot is false, it sets it to true indicating that there is some form of loot in the instance of Tile
     */
    public void setLoot() {
        if (!hasLoot) {
            hasLoot = true;
        }
    }

    /**
     * The method converts the character read from level file to equivalent-
     * colour of javafx.
     * @param colourChar this parameter represents the character read from levelFile.
     * @return recursive switch statement we assign each character read from a file to javafx color-
     * class equivalent.
     * by default returns black.
     */
    private Color charToColour(char colourChar) {
        return switch (colourChar) {
            case 'Y' -> Color.GOLD;
            case 'R' -> Color.CRIMSON;
            case 'G' -> Color.GREEN;
            case 'M' -> Color.MEDIUMORCHID;
            case 'C' -> Color.SKYBLUE;
            case 'B' -> Color.ROYALBLUE;
            default -> Color.BLACK;
        };
    }

    /**
     * The checks the if the loot xCoord and yCoord are equal to tile's x and y coordinates.
     * @return true if the coordinates are equal.
     * otherwise return false.
     */
    public boolean hasLoot() {
        boolean lootValue = false;
        int x = Loot.getXPos();
        int y = Loot.getYPos();
        if (Loot.getXPos() == tileXCoordinate && Loot.getYPos() == tileYCoordinate) {
            lootValue = true;
        }
        return lootValue;
    }

    /**
     * The removeAllItems method removes all the items from the board.
     * for example hasRedLever by default return true what it collected by NPC or characters-
     * removes from the level. This is crucial for the bomb functionality.
     */
    public void removeAllItems() {
        hasRedLever = false;
        hasGreenLever = false;
        hasDiamond = false;
        hasRuby = false;
        hasDollar = false;
        hasCent = false;
        hasClock = false;
        hasBomb = false;
    }

    /**
     * The method returns arrays of colors
     * @return arrays of colours
     */
    public Color[] getColours() {
        return colours;
    }

    /**
     * The method get tileXCoordinate
     * @return value of xCoordinates
     */
    public int getTileXCoordinate() {
        return this.tileXCoordinate;
    }

    /**
     * This method returns yCoordinate of the tile.
     * @return value of the tile yCoordinates
     */
    public int getTileYCoordinate() {
        return this.tileYCoordinate;
    }
}
