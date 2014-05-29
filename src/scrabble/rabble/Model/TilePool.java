package scrabble.rabble.Model;

import java.util.ArrayList;
import java.util.Random;

public class TilePool {
    private ArrayList<Tile> tp = new ArrayList<Tile>();  // List of tiles
    
    public TilePool(ArrayList<String> sArray){
        for(int i = 0;i < sArray.size();i++){
            int wordSize = sArray.get(i).length();
            for(int x = 0;x < wordSize;x++){
                tp.add(new Tile(sArray.get(i).charAt(x)));
            }
        }
    }
    
    public ArrayList<Tile> getTiles (int n){
        Random r = new Random();
        ArrayList<Tile> temp = new ArrayList<Tile>();
        for (int i = 0; i < n; i++){
            int x = r.nextInt(tp.size());
            temp.add(tp.remove(x));
        }
        return temp;
    }
    
    public int length(){
        return tp.size();
    }
    
    public void dump(){
        for(int i = 0; i < tp.size();i++){
            System.out.println(tp.get(i).getValue());
        }
    }
}