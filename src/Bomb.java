/**
 * This class represents a Bomb. If an NPC/Player occupies an adjacent Tile
 * it should explode and destroy all items on the board.
 * @author Ben Rees (2015742), Ali Muktar(2149459).
 */
public class Bomb {

    private int xPos; // x coordinate of the bomb
    private int yPos; // y coordinate of the bomb
    private boolean detonated = false; // If the bomb has been detonated

    /**
     * Bomb initially takes in an x and y coordinate
     * @param xPos x coordinate of bomb
     * @param yPos y coordinate of bomb
     */
    public Bomb(int xPos, int yPos) {
        this.xPos = xPos;
        this.yPos = yPos;
    }

    /**
     * Changes the detonated flag to true once the bomb as been blown up
     */
    public void detonate() {
        detonated = true;
    }

    /**
     * Return if the bomb has been detonated or not
     * @return Detonated flag
     */
    public boolean hasDetonated() {
        return detonated;
    }
}
