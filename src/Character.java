/**
 * This class represents the general skeleton for a Character. Having an x and y coordinate
 * along with a generic movement method that needs to implemented for each one of Characters
 * children.
 * @author Collective
 */
public abstract class Character {

    private int x; // x coordinate of the Character
    private int y; // y coordinate of the Character

    private boolean collided = false;

    /**
     * Constructor takes the initial position of the Character
     * @param x initial x coordinate of Character
     * @param y initial y coordinate of Character
     */
    public Character(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Every Character has a unique set of movement rules. Meaning they need to implement
     * their own version of a move() method, yet they all need a move method.
     */
    public abstract void move();

    public void setCollided() {
        collided = true;
    }
    public boolean hasCollided() {
        return collided;
    }

    /**
     * Gets the Characters current Y coordinate
     * @return Characters Y coordinate
     */
    public int getCharacterY() {
        return y;
    }

    /**
     * Gets the Character current X coordinate
     * @return Characters X coordinate
     */
    public int getCharacterX() {
        return x;
    }

    /**
     * Changes Characters y coordinate
     * @param newY New y coordinate to change to
     */
    public void setCharacterY(int newY) {
        y = newY;
    }

    /**
     * Changes Characters x coordinate
     * @param newX New x coordinate to change to
     */
    public void setCharacterX(int newX) {
        x = newX;
    }
}