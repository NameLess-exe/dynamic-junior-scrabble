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
    /*private Tile head = null;
    private Tile tail = null;
    
    // Nested class containing a character and a pointer
    private class Node{
        String letter;
        Node next;
    }
    
    // Adds a character of type string into the list
    private void add(String s){
        Node temp = new Node();
        temp.letter = s;                        // load the letter into a new Tile
        if (head == null){
            temp.next = head;
            head = temp;                        // if the pool is empty, add the tile to the front
            tail = temp;
        }
        else{
            tail.next = temp;                   // otherwise add it to the "bottom" of the list
            tail = temp;
        }
    }
    
    // Return the length of the list
    public int length(){
        Node temp = head;
        int i = 0;
        while(temp != null){
            i++;
            temp = temp.next;
        }
        return i;
    }
    
    // Remove the nth item and return it
    public String remove(int n){
        String s = "";
        int x = 1;
        Node temp = head;
        while(temp != null){
            if (n == 0){						// removing the first item
                s = head.letter;
                head = temp.next;               // set the head to the second item
                return s;
            }
            if (n == x){						// if the nth item (the next item in the list)
                s = temp.next.letter;           // get the next character
                temp.next = temp.next.next;
                return s;
            }
            else{
                x++;
                temp = temp.next;
            }
        }
        return s;
    }
    
    // Sets up a pool of the original characters
    public void getPool(){
        for (int i = 0; i < 9;i++) add("a");
        for (int i = 0; i < 2;i++) add("b");
        for (int i = 0; i < 2;i++) add("c");
        for (int i = 0; i < 3;i++) add("d");
        for (int i = 0; i < 9;i++) add("e");
        for (int i = 0; i < 1;i++) add("f");
        for (int i = 0; i < 2;i++) add("g");
        for (int i = 0; i < 2;i++) add("h");
        for (int i = 0; i < 5;i++) add("i");
        for (int i = 0; i < 1;i++) add("j");
        for (int i = 0; i < 1;i++) add("k");
        for (int i = 0; i < 5;i++) add("l");
        for (int i = 0; i < 2;i++) add("m");
        for (int i = 0; i < 4;i++) add("n");
        for (int i = 0; i < 7;i++) add("o");
        for (int i = 0; i < 2;i++) add("p");
        for (int i = 0; i < 1;i++) add("q");
        for (int i = 0; i < 5;i++) add("r");
        for (int i = 0; i < 4;i++) add("s");
        for (int i = 0; i < 5;i++) add("t");
        for (int i = 0; i < 3;i++) add("u");
        for (int i = 0; i < 1;i++) add("v");
        for (int i = 0; i < 2;i++) add("w");
        for (int i = 0; i < 1;i++) add("x");
        for (int i = 0; i < 2;i++) add("y");
        for (int i = 0; i < 1;i++) add("z");
        for (int i = 0; i < 2;i++) add(" ");
        
    }
    
    // Return a string array of tiles
    public ArrayList<Tile> getTiles(int n){
        ArrayList[] tList = new String[n];
        Random r = new Random();
        for(int i = 0; i < n; i++){
            int x = r.nextInt(length());            // get "n" number of random characters
            tList.add(remove(x));					// remove this tile from the pool, place it into the sArray
        }
        return tList;
    }
    
    public boolean isEmpty(){
    	if (head == null) return true;
    	else return false;
    }
    // Print out the list of characters
    public void dump(){
        Tile temp = head;
        while(temp != null){
            System.out.println(temp.letter);
            temp = temp.next;
        }
    }
     */
}