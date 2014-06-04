package scrabble.rabble;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;

/**
 * Created by Leo on 5/21/2014.
 */
public class Dictionary implements Iterable<String> {
    private ArrayList<String> dictionary;
    private int pointer;


    public Dictionary() {
        this.dictionary = new ArrayList<String>();
        this.pointer = 0;
    }

    public void populate(String fileName) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            for (String line; (line = br.readLine()) != null; ) {
                addWord(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addWord(String word) {
        Random rnd = new Random();
        if (word.length() > 5)
            return;
        dictionary.add(rnd.nextInt(dictionary.size() + 1), word);
    }

    public String get(int n) {
        return dictionary.get(n);
    }

    public String get() {
        if (pointer++ > dictionary.size())
            Collections.shuffle(dictionary);
        return dictionary.get(pointer);
    }

    public int size() {
        return dictionary.size();
    }

    @Override
    public Iterator<String> iterator() {
        return new DictionaryIterator();
    }

    private class DictionaryIterator implements Iterator<String> {
        private int i = 0;

        @Override
        public boolean hasNext() {
            return i < dictionary.size();
        }

        @Override
        public String next() {
            return dictionary.get(i++);
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
