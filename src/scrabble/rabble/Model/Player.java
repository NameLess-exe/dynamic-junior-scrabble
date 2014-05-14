package scrabble.rabble.Model;

public class Player{
    public String name;
    public String[] tiles = new String[5];
    public int identifier;
    public int points;
    
    boolean isTurn = false;

    public void construct(String[] s){
            tiles = s;
    }
    
    /*
    // remove elements in s[], replace with ns[]
    public void replace(String[] r, String[] ns){
    	int x = 0;
    	for (int i = 0; i < 5; i++){
    		if(tiles[i] == r[x]){		// if this element is to be removed
  				tiles[i] = ns[x];
   				x++;
   			}
   			if(x >= r.length)break; // if x is outside of bounds
   		}
    }
    */
    
    // dump all the tiles in the player
    public void showTiles(){
        for (int i = 0; i < 5; i++){
            System.out.println(tiles[i]);
        }
    }
}
