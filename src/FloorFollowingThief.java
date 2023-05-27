
import javafx.scene.paint.Color;

/**
 * Floor Following Thief class represents a type of character on the board. It automatically moves around the board
 * one tile at a time, making sure that it only moves if the next tile has the same colour as the tile it is standing
 * on. It also moves sticking to the left wall and tries to steal all the collectable items on the board before the
 * player collects them.
 * @author Fahim Samady (2035827), Ben Rees (2015742)
 */
public class FloorFollowingThief extends Character {
    //X and Y coordinates of Floor Following Thief
    private int floorFThiefXCoord;
    private int floorFThiefYCoord;

    //Direction the Floor Following Thief is moving in
    private String direction;

    //The colour Floor Following Thief is following around the board
    private Color colourToFollow;

    /*
    * Relative direction the Floor Following Thief is facing in. We use this to change its direction relative to the
    * direction it is currently headed.
    */
    private static double facingDirection = 0;

    /**
     * Constructor for FloorFollowingThief
     * Creates a new instance of FloorFollowingThief
     * @param startingX - the starting X coordinate of Floor Following Thief
     * @param startingY - the starting Y coordinate of Floor Following Thief
     * @param colourToFollow - the colour Floor Following Thief will follow around the board
     * @param direction - the direction Floor Following Thief will start moving in when spawned
     */
    public FloorFollowingThief(int startingX, int startingY, Color colourToFollow,String direction) {
        super(startingX, startingY);
        this.floorFThiefXCoord = super.getCharacterX();
        this.floorFThiefYCoord = super.getCharacterY();
        this.colourToFollow = colourToFollow;
        this.direction = direction;

    }

    /**
     * The move method implements the movement algorithms for each of the NPCs. The Floor following
     * movement paradigm is very simple compared to the other NPCs.
     * first we call the Bomb() method to check if it has been detonated.
     * then check the current direction if the current direction is right. check the top tile
     * if it is valid move for the FFT if it is not keep checking clock wise. if statement is
     * true then move the FFT to that direction. This method is called every second from the Level class.
     */

    public void move() {
        Tile currentTile = Level.getTile(getFloorFThiefXCoord(), getFloorFThiefYCoord());
        int dx;
        int dy;
        Bomb(); //call the Bomb method every move.
        // if the current direction is right it will check all the possible move
        if (direction.equals("Right")){
            // check one tile above if it is a valid move, if it is then the FFT set the direction to up
            // and keep checking clock wise.
            if (getAboveTile().hasCommonColour(currentTile) && notTopTiles()
                    && doesNextTileHaveColourToFollow(getAboveTile())) {
                setDirection("Up");
            }else if(getRightTile().hasCommonColour(currentTile)
                    && notRightTiles()&& doesNextTileHaveColourToFollow(getRightTile())) {
                setDirection("Right");
            }else if (getBelowTile().hasCommonColour(currentTile)
                    && notBelowTiles() && doesNextTileHaveColourToFollow(getBelowTile())) {
                setDirection("Down");
            }else if(getLeftTile().hasCommonColour(currentTile)
                    && notLeftTiles() && doesNextTileHaveColourToFollow(getLeftTile())){
                setDirection("Left");
            }
        }
        // if the direction is down check the right tile first
        // if it is not valid move check clockwise.
        else if (direction.equals("Down")){
            if(getRightTile().hasCommonColour(currentTile)
                    && notRightTiles()&& doesNextTileHaveColourToFollow(getRightTile())) {
                setDirection("Right");
            }else if (getBelowTile().hasCommonColour(currentTile)
                    && notBelowTiles() && doesNextTileHaveColourToFollow(getBelowTile()) ) {
                setDirection("Down");
            }else if (getLeftTile().hasCommonColour(currentTile)
                    && notLeftTiles() &&doesNextTileHaveColourToFollow(getLeftTile())){
                setDirection("Left");
            }else if (getAboveTile().hasCommonColour(currentTile)
                    && notTopTiles() && doesNextTileHaveColourToFollow(getAboveTile())){
                setDirection("Up");
            }
        }
        // if the current direction is Left, check the below tile first
        // if it is not valid move check clockwise.
        else if (direction.equals("Left")){
            if (getBelowTile().hasCommonColour(currentTile)
                    && notBelowTiles() &&doesNextTileHaveColourToFollow(getBelowTile()) ) {
                setDirection("Down");
            }else if (getLeftTile().hasCommonColour(currentTile)
                    && notLeftTiles() &&doesNextTileHaveColourToFollow(getLeftTile())){
                setDirection("Left");
            }else if (getAboveTile().hasCommonColour(currentTile)
                    && notTopTiles() &&doesNextTileHaveColourToFollow(getAboveTile())){
                setDirection("Up");
            }else if(getRightTile().hasCommonColour(currentTile)
                    && notRightTiles()&&doesNextTileHaveColourToFollow(getRightTile())) {
                setDirection("Right");
            }
        }else if (direction.equals("Up")){  // if the current direction is up, check the left tile first
            if (getLeftTile().hasCommonColour(currentTile) // if it is not valid move check clockwise.
                    && notLeftTiles() &&doesNextTileHaveColourToFollow(getLeftTile())){
                setDirection("Left");
            }else if (getAboveTile().hasCommonColour(currentTile)
                    && notTopTiles() &&doesNextTileHaveColourToFollow(getAboveTile())){
                setDirection("Up");
            }else if(getRightTile().hasCommonColour(currentTile)
                    && notRightTiles()&& doesNextTileHaveColourToFollow(getRightTile())) {
                setDirection("Right");
            }else if (getBelowTile().hasCommonColour(currentTile)
                    && notBelowTiles() &&doesNextTileHaveColourToFollow(getBelowTile())) {
                setDirection("Down");
            }
        }
        // Based on the direction, assign the offset coordinates appropriately:
        switch (direction) {
            case "Up" -> {
                facingDirection = -90;
                dx = 0;
                dy = -1;
            }
            case "Right" -> {
                facingDirection = 0;
                dx = 1;
                dy = 0;
            }
            case "Left" -> {
                facingDirection = 180;
                dx = -1;
                dy = 0;
            }
            case "Down" -> {
                facingDirection = 90;
                dx = 0;
                dy = 1;
            }
            default -> {
                dx = 0;
                dy = 0;
            }
        }
        int tx = floorFThiefXCoord + dx;
        int ty = floorFThiefYCoord + dy;
        floorFThiefXCoord = tx;
        floorFThiefYCoord = ty;
        Tile nextTile = Level.getTile(tx, ty);
        nextTile.setFloorFollowingThief(this);
        currentTile.setFloorFollowingThief(null);
    }

    /**
     * Checks if the floor following thief is not on the top most row
     * @return True, if the y coordinate of Floor Following Thief is greater than 0
     *          False, otherwise
     */
    private boolean notTopTiles() {
        return getFloorFThiefYCoord() > 0;
    }

    /**
     * Checks if the floor following thief is not on the right most column
     * @return True, if x coordinate of Floor Following Thief is less than (width of Level - 1)
     *          False, otherwise
     */
    private boolean notRightTiles() {
        return getFloorFThiefXCoord() < (Level.getWidth()-1);
    }

    /**
     * Checks if the floor following thief is not on the left most column
     * @return True, if x coordinate of Floor Following Thief is greater than 0
     *          False, otherwise
     */
    private boolean notLeftTiles() {
        return getFloorFThiefXCoord() > 0;
    }

    /**
     * Checks if the floor following thief is not on the bottom most row
     * @return True, if y coordinate of Floor Following Thief is less than (Level height - 1)
     *          False, otherwise
     */
    private boolean notBelowTiles() {
        return getFloorFThiefYCoord() < (Level.getHeight()-1);
    }

    /**
     * Checks if the next tile Floor Following Thief about to move to have a common colour as the current tile
     * @param nextTile - Instance of the next tile Floor Following Thief is about to move to
     * @return True, if the next tile has a common colour with the current tile
     *          False, otherwise
     */
    private boolean doesNextTileHaveColourToFollow(Tile nextTile) {
        Color[] nextTileColours = nextTile.getColours();
        boolean doesHaveColour = false;
        for (int i = 0; i < nextTileColours.length; i ++) {
            if (nextTileColours[i] == colourToFollow) {
                doesHaveColour = true;
            }
        }
        return doesHaveColour;
    }

    /**
     * this method check if the FFT close to the bomb it will detonate,
     * if it is it will remove form the tile.
     */
    private void Bomb(){
        Bomb bomb = null; // Initialise a new bomb, so we can detonate it if there is one on the Tiles.
        // If the right Tile has a bomb, get the bomb, and remove it from the Tile:
        if (getRightTile().hasBomb()) {
            bomb = getRightTile().getBomb();
            getRightTile().setBomb(null);
            // If the left Tile has a bomb, get the bomb, and remove it from the Tile:
        } else if (getLeftTile().hasBomb()) {
            bomb = getLeftTile().getBomb();
            getLeftTile().setBomb(null);
            // If the above Tile has a bomb, get the bomb, and remove it from the Tile:
        } else if (getAboveTile().hasBomb()) {
            bomb = getAboveTile().getBomb();
            getAboveTile().setBomb(null);
            // If the below Tile has a bomb, get the bomb, and remove it from the Tile:
        } else if (getBelowTile().hasBomb()) {
            bomb = getAboveTile().getBomb();
            getBelowTile().setBomb(null);
        }
        // If we found a bomb above, detonate it!
        if (bomb != null) {
            bomb.detonate();
        }
    }

    /**
     * Gets the X coordinate of the floor following thief
     * @return floorFThiefXCoord - the X coordinate of Floor Following Thief
     */
    public int getFloorFThiefXCoord() {
        return floorFThiefXCoord;
    }

    /**
     * Gets the Y coordinate of the floor following thief
     * @return floorFThiefYCoord - the Y coordinate of the Floor Following Thief
     */
    public int getFloorFThiefYCoord() {
        return floorFThiefYCoord;
    }

    /**
     * Sets the direction Floor Following Thief is moving to
     * @param direction - String of the name of the direction. Can only be "Up", "Down", "Left" or "Right"
     */
    private void setDirection( String direction ) {
        this.direction = direction;
    }

    /**
     * Get the left tile of the FFT
     * @return left tile
     */
    private Tile getLeftTile(){
        return Level.getTile(getFloorFThiefXCoord()-1, getFloorFThiefYCoord());
    }

    /**
     * Get the above tile of the FFT
     * @return the above tile
     */
    private Tile getAboveTile(){
        return Level.getTile( getFloorFThiefXCoord(),getFloorFThiefYCoord()-1);
    }

    /**
     * Get below tile of the FFT
     * @return below tile
     */
    private Tile getBelowTile(){
        return Level.getTile(getFloorFThiefXCoord(), getFloorFThiefYCoord()+1);
    }

    /**
     * Get right tile
     * @return right tile
     */
    private Tile getRightTile(){
        return Level.getTile(getFloorFThiefXCoord()+1, getFloorFThiefYCoord());
    }
}
