/**
 * This enum LootType stores a collectable items.
 * All collectable items have value assigned to them.
 * With Diamond being the most expensive and least is cent.
 * @Ali Muktar (2149459)
 */
public enum LootType {
    CENT(1),
    RUBIE(3), DIAMOND(4), DOLLAR(2);

    private final int value; //This variable initialises an int value to loot types

    /**
     * Constructor is an integer value of the loots.
     * @param value
     */
   LootType (int value){
       this.value = value;
   }
    /**
     * This gets the value of loot types.
     * @return the values.
     */
   public int getValue(){
       return value;
   }
}
