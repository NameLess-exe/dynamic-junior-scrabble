package scrabble.rabble.model;


public class Tile extends Sendable{
    @Override
	public Type getType() {
		// TODO Auto-generated method stub
		return Type.TILE;
	}
	char value;
	int index;
    
    public Tile(char c){
        value = c;
    }
    
    public char getValue(){
        return value;
    }
    
    public void setIndex(int i){
    	index = i;
    }
    
    public int getIndex(){
    	return index;
    }
}