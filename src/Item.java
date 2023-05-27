public class Item {
    private int xPos;
    private int yPos;
    private int width;
    private int height;
    private Tile tile;


    private boolean hasCollided;


    public Item(int xPos, int yPos, int width, int height) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.width = width;
        this.height = height;

    }

    public void setXPos(int xPos) {
        this.xPos = xPos;
    }

    public void setYPos(int yPos) {
        this.yPos = yPos;
    }

    public int itemXPos() {
        return xPos;
    }

    public int itemYPos() {
        return yPos;
    }

    public boolean hasCollision() {
        return hasCollided;
    }
}
