package scrabble.rabble;

//import scrabble.rabble.Model.Player;
import java.util.ArrayList;

public class PlayerList{
    ArrayList<Player> pl = new ArrayList<Player>();
    
    public void addPlayer(Player p){
        p.setIdentifier(length());
        pl.add(p);
    }
    
    public void update(Player p){
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
    public int length(){
        return pl.size();
    }
    
    /*
    TilePool tp = new TilePool();
    Node head = null;
    
    private class Node{
        Player player;
        Node next;
    }
    
    
    public void init(){
        tp.getPool();
    }
    public void addPlayer(String name){
        Player p = new Player();
        p.name = name;
        p.identifier = numPlayers();
        p.tiles = tp.getTiles(5);
        Node tmp = new Node();
        tmp.player = p;
        Node temp = head;
        if (temp == null){                                      // this player is the only item
            tmp.next = head;
            head = tmp;
        }
        while(temp != null){
            if (temp.player.name.compareTo(p.name) < 0){        // this player goes before the current
                tmp.next = temp;
                head = tmp;
                break;
            }
            else if( temp.player.name.compareTo(p.name) == 0){
            	tmp.next = temp.next;
            	temp.next = tmp;
            	break;
            }
            else if (temp.player.name.compareTo(p.name) > 0){
                if(temp.next == null){                          // belongs after, and no items afterwards
					tmp.next = temp.next;
					temp.next = tmp;
					break;
				}
				else if(temp.next.player.name.compareTo(p.name) < 0){
					tmp.next = temp.next;
					temp.next = tmp;						// insert between
					break;
				}
				else{
					temp = temp.next;
                }
            }
        }
    }
    
    public int numPlayers(){
    	int i = 0;
    	Node temp = head;
    	while (temp != null){
    		i++;
    		temp = temp.next;
    	}
    	return i;
    }
    
    public void update(Player p){
    	Node temp = head;
    	while (temp != null){
    		if (temp.player.name == p.name){
    			temp.player = p;
    			break;
    		}
    		else temp = temp.next;
    	}
    }
    
    
    public void dump(){
        Node temp = head;
        while(temp != null){
            System.out.println(temp.player.name);
            temp = temp.next;
        }
    }
     */
}