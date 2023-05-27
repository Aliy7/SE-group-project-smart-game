public class SmartThief extends Character {

    private boolean hasBoolean;
    private int yPosition;
    private int xPosition;

    private String [] direction  = {"Up", "Down", "Right", "Left"};
    public SmartThief(int xPosition, int yPosition) {
        super(xPosition, yPosition);
       this.xPosition = xPosition;
       this.yPosition = yPosition;
    }

    public void move() {
        Tile currentTile = Level.getTile(xPosition, yPosition);
    }

    public void addScore(LootType loot) {
    }

    public static int getThiefXPos(){

        return 0;
    }
    public static void getYPos(){
        //return getYPos();
    }
    /*
        This method should return the closest tile that has loot on it:

    public Tile findNearestLoot(Tile currentTile) {
        Tile[][] otherTiles = currentTile.getOtherTiles();
        int smallestDistance = Integer.MAX_VALUE;
        Tile closestTile = null;
        for (int x = 0; x < otherTiles.length; x++) {
            for (int y = 0; y < otherTiles.length; y++) {
                if (otherTiles[x][y].hasRedLever() || otherTiles[x][y].hasGreenLever()) {
                    int distanceToTile = (int) Math.sqrt(Math.pow((double) otherTiles[x][y].getX()
                            - (double)currentTile.getX(), 2) + Math.pow((double) otherTiles[x][y].getY()
                            - (double) currentTile.getY(), 2));
                    if (distanceToTile < smallestDistance) {
                        smallestDistance = distanceToTile;
                        closestTile = otherTiles[x][y];
                    }
                }
            }
        }
        return closestTile;
    }

     */
}
//boolean structure of smartThief algorithm
/*public class SmartThief extends Thief {
if(lootExit){
findNearestLoot;
if(pathExit){
moveToLoot;
}else if (leverExit){
findNearestLever
if(pathExit){
moveToLever;
}else{
randomMove;
}
if(!lootExit&&!pathExit){
moveToDoor
}
}
 */