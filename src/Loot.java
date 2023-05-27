/**
 * The Loot extends the Collectable item keeps track of all loot type-
 * Furthermore, this class keeps track amount of loot, total score and highest possible score.
 * @author Ali Muktar, Ben Rees
 * @version 1.1.0
 */
public class Loot {
    private static boolean hasCent;
    private static boolean hasRuby;
    private static boolean hasDiamond;
    private static boolean hasDollar;
    private LootType loot;

    // X and Y position of the loot:
    private static int xPos;
    private static int yPos;

    private final static int centScore = 1; // How much the players score increases when they pick up a cent
    private final static int dollarScore = 2; // How much the players score increases when they pick up a dollar
    private final static int rubyScore = 3; // How much the players score increases when they pick up a ruby
    private final static int diamondScore = 4; // How much the players score increases when they pick up a diamond

    /**
     * This a constructor of Loot class.
     * @param xPos checks of xPosition of the loot on the tile.
     * @param yPos checks yPosition of the loot on the tile.
     * @param width deals width of the loots
     * @param height deals with the height of the loot
     */
    public Loot(int xPos, int yPos, int width, int height) {
        this.xPos = xPos;
        this.yPos = yPos;
    }

    /**
     * This checks hasRuby if hasRuby is true.
     * @return hasRuby.
     */
    private static boolean hasRuby() {
        return hasRuby;
    }

    /**
     * This method checks hasDiamond
     * @return hasDiamond
     */
    private static boolean hasDiamond() {
        return hasDiamond;
    }

    /**
     * The method gets the loots X position on the tile.
     * @return x coordinates of the item on the tile.
     */
    public static int getXPos(){
        return xPos;
    }

    /**
     * The method gets the loots Y position on the tile.
     * @return y coordinates of the item on the tile.
     */
    public static int getYPos(){
        return yPos;
    }

    /**
     * Gets the Diamond LootType
     * @return Diamond LootType
     */
    public LootType getDiamond() {
        return LootType.DIAMOND;
    }

    /**
     * Gets the Ruby LootType
     * @return Ruby LootType
     */
    public LootType getRubies() {
        return LootType.RUBIE;
    }

    /**
     * Gets the Dollar LootType
     * @return Dollar LootType
     */
    public LootType getDollar() {
        return LootType.DOLLAR;
    }

    /**
     * Gets the Cent LootType
     * @return Cent LootType
     */
    public LootType getCent() {
        return LootType.CENT;
    }

    /**
     * Gets the score of a Diamond
     * @return Score of a Diamond
     */
    public static int getDiamondScore() {
        return diamondScore;
    }

    /**
     * Gets the score of a Ruby
     * @return Score of a Ruby
     */
    public static int getRubyScore() {
        return rubyScore;
    }

    /**
     * Gets the score of a Dollar
     * @return Score of a Dollar
     */
    public static int getDollarScore() {
        return dollarScore;
    }

    /**
     * Gets the score of a Cent
     * @return Score of a Cent
     */
    public static int getCentScore() {
        return centScore;
    }

    /**
     * There method determines a lootType then increments the square by value acquired
     */
    public void checkLootType(){
        int score = 0;
        if(loot == LootType.DIAMOND){
            score++;
        } else if(loot ==LootType.CENT){
            score++;
        } else if(loot ==LootType.RUBIE){
            score++;
        } else if(loot == LootType.DOLLAR){
            score++;
        }
    }
}