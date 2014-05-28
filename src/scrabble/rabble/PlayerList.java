package scrabble.rabble;

import java.util.ArrayList;

public class PlayerList extends Sendable{
    ArrayList<Player> pl = new ArrayList<Player>();
    
    @Override
	public Type getType() {
		// TODO Auto-generated method stub
		return Type.PLAYERLIST;
	}

	public void addPlayer(Player p){
        pl.add(p);
    }
	
	public Player removePlayer(int i){
		return pl.remove(i);
	}
	
	public Player get(int i){
		return pl.get(i);
	}
    
    public void setPlayer(Player p){
        for (int i = 0; i < pl.size(); i++){
            if (pl.get(i).getIdentifier() == p.getIdentifier()){
                pl.set(i, p);
            }
        }
    }
    
    public Player getPlayer(int identifier){
        for(int i =0; i < pl.size();i++){
            Player p = pl.get(i);
            if (p.getIdentifier() == identifier){
                return p;
            }
        }
        return null;
    }
    public int size(){
        return pl.size();
    }
}