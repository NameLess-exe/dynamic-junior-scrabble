package scrabble.rabble.model;
import java.util.ArrayList;

public class Player extends Sendable{
    String name;                     				// Player name displayed by the client
    int identifier;                  				// Integer value used to determine players, should be the number of players when joined
    int age;                         				// Order is determined by age
    int points;                      				// Number of points a player has
    int avatar = -1;								// 0-8 representing a picture, -1 = null
    boolean isTurn = false;                 		// boolean controlling if the player is able to play or not
    ArrayList<Tile> tiles = new ArrayList<Tile>();  // List of tiles
    
    @Override
	public Type getType() {
		// TODO Auto-generated method stub
		return Type.PLAYER;
	}
    
    public void setAvatar(int i){
    	avatar = i;
    }

    public int getAvatar(){
    	return avatar;
    }
    
	// Set the player name
    public void setName(String s){
        name = s;
    }
    
    // Get the player name
    public String getName(){
        return name;
    }
    
    // Set the player identifier
    public void setIdentifier(int i){
        identifier = i;
    }
    
    // Get the player identifier
    public int getIdentifier(){
        return identifier;
    }
    
    // Set the player age
    public void setAge(int i){
        age = i;
    }
    
    // Get the player age
    public int getAge(){
        return age;
    }
    
    // Set the player age
    public void setPoints(int i){
        points = i;
    }
    
    // Get the player age
    public int getPoints(){
        return points;
    }
    
    public void setTurn(boolean b){
        isTurn = b;
    }
    
    public boolean getTurn(){
        return isTurn;
    }
    
    public ArrayList<Tile> getTiles(){
        return tiles;
    }
    public void setTiles(ArrayList<Tile> t){
    	tiles = t;
    }
    
    // dump all the tiles in the player
    public void showTiles(){
        String temp = "Tiles: ";
        System.out.println("_________________");
        System.out.println("Name     = " + name);
        System.out.println("Age      = " + age);
        System.out.println("Identity = " + identifier);
        System.out.println("Points   = " + points);
        System.out.println("isTurn   = " + isTurn);
        for (int i = 0; i < tiles.size(); i++) temp += tiles.get(i).getValue() + ", ";
        System.out.println(temp);
        System.out.println("_________________");
    }
    
    public void removeTile(Tile t){
        for (int i = 0; i < tiles.size();i++){
            Tile temp = tiles.get(i);
            if(temp.getValue() == t.getValue()) tiles.remove(i);
        }
    }
    
    public void addTile (Tile t){
        tiles.add(t);
    }
    
    // remove elements in s[], replace with ns[]
    public void replace(ArrayList<Tile> playedTiles, ArrayList<Tile> newTiles){
        ArrayList<Tile> temp = new ArrayList<Tile>();
        boolean b = false;
        for (int i = 0; i < tiles.size(); i++){
            
            b = false;
            for(int x = 0; x < playedTiles.size();x++){
                if (tiles.get(i) == playedTiles.get(x)){
                    playedTiles.remove(x);
                    b = true;
                    break;
                }
            }
            if (b == false) temp.add(tiles.get(i));
            
        }
        for (int i = 0; i < newTiles.size(); i++) temp.add(newTiles.get(i));
        tiles = temp;
    }
    

    /*
    // remove elements in s[], replace with ns[]
    public void replace(ArrayList<Tile> playedTiles, ArrayList<Tile> newTiles){
        ArrayList<Tile> temp = new ArrayList<Tile>();
        int counter = 0;
        for (int i = 0; i < tiles.size(); i++){
            if(counter >= playedTiles.size()) break; // if x is outside of bounds
            else if(tiles.get(i) == playedTiles.get(counter)){		// if this element is to be removed
                tiles.set(i, newTiles.get(counter));
                counter++;
            }
        }
        System.out.println("DONE");
    }
    */
}
