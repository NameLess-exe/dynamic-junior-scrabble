package scrabble.rabble.model;


public class Tile extends Sendable{
    @Override
	public Type getType() {
		// TODO Auto-generated method stub
		return Type.TILE;
	}
	char value;
    
    public Tile(char c){
        value = c;
    }
    public char getValue(){
        return value;
    }
}