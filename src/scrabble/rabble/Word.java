package scrabble.rabble;


import java.util.Iterator;
import java.util.Random;

/**
 * Created by Leo on 5/21/2014.
 */
public class Word implements Iterable<Character> {

    private String word;
    private Orientation orientation;

    public Word(String word, Orientation orientation) {
        this.word = word;
        this.orientation = orientation;
    }

    public char charAt(int n) {
        return word.charAt(n);
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public int length() {
        return word.length();
    }

    public int indexOf(char c) {
        return word.indexOf(c);
    }

    public Word shuffle() {
        // Fisher-Yates shuffle
        Random rnd = new Random();
        char[] string = word.toCharArray();
        for (int i = 0; i < word.length(); i++) {
            int r = rnd.nextInt(word.length());
            string[i] = new char[]{string[r], string[r] = string[i]}[0];
        }
        return new Word(String.valueOf(string), orientation);
    }

    @Override
    public String toString() {
        return word;
    }

    @Override
    public Iterator<Character> iterator() {
        return new WordIterator();
    }

    private class WordIterator implements Iterator<Character> {
        private int i = 0;

        @Override
        public boolean hasNext() {
            return i < word.length();
        }

        @Override
        public Character next() {
            return word.charAt(i++);
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
