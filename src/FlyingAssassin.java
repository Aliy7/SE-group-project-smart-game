/**
 * The FlyingAssassin class represent one of many NPCs in the game.
 * It has a behaviour patterns of following a specific horizontal/vertical pathway until reaching the edge of
 * the board and turning back on itself. It does not interact with items, but destroys other NPCs and eliminates
 * the player. The Class fundamentally represents a coordinate updating algorithm.
 * @author Ben Rees (2015742)
 */

public class FlyingAssassin extends Character {

    private static int speed;

    private String direction; // Direction that the Flying assassin is following (Left, Right, Up, Down)
    public static final int ROTATE_LEFT = 1;
    public static final int ROTATE_RIGHT = -1;
    private static int dx;
    private static int dy;

    /**
     * Constructor takes a starting coordinate tuple and the direction that
     * the FlyingAssassin should follow.
     * @param xCoord X coordinate on the board
     * @param yCoord Y coordinate on the board
     * @param direction Direction to follow
     */
    public FlyingAssassin(int xCoord, int yCoord, String direction) {
        super(xCoord, yCoord);
        this.direction = direction;
    }

    /**
     * The move method implements the movement algorithms for each of the NPCs. The flying assassin
     * movement paradigm is very simple compared to the other NPCs. We firstly check to see if the
     * FA moves in its current direction will it be out of bounds, if it will be, we just flip the direction
     * to the opposite one. For example, if the direction is right, and the FA is about to leave the board, we just
     * change the direction to left for the next tick. We then set offset coordinates that allow us to easily
     * change the coordinates of the FA based on direction. For example if it needs to move right, then the
     * offset coordinates need to update X by 1 and Y by 0.
     * This method is called every second from the Level class.
     */
    public void move() {

        // Get the current Tile that the FA is standing on:
        Tile currentTile = Level.getTile(super.getCharacterX(), super.getCharacterY());

        Tile aboveTile = Level.getTile(super.getCharacterX(), super.getCharacterY() - 1);
        Tile belowTile = Level.getTile(super.getCharacterX(), super.getCharacterY() + 1);
        Tile rightTile = Level.getTile(super.getCharacterX() + 1, super.getCharacterY());
        Tile leftTile = Level.getTile(super.getCharacterX() - 1, super.getCharacterY());

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

        // If the FA has reached the right of the board, flip the direction:
        if (super.getCharacterX() == (Level.getWidth()-1) && direction.equals("Right")) {
            direction = inverseDirection(direction);
        }
        // If the FA has reached the left of the board, flip the direction:
        if (super.getCharacterX() == 0 && direction.equals("Left")) {
            direction = inverseDirection(direction);
        }
        // If the FA has reached the top of the board, flip the direction:
        if (super.getCharacterY() == 0 && direction.equals("Up")) {
            direction = inverseDirection(direction);
        }
        // If the Fa has reached the bottom of the board, flip the direction:
        if (super.getCharacterY() == (Level.getHeight()-1) && direction.equals("Down")) {
            direction = inverseDirection(direction);
        }

        // Based on the direction, assign the offset coordinates appropriately:
        switch (direction) {
            // If it is right, increase x by 1:
            case "Right" -> {
                dx = 1;
                dy = 0;
            }
            // If it is left, decrease x by 1:
            case "Left" -> {
                dx = -1;
                dy = 0;
            }
            // If it is down, increase y by 1:
            case "Down" -> {
                dx = 0;
                dy = 1;
            }
            // If it is up, decrease y by 1:
            case "Up" -> {
                dx = 0;
                dy = -1;
            }
            default -> {
                dx = 0;
                dy = 0;
            }
        }

        // Apply the determined offset to the current x and y coordinates of the FA:
        int tx = super.getCharacterX() + dx;
        int ty = super.getCharacterY() + dy;

        // Update actual coordinates to offset coordinates (new coordinates become offset coordinates):
        super.setCharacterX(tx);
        super.setCharacterY(ty);

        // The next Tile is the tile at the offset coordinates (If direction is right, next Tile is one to the right):
        Tile nextTile = Level.getTile(tx, ty);
        nextTile.setFlyingAssassin(this); // Populate the next Tile with this FA, so then the GC can draw it
        currentTile.removeFlyingAssassin(); // Remove flying assassin from current Tile so GC doesn't draw it

    }

    /**
     * This method helps a lot with changing the direction of a Flying Assassin. It takes in a direction and
     * then outputs the inverse of this direction.
     * @param dir A direction <-, ->, ^, down
     * @return The opposite of this direction.
     */
    private String inverseDirection(String dir) {
        // Work out the inverse of the inputted direction:
        return switch (dir) {
            // Inverse of Right is Left:
            case "Right" -> "Left";
            // Inverse of Left is Right:
            case "Left" -> "Right";
            // Inverse of Up is Down:
            case "Up" -> "Down";
            // Inverse of Down is Up:
            case "Down" -> "Up";
            default -> dir;
        };
    }
}
