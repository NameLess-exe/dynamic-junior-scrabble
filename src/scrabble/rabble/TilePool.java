package scrabble.rabble;

import java.util.Random;
import java.util.ArrayList;

class TilePool{
    private ArrayList<Tile> tp = new ArrayList<Tile>();  // List of tiles
    
    public TilePool(){
        for (int i = 0; i < 9;i++) tp.add(new Tile('a'));
        for (int i = 0; i < 2;i++) tp.add(new Tile('b'));
        for (int i = 0; i < 2;i++) tp.add(new Tile('c'));
        for (int i = 0; i < 3;i++) tp.add(new Tile('d'));
        for (int i = 0; i < 9;i++) tp.add(new Tile('e'));
        for (int i = 0; i < 1;i++) tp.add(new Tile('f'));
        for (int i = 0; i < 2;i++) tp.add(new Tile('g'));
        for (int i = 0; i < 2;i++) tp.add(new Tile('h'));
        for (int i = 0; i < 5;i++) tp.add(new Tile('i'));
        for (int i = 0; i < 1;i++) tp.add(new Tile('j'));
        for (int i = 0; i < 1;i++) tp.add(new Tile('k'));
        for (int i = 0; i < 5;i++) tp.add(new Tile('l'));
        for (int i = 0; i < 2;i++) tp.add(new Tile('m'));
        for (int i = 0; i < 4;i++) tp.add(new Tile('n'));
        for (int i = 0; i < 7;i++) tp.add(new Tile('o'));
        for (int i = 0; i < 2;i++) tp.add(new Tile('p'));
        for (int i = 0; i < 1;i++) tp.add(new Tile('q'));
        for (int i = 0; i < 5;i++) tp.add(new Tile('r'));
        for (int i = 0; i < 4;i++) tp.add(new Tile('s'));
        for (int i = 0; i < 5;i++) tp.add(new Tile('t'));
        for (int i = 0; i < 3;i++) tp.add(new Tile('u'));
        for (int i = 0; i < 1;i++) tp.add(new Tile('v'));
        for (int i = 0; i < 2;i++) tp.add(new Tile('w'));
        for (int i = 0; i < 1;i++) tp.add(new Tile('x'));
        for (int i = 0; i < 2;i++) tp.add(new Tile('y'));
        for (int i = 0; i < 1;i++) tp.add(new Tile('z'));
        for (int i = 0; i < 2;i++) tp.add(new Tile(' '));

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