package scrabble.rabble;

import java.util.Random;

class TilePool{
    
    private Tile head = null;
    private Tile tail = null;
    
    // Nested class containing a character and a pointer
    private class Tile{
        String letter;
        Tile next;
    }
    
    // Adds a character of type string into the list
    private void add(String s){
        Tile temp = new Tile();
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
        Tile temp = head;
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
        Tile temp = head;
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
    public String[] getTiles(int n){
        String[] sArray = new String[n];
        Random r = new Random();
        for(int i = 0; i < n; i++){
            int x = r.nextInt(length());            // get "n" number of random characters
            sArray[i] = remove(x);					// remove this tile from the pool, place it into the sArray
        }
        return sArray;
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
}